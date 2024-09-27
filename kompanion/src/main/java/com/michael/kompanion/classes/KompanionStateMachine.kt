package com.michael.kompanion.classes

/**
 * Simple state machine for controlled state transitions.
 *
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
 * println(stateMachine.currentState) // Idle
 * stateMachine.transition(Event.Start)
 * println(stateMachine.currentState) // Active
 *
 */
class KompanionStateMachine<S, E>(
    initialState: S,
    private val transitionRules: Map<S, Map<E, S>>
) {
    var currentState: S = initialState
        private set

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
