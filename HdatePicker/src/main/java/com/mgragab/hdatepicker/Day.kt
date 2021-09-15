package com.mgragab.hdatepicker

import org.joda.time.DateTime
import java.util.*

/**
 * Created By MGRagab 15/09/2021
 */
class Day constructor(private var date: DateTime) {

    private var selected = false
    private var monthPattern = "MMMM YYYY"


    fun getDay(): String {
        return date.dayOfMonth.toString()
    }

    fun getWeekDay(): String {
        return date.toString("EEE", Locale.getDefault()).uppercase()
    }

    fun getMonth(): String? {
        return getMonth("")
    }

    fun getMonth(pattern: String): String? {
        if (pattern.isNotEmpty()) monthPattern = pattern
        return date.toString(monthPattern, Locale.getDefault())
    }

    fun getDate(): DateTime {
        return date.withTime(0, 0, 0, 0)
    }

    fun isToday(): Boolean {
        val today = DateTime().withTime(0, 0, 0, 0)
        return getDate().millis == today.millis
    }

    fun setSelected(selected: Boolean) {
        this.selected = selected
    }

    fun isSelected(): Boolean {
        return selected
    }
}