---
name: orbit-mvi-kmp
description: Orbit MVI v10.0.0 patterns for this project's Kotlin Multiplatform + Compose Multiplatform screens — ContainerHost, intent/reduce/postSideEffect, collectAsState, SavedStateHandle persistence, iOS Swift bridging, orbit-test DSL. The project's shared/commonMain screens use Orbit MVI by convention. Load this skill when the user is implementing or modifying a ViewModel, a screen state/side-effect contract, an MVI flow, a ContainerHost, or any commonMain/CMP screen — even if they only say "add a viewmodel", "wire up state", "make this screen reactive", "emit a navigation event", or "test this VM". Also load it for tasks about exposing a shared Kotlin ViewModel to SwiftUI/iOS, or about subscribing to long-running flows from inside a ViewModel. Do not load for pure DI/repository/network/Gradle/DB-schema work even when "viewmodel" is mentioned in passing as part of a larger wiring task.
---

# Orbit MVI for Kotlin Multiplatform

**Library:** `org.orbit-mvi:orbit-*` · **Version:** `10.0.0` · **License:** Apache 2.0
**Platforms:** Android · iOS · JVM/Desktop · all Kotlin/Native targets · Compose Multiplatform (v10+)
**Docs:** https://orbit-mvi.org · **Repo:** https://github.com/orbit-mvi/orbit-mvi

This skill is the project's MVI playbook. Screens in `shared/commonMain` use Orbit; new screens should follow the same pattern unless there's a deliberate reason not to.

---

## When to read which file

| You're doing… | Read |
|---|---|
| New screen / ViewModel / state contract | This file (sections 1–5) |
| Testing a ViewModel | `references/testing.md` |
| Long-running flows, exception handling, sealed-state intents | `references/threading-and-flows.md` |
| Consuming the shared VM from SwiftUI | `references/ios-bridging.md` |

Keep this file in context. Pull a reference only when the task lands in that section.

---

## 1. Artifacts (Maven coordinates)

| Artifact | Purpose | commonMain |
|---|---|---|
| `org.orbit-mvi:orbit-core:10.0.0` | `Container`, `ContainerHost`, `intent`/`reduce`/`postSideEffect` | ✅ |
| `org.orbit-mvi:orbit-viewmodel:10.0.0` | KMP `ViewModel` factory + `SavedStateHandle` | ✅ |
| `org.orbit-mvi:orbit-compose:10.0.0` | `collectAsState`, `collectSideEffect` (CMP) | ✅ |
| `org.orbit-mvi:orbit-test:10.0.0` | `test()`, `expectState`, `expectSideEffect` | ✅ (commonTest) |

### Version catalog

```toml
[versions]
orbit = "10.0.0"

[libraries]
orbit-core      = { module = "org.orbit-mvi:orbit-core",      version.ref = "orbit" }
orbit-viewmodel = { module = "org.orbit-mvi:orbit-viewmodel", version.ref = "orbit" }
orbit-compose   = { module = "org.orbit-mvi:orbit-compose",   version.ref = "orbit" }
orbit-test      = { module = "org.orbit-mvi:orbit-test",      version.ref = "orbit" }
```

```kotlin
// shared/build.gradle.kts
commonMain.dependencies {
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.compose)
}
commonTest.dependencies {
    implementation(libs.orbit.test)
    implementation(libs.kotlinx.coroutines.test)
}
```

---

## 2. Mental model

```
UI Intent ──► ContainerHost.intent { } ──► reduce { state.copy(...) } ──► stateFlow ──► UI
                                       └─► postSideEffect(...)        ──► sideEffectFlow ──► UI (one-shot)
```

| Concept | Role |
|---|---|
| **Container** | The engine. Holds state, fans out updates, owns the coroutine scope. |
| **ContainerHost<S, E>** | Interface your ViewModel implements. Gives you `intent`, `reduce`, `postSideEffect`. |
| **`intent { }`** | Business logic. Non-blocking for the caller; serialized on the container's event loop. |
| **`reduce { state.copy(...) }`** | Atomic state update. Inside the block `state` is an immutable snapshot — never suspend here. |
| **`postSideEffect(e)`** | One-shot event (toast, navigation, analytics). Cached if no observer — events don't get lost. |

**The hard rule:** UI never makes a business decision. It renders state and dispatches intents. Everything else lives in the ViewModel.

For `subIntent`, `repeatOnSubscription`, `runOn`, and `Container.Settings` → `references/threading-and-flows.md`.

---

## 3. Screen contract template (commonMain)

Three files per screen, in `shared/src/commonMain/kotlin/.../feature/<name>/`:

**`WorkoutContract.kt`** — state + side effects

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutState(
    val isLoading: Boolean = false,
    val exercises: List<ExerciseUi> = emptyList(),
    val currentSetIndex: Int = 0,
    val error: String? = null,
)

sealed interface WorkoutSideEffect {
    data class ShowToast(val message: String) : WorkoutSideEffect
    data object NavigateToSummary : WorkoutSideEffect
    data class PlayRestTimer(val seconds: Int) : WorkoutSideEffect
}
```

**`WorkoutViewModel.kt`** — business logic

```kotlin
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class WorkoutViewModel(
    private val repo: WorkoutRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<WorkoutState, WorkoutSideEffect> {

    override val container = container<WorkoutState, WorkoutSideEffect>(
        initialState = WorkoutState(),
        savedStateHandle = savedStateHandle,
        serializer = WorkoutState.serializer(),  // KMP-safe persistence
    ) {
        loadExercises()                          // runs once at container creation
    }

    fun loadExercises() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        runCatching { repo.fetchTodayWorkout() }
            .onSuccess { list -> reduce { state.copy(isLoading = false, exercises = list) } }
            .onFailure  { e    ->
                reduce { state.copy(isLoading = false, error = e.message) }
                postSideEffect(WorkoutSideEffect.ShowToast("Yüklənmədi"))
            }
    }

    fun completeSet() = intent {
        val next = state.currentSetIndex + 1
        reduce { state.copy(currentSetIndex = next) }
        if (next >= state.exercises.size) postSideEffect(WorkoutSideEffect.NavigateToSummary)
        else                              postSideEffect(WorkoutSideEffect.PlayRestTimer(60))
    }
}
```

> **KMP ViewModel note:** `androidx.lifecycle:lifecycle-viewmodel` 2.8+ ships a multiplatform `ViewModel`. Orbit's `orbit-viewmodel` artifact builds on that — it's not Android-only. `viewModelScope` auto-cancels the container.

---

## 4. Compose Multiplatform UI

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun WorkoutScreen(
    viewModel: WorkoutViewModel,
    onNavigateToSummary: () -> Unit,
    showToast: (String) -> Unit,
    startTimer: (Int) -> Unit,
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is WorkoutSideEffect.ShowToast      -> showToast(effect.message)
            is WorkoutSideEffect.PlayRestTimer  -> startTimer(effect.seconds)
            WorkoutSideEffect.NavigateToSummary -> onNavigateToSummary()
        }
    }

    WorkoutContent(
        state = state,
        onCompleteSet = viewModel::completeSet,
        onRetry = viewModel::loadExercises,
    )
}

@Composable
private fun WorkoutContent(
    state: WorkoutState,
    onCompleteSet: () -> Unit,
    onRetry: () -> Unit,
) {
    // pure render — no business logic, no VM reference
}
```

**Why stateless content?** `collectAsState` and `collectSideEffect` are lifecycle-aware (subscribe at `STARTED`). Splitting the VM-aware composable from the pure-render one keeps previews + UI tests easy and matches the project's CMP convention.

**TextField tip:** for text input use Compose `TextFieldState` and pipe `snapshotFlow { state.textFieldState.text }` into the VM. Avoids threading data-loss vs. callback-based `onValueChange`.

---

## 5. SavedStateHandle persistence

| Situation | Strategy |
|---|---|
| State is `@Serializable` (KMP) | `container(initialState, savedStateHandle, serializer = State.serializer())` |
| Android-only screen | `@Parcelize` on state, pass only `savedStateHandle` |
| Big data (lists > a few KB, images) | **Don't** put it in SavedStateHandle — use SQLDelight / DataStore, keep only IDs in state |

Bundle storage is roughly ~500 KB safe / ~1 MB hard cap, and unreliable across process death.

---

## 6. Patterns / anti-patterns (quick reference)

✅ **Do**
- One screen = three files: `*Contract.kt`, `*ViewModel.kt`, `*Screen.kt`.
- Navigation, toast, analytics → `postSideEffect`.
- Long-running observers → `repeatOnSubscription` (see threading reference).
- Stateless content composable separated from the VM-aware composable.
- iOS: exactly one `for await` consumer for `sideEffectFlow` (see ios-bridging reference).

❌ **Don't**
- Suspend inside `reduce` — only state transformation belongs there.
- Make business decisions in the UI (`when (state) { … -> repo.fetch() }`).
- Spin up your own `CoroutineScope` in the VM — Orbit owns one.
- Park large blobs in `SavedStateHandle`.
- Attach two collectors to `sideEffectFlow` — effects get split.

---

## 7. fitnessApp project mapping

- **Layout:** `shared/src/commonMain/kotlin/.../feature/<name>/{Contract,ViewModel,Screen}.kt`. CMP screens live in `commonMain`.
- **DI (Koin):** `viewModel { params -> WorkoutViewModel(get(), params.get()) }` — pass `SavedStateHandle` via `parametersOf(savedStateHandle)` from the platform side.
- **Offline-first:** use `repeatOnSubscription { repo.observeLocalSessions().collect { … } }` so the VM observes SQLDelight only while the UI is alive. Aligns with CLAUDE.md's offline-first invariant.
- **AI cost-control:** AI plan triggers must enter through an `intent { }`. UI never calls AI runtime directly; this lets the VM enforce the §5.2 5-trigger cap (CLAUDE.md).
- **Volt design system:** `*Screen.kt`'s `WorkoutContent` consumes tokens from `project-context.md §1b` — no business logic, no theme decisions inside the VM.

---

## Sources

- https://orbit-mvi.org/ · /Core/ · /Core/architecture/ · /ViewModel/ · /Compose/ · /Test/
- https://github.com/orbit-mvi/orbit-mvi
