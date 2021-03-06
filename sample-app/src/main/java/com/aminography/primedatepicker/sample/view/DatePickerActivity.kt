package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.fragment.PrimeDatePickerBottomSheet
import com.aminography.primedatepicker.sample.*
import kotlinx.android.synthetic.main.activity_date_picker.*
import java.util.*


class DatePickerActivity : AppCompatActivity(), PrimeDatePickerBottomSheet.OnDayPickedListener {

    private var datePicker: PrimeDatePickerBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        showDatePickerButton.setOnClickListener {
            val calendarType = when {
                civilRadioButton.isChecked -> CalendarType.CIVIL
                persianRadioButton.isChecked -> CalendarType.PERSIAN
                hijriRadioButton.isChecked -> CalendarType.HIJRI
                japaneseRadioButton.isChecked -> CalendarType.JAPANESE
                else -> CalendarType.CIVIL
            }

            val pickType = when {
                singleRadioButton.isChecked -> PickType.SINGLE
                rangeRadioButton.isChecked -> PickType.RANGE_START
                multipleRadioButton.isChecked -> PickType.MULTIPLE
                else -> PickType.SINGLE
            }

            val minDateCalendar: PrimeCalendar?
            if (minDateCheckBox.isChecked) {
                minDateCalendar = CalendarFactory.newInstance(calendarType)
                minDateCalendar[Calendar.MONTH] -= 5
            } else {
                minDateCalendar = null
            }

            val maxDateCalendar: PrimeCalendar?
            if (maxDateCheckBox.isChecked) {
                maxDateCalendar = CalendarFactory.newInstance(calendarType)
                maxDateCalendar[Calendar.MONTH] += 5
            } else {
                maxDateCalendar = null
            }

            val typeface = when (calendarType) {
                CalendarType.CIVIL -> FONT_PATH_CIVIL
                CalendarType.PERSIAN -> FONT_PATH_PERSIAN
                CalendarType.HIJRI -> FONT_PATH_HIJRI
                CalendarType.JAPANESE -> FONT_PATH_JAPANESE
            }

            val today = CalendarFactory.newInstance(calendarType)

            datePicker = PrimeDatePickerBottomSheet.newInstance(
                    currentDateCalendar = today,
                    minDateCalendar = minDateCalendar,
                    maxDateCalendar = maxDateCalendar,
                    pickType = pickType,
                    typefacePath = typeface
            )
            datePicker?.setOnDateSetListener(this)
            datePicker?.show(supportFragmentManager, PICKER_TAG)
        }

    }

    override fun onResume() {
        super.onResume()
        datePicker = supportFragmentManager.findFragmentByTag(PICKER_TAG) as? PrimeDatePickerBottomSheet
        datePicker?.setOnDateSetListener(this)
    }

    override fun onSingleDayPicked(singleDay: PrimeCalendar) {
        longToast(singleDay.longDateString)
    }

    override fun onRangeDaysPicked(startDay: PrimeCalendar, endDay: PrimeCalendar) {
        longToast("From: ${startDay.longDateString}\nTo: ${endDay.longDateString}")
    }

    override fun onMultipleDaysPicked(multipleDays: List<PrimeCalendar>) {
        longToast(multipleDays.joinToString(" -\n") { it.longDateString })
    }

    private fun longToast(text: String) =
            Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    companion object {
        const val PICKER_TAG = "PrimeDatePickerBottomSheet"
    }

}
