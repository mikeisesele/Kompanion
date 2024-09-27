package com.michael.kompanion.classes


/**
 * Event bus for broadcasting and listening to events within the same process.
 *
 * data class MessageEvent(val message: String)
 *
 * val bus = EventBus()
 *
 * bus.subscribe(MessageEvent::class.java) {
 *     println("Received message: ${it.message}")
 * }
 *
 * bus.publish(MessageEvent("Hello, World!")) // Output: Received message: Hello, World!
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
