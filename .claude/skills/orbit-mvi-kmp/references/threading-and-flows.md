# Orbit MVI — Threading, Flows, and Container Settings

## Threading model

- Every `Container` has a single coroutine **event loop** that serializes intents.
- `container.intent { … }` doesn't block the caller — it enqueues onto the loop.
- Inside `intent { }`, suspend calls suspend the loop. If you do CPU/IO work, hop with `withContext`.
- `reduce { }` runs synchronously on the loop and **must not suspend** — only return the new state.

```kotlin
fun compressAndSave(bitmap: Bitmap) = intent {
    val compressed = withContext(Dispatchers.Default) { heavyCompress(bitmap) }
    val url = withContext(Dispatchers.IO) { repo.upload(compressed) }
    reduce { state.copy(thumbnailUrl = url) }
}
```

## repeatOnSubscription

Use for **infinite/hot flows** (sensors, websockets, DB observers). Orbit only runs the block while there's at least one active subscriber to `stateFlow` / `sideEffectFlow` — avoids leaking long-running collectors when the screen is gone.

```kotlin
fun observeLocalSessions() = intent(idlingResource = false) {
    repeatOnSubscription {
        repo.observeLocalSessions().collect { sessions ->
            reduce { state.copy(sessions = sessions) }
        }
    }
}
```

`idlingResource = false` tells Espresso-style tests this intent runs forever and shouldn't block test idle.

## subIntent

A suspend function that has the same DSL as `intent`. Use it to decompose a large intent or run pieces in parallel.

```kotlin
suspend fun loadProfile() = subIntent {
    val p = repo.profile()
    reduce { state.copy(profile = p) }
}

suspend fun loadStats() = subIntent {
    val s = repo.stats()
    reduce { state.copy(stats = s) }
}

fun loadDashboard() = intent {
    coroutineScope {
        launch { loadProfile() }
        launch { loadStats() }
    }
    postSideEffect(DashboardSideEffect.LoadComplete)
}
```

## runOn (sealed-class state)

Run a block only while state matches a given subtype; auto-cancels when state changes.

```kotlin
sealed interface WorkoutPhase {
    data object Idle : WorkoutPhase
    data class Active(val startedAt: Instant) : WorkoutPhase
    data object Resting : WorkoutPhase
}

fun startTickerWhenActive() = intent {
    runOn(WorkoutPhase.Active::class) {
        while (true) { delay(1000); reduce { /* tick */ } }
    }
}
```

## Exception handling — Container.Settings

By default Orbit lets exceptions propagate, which can kill the parent scope. In a ViewModel context this means the screen breaks for the user. Install a handler:

```kotlin
override val container = container<S, E>(
    initialState = S(),
    buildSettings = {
        exceptionHandler = CoroutineExceptionHandler { _, t ->
            Logger.e("orbit", t)
            // Container keeps working; later intents still run.
        }
    }
)
```

Other useful settings:

| Setting | Default | When to change |
|---|---|---|
| `intentLaunchingDispatcher` | `Dispatchers.Default` | Switch to a single-thread dispatcher for deterministic tests outside `orbit-test`. |
| `repeatOnSubscribedStopTimeout` | `100.milliseconds` | Increase if screens re-subscribe quickly during navigation (avoids unnecessary restart). |
| `sideEffectBufferSize` | `Channel.UNLIMITED` | Cap to detect runaway producers in dev. |

## Side-effect caching guarantee

`postSideEffect` writes into an internal channel. If no observer is connected, **effects are cached**. The first observer to attach drains them in order. This is why one-shot navigation events fired during cold-start are still delivered after the UI binds.

The flip side: if two observers attach to `sideEffectFlow`, effects are split between them randomly. **One observer per container** is the rule.

## Anti-patterns

- `reduce { repo.fetch() }` — never suspend inside reduce. Move the call to the surrounding `intent` and reduce only the result.
- Starting an infinite collect inside a plain `intent` without `repeatOnSubscription` — leaks when the screen dies.
- Creating a `CoroutineScope` inside the ViewModel for "background work" — Orbit already owns one; use `intent` / `subIntent` instead.
- Sharing `sideEffectFlow` across two collectors — silently drops events. Hoist to a wrapper that re-broadcasts via `SharedFlow` if you really need multi-cast.
