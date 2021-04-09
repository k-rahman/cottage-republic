package fi.oamk.cottagerepublic.ui.cottageDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.savvi.rangedatepicker.CalendarPickerView
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCalendarBinding
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var  binding: FragmentCalendarBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 10);

        val lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);

        val calendar = view.findViewById<CalendarPickerView>(R.id.calendar)
        calendar.init(lastYear.time , nextYear.time)
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(Date())
    }
}