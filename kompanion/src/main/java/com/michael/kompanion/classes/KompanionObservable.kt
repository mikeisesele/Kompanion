package com.michael.kompanion.classes

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Observable class with support for multiple subscribers.
 *
 * This class allows you to observe changes to a value. Multiple observers (callbacks) can subscribe to be notified
 * whenever the value changes, and they will be invoked when `setValue` is called.
 * It follows the observer pattern, where the observers are notified when the observed data changes.
 *
 * Example usage:
 *
 * ```Kt
 *
 * val observable = KompanionObservable(0)
 *
 * observable.subscribe { println("Observer 1: $it") }
 * observable.subscribe { println("Observer 2: $it") }
 *
 * observable.setValue(10) // Both observers are notified: "Observer 1: 10", "Observer 2: 10"
 * ```
 *
 * When to use:
 * Use this class when you want to have an observable value with multiple subscribers that need to react to changes.
 * It's especially useful in scenarios where the state changes need to be propagated to various components, like in UI data binding, event listeners, or reactive programming scenarios.
 *
 * @param value The initial value of the observable.
 */
class KompanionObservable<T>(private var value: T) {
    private val observers = mutableListOf<(T) -> Unit>()

    /**
     * Adds an observer to be notified when the value changes.
     *
     * @param observer A lambda function that will be invoked with the new value whenever `setValue` is called.
     */
    fun subscribe(observer: (T) -> Unit) {
        observers.add(observer)
    }

    /**
     * Removes an observer so it will no longer be notified of value changes.
     *
     *
     * @param observer The observer to remove.
     */
    fun unsubscribe(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    /**
     * Updates the value and notifies all subscribed observers of the change.
     *
     *
     * When to use:
     * Call this method whenever you want to update the observable's value and propagate the change to all subscribers.
     *
     * @param newValue The new value to set.
     */
    fun setValue(newValue: T) {
        value = newValue
        notifyObservers()
    }

    /**
     * Retrieves the current value of the observable.
     *
     * When to use:
     * Use this method when you need to get the current value of the observable without subscribing to changes.
     *
     * @return The current value.
     */
    fun getValue(): T = value

    /**
     * Notifies all subscribed observers of the current value. This is called internally when the value changes.
     */
    private fun notifyObservers() {
        observers.forEach { it(value) }
    }
}
