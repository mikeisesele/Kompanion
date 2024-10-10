package com.michael.kompanion.classes


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Simple state machine for controlled state transitions.
 *
 * The `KompanionStateMachine` class models a simple finite state machine that allows controlled transitions between states
 * based on events. It takes an initial state and a set of transition rules that define which state an event will move to.
 *
 * The `transition()` method is used to move from one state to another if a valid event is provided.
 *
 * Example usage:
 *
 * ```Kt
 *
 * enum class State { Idle, Active, Completed }
 * enum class Event { Start, Finish }
 *
 * val stateMachine = KompanionStateMachine(
 *     initialState = State.Idle,
 *     transitionRules = mapOf(
 *         State.Idle to mapOf(Event.Start to State.Active),
 *         State.Active to mapOf(Event.Finish to State.Completed)
 *     )
 * )
 *
 * println(stateMachine.currentState) // Output: Idle
 *
 * stateMachine.transition(Event.Start)
 *
 * println(stateMachine.currentState) // Output: Active
 *
 * stateMachine.transition(Event.Finish)
 *
 * println(stateMachine.currentState) // Output: Completed
 * ```
 *
 * When to use:
 * Use this class when you need to model a finite state machine with defined state transitions. It's useful for scenarios like
 * workflow management, UI state transitions, game development, or any situation where you need to enforce specific rules
 * around transitioning between states.
 *
 * @param initialState The starting state of the state machine.
 * @param transitionRules A map defining the valid state transitions, where each state points to a map of events and their corresponding next states.
 */
class KompanionStateMachine<S, E>(
    initialState: S,
    private val transitionRules: Map<S, Map<E, S>>
) {
    private var currentState: S = initialState

    /**
     * Attempts to transition to a new state based on the given event.
     *
     * Example usage:
     *
     * ```Kt
     *
     * val successful = stateMachine.transition(Event.Start)
     * if (successful) {
     *     println("Transitioned to: ${stateMachine.currentState}")
     * } else {
     *     println("Invalid transition.")
     * }
     * ```
     *
     * When to use:
     * Call this method when you want to move the state machine to a new state based on an event.
     * If the event is valid for the current state, it will transition to the corresponding new state.
     * Otherwise, it will return `false` and the state will not change.
     *
     * @param event The event that triggers the transition.
     * @return `true` if the transition was successful, `false` otherwise.
     */
    fun transition(event: E): Boolean {
        val nextState = transitionRules[currentState]?.get(event)
        return if (nextState != null) {
            currentState = nextState
            true
        } else {
            false
        }
    }
}


