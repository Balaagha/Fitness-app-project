# Orbit MVI — iOS Bridging (KMM)

Orbit ViewModels live in `commonMain`. iOS consumes them through the shared XCFramework. SwiftUI screens subscribe to `container.stateFlow` and `container.sideEffectFlow`.

## Recommended Swift interop tooling

| Tool | Why |
|---|---|
| **SKIE** (Touchlab) | Auto-generates `Flow<T>` → Swift `AsyncSequence`, sealed-class enums, suspend → `async`. Lowest-friction option. |
| **KMP-NativeCoroutines** | Manual `@NativeCoroutines` annotations; works without SKIE but verbose. |
| **Manual `Flow.collect`** | Last resort — wrap each flow in a Kotlin helper that exposes a `(T) -> Unit` callback. |

## SwiftUI screen template (with SKIE)

```swift
import shared
import SwiftUI

@MainActor
final class WorkoutScreenModel: ObservableObject {
    @Published var state: WorkoutState = WorkoutState(...)
    private let vm: WorkoutViewModel
    private var stateTask: Task<Void, Never>?
    private var effectTask: Task<Void, Never>?

    init(vm: WorkoutViewModel, navigate: @escaping (Route) -> Void) {
        self.vm = vm

        stateTask = Task { [weak self] in
            for await s in vm.container.stateFlow {
                self?.state = s
            }
        }

        effectTask = Task { [weak self] in
            for await effect in vm.container.sideEffectFlow {
                switch effect {
                case let toast as WorkoutSideEffect.ShowToast:
                    self?.showToast(toast.message)
                case is WorkoutSideEffect.NavigateToSummary:
                    navigate(.summary)
                case let timer as WorkoutSideEffect.PlayRestTimer:
                    self?.startTimer(seconds: Int(timer.seconds))
                default: break
                }
            }
        }
    }

    deinit {
        stateTask?.cancel()
        effectTask?.cancel()
        vm.container.cancel()   // free coroutine scope
    }

    func completeSet() { vm.completeSet() }
}
```

## Hard rules

1. **One side-effect consumer per ViewModel.** `Container.sideEffectFlow` is designed for a single subscriber to guarantee caching of unconsumed events. Two `for await` loops on the same `sideEffectFlow` will silently lose effects.
2. **Cancel the container in `deinit`.** On iOS there is no Android `ViewModelStoreOwner`, so `viewModelScope` does not auto-cancel. Call `vm.container.cancel()` (or use `viewModelScope.cancel()`) when the screen tears down to prevent coroutine leaks.
3. **Keep state on `@MainActor`.** SwiftUI publishes only on the main actor. Wrap the `for await` body or hop with `await MainActor.run { }`.
4. **Don't mutate state from Swift.** Always go through `vm.someIntent()`. Treat `state` as read-only.

## State serialization across platforms

If state is `@Serializable` and you pass it through Apple's `SavedStateHandle` equivalent (KMP common ViewModel 2.8+ supports this), restoration works on both sides. For images / binary blobs, persist via SQLDelight and keep only IDs in state.

## Threading note

- All Orbit operators dispatch on a single per-container event loop. Calling `vm.foo()` from Swift's main thread is safe — Orbit hops to its own dispatcher before running the intent.
- Long `withContext(Dispatchers.Default)` blocks inside intents run on KMP's Default dispatcher (Kotlin/Native uses `NSOperationQueue`-backed pool). Don't assume main-thread.
