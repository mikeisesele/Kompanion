package com.michael.kompanion.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.michael.kompanion.utils.safeNullableReturnableOperation
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

    return safeNullableReturnableOperation(
        operation = {
            formatter.parse(this)
        },
        actionOnException = {
            it?.printStackTrace()
        },
        exceptionMessage = "SimpleDateFormat parser failed "
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
