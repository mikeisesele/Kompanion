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


/**
 * Author: Michael Isesele
 * Year: 2024
 * LinkedIn: [https://www.linkedin.com/in/michael-isesele/]
 * GitHub: [https://github.com/mikeisesele]
 *
 */


/*
 * Converts a date string in the format "yyyy-MM-dd HH:mm:ss" to a more readable format "dd MMMM yyyy".
 * Requires API level 26 (Android O) or above as it uses LocalDateTime and DateTimeFormatter.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.kompanionToReadableDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val dateTime = LocalDateTime.parse(this, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return dateTime.format(outputFormatter)
}

/*
 * Converts a Date object into a formatted string in the specified format.
 * The format follows the SimpleDateFormat patterns (e.g., "dd/MM/yyyy").
 */
fun Date.kompanionFormatToString(format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(this)
}

/*
 * Parses a string into a Date object based on the specified format.
 * The format follows the SimpleDateFormat patterns (e.g., "dd/MM/yyyy").
 */
fun String.kompanionToDate(format: String): Date? {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return kompanionSafeNullableReturnableOperation(
        operation = { formatter.parse(this) },
        actionOnException = { it?.printStackTrace() }
    )
}

/*
 * Checks if the current Date object represents today's date.
 * Compares both the year and day of the year to determine if the date is today.
 */
fun Date.kompanionIsToday(): Boolean {
    val today = Calendar.getInstance()
    val date = Calendar.getInstance()
    date.time = this
    return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
}

