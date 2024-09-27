import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/*
 * Deep equals for data classes using reflection.
 *
 * data class Person(val name: String, val age: Int, val friends: List<Person>)
 *
 * val person1 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 * val person2 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 *
 * println(deepEquals(person1, person2))  // Output: true
 */
fun <T : Any> kompanionDeepEquals(obj1: T, obj2: T): Boolean {
    val kClass = obj1::class
    if (obj1::class != obj2::class) return false

    for (property in kClass.memberProperties) {
        val value1 = (property as KProperty1<T, *>).get(obj1)
        val value2 = (property as KProperty1<T, *>).get(obj2)

        if (value1 is List<*> && value2 is List<*>) {
            if (!value1.zip(value2).all { (v1, v2) -> kompanionDeepEquals(v1, v2) }) return false
        } else if (value1 is Map<*, *> && value2 is Map<*, *>) {
            if (!value1.keys.all { key -> kompanionDeepEquals(value1[key], value2[key]) }) return false
        } else if (value1 != value2) {
            return false
        }
    }
    return true
}


/*
 * Deep equals for data classes using reflection.
 *
 * data class Person(val name: String, val age: Int, val friends: List<Person>)
 *
 * val person1 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 * val person2 = Person("John", 25, listOf(Person("Doe", 30, emptyList())))
 *
 * println(deepEquals(person1, person2))  // Output: true
 */
fun <T : Any> kompanionDeepEquals(obj1: T?, obj2: T?): Boolean {
    if (obj1 == null || obj2 == null) return obj1 === obj2
    return kompanionDeepEquals(obj1, obj2)
}




/**
 * Deep copy for data classes using reflection.
 *
 * data class Address(val city: String)
 * data class Person(val name: String, val address: Address)
 *
 * val person1 = Person("John", Address("New York"))
 * val person2 = person1.deepCopy()
 *
 * person2.address.city = "San Francisco"
 *
 * println(person1) // Output: Person(name=John, address=Address(city=New York))
 * println(person2) // Output: Person(name=John, address=Address(city=San Francisco))
 *
 *
 */

/**
 * Deep copy for data classes using reflection.
 */
fun <T : Any> T.kompanionDeepCopyObject(): T {
    val kClass: KClass<T> = this::class as KClass<T>
    val constructor = kClass.primaryConstructor
        ?: throw IllegalArgumentException("Class must have a primary constructor")

    val args = constructor.parameters.map { param ->
        val value = kClass.memberProperties.first { it.name == param.name }.get(this)
        when (value) {
            is List<*> -> value.map { it?.deepCopy() }
            is Map<*, *> -> value.mapValues { it.value?.deepCopy() }
            is Set<*> -> value.map { it?.deepCopy() }.toSet()
            else -> value
        }
    }.toTypedArray()

    return constructor.call(*args)
}

// Extension function for deep copy of nullable types
fun <T : Any> T?.deepCopy(): T? = this?.deepCopy()



/**
 * Command interface for actions.
 */
interface Command {
    fun execute()
}

/**
 * Extension function to convert a lambda into a Command.
 *
 * val printCommand = { println("Print command executed!") }.toCommand()
 * val anotherCommand = { println("Another command!") }.toCommand()
 *
 * kompanionExecuteCommands(printCommand, anotherCommand)
 *
 */
fun (() -> Unit).kompanionToCommand(): Command {
    return object : Command {
        override fun execute() {
            this@kompanionToCommand()
        }
    }
}

/**
 * Extension function to convert a lambda into a Command.
 *
 * val printCommand = { println("Print command executed!") }.toCommand()
 * val anotherCommand = { println("Another command!") }.toCommand()
 *
 * kompanionExecuteCommands(printCommand, anotherCommand)
 *
 */
fun kompanionExecuteCommands(vararg commands: Command) {
    commands.forEach { it.execute() }
}
