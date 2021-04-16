package fi.oamk.cottagerepublic.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.savvi.rangedatepicker.CalendarPickerView
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCalendarBinding
import fi.oamk.cottagerepublic.ui.cottageDetail.CottageDetailFragmentDirections
import fi.oamk.cottagerepublic.ui.cottageDetail.CottageDetailViewModel
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var viewModel: CottageDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.lifecycleOwner = this

        val backStackEntry = findNavController().getBackStackEntry(R.id.cottageDetailFragment)
        viewModel = ViewModelProvider(backStackEntry).get(CottageDetailViewModel::class.java)
        binding.viewModel = viewModel

        // start at the current year, current month, current day
        val start = Calendar.getInstance()

        // end at next year (365 days)
        val end = Calendar.getInstance()
        end.add(Calendar.DAY_OF_YEAR, 365)

        val arrayList = arrayListOf<Date>()
        val dateformat = SimpleDateFormat("dd-MM-yyyy")

        val d1 = "22-4-2021"
        val d2 = "23-4-2021"
        val d3 = "24-4-2021"
        val d4 = "25-4-2021"
        val d5 = "1-5-2021"

        val newdate = dateformat.parse(d3)
        val newdate2 = dateformat.parse(d2)
        val newdate3 = dateformat.parse(d1)
        val newdate4 = dateformat.parse(d4)
        val newdate5 = dateformat.parse(d5)
        arrayList.add(newdate!!)
        arrayList.add(newdate2!!)
        arrayList.add(newdate3!!)
        arrayList.add(newdate4!!)
        arrayList.add(newdate5!!)

        // sort the reserved date list (just in case)
        arrayList.sort()

        binding.calendar.init(start.time, end.time, SimpleDateFormat("MMMM  YYYY", Locale.getDefault()))
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withHighlightedDates(arrayList)

        val datesToDisable = mutableListOf<Date?>()
        val reservedCalendar = Calendar.getInstance()

        binding.calendar.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date?) {

                // set number of nights (check BindingAdapters)
                binding.numberOfNights = binding.calendar.selectedDates.size - 1

                // disable the days from the first reserved day after the selected day till the end of the calendar
                // client must select consecutive days
                for (reservedDate in arrayList) {
                    if (date!! < reservedDate && binding.calendar.selectedDates.size <= 2) {
                        val timeDifference = end.time.time - reservedDate.time // get time difference
                        val days = (timeDifference / (1000 * 60 * 60 * 24)).toInt() // get days
                        reservedCalendar.time = reservedDate // set date
                        datesToDisable.clear()
                        for (i in 1 until days) {
                            reservedCalendar.add(Calendar.DAY_OF_YEAR, 1) // add days to current date
                            datesToDisable.add(reservedCalendar.time)
                        }
                        binding.calendar.highlightDates(datesToDisable)

                        // when the first reserved date is found, quit
                        break
                    }

                    // if 2 days are selected open the free days
                    if (binding.calendar.selectedDates.size > 2) {
                        binding.calendar.clearHighlightedDates()
                        binding.calendar.highlightDates(arrayList)
                    }
                }
            }

            override fun onDateUnselected(date: Date?) {
            }
        })

        viewModel.navigateToBookingDetails.observe(viewLifecycleOwner, {
            if (it) {
                findNavController().navigate(CottageDetailFragmentDirections.actionCottageDetailFragmentToBookingDetailFragment())
                viewModel.onBookingNavigated()
            }
        })

        return binding.root
    }
}