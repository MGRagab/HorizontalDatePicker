package com.mgragab.hdatepicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created By MGRagab 15/09/2021
 */
class HorizontalPickerRecyclerView : RecyclerView, OnItemClickedListener, View.OnClickListener {
    private lateinit var adapter: HorizontalPickerAdapter
    private var lastPosition = 0
    private lateinit var layoutManager: LinearLayoutManager
    private var itemWidth = 0f
    private lateinit var listener: HorizontalPickerListener
    private var offset = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(
        context, attrs
    )

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    )

    fun init(
        context: Context?,
        daysToPlus: Int,
        initialOffset: Int,
        mBackgroundColor: Int,
        mDateSelectedColor: Int,
        mDateSelectedTextColor: Int,
        mTodayDateTextColor: Int,
        mTodayDateBackgroundColor: Int,
        mDayOfWeekTextColor: Int,
        mUnselectedDayTextColor: Int
    ) {
        offset = initialOffset
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        setLayoutManager(layoutManager)
        post {
            itemWidth = (measuredWidth / 7).toFloat()
            adapter = HorizontalPickerAdapter(
                itemWidth.toInt(),
                this@HorizontalPickerRecyclerView,
                getContext(),
                daysToPlus,
                initialOffset,
                mBackgroundColor,
                mDateSelectedColor,
                mDateSelectedTextColor,
                mTodayDateTextColor,
                mTodayDateBackgroundColor,
                mDayOfWeekTextColor,
                mUnselectedDayTextColor
            )
            setAdapter(adapter)
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this@HorizontalPickerRecyclerView)
            removeOnScrollListener(onScrollListener)
            addOnScrollListener(onScrollListener)
        }
    }

    private val onScrollListener: OnScrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                SCROLL_STATE_IDLE -> {
                    listener.onStopDraggingPicker()
                    val position = (computeHorizontalScrollOffset() / itemWidth + 3.5).toInt()
                    if (position != -1 && position != lastPosition) {
                        selectItem(true, position)
                        selectItem(false, lastPosition)
                        lastPosition = position
                    }
                }
                SCROLL_STATE_DRAGGING -> listener.onDraggingPicker()
            }
        }

    }

    private fun selectItem(isSelected: Boolean, position: Int) {
        adapter.getItem(position).setSelected(isSelected)
        adapter.notifyItemChanged(position)
        if (isSelected) {
            listener.onDateSelected(adapter.getItem(position))
        }
    }

    fun setListener(listener: HorizontalPickerListener) {
        this.listener = listener
    }

    override fun onClickView(v: View, adapterPosition: Int) {
        if (adapterPosition != lastPosition) {
            selectItem(true, adapterPosition)
            selectItem(false, lastPosition)
            lastPosition = adapterPosition
        }
    }

    override fun onClick(v: View) {
        setDate(DateTime())
    }

    override fun smoothScrollToPosition(position: Int) {
        val smoothScroller: SmoothScroller = CenterSmoothScroller(context)
        smoothScroller.targetPosition = position
        post { layoutManager.startSmoothScroll(smoothScroller) }
    }

    fun setDate(date: DateTime) {
        val today = DateTime().withTime(0, 0, 0, 0)
        val difference =
            Days.daysBetween(date, today).days * if (date.year < today.millis) -1 else 1
        smoothScrollToPosition(offset + difference)
    }

    private class CenterSmoothScroller(context: Context) :
        LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }
    }
}