package com.michael.kompanion.classes


/**
 * Fixed-size circular buffer.
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
