package com.mgragab.hdatepicker

import org.joda.time.DateTime

/**
 * Created By MGRagab 15/09/2021
 */
interface DatePickerListener {
    fun onDateSelected(dateSelected: DateTime)
}