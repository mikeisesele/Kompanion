package com.michael.kompanion.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.utils.kompanionSafeNullableReturnableOperation
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Extension function to convert date string to readable format
@RequiresApi(Build.VERSION_CODES.O)
fun String.toReadableDate(): String {
    // Define the input format (yyyy-MM-dd HH:mm:ss)
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    // Parse the string to a LocalDateTime object
    val dateTime = LocalDateTime.parse(this, inputFormatter)

    // Define the output format (d MMMM yyyy)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())

    // Return the formatted date string
    return dateTime.format(outputFormatter)
}


fun Date.formatToString(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(this)
}

fun String.toDate(format: String): Date? {

    val formatter = SimpleDateFormat(format, Locale.getDefault())

    return kompanionSafeNullableReturnableOperation(
        operation = {
            formatter.parse(this)
        },
        actionOnException = {
            it?.printStackTrace()
        },
    )
}

fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.time = this
    return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
}


@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime?.isWithinDaysFromToday(days: Long): Boolean = this?.let {
    val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
    it.isAfter(currentTime.minusDays(days)) && it.isBeforeOrEqual(currentTime)
} ?: false

@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.isBeforeOrEqual(other: ZonedDateTime): Boolean = isBefore(other) || isEqual(other)


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toReadable(): String {
    val date = LocalDate.now()

    val day = when {
        this.year == date.year && this.dayOfYear == date.dayOfYear -> {
            "Today"
        }

        this.year == date.year && this.dayOfYear == date.dayOfYear - 1 -> {
            "Yesterday"
        }

        else -> {
            "${this.dayOfWeek}"
        }
    }

    val hour = if (this.hour > 12) {
        this.hour - 12
    } else {
        this.hour
    }

    val minute = if (this.minute < 10) {
        "0${this.minute}"
    } else {
        this.minute
    }

    val amPm = if (this.hour >= 12) {
        "PM"
    } else {
        "AM"
    }

    return "$day: $hour:$minute $amPm"
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toReadableWeekDay(): String {
    val today = LocalDate.now()

    return when {
        this.year == today.year && this.dayOfYear == today.dayOfYear -> {
            "Today"
        }
        this.year == today.year && this.dayOfYear == today.dayOfYear - 1 -> {
            "Yesterday"
        }
        else -> {
            "${this.dayOfWeek}, ${this.month} ${this.dayOfMonth}, ${this.year}"
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toReadableTime(): String {
    val hour = if (this.hour > 12) {
        this.hour - 12
    } else {
        this.hour
    }

    val minute = if (this.minute < 10) {
        "0${this.minute}"
    } else {
        this.minute.toString()
    }

    val amPm = if (this.hour >= 12) {
        "PM"
    } else {
        "AM"
    }

    return "$hour:$minute $amPm"
}


/**
 * Converts LocalDate to a formatted string.
 * @param pattern The desired date format pattern.
 * @return The formatted date string.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.formatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Adds a specified number of days to the LocalDate.
 * @param days Number of days to add.
 * @return A new LocalDate instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.addDays(days: Long): LocalDate {
    return this.plusDays(days)
}

/**
 * Checks if the LocalDate is today.
 * @return True if it is today, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isToday(): Boolean {
    return this.isEqual(LocalDate.now())
}

/**
 * Checks if the LocalDate is in the past.
 * @return True if in the past, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isInPast(): Boolean {
    return this.isBefore(LocalDate.now())
}



/**
 * Converts LocalDateTime to a formatted string.
 * @param pattern The desired date format pattern.
 * @return The formatted date string.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.formatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Adds a specified number of days to the LocalDateTime.
 * @param days Number of days to add.
 * @return A new LocalDateTime instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.addDays(days: Long): LocalDateTime {
    return this.plusDays(days)
}

/**
 * Checks if the LocalDateTime is in the past.
 * @return True if in the past, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.isInPast(): Boolean {
    return this.isBefore(LocalDateTime.now())
}

/**
 * Converts Date to LocalDateTime.
 * @return The corresponding LocalDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), java.time.ZoneId.systemDefault())
}

/**
 * Converts Date to LocalDate.
 * @return The corresponding LocalDate.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate(): LocalDate {
    return this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
}


/**
 * Converts ZonedDateTime to a formatted string.
 * @param pattern The desired date format pattern.
 * @return The formatted date string.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.formatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Converts ZonedDateTime to LocalDateTime.
 * @return The corresponding LocalDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.toLocalDateTime(): LocalDateTime {
    return this.toLocalDateTime()
}

/**
 * Adds a specified number of hours to the ZonedDateTime.
 * @param hours Number of hours to add.
 * @return A new ZonedDateTime instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.addHours(hours: Long): ZonedDateTime {
    return this.plusHours(hours)
}

/**
 * Checks if the ZonedDateTime is in the past.
 * @return True if in the past, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.isInPast(): Boolean {
    return this.isBefore(ZonedDateTime.now())
}

/**
 * Returns the difference in days between this LocalDateTime and another.
 * @param other The other LocalDateTime to compare with.
 * @return The number of days difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.daysBetween(other: LocalDateTime): Long {
    return ChronoUnit.DAYS.between(this, other)
}

/**
 * Checks if the LocalDateTime is within a given range.
 * @param start The start of the range.
 * @param end The end of the range.
 * @return True if this LocalDateTime is within the range, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.isInRange(start: LocalDateTime, end: LocalDateTime): Boolean {
    return this.isEqual(start) || this.isEqual(end) || (this.isAfter(start) && this.isBefore(end))
}

/**
 * Converts LocalDateTime to Unix timestamp (seconds since epoch).
 * @return The Unix timestamp.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toUnixTimestamp(): Long {
    return this.atZone(java.time.ZoneId.systemDefault()).toEpochSecond()
}


/**
 * Returns the difference in months between this LocalDate and another.
 * @param other The other LocalDate to compare with.
 * @return The number of months difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.monthsBetween(other: LocalDate): Long {
    return ChronoUnit.MONTHS.between(this, other)
}

/**
 * Returns the difference in years between this LocalDate and another.
 * @param other The other LocalDate to compare with.
 * @return The number of years difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.yearsBetween(other: LocalDate): Long {
    return ChronoUnit.YEARS.between(this, other)
}

/**
 * Checks if this LocalDate is a weekend.
 * @return True if it is Saturday or Sunday, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isWeekend(): Boolean {
    return this.dayOfWeek.value == 6 || this.dayOfWeek.value == 7
}

/**
 * Checks if this Date is the same day as another Date.
 * @param other The other Date to compare with.
 * @return True if they are on the same day, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.isSameDay(other: Date): Boolean {
    val thisLocalDate = this.toLocalDate()
    val otherLocalDate = other.toLocalDate()
    return thisLocalDate.isEqual(otherLocalDate)
}

/**
 * Converts Date to ZonedDateTime.
 * @return The corresponding ZonedDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.toZonedDateTime(): ZonedDateTime {
    return this.toInstant().atZone(ZoneId.systemDefault())
}


/**
 * Returns the difference in hours between this ZonedDateTime and another.
 * @param other The other ZonedDateTime to compare with.
 * @return The number of hours difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.hoursBetween(other: ZonedDateTime): Long {
    return ChronoUnit.HOURS.between(this, other)
}

/**
 * Checks if this ZonedDateTime is in the same zone as another.
 * @param other The other ZonedDateTime to compare with.
 * @return True if they are in the same zone, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.isSameZone(other: ZonedDateTime): Boolean {
    return this.zone == other.zone
}

/**
 * Subtracts a specified number of days from the LocalDateTime.
 * @param days Number of days to subtract.
 * @return A new LocalDateTime instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.subtractDays(days: Long): LocalDateTime {
    return this.minusDays(days)
}

/**
 * Returns the start of the day for this LocalDateTime.
 * @return A LocalDateTime set to the start of the day.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.startOfDay(): LocalDateTime {
    return this.toLocalDate().atStartOfDay()
}

/**
 * Returns the end of the day for this LocalDateTime.
 * @return A LocalDateTime set to the end of the day.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.endOfDay(): LocalDateTime {
    return this.toLocalDate().atTime(23, 59, 59, 999999999)
}


/**
 * Returns the first day of the month for this LocalDate.
 * @return The first day of the month.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.firstDayOfMonth(): LocalDate {
    return this.withDayOfMonth(1)
}

/**
 * Returns the last day of the month for this LocalDate.
 * @return The last day of the month.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.lastDayOfMonth(): LocalDate {
    return this.withDayOfMonth(this.lengthOfMonth())
}

/**
 * Checks if this LocalDate is a leap year.
 * @return True if it is a leap year, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isLeapYear(): Boolean {
    return this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0)
}

/**
 * Converts Date to LocalDateTime with a specified ZoneId.
 * @param zoneId The ZoneId to use for the conversion.
 * @return The corresponding LocalDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return this.toInstant().atZone(zoneId).toLocalDateTime()
}

/**
 * Checks if this Date is within a specified number of days from now.
 * @param days Number of days to check against.
 * @return True if within the range, otherwise false.
 */
fun Date.isWithinDaysFromNow(days: Int): Boolean {
    val now = Date()
    val futureDate = Date(now.time + days * 24 * 60 * 60 * 1000)
    return this.after(now) && this.before(futureDate)
}

/**
 * Returns the number of milliseconds since the epoch for this Date.
 * @return The milliseconds since epoch.
 */
fun Date.toEpochMilliseconds(): Long {
    return this.time
}

/**
 * Converts ZonedDateTime to a LocalDate with the same date.
 * @return The corresponding LocalDate.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.toLocalDate(): LocalDate {
    return this.toLocalDate()
}

/**
 * Checks if this ZonedDateTime is in the future.
 * @return True if it is in the future, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.isInFuture(): Boolean {
    return this.isAfter(ZonedDateTime.now())
}

/**
 * Returns the number of days remaining in the month for this ZonedDateTime.
 * @return The number of days left in the month.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.daysRemainingInMonth(): Long {
    val lastDayOfMonth = this.toLocalDate().withDayOfMonth(this.toLocalDate().lengthOfMonth())
    return ChronoUnit.DAYS.between(this.toLocalDate(), lastDayOfMonth)
}

/**
 * Parses a date string to LocalDate using a specified format.
 * @param dateString The date string to parse.
 * @param format The format to use for parsing.
 * @return The corresponding LocalDate or null if parsing fails.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDate(dateString: String, format: String): LocalDate? {
    return try {
        LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format))
    } catch (e: DateTimeParseException) {
        null // or handle the exception as needed
    }
}

/**
 * Parses a date string to LocalDateTime using a specified format.
 * @param dateString The date string to parse.
 * @param format The format to use for parsing.
 * @return The corresponding LocalDateTime or null if parsing fails.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseLocalDateTime(dateString: String, format: String): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(format))
    } catch (e: DateTimeParseException) {
        null // or handle the exception as needed
    }
}

/**
 * Parses a date string to ZonedDateTime using a specified format.
 * @param dateString The date string to parse.
 * @param format The format to use for parsing.
 * @return The corresponding ZonedDateTime or null if parsing fails.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun parseZonedDateTime(dateString: String, format: String): ZonedDateTime? {
    return try {
        ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern(format))
    } catch (e: DateTimeParseException) {
        null // or handle the exception as needed
    }
}
