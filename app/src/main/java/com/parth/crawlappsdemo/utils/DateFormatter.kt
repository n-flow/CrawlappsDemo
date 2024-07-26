@file:Suppress("unused")

package com.parth.crawlappsdemo.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern


enum class DateFormats(val dateFormat: String) {
    FORMAT_1("yyyy-MM-dd HH:mm:ss"), FORMAT_2("yyyy-MM-dd HH:mm"),
    FORMAT_3("yyyyMMdd-HHmmss"), FORMAT_4("yyyy-MM-dd"),
    FORMAT_5("MM-dd"), FORMAT_6("HH:mm:ss"),
    FORMAT_7("HH:mm"), FORMAT_8("yyyy-MM"),
    FORMAT_9("yyyy/MM/dd HH:mm:ss"), FORMAT_10("HH:mm a"),
    FORMAT_11("EEE, d MMM yyyy"), FORMAT_12("EEE-ddMMMyyyy"),
    FORMAT_13("MM/dd/yyyy"), FORMAT_14("d MMM, yyyy"),
    FORMAT_15("dd-MM-yyyy"), FORMAT_16("yyyy/MM/dd"),
    FORMAT_17("yyyy-MM-dd"), FORMAT_18("dd/MM/yyyy"),
    FORMAT_19("hh:mm a"), FORMAT_20("hh:mm aa"),
    FORMAT_21("dd, MMM yyyy"), FORMAT_22("dd MMM yyyy"),
    FORMAT_23("dd MMM, yyyy hh:mm::ss aa"), FORMAT_24("dd, MMM"),
    FORMAT_25("E, dd MMM"), FORMAT_26("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    FORMAT_27("MMMM"), FORMAT_28("dd"),
    FORMAT_29("yyyy-MM-dd'T'HH:mm:ss.000000"), FORMAT_30("MMMM dd, yyyy"),
    FORMAT_31("MMM dd"), FORMAT_32("dd MMM, hh:mm aa"),
    FORMAT_33("yyyy-MM-dd'T'HH:mm:ss.SSS'GMT'"), FORMAT_34("dd/MMM/yyyy"),
    FORMAT_35("MMM dd, yyyy"), FORMAT_36("mm:ss"), FORMAT_37("yyyy-MMM-dd");
}

fun isDate(date: String): Boolean {
    val reg =
        "^((\\d{2}(([02468][048])|([13579][26]))-?((((0?" + "[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))" +
                "|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|" +
                "(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12" +
                "35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))" +
                "-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))" +
                "-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[" +
                "1-9])|(1[0-9])|(2[0-8]))))))"
    val p = Pattern.compile(reg)
    return p.matcher(date).matches()
}

fun getToday(
    year: Int? = null,
    month: Int? = null,
    date: Int? = null,
    hourOfDay: Int? = null,
    minute: Int? = null,
    second: Int? = null,
): Date {

    val today = Calendar.getInstance()
    today.set(Calendar.YEAR, year ?: today.get(Calendar.YEAR))
    today.set(Calendar.MONTH, month ?: today.get(Calendar.MONTH))
    today.set(Calendar.DATE, date ?: today.get(Calendar.DATE))
    today.set(Calendar.HOUR_OF_DAY, hourOfDay ?: today.get(Calendar.HOUR_OF_DAY))
    today.set(Calendar.MINUTE, minute ?: today.get(Calendar.MINUTE))
    today.set(Calendar.SECOND, second ?: today.get(Calendar.SECOND))

    return today.time
}

fun getUtcTimeFormat(date: Date, dateFormat: DateFormats): String {
    val formatter = SimpleDateFormat(dateFormat.dateFormat, Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(date)
}

fun stringToDate(date: String, inputFormat: DateFormats): Date? {
    var d: Date? = null
    val formatter = SimpleDateFormat(inputFormat.dateFormat, Locale.ENGLISH)
    try {
        d = formatter.parse(date)
    } catch (e: Exception) {
        e.printStackTrace(System.err)
    }
    return d
}

fun dateToString(date: Date, outputFormat: DateFormats): String {
    var result = ""
    val formatter = SimpleDateFormat(outputFormat.dateFormat, Locale.ENGLISH)
    try {
        result = formatter.format(date)
    } catch (e: Exception) {
        e.printStackTrace(System.err)
    }
    return result
}

fun dateToString(time: Long, outputFormat: DateFormats): String {
    val date = Date(time)
    var result = ""
    val formatter = SimpleDateFormat(outputFormat.dateFormat, Locale.ENGLISH)
    try {
        result = formatter.format(date)
    } catch (e: Exception) {
        e.printStackTrace(System.err)
    }
    return result
}

fun Context.openDatePickerDialog(dateSelected: (Long) -> Unit = {}) {
    val myCalendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        this, { _, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            dateSelected.invoke(myCalendar.time.time)
        }, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH]
    )
    datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
    datePicker.show()
}