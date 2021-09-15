package com.mgragab.hdatepicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import org.joda.time.DateTime

/**
 * Created By MGRagab 15/09/2021
 */
class HorizontalPicker : LinearLayout, HorizontalPickerListener {
    private lateinit var vHover: View
    private lateinit var tvMonth: TextView
    private lateinit var tvToday: TextView
    private lateinit var listener: DatePickerListener
    private lateinit var monthListener: OnTouchListener
    private lateinit var rvDays: HorizontalPickerRecyclerView
    var days = 0
        private set
    var offset = 0
        private set
    private var mDateSelectedColor = -1
    private var mDateSelectedTextColor = -1
    private var mMonthAndYearTextColor = -1
    private var mTodayButtonTextColor = -1
    private var showTodayButton = true
    private var mMonthPattern = ""
    private var mTodayDateTextColor = -1
    private var mTodayDateBackgroundColor = -1
    private var mDayOfWeekTextColor = -1
    private var mUnselectedDayTextColor = -1

    constructor(context: Context) : super(context) {
        internInit()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        internInit()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        internInit()
    }

    private fun internInit() {
        days = NO_SETTED
        offset = NO_SETTED
    }

    fun setListener(listener: DatePickerListener): HorizontalPicker {
        this.listener = listener
        return this
    }

    fun setMonthListener(listener: OnTouchListener): HorizontalPicker {
        monthListener = listener
        return this
    }

    fun setDate(date: DateTime) {
        rvDays.post { rvDays.setDate(date) }
    }

    fun init() {
        inflate(context, R.layout.horizontal_picker, this)
        rvDays = findViewById<View>(R.id.rvDays) as HorizontalPickerRecyclerView
        val DEFAULT_DAYS_TO_PLUS = 120
        val finalDays = if (days == NO_SETTED) DEFAULT_DAYS_TO_PLUS else days
        val DEFAULT_INITIAL_OFFSET = 7
        val finalOffset = if (offset == NO_SETTED) DEFAULT_INITIAL_OFFSET else offset
        vHover = findViewById(R.id.vHover)
        tvMonth = findViewById<View>(R.id.tvMonth) as TextView
        if (this::monthListener.isInitialized) {
            tvMonth.isClickable = true
            tvMonth.setOnTouchListener(monthListener)
        }
        tvToday = findViewById<View>(R.id.tvToday) as TextView
        rvDays.setListener(this)
        tvToday.setOnClickListener(rvDays)
        tvMonth.setTextColor(
            if (mMonthAndYearTextColor != -1) mMonthAndYearTextColor else getColor(
                R.color.primaryTextColor
            )
        )
        tvToday.visibility = if (showTodayButton) VISIBLE else INVISIBLE
        tvToday.setTextColor(
            if (mTodayButtonTextColor != -1) mTodayButtonTextColor else getColor(
                R.color.colorPrimary
            )
        )
        val mBackgroundColor = backgroundColor
        setBackgroundColor(if (mBackgroundColor != Color.TRANSPARENT) mBackgroundColor else Color.WHITE)
        mDateSelectedColor =
            if (mDateSelectedColor == -1) getColor(R.color.colorPrimary) else mDateSelectedColor
        mDateSelectedTextColor =
            if (mDateSelectedTextColor == -1) Color.WHITE else mDateSelectedTextColor
        mTodayDateTextColor =
            if (mTodayDateTextColor == -1) getColor(R.color.primaryTextColor) else mTodayDateTextColor
        mDayOfWeekTextColor =
            if (mDayOfWeekTextColor == -1) getColor(R.color.secundaryTextColor) else mDayOfWeekTextColor
        mUnselectedDayTextColor =
            if (mUnselectedDayTextColor == -1) getColor(R.color.primaryTextColor) else mUnselectedDayTextColor
        rvDays.init(
            context,
            finalDays,
            finalOffset,
            mBackgroundColor,
            mDateSelectedColor,
            mDateSelectedTextColor,
            mTodayDateTextColor,
            mTodayDateBackgroundColor,
            mDayOfWeekTextColor,
            mUnselectedDayTextColor
        )
    }

    private fun getColor(colorId: Int): Int {
        return resources.getColor(colorId)
    }

    private val backgroundColor: Int
        get() {
            var color = Color.TRANSPARENT
            val background = background
            if (background is ColorDrawable) color = background.color
            return color
        }

    override fun post(action: Runnable): Boolean {
        return rvDays.post(action)
    }

    override fun onStopDraggingPicker() {
        if (vHover.visibility == VISIBLE) vHover.visibility = INVISIBLE
    }

    override fun onDraggingPicker() {
        if (vHover.visibility == INVISIBLE) vHover.visibility = VISIBLE
    }

    override fun onDateSelected(item: Day) {
        tvMonth.text = item.getMonth(mMonthPattern)
        if (showTodayButton) tvToday.visibility = if (item.isToday()) INVISIBLE else VISIBLE
        if (this::listener.isInitialized) {
            listener.onDateSelected(item.getDate())
        }
    }

    fun setDays(days: Int): HorizontalPicker {
        this.days = days
        return this
    }

    fun setOffset(offset: Int): HorizontalPicker {
        this.offset = offset
        return this
    }

    fun setDateSelectedColor(@ColorInt color: Int): HorizontalPicker {
        mDateSelectedColor = color
        return this
    }

    fun setDateSelectedTextColor(@ColorInt color: Int): HorizontalPicker {
        mDateSelectedTextColor = color
        return this
    }

    fun setMonthAndYearTextColor(@ColorInt color: Int): HorizontalPicker {
        mMonthAndYearTextColor = color
        return this
    }

    fun setTodayButtonTextColor(@ColorInt color: Int): HorizontalPicker {
        mTodayButtonTextColor = color
        return this
    }

    fun showTodayButton(show: Boolean): HorizontalPicker {
        showTodayButton = show
        return this
    }

    fun setTodayDateTextColor(color: Int): HorizontalPicker {
        mTodayDateTextColor = color
        return this
    }

    fun setTodayDateBackgroundColor(@ColorInt color: Int): HorizontalPicker {
        mTodayDateBackgroundColor = color
        return this
    }

    fun setDayOfWeekTextColor(@ColorInt color: Int): HorizontalPicker {
        mDayOfWeekTextColor = color
        return this
    }

    fun setUnselectedDayTextColor(@ColorInt color: Int): HorizontalPicker {
        mUnselectedDayTextColor = color
        return this
    }

    fun setMonthPattern(pattern: String): HorizontalPicker {
        mMonthPattern = pattern
        return this
    }

    companion object {
        private const val NO_SETTED = -1
    }
}