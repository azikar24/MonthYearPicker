package com.azikar24.monthyearpicker

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.azikar24.monthyearpicker.databinding.MonthYearPickerLayoutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shawnlin.numberpicker.NumberPicker
import java.text.DateFormatSymbols
import java.util.*
import kotlin.collections.ArrayList


class MonthYearPicker(builder: Builder) {
    private val viewModel: MonthYearPickerViewModel

    private val themeMonthYearPicker: Int
    private val context: Context
    private val title: String?
    private val isCancellable: Boolean
    private val locale: Locale?
    private val minDate: Date
    private val maxDate: Date
    private val selectedDate: Date
    private val isShortMonths: Boolean
    private val callback: OnSelected?
    private val callbackNatural: OnSelectedNatural?
    private val lifecycleOwner: LifecycleOwner?
    private val typeFace: Typeface?


    init {
        themeMonthYearPicker = builder.themeMonthYearPicker
        context = builder.context
        title = builder.title
        isCancellable = builder.isCancellable
        locale = builder.locale
        minDate = builder.minDate
        maxDate = builder.maxDate
        selectedDate = builder.selectedDate
        isShortMonths = builder.isShortMonths
        callback = builder.callback
        callbackNatural = builder.callbackNatural
        lifecycleOwner = builder.lifecycleOwner
        typeFace = builder.typeFace
        viewModel = ViewModelProvider(context as ViewModelStoreOwner)[MonthYearPickerViewModel::class.java]
    }

    private lateinit var binding: MonthYearPickerLayoutBinding

    fun show() {
        val minCalendar = minDate.toCalendar()
        val maxCalendar = maxDate.toCalendar()
        val selectedDate = selectedDate.toCalendar()

        if (minCalendar.after(maxCalendar)) {
            throw IllegalArgumentException("minimum date shouldn't be after maximum date")
        }
        if (selectedDate.before(minCalendar) || selectedDate.after(maxCalendar)) {
            throw IllegalArgumentException("selected date is not between the minimum and maximum dates")
        }
        context.setTheme(themeMonthYearPicker)

        binding = MonthYearPickerLayoutBinding.inflate(LayoutInflater.from(context))
        title?.let {
            binding.titleTextView.typeface = typeFace
            binding.titleTextView.visibility = View.VISIBLE
            binding.titleTextView.text = title
        }

        val dialog = MaterialAlertDialogBuilder(context)
            .setCancelable(isCancellable)
            .setView(binding.root)
            .create()
        dialog.show()

        setupMonths(minCalendar, maxCalendar, selectedDate)
        setupYears(minCalendar, maxCalendar, selectedDate)
        setupButtons(dialog)
    }

    private fun setupButtons(dialog: AlertDialog) {
        binding.positiveActionTextView.typeface = typeFace
        binding.positiveActionTextView.setOnClickListener {
            callback?.onSelected(binding.yearSpinner.value, binding.monthSpinner.value % 12)
            callbackNatural?.onSelectedNatural(binding.yearSpinner.value, (binding.monthSpinner.value % 12) + 1)
            dialog.dismiss()
        }
        binding.negativeActionTextView.typeface = typeFace
        binding.negativeActionTextView.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun initNumberPickers(
        picker: NumberPicker,
        minValue: Int,
        maxValue: Int,
        displayedValues: ArrayList<String>,
    ) {
        picker.wrapSelectorWheel = false
        picker.displayedValues = displayedValues.toTypedArray()

        picker.minValue = minValue
        picker.maxValue = maxValue

        picker.typeface = typeFace
        picker.setSelectedTypeface(typeFace)
    }

    private fun setupYears(minCalendar: Calendar, maxCalendar: Calendar, selectedDate: Calendar) {
        val yearDiff = minCalendar.yearDiff(maxCalendar)
        val currentYear = minCalendar.get(Calendar.YEAR)
        val yearsDisplayValues = ArrayList<String>().apply {
            for (i in (minCalendar.get(Calendar.YEAR)..yearDiff + currentYear)) {
                add((i).toString())
            }
        }

        initNumberPickers(
            picker = binding.yearSpinner,
            minValue = currentYear,
            maxValue = yearDiff.toInt() + currentYear,
            displayedValues = yearsDisplayValues
        )
        if (viewModel.selectedYear.value == null) {
            val selectedYear = minCalendar.yearDiff(selectedDate)
            viewModel.selectedYear.value = selectedYear.toInt() + currentYear
        }
        viewModel.selectedYear.value?.let {
            binding.yearSpinner.value = it
        }

        viewModel.selectedYear.value?.let {
            binding.yearSpinner.value = it
        }
        binding.yearSpinner.setOnValueChangedListener { _, oldVal, newVal ->
            if (oldVal > newVal) {
                binding.monthSpinner.value -= 12
            } else {
                binding.monthSpinner.value += 12
            }
            viewModel.selectedMonth.value = binding.monthSpinner.value
            viewModel.selectedYear.value = newVal
        }
    }

    private fun setupMonths(minCalendar: Calendar, maxCalendar: Calendar, selectedDate: Calendar) {
        val months = DateFormatSymbols(locale ?: Locale.ROOT).run {
            if (isShortMonths) shortMonths else months
        }

        val monthDiff = minCalendar.monthDiff(maxCalendar) + 1
        val currentMonth = minCalendar.get(Calendar.MONTH)
        val monthDisplayValues = ArrayList<String>().apply {
            val minMonth = minCalendar.get(Calendar.MONTH)
            for (i in (minMonth..monthDiff + currentMonth)) {
                add(months[(i % 12).toInt()])
            }
        }

        initNumberPickers(
            picker = binding.monthSpinner,
            minValue = currentMonth,
            maxValue = monthDiff.toInt() + currentMonth - 1,
            displayedValues = monthDisplayValues
        )
        if (viewModel.selectedMonth.value == null) {
            val selectedMonth = minCalendar.monthDiff(selectedDate)
            viewModel.selectedMonth.value = selectedMonth.toInt() + currentMonth
        }
        viewModel.selectedMonth.value?.let {
            binding.monthSpinner.value = it
        }

        binding.monthSpinner.setOnValueChangedListener { _, oldVal, newVal ->
            val actualNewValue = newVal % 12
            val actualOldValue = oldVal % 12

            if (actualNewValue == 0 && actualOldValue == 11) {
                binding.yearSpinner.value++
            }
            if (actualNewValue == 11 && actualOldValue == 0) {
                binding.yearSpinner.value--
            }
            viewModel.selectedYear.value = binding.yearSpinner.value
            viewModel.selectedMonth.value = newVal

        }
    }

    fun interface OnSelected {
        fun onSelected(year: Int, month: Int)
    }

    fun interface OnSelectedNatural {
        fun onSelectedNatural(year: Int, month: Int)
    }

    class Builder(val context: Context) {
        var themeMonthYearPicker: Int = -1
        var title: String? = null
        var isCancellable: Boolean = true
        var locale: Locale? = null

        var minDate: Date = Date()
        var maxDate: Date = Date()
        var selectedDate: Date = Date()

        var isShortMonths: Boolean = false
        var callback: OnSelected? = null
        var callbackNatural: OnSelectedNatural? = null
        var lifecycleOwner: LifecycleOwner? = null
        var typeFace: Typeface? = null

        fun setStyle(style: Int) = apply {
            this.themeMonthYearPicker = style
        }

        fun setTypeFace(typeFace: Typeface?) = apply {
            this.typeFace = typeFace
        }

        fun setTitle(title: String) = apply {
            this.title = title
        }

        fun setCancelable(cancelable: Boolean) = apply {
            this.isCancellable = cancelable
        }

        fun setLocale(locale: Locale) = apply {
            this.locale = locale
        }

        fun setMinDate(minDate: Date) = apply {
            this.minDate = minDate
        }

        fun setMaxDate(maxDate: Date) = apply {
            this.maxDate = maxDate
        }

        fun setSelectedDate(selectedDate: Date) = apply {
            this.selectedDate = selectedDate
        }

        fun setShortMonths(isShortMonths: Boolean) = apply {
            this.isShortMonths = isShortMonths
        }

        fun setCallback(callback: OnSelected) = apply {
            this.callback = callback
        }

        fun setCallbackNatural(callbackNatural: OnSelectedNatural) = apply {
            this.callbackNatural = callbackNatural
        }

        fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) = apply {
            this.lifecycleOwner = lifecycleOwner
        }

        fun build() = MonthYearPicker(this)
    }
}