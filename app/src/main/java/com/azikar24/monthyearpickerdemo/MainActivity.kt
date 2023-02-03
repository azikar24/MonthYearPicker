package com.azikar24.monthyearpickerdemo

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.azikar24.monthyearpicker.MonthYearPicker
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.startPicker).setOnClickListener {
            val monthYearPickerBuilder = MonthYearPicker.Builder(this)
                .setTitle("Title")
                .setLifeCycleOwner(this)
                .setStyle(R.style.ThemeMonthYearPicker)
                .setLocale(Locale("en", "kw"))
                .setMinDate(nextMonths(2))
                .setMaxDate(nextMonths(120))
                .setSelectedDate(nextMonths(50))
                .setShortMonths(true)
                .setCancelable(false)
                .setCallbackNatural { year, month ->
                    findViewById<TextView>(R.id.dateTextView).text = "Month: $month, Year: $year"
                }
            monthYearPickerBuilder.build().show()
        }
    }


    private fun nextMonths(months: Int): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, months)
        return cal.time
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreate()
    }
}