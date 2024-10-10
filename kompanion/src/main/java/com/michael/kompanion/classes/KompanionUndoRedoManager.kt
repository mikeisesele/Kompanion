package com.michael.kompanion.classes

/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/**
 * Command interface for executing, undoing, and redoing operations.
 *
 * When to use:
 * - Implement this interface for any action that can be executed, undone, and optionally redone.
 * - Suitable for text editors, drawing applications, or any system that needs undo/redo functionality.
 *
 * Example:
 * ```Kt
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
 * ```
 */
interface Command {
    fun execute()
    fun undo()
}

/**
 * A manager for handling command execution with undo and redo functionality.
 *
 * When to use:
 * - Use this class when you need to manage a sequence of operations where users can undo or redo actions.
 * - This is useful in text editing, graphics editing, or similar scenarios where users might want to reverse or replay actions.
 *
 * How it works:
 * - Commands are executed and stored in the undo stack.
 * - When an undo action is requested, the last command is popped from the undo stack, its `undo()` method is called, and it is moved to the redo stack.
 * - Redo pops the last command from the redo stack, calls its `execute()` method again, and moves it back to the undo stack.
 *
 * Example usage:
 * ```Kt
 * class TextEditor {
 *     var text: String = ""
 * }
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
 * val editor = TextEditor()
 * val undoRedoManager = KompanionUndoRedoManager()
 *
 * // Execute a command to change the text
 * val changeTextCommand = ChangeTextCommand(editor, "Hello, World!")
 * undoRedoManager.executeCommand(changeTextCommand)
 *
 * println("Current text: ${editor.text}") // Output: Hello, World!
 *
 * // Undo the change
 * undoRedoManager.undo()
 * println("After undo: ${editor.text}") // Output: (empty string)
 *
 * // Redo the change
 * undoRedoManager.redo()
 * println("After redo: ${editor.text}") // Output: Hello, World!
 * ```
 */
class KompanionUndoRedoManager {
    private val undoStack = mutableListOf<Command>()
    private val redoStack = mutableListOf<Command>()

    /**
     * Executes a command, adds it to the undo stack, and clears the redo stack.
     *
     * @param command The command to execute.
     *
     * When to use:
     * - Call this method to perform a new action (like changing text, drawing, etc.).
     * - This will also clear any redo history since the state has changed.
     *
     * Example:
     * ```Kt
     * undoRedoManager.executeCommand(changeTextCommand)
     * ```
     */
    fun executeCommand(command: Command) {
        command.execute()
        undoStack.add(command)
        redoStack.clear()
    }

    /**
     * Undoes the last executed command and moves it to the redo stack.
     *
     * When to use:
     * - Call this method when you want to undo the last action performed.
     *
     * Example:
     * ```Kt
     * undoRedoManager.undo()
     * ```
     */
    fun undo() {
        if (undoStack.isNotEmpty()) {
            val command = undoStack.removeLast()
            command.undo()
            redoStack.add(command)
        }
    }

    /**
     * Redoes the last undone command and moves it back to the undo stack.
     *
     * When to use:
     * - Call this method when you want to redo the last action that was undone.
     *
     * Example:
     * ```Kt
     * undoRedoManager.redo()
     * ```
     */
    fun redo() {
        if (redoStack.isNotEmpty()) {
            val command = redoStack.removeLast()
            command.execute()
            undoStack.add(command)
        }
    }
}
