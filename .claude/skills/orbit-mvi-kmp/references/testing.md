# Orbit MVI — Testing Reference

Artifact: `org.orbit-mvi:orbit-test:10.0.0` (KMP, commonTest).

## Setup

```kotlin
// shared/build.gradle.kts
commonTest.dependencies {
    implementation(libs.orbit.test)
    implementation(libs.kotlinx.coroutines.test)
}
```

## Test DSL entry point

```kotlin
ContainerHost.test(scope = testScope, initialState = StateOverride())
```

- Default: container init lambda (the `container { … }` trailing block) is **skipped** so the test starts from a clean state. Call `runOnCreate()` if you want it to run.
- Delays are skipped by default. Inject your own `TestScope` to control time.

## Full example

```kotlin
import kotlinx.coroutines.test.runTest
import org.orbitmvi.orbit.test.test
import kotlin.test.Test

class WorkoutViewModelTest {

    @Test
    fun `completeSet advances index and posts rest timer`() = runTest {
        val vm = WorkoutViewModel(FakeRepo(twoExercises), SavedStateHandle())
        vm.test(this, initialState = WorkoutState(exercises = twoExercises)) {
            runOnCreate()                            // executes the init intent
            expectState { copy(isLoading = true) }
            expectState { copy(isLoading = false, exercises = twoExercises) }

            containerHost.completeSet()
            expectState { copy(currentSetIndex = 1) }
            expectSideEffect(WorkoutSideEffect.PlayRestTimer(60))
        }
    }

    @Test
    fun `last set navigates to summary`() = runTest {
        val vm = WorkoutViewModel(FakeRepo(oneExercise), SavedStateHandle())
        vm.test(this, initialState = WorkoutState(exercises = oneExercise)) {
            containerHost.completeSet()
            expectState { copy(currentSetIndex = 1) }
            expectSideEffect(WorkoutSideEffect.NavigateToSummary)
        }
    }
}
```

## DSL reference

| Call | Purpose |
|---|---|
| `runOnCreate()` | Manually execute the container init block. Must be first. Returns a `Job` if you want to `join()` or `cancel()`. |
| `containerHost.someIntent()` | Trigger an intent — returns a `Job`. Call `job.join()` before assertions if the intent is async. |
| `expectState { copy(...) }` | Asserts the next emitted state equals `previousState.copy(...)`. Conflated. |
| `expectStateOn<SubType> { copy(...) }` | For sealed-class state — asserts the next state is `SubType` and matches the copy. |
| `expectSideEffect(eff)` | Asserts the next side effect equals `eff`. Sequential. |
| `skip(n)` | Skip `n` items (state or side-effect) explicitly. |
| `cancelAndIgnoreRemainingItems()` | Drain infinite flows so the test can return. |

## Virtual time control

```kotlin
val scope = TestScope()
ExampleViewModel().test(scope) {
    val job = containerHost.incrementForever()
    scope.advanceTimeBy(30_001)
    expectState { copy(count = 30) }
    job.cancel()
}
```

## Common pitfalls

- **Forgetting `runOnCreate()`**: tests fail because expected init state never appears.
- **Async intent + immediate assertion**: call `job.join()` after `containerHost.foo()` when the intent does `withContext` / network.
- **Conflated state**: rapid `reduce` calls may collapse — assert the final state, not every intermediate one.
- **Side-effect order matters**: `expectSideEffect` is FIFO; rearranging `postSideEffect` order in code breaks tests.
