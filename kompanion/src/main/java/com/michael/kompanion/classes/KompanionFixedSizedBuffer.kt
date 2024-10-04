package com.michael.kompanion.classes


/**
 * Fixed-size circular buffer.
 *
 *```Kt
 *  class EventLogger(private val capacity: Int) {
 *     private val eventBuffer = KompanionFixedSizedBuffer<String>(capacity)
 *
 *     fun logEvent(event: String) {
 *         eventBuffer.add(event)
 *         println("Logged event: $event")
 *     }
 *
 *     fun getRecentEvents(): List<String> {
 *         return eventBuffer.getAll()
 *     }
 *  }
 *
 *  val logger = EventLogger(capacity = 5)
 *
 *  // Logging events
 *  logger.logEvent("Event 1: User logged in")
 *  logger.logEvent("Event 2: User clicked on button")
 *  logger.logEvent("Event 3: User logged out")
 *  logger.logEvent("Event 4: User updated profile")
 *  logger.logEvent("Event 5: User uploaded a file")
 *
 *  // The buffer is now full, logging the next event will remove the oldest one
 *  logger.logEvent("Event 6: User deleted a file")
 *
 *   // Retrieve recent events
 *  println("Recent events: ${logger.getRecentEvents()}")
 *  // Output: Recent events: [Event 2, Event 3, Event 4, Event 5, Event 6]
 *```
 *
 *
 */
class KompanionFixedSizedBuffer<T>(private val capacity: Int) {
    private val buffer = ArrayList<T>(capacity)

    fun add(item: T) {
        if (buffer.size == capacity) {
            buffer.removeAt(0)
        }
        buffer.add(item)
    }

    fun getAll(): List<T> = buffer.toList()
}
