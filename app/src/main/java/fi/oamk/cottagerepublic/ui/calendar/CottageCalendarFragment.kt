package fi.oamk.cottagerepublic.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.savvi.rangedatepicker.CalendarPickerView
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.databinding.FragmentCalendarBinding
import fi.oamk.cottagerepublic.ui.cottageDetail.CottageDetailFragmentDirections
import fi.oamk.cottagerepublic.ui.cottageDetail.CottageDetailViewModel
import fi.oamk.cottagerepublic.util.Resource
import java.text.SimpleDateFormat
import java.util.*

class CottageCalendarFragment : Fragment() {
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var viewModel: CottageDetailViewModel
    private var listOfReservedDates = listOf<Date>()

    // start at the current year, current month, current day
    private val calendarStartDate = Calendar.getInstance()

    // end at next year (365 days)
    private val calendarEndDate = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        initViewModel()
        setObservers()
        initCalendar()

        binding.calendar.setOnDateSelectedListener(handleOnDateSelected())

        return binding.root
    }

    private fun initViewModel() {
        val backStackEntry = findNavController().getBackStackEntry(R.id.cottageDetailFragment)
        viewModel = ViewModelProvider(backStackEntry).get(CottageDetailViewModel::class.java)
        binding.cottageDetailViewModel = viewModel
    }

    private fun setObservers() {
        viewModel.cottageReservations.observe(viewLifecycleOwner, {
            setReservedDates(it)
        })

        viewModel.showCalendar.observe(viewLifecycleOwner, {
            toggleCalendar(it)
        })

        viewModel.navigateToBookingDetails.observe(viewLifecycleOwner, {
            if (it) {
                navigateToBookingDetail(formatSelectedDates())
            }
        })
    }

    private fun initCalendar() {
        calendarEndDate.add(Calendar.DAY_OF_YEAR, 365)

        if (viewModel.selectedDates.get() != null) {
            val dateRange = mutableListOf<Date>()
            dateRange.add(viewModel.selectedDates.get()!!.first())
            dateRange.add(viewModel.selectedDates.get()!!.last())
            binding.calendar.init(
                calendarStartDate.time,
                calendarEndDate.time,
                SimpleDateFormat("MMMM  YYYY", Locale.getDefault())
            )
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(dateRange)
        } else {
            binding.calendar.init(
                calendarStartDate.time,
                calendarEndDate.time,
                SimpleDateFormat("MMMM  YYYY", Locale.getDefault())
            )
                .inMode(CalendarPickerView.SelectionMode.RANGE)
        }
    }

    private fun setReservedDates(queryResult: Resource<Any>) {
        when (queryResult) {
            is Resource.Loading -> {
                binding.progressIndicator.bringToFront()
                binding.progressIndicator.show()
            }
            is Resource.Success -> {
                val dateformat = SimpleDateFormat("dd-MM-yyyy")
                val data = queryResult.data as List<String>

                listOfReservedDates = data.map { date ->
                    dateformat.parse(date)
                }.sorted()

                binding.calendar.highlightDates(listOfReservedDates)
                binding.progressIndicator.hide()
            }
            is Resource.Failure -> {
            }
        }
    }

    private fun toggleCalendar(showCalendar: Boolean) {
        if (showCalendar) {
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_in_top)
                show(this@CottageCalendarFragment)
            }

        } else {
            parentFragmentManager.commit {
                setCustomAnimations(R.anim.slide_out_bottom, R.anim.slide_out_top)
                hide(this@CottageCalendarFragment)
            }
        }
    }

    private fun formatSelectedDates(): List<String> {
        val formatDates = SimpleDateFormat("dd-MM-yyyy")

        return binding.calendar.selectedDates.map { date ->
            formatDates.format(date)
        }
    }

    private fun navigateToBookingDetail(selectedDates: List<String>) {
        findNavController().navigate(
            CottageDetailFragmentDirections
                .actionCottageDetailFragmentToBookingDetailFragment(
                    viewModel.selectedCottage.value!!,
                    selectedDates.toTypedArray()
                )
        )
        viewModel.onBookingNavigated()
    }

    private fun handleOnDateSelected(): CalendarPickerView.OnDateSelectedListener {
        val datesToDisable = mutableListOf<Date?>()
        val reservedCalendar = Calendar.getInstance()

        return object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date?) {
                // set number of nights (check BindingAdapters)
                viewModel.numberOfNights.set(binding.calendar.selectedDates.size - 1)
                viewModel.selectedDates.set(binding.calendar.selectedDates)

                // disable the days from the first reserved day after the selected day till the end of the calendar
                // client must select consecutive days
                for (reservedDate in listOfReservedDates) {
                    if (date!! < reservedDate && binding.calendar.selectedDates.size < 2) {
                        val timeDifference = calendarEndDate.time.time - reservedDate.time // get time difference
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

                    // if at least 2 days are selected open the free days
                    if (binding.calendar.selectedDates.size >= 2) {
                        binding.calendar.clearHighlightedDates()
                        binding.calendar.highlightDates(listOfReservedDates)
                    }
                }
            }

            override fun onDateUnselected(date: Date?) {
            }
        }
    }
}