package com.michael.kompanion.classes

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */

/**
 * Event bus for broadcasting and listening to events within the same process.
 *
 * ```Kt
 *
 * data class MessageEvent(val message: String)
 *
 * val bus = KompanionEventBus()
 *
 * bus.subscribe(MessageEvent::class.java) {
 *     println("Received message: ${it.message}")
 * }
 *
 * bus.publish(MessageEvent("Hello, World!")) // Output: Received message: Hello, World!
 * ```
 *
 */
class KompanionEventBus {
    private val listeners = mutableMapOf<Class<*>, MutableList<(Any) -> Unit>>()

    fun <T : Any> subscribe(eventType: Class<T>, listener: (T) -> Unit) {
        listeners.getOrPut(eventType) { mutableListOf() }.add { listener(it as T) }
    }

    fun publish(event: Any) {
        listeners[event::class.java]?.forEach { it(event) }
    }
}
