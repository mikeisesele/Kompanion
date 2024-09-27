package com.michael.kompanion.classes

/**
 * Command interface for executing, undoing, and redoing operations.
 */
interface Command {
    fun execute()
    fun undo()
}

/**
 * Invoker to manage command execution with undo/redo support.
 */
class KompanionUndoRedoManager {
    private val undoStack = mutableListOf<Command>()
    private val redoStack = mutableListOf<Command>()

    fun executeCommand(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val command = undoStack.removeLast()
            command.undo()
            redoStack.add(command)
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val command = redoStack.removeLast()
            command.execute()
            undoStack.add(command)
        }
    }
}
