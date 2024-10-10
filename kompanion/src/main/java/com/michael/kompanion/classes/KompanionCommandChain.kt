package com.michael.kompanion.classes

/**
 * Command interface for actions.
 *
 * When to use:
 * - Use this interface when you want to encapsulate an action in a class to allow for execution and control of that action.
 * - This is useful for command pattern implementations, e.g., undo/redo actions, or actions queued to be executed.
 */
interface CommandChain {
    fun execute()
}

/**
 * Extension function to convert a lambda into a Command.
 *
 * When to use:
 * - This utility allows you to convert lambdas into `Command` objects. This is useful when you want to quickly define commands for execution.
 *
 * Example:
 * ```Kt
 * val printCommand = { println("Print command executed!") }.kompanionToCommand()
 * val anotherCommand = { println("Another command!") }.kompanionToCommand()
 *
 * kompanionExecuteCommands(printCommand, anotherCommand)
 * ```
 *
 * @return A Command instance that wraps the lambda.
 */
fun (() -> Unit).kompanionToCommandChain(): CommandChain {
    return object : CommandChain {
        override fun execute() {
            this@kompanionToCommandChain()
        }
    }
}

/**
 * Executes a series of Command instances.
 *
 * When to use:
 * - Use this function when you want to execute a sequence of commands in a controlled manner, allowing you to define the order of operations.
 *
 * Example:
 * ```Kt
 * val command1 = { println("Command 1 executed") }.kompanionToCommand()
 * val command2 = { println("Command 2 executed") }.kompanionToCommand()
 *
 * kompanionExecuteCommands(command1, command2)
 * ```
 *
 * @param commands Vararg of Command instances to execute.
 */
fun kompanionExecuteCommands(vararg commands: Command) {
    commands.forEach { it.execute() }
}