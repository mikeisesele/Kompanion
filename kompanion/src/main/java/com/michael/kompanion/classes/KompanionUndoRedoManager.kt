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
 *
 * ```Kt
 *
 * class ChangeTextCommand(private val editor: TextEditor, private val newText: String) : Command {
 *     private val oldText: String = editor.text
 *
 *     override fun execute() {
 *         editor.text = newText
 *     }
 *
 *     override fun undo() {
 *         editor.text = oldText
 *     }
 * }
 *
 * class TextEditor {
 *     var text: String = ""
 * }
 *
 *
 * Usage
 *
 *     val editor = TextEditor()
 *     val undoRedoManager = KompanionUndoRedoManager()
 *
 *     // Execute a command to change the text
 *     val changeTextCommand = ChangeTextCommand(editor, "Hello, World!")
 *     undoRedoManager.executeCommand(changeTextCommand)
 *
 *     println("Current text: ${editor.text}") // Output: Hello, World!
 *
 *     // Undo the change
 *     undoRedoManager.undo()
 *     println("After undo: ${editor.text}") // Output: (empty string)
 *
 *     // Redo the change
 *     undoRedoManager.redo()
 *     println("After redo: ${editor.text}") // Output: Hello, World!
 *```
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