/*
 * Checks if a ZonedDateTime is within a specified number of days from today.
 * Returns false if the ZonedDateTime is null.
 * Requires API level 26 (Android O) or above.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime?.kompanionIsWithinDaysFromToday(days: Long): Boolean = this?.let {
    val currentTime = ZonedDateTime.now(ZoneId.systemDefault())
    it.isAfter(currentTime.minusDays(days)) && it.kompanionIsBeforeOrEqual(currentTime)
} ?: false

/*
 * Checks if the current ZonedDateTime is before or equal to another ZonedDateTime.
 * Requires API level 26 (Android O) or above.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionIsBeforeOrEqual(other: ZonedDateTime): Boolean =
    isBefore(other) || isEqual(other)

/*
 * Converts a LocalDateTime object to a human-readable format indicating whether the date is
 * today, yesterday, or provides the name of the day (e.g., "Monday").
 * It also formats the time in 12-hour format with AM/PM.
 * Requires API level 26 (Android O) or above.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionToReadable(): String {
    val date = LocalDate.now()

    val day = when {
        this.year == date.year && this.dayOfYear == date.dayOfYear -> "Today"
        this.year == date.year && this.dayOfYear == date.dayOfYear - 1 -> "Yesterday"
        else -> "${this.dayOfWeek}"
    }

    val hour = if (this.hour > 12) this.hour - 12 else this.hour
    val minute = if (this.minute < 10) "0${this.minute}" else this.minute
    val amPm = if (this.hour >= 12) "PM" else "AM"

    return "$day: $hour:$minute $amPm"
}

/*
 * Returns a readable string representation of the LocalDateTime, showing whether it's
 * today, yesterday, or giving the full date in the format of "DayOfWeek, Month Day, Year".
 * Requires API level 26 (Android O) or above.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionToReadableWeekDay(): String {
    val today = LocalDate.now()

    return when {
        this.year == today.year && this.dayOfYear == today.dayOfYear -> "Today"
        this.year == today.year && this.dayOfYear == today.dayOfYear - 1 -> "Yesterday"
        else -> "${this.dayOfWeek}, ${this.month} ${this.dayOfMonth}, ${this.year}"
    }
}

/*
 * Converts a LocalDateTime object to a readable time string in 12-hour format
 * with AM/PM (e.g., "10:30 AM").
 * Requires API level 26 (Android O) or above.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionToReadableTime(): String {
    val hour = if (this.hour > 12) this.hour - 12 else this.hour
    val minute = if (this.minute < 10) "0${this.minute}" else this.minute.toString()
    val amPm = if (this.hour >= 12) "PM" else "AM"

    return "$hour:$minute $amPm"
}

/**
 * Converts LocalDate to a formatted string.
 * @param pattern The desired date format pattern.
 * @return The formatted date string.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionFormatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Adds a specified number of days to the LocalDate.
 * @param days Number of days to add.
 * @return A new LocalDate instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionAddDays(days: Long): LocalDate {
    return this.plusDays(days)
}

/**
 * Checks if the LocalDate is today.
 * @return True if it is today, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionIsToday(): Boolean {
    return this.isEqual(LocalDate.now())
}

/**
 * Checks if the LocalDate is in the past.
 * @return True if in the past, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionIsInPast(): Boolean {
    return this.isBefore(LocalDate.now())
}



/**
 * Converts LocalDateTime to a formatted string.
 * @param pattern The desired date format pattern.
 * @return The formatted date string.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionFormatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Adds a specified number of days to the LocalDateTime.
 * @param days Number of days to add.
 * @return A new LocalDateTime instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionAddDays(days: Long): LocalDateTime {
    return this.plusDays(days)
}

/**
 * Checks if the LocalDateTime is in the past.
 * @return True if in the past, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionIsInPast(): Boolean {
    return this.isBefore(LocalDateTime.now())
}

/**
 * Converts Date to LocalDateTime.
 * @return The corresponding LocalDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.kompanionToLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(this.toInstant(), java.time.ZoneId.systemDefault())
}

/**
 * Converts Date to LocalDate.
 * @return The corresponding LocalDate.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.kompanionToLocalDate(): LocalDate {
    return this.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
}


/**
 * Converts ZonedDateTime to a formatted string.
 * @param pattern The desired date format pattern.
 * @return The formatted date string.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionFormatToString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

/**
 * Converts ZonedDateTime to LocalDateTime.
 * @return The corresponding LocalDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionToLocalDateTime(): LocalDateTime {
    return this.toLocalDateTime()
}

/**
 * Adds a specified number of hours to the ZonedDateTime.
 * @param hours Number of hours to add.
 * @return A new ZonedDateTime instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionAddHours(hours: Long): ZonedDateTime {
    return this.plusHours(hours)
}

/**
 * Checks if the ZonedDateTime is in the past.
 * @return True if in the past, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionIsInPast(): Boolean {
    return this.isBefore(ZonedDateTime.now())
}

/**
 * Returns the difference in days between this LocalDateTime and another.
 * @param other The other LocalDateTime to compare with.
 * @return The number of days difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionDaysBetween(other: LocalDateTime): Long {
    return ChronoUnit.DAYS.between(this, other)
}

/**
 * Checks if the LocalDateTime is within a given range.
 * @param start The start of the range.
 * @param end The end of the range.
 * @return True if this LocalDateTime is within the range, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionIsInRange(start: LocalDateTime, end: LocalDateTime): Boolean {
    return this.isEqual(start) || this.isEqual(end) || (this.isAfter(start) && this.isBefore(end))
}

/**
 * Converts LocalDateTime to Unix timestamp (seconds since epoch).
 * @return The Unix timestamp.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionToUnixTimestamp(): Long {
    return this.atZone(java.time.ZoneId.systemDefault()).toEpochSecond()
}


/**
 * Returns the difference in months between this LocalDate and another.
 * @param other The other LocalDate to compare with.
 * @return The number of months difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionMonthsBetween(other: LocalDate): Long {
    return ChronoUnit.MONTHS.between(this, other)
}

/**
 * Returns the difference in years between this LocalDate and another.
 * @param other The other LocalDate to compare with.
 * @return The number of years difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionYearsBetween(other: LocalDate): Long {
    return ChronoUnit.YEARS.between(this, other)
}

/**
 * Checks if this LocalDate is a weekend.
 * @return True if it is Saturday or Sunday, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionIsWeekend(): Boolean {
    return this.dayOfWeek.value == 6 || this.dayOfWeek.value == 7
}

/**
 * Checks if this Date is the same day as another Date.
 * @param other The other Date to compare with.
 * @return True if they are on the same day, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.kompanionIsSameDay(other: Date): Boolean {
    val thisLocalDate = this.kompanionToLocalDate()
    val otherLocalDate = other.kompanionToLocalDate()
    return thisLocalDate.isEqual(otherLocalDate)
}

/**
 * Converts Date to ZonedDateTime.
 * @return The corresponding ZonedDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.kompanionToZonedDateTime(): ZonedDateTime {
    return this.toInstant().atZone(ZoneId.systemDefault())
}


/**
 * Returns the difference in hours between this ZonedDateTime and another.
 * @param other The other ZonedDateTime to compare with.
 * @return The number of hours difference.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionHoursBetween(other: ZonedDateTime): Long {
    return ChronoUnit.HOURS.between(this, other)
}

/**
 * Checks if this ZonedDateTime is in the same zone as another.
 * @param other The other ZonedDateTime to compare with.
 * @return True if they are in the same zone, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionIsSameZone(other: ZonedDateTime): Boolean {
    return this.zone == other.zone
}

/**
 * Subtracts a specified number of days from the LocalDateTime.
 * @param days Number of days to subtract.
 * @return A new LocalDateTime instance.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionSubtractDays(days: Long): LocalDateTime {
    return this.minusDays(days)
}

/**
 * Returns the start of the day for this LocalDateTime.
 * @return A LocalDateTime set to the start of the day.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionStartOfDay(): LocalDateTime {
    return this.toLocalDate().atStartOfDay()
}

/**
 * Returns the end of the day for this LocalDateTime.
 * @return A LocalDateTime set to the end of the day.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionEndOfDay(): LocalDateTime {
    return this.toLocalDate().atTime(23, 59, 59, 999999999)
}


/**
 * Returns the first day of the month for this LocalDate.
 * @return The first day of the month.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionFirstDayOfMonth(): LocalDate {
    return this.withDayOfMonth(1)
}

/**
 * Returns the last day of the month for this LocalDate.
 * @return The last day of the month.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionLastDayOfMonth(): LocalDate {
    return this.withDayOfMonth(this.lengthOfMonth())
}

/**
 * Checks if this LocalDate is a leap year.
 * @return True if it is a leap year, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.kompanionIsLeapYear(): Boolean {
    return this.year % 4 == 0 && (this.year % 100 != 0 || this.year % 400 == 0)
}

/**
 * Converts Date to LocalDateTime with a specified ZoneId.
 * @param zoneId The ZoneId to use for the conversion.
 * @return The corresponding LocalDateTime.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Date.kompanionToLocalDateTime(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return this.toInstant().atZone(zoneId).toLocalDateTime()
}

/**
 * Checks if this Date is within a specified number of days from now.
 * @param days Number of days to check against.
 * @return True if within the range, otherwise false.
 */
fun Date.kompanionIsWithinDaysFromNow(days: Int): Boolean {
    val now = Date()
    val futureDate = Date(now.time + days * 24 * 60 * 60 * 1000)
    return this.after(now) && this.before(futureDate)
}

/**
 * Returns the number of milliseconds since the epoch for this Date.
 * @return The milliseconds since epoch.
 */
fun Date.kompanionToEpochMilliseconds(): Long {
    return this.time
}

/**
 * Converts ZonedDateTime to a LocalDate with the same date.
 * @return The corresponding LocalDate.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionToLocalDate(): LocalDate {
    return this.toLocalDate()
}

/**
 * Checks if this ZonedDateTime is in the future.
 * @return True if it is in the future, otherwise false.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionIsInFuture(): Boolean {
    return this.isAfter(ZonedDateTime.now())
}

/**
 * Returns the number of days remaining in the month for this ZonedDateTime.
 * @return The number of days left in the month.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ZonedDateTime.kompanionDaysRemainingInMonth(): Long {
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
fun kompanionParseLocalDate(dateString: String, format: String): LocalDate? {
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
fun kompanionParseLocalDateTime(dateString: String, format: String): LocalDateTime? {
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
fun kompanionParseZonedDateTime(dateString: String, format: String): ZonedDateTime? {
    return try {
        ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern(format))
    } catch (e: DateTimeParseException) {
        null // or handle the exception as needed
    }
}


// Enum class for different date patterns
enum class DatePattern(val pattern: String) {
    /**
     * Format: dd/MM/yyyy
     * Example Output: 08/10/2024
     */
    DD_MM_YYYY("dd/MM/yyyy"),

    /**
     * Format: dd-MM-yyyy
     * Example Output: 08-10-2024
     */
    DD_DASH_MM_DASH_YYYY("dd-MM-yyyy"),

    /**
     * Format: MM/dd/yyyy
     * Example Output: 10/08/2024
     */
    MM_DD_YYYY("MM/dd/yyyy"),

    /**
     * Format: yyyy/MM/dd
     * Example Output: 2024/10/08
     */
    YYYY_MM_DD("yyyy/MM/dd"),

    /**
     * Format: dd MMMM yyyy
     * Example Output: 08 October 2024
     */
    DD_MMMM_YYYY("dd MMMM yyyy"),

    /**
     * Format: MMMM dd, yyyy
     * Example Output: October 08, 2024
     */
    MMMM_DD_YYYY("MMMM dd, yyyy"),

    /**
     * Format: yyyy/MM/dd HH:mm
     * Example Output: 2024/10/08 15:30
     */
    YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),

    /**
     * Format: dd MMM yyyy
     * Example Output: 08 Oct 2024
     */
    DD_MMM_YYYY("dd MMM yyyy"),

    /**
     * Format: hh:mm a, dd/MM/yyyy
     * Example Output: 03:30 PM, 08/10/2024
     */
    HH_MM_AAAA("hh:mm a, dd/MM/yyyy"),

    /**
     * Format: dd/MM/yy
     * Example Output: 08/10/24
     */
    DD_MM_YY("dd/MM/yy"),

    /**
     * Format: yyyy-MM-dd
     * Example Output: 2024-10-08
     */
    ISO_LOCAL_DATE("yyyy-MM-dd"),

    /**
     * Format: yyyy-MM-dd'T'HH:mm:ss
     * Example Output: 2024-10-08T15:30:00
     */
    ISO_LOCAL_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss"),

    /**
     * Format: EEE, dd MMM yyyy HH:mm:ss z
     * Example Output: Tue, 08 Oct 2024 15:30:00 UTC
     */
    RFC_1123_DATE_TIME("EEE, dd MMM yyyy HH:mm:ss z"),

    /**
     * Format: dd/MM/yyyy HH:mm
     * Example Output: 08/10/2024 15:30
     */
    DD_MM_YYYY_HH_MM("dd/MM/yyyy HH:mm"),

    /**
     * Format: MM/dd/yyyy HH:mm
     * Example Output: 10/08/2024 15:30
     */
    MM_DD_YYYY_HH_MM("MM/dd/yyyy HH:mm"),

    /**
     * Format: dd 'of' MMMM, yyyy
     * Example Output: 08 of October, 2024
     */
    DD_MONTH_YYYY("dd 'of' MMMM, yyyy"),

    /**
     * Format: MMMM dd, yyyy
     * Example Output: October 08, 2024
     */
    MONTH_DAY_YEAR("MMMM dd, yyyy"),

    /**
     * Format: dd MMMM
     * Example Output: 08 October
     */
    DD_MMMM("dd MMMM"),

    /**
     * Format: HH:mm
     * Example Output: 15:30
     */
    HH_MM("HH:mm"),

    /**
     * Format: hh:mm a
     * Example Output: 03:30 PM
     */
    HH_MM_AAA("hh:mm a"),

    /**
     * Format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * Example Output: 2024-10-08T15:30:00.000Z
     */
    ISO_INSTANT("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),

    /**
     * Format: EEEE, dd MMMM yyyy
     * Example Output: Tuesday, 08 October 2024
     */
    FULL_DATE("EEEE, dd MMMM yyyy"),

    /**
     * Format: M/d/yy
     * Example Output: 10/8/24
     */
    SHORT_DATE("M/d/yy"),

    /**
     * Format: MMMM dd, yyyy
     * Example Output: October 08, 2024
     */
    LONG_DATE("MMMM dd, yyyy"),

    /**
     * Format: yyyy-MM-dd HH:mm:ss
     * Example Output: 2024-10-08 15:30:00
     */
    TIMESTAMP("yyyy-MM-dd HH:mm:ss"),

    /**
     * Format: dd.MM.yyyy HH:mm:ss
     * Example Output: 08.10.2024 15:30:00
     */
    CUSTOM_FORMAT("dd.MM.yyyy HH:mm:ss")
}


/**
 * Formats a LocalDateTime to a String based on the specified date pattern.
 * @param datePattern The date pattern to use for formatting, defined in the DatePattern enum.
 * @return A formatted string representing the date, or a relative time description if the date is within the last day.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionFormatForDisplay(datePattern: DatePattern): String {
    val now = LocalDateTime.now()
    val daysDifference = ChronoUnit.DAYS.between(this, now)

    return if (daysDifference >= 1) {
        // Use the selected pattern for formatting
        val formatter = DateTimeFormatter.ofPattern(datePattern.pattern)
        this.format(formatter)
    } else {
        // Format as hours
        val hoursDifference = ChronoUnit.HOURS.between(this, now)
        if (hoursDifference >= 1) {
            "$hoursDifference hour${if (hoursDifference > 1) "s" else ""} ago"
        } else {
            "Just now" // If it's less than an hour
        }
    }
}

// Enum class for time direction
enum class TimeDirection {
    FUTURE,
    PAST
}

/**
 * Generates a random LocalDateTime within the specified number of days in the past or future.
 * @param days The number of days for generating a random time.
 * @param direction Specifies whether to generate the time in the future or the past.
 * @return A random LocalDateTime on the specified day.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionRandomTimeWithinDays(days: Int, direction: TimeDirection): LocalDateTime {

    return if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return when (direction) {
            TimeDirection.FUTURE -> {
                // Calculate the target date in the future
                val targetDate = this.plusDays(days.toLong())
                // Generate random hour and minute
                val randomHour = 24.kompanionRandom(0) // 0 to 23 inclusive
                val randomMinute = 60.kompanionRandom(0) // 0 to 59 inclusive
                targetDate.withHour(randomHour).withMinute(randomMinute).withSecond(0).withNano(0)
            }

            TimeDirection.PAST -> {
                // Calculate the target date in the past
                val targetDate = this.minusDays(days.toLong())
                // Generate random hour and minute
                val randomHour = 24.kompanionRandom(0) // 0 to 23 inclusive
                val randomMinute = 60.kompanionRandom(0) // 0 to 59 inclusive
                targetDate.withHour(randomHour).withMinute(randomMinute).withSecond(0).withNano(0)
            }
        }
    } else {
        LocalDateTime.now()
    }
}


/**
 * Generates a random LocalDateTime within a specified number of days.
 * @param days The number of days for generating a random date.
 * @param direction Specifies whether to generate the date in the future or the past.
 * @return A random LocalDateTime on a day within the specified range.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.kompanionRandomDayWithin(days: Int, direction: TimeDirection): LocalDateTime {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Calculate the range of dates
        val targetDate = when (direction) {
            TimeDirection.FUTURE -> this.plusDays(days.toLong())
            TimeDirection.PAST -> this.minusDays(days.toLong())
        }

        // Get the starting date based on the direction
        val startingDate = when (direction) {
            TimeDirection.FUTURE -> this // Today for future
            TimeDirection.PAST -> targetDate // The past target date
        }

        // Calculate the number of days between the two dates
        val daysBetween = ChronoUnit.DAYS.between(startingDate, targetDate).toInt()

        // Generate a random day offset within the range
        val randomDayOffset = (daysBetween + 1).kompanionRandom(0)

        // Return a random date within the specified range
        startingDate.plusDays(randomDayOffset.toLong()).withHour(0).withMinute(0).withSecond(0).withNano(0)
    } else {
        LocalDateTime.now() // Fallback for devices below API level 26
    }
}
