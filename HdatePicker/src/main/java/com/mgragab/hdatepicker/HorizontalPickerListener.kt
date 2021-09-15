package com.mgragab.hdatepicker

/**
 * Created By MGRagab 15/09/2021
 */
interface HorizontalPickerListener {
    fun onStopDraggingPicker()
    fun onDraggingPicker()
    fun onDateSelected(item: Day)

}