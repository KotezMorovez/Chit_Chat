package com.example.chit_chat.utils

import android.content.res.Resources
import com.example.chit_chat.R
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import org.joda.time.DateTime

class DateUtils @Inject constructor(
    private val resources: Resources
) {
    private val dateFormatter = SimpleDateFormat("dd MMM", Locale.forLanguageTag("ru"))
    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.forLanguageTag("ru"))

    fun getElapsedTime(fromDate: DateTime): String {
        val now = DateTime()
        val todayMidnight = DateTime().withTimeAtStartOfDay().millis
        val firstDayOfCurrentYear = DateTime().withDayOfYear(1).millis

        val yesterdayMidnight = todayMidnight - MILLIS_IN_DAY
        val publishedAt = fromDate.millis
        val period = now.millis - fromDate.millis

        val date: String = dateFormatter
            .format(fromDate.toDate())
            .replace(".", "")

        val time: String = timeFormatter
            .format(fromDate.toDate())

        if (period < MILLIS_IN_HOUR) {
            return resources.getString(R.string.date_minutes_ago, period / MILLIS_IN_MINUTE)
        }

        if (period < MILLIS_IN_THREE_HOUR) {
            return resources.getString(R.string.date_hours_ago, period / MILLIS_IN_HOUR)
        }

        if (publishedAt >= todayMidnight) {
            return resources.getString(R.string.date_today_at, time)
        }

        if (publishedAt >= yesterdayMidnight) {
            return resources.getString(R.string.date_yesterday_at, time)
        }

        if (publishedAt >= firstDayOfCurrentYear){
            return resources.getString(R.string.date_less_year_ago, date, time)
        }

        val year = fromDate.year().asText
        return resources.getString(R.string.date_years_ago, year)
    }

    companion object {
        private const val MILLIS_IN_MINUTE = 60 * 1000L
        private const val MILLIS_IN_HOUR = 60 * 60 * 1000L
        private const val MILLIS_IN_THREE_HOUR = 3 * 60 * 60 * 1000L
        private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
    }
}