
# Month Year Picker

Since android doesn't have a direct way to do month year picker, here is a customizable library to do so.





## Screenshots

![App Screenshot](https://azikar24.com/wp-content/uploads/2023/02/monthYearPickerScreenShoot-3.png)


## Features

- Support Theme Change persisting data
- Selected Date
- Minumum and Maximum dates


## Usage/Examples


To start the picker

```kotlin
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
                    println("Month: $month, Year: $year")
                }
    monthYearPickerBuilder.build().show()
```
    
To customize the picker style

```xml
        <item name="backgroundColor">?colorSurface</item>
        <item name="titleColor">?colorOnBackground</item>

        <item name="dayPickerActive">?colorOnSecondary</item>
        <item name="dayPickerInActive">?colorOnBackground</item>
        <item name="yearPickerActive">?colorOnSecondary</item>
        <item name="yearPickerInActive">?colorOnBackground</item>


        <item name="dividerColor">?colorSecondary</item>
        <item name="dividerBackgroundColor">?colorSecondary</item>

        <item name="negativeTextColor">?colorPrimary</item>
        <item name="positiveTextColor">?colorPrimary</item>
```