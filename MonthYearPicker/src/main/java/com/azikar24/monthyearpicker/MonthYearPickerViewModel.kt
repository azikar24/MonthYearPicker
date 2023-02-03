package com.azikar24.monthyearpicker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MonthYearPickerViewModel : ViewModel() {
    val selectedMonth = MutableLiveData<Int>()
    val selectedYear = MutableLiveData<Int>()
}