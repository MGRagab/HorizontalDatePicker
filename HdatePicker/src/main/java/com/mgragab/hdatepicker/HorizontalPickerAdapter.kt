package com.mgragab.hdatepicker

import android.app.AlarmManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import java.util.*

/**
 * Created By MGRagab 15/09/2021
 */
class HorizontalPickerAdapter(
    private val itemWidth: Int,
    private val listener: OnItemClickedListener,
    context: Context,
    daysToCreate: Int,
    offset: Int,
    mBackgroundColor: Int,
    mDateSelectedColor: Int,
    mDateSelectedTextColor: Int,
    mTodayDateTextColor: Int,
    mTodayDateBackgroundColor: Int,
    mDayOfWeekTextColor: Int,
    mUnselectedDayTextColor: Int
) : RecyclerView.Adapter<HorizontalPickerAdapter.ViewHolder>() {
    private val mBackgroundColor: Int
    private val mDateSelectedTextColor: Int
    private val mDateSelectedColor: Int
    private val mTodayDateTextColor: Int
    private val mTodayDateBackgroundColor: Int
    private val mDayOfWeekTextColor: Int
    private val mUnselectedDayTextColor: Int
    private val items: ArrayList<Day> = ArrayList()
    private fun generateDays(n: Int, initialDate: Long, cleanArray: Boolean) {
        if (cleanArray) items.clear()
        var i = 0
        while (i < n) {
            val actualDate = DateTime(initialDate + DAY_MILLIS * i++)
            items.add(Day(actualDate))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvDay.text = item.getDay()
        holder.tvWeekDay.text = item.getWeekDay()
        holder.tvWeekDay.setTextColor(mDayOfWeekTextColor)
        when {
            item.isSelected() -> {
                holder.tvDay.setBackgroundDrawable(getDaySelectedBackground(holder.itemView))
                holder.tvDay.setTextColor(mDateSelectedTextColor)
            }
            item.isToday() -> {
                holder.tvDay.setBackgroundDrawable(getDayTodayBackground(holder.itemView))
                holder.tvDay.setTextColor(mTodayDateTextColor)
            }
            else -> {
                holder.tvDay.setBackgroundColor(mBackgroundColor)
                holder.tvDay.setTextColor(mUnselectedDayTextColor)
            }
        }
    }

    private fun getDaySelectedBackground(view: View): Drawable {
        val drawable = view.resources.getDrawable(R.drawable.background_day_selected)
        DrawableCompat.setTint(drawable, mDateSelectedColor)
        return drawable
    }

    private fun getDayTodayBackground(view: View): Drawable {
        val drawable = view.resources.getDrawable(R.drawable.background_day_today)
        if (mTodayDateBackgroundColor != -1) DrawableCompat.setTint(
            drawable,
            mTodayDateBackgroundColor
        )
        return drawable
    }

    fun getItem(position: Int): Day {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tvDay: TextView
        var tvWeekDay: TextView
        override fun onClick(v: View) {
            listener.onClickView(v, adapterPosition)
        }

        init {
            tvDay = itemView.findViewById<View>(R.id.tvDay) as TextView
            tvDay.width = itemWidth
            tvWeekDay = itemView.findViewById<View>(R.id.tvWeekDay) as TextView
            itemView.setOnClickListener(this)
        }
    }

    companion object {
        private const val DAY_MILLIS = AlarmManager.INTERVAL_DAY
    }

    init {
        generateDays(daysToCreate, DateTime().minusDays(offset).millis, false)
        this.mBackgroundColor = mBackgroundColor
        this.mDateSelectedTextColor = mDateSelectedTextColor
        this.mDateSelectedColor = mDateSelectedColor
        this.mTodayDateTextColor = mTodayDateTextColor
        this.mTodayDateBackgroundColor = mTodayDateBackgroundColor
        this.mDayOfWeekTextColor = mDayOfWeekTextColor
        this.mUnselectedDayTextColor = mUnselectedDayTextColor
    }
}