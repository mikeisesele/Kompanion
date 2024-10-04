package com.michael.kompanion.classes

/**
 * Observable class with support for multiple subscribers.
 *
 * ```Kt
 *
 * val observable = Observable(0)
 *
 * observable.subscribe { println("Observer 1: $it") }
 * observable.subscribe { println("Observer 2: $it") }
 *
 * observable.setValue(10) // Both observers are notified
 * ```
 *
 */
class KompanionObservable<T>(private var value: T) {
    private val observers = mutableListOf<(T) -> Unit>()

    fun subscribe(observer: (T) -> Unit) {
        observers.add(observer)
    }

    fun unsubscribe(observer: (T) -> Unit) {
        observers.remove(observer)
    }

    fun setValue(newValue: T) {
        value = newValue
        notifyObservers()
    }

    fun getValue(): T = value

    private fun notifyObservers() {
        observers.forEach { it(value) }
    }
}
