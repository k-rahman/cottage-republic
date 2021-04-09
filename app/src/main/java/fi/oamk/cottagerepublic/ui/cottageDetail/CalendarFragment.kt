package fi.oamk.cottagerepublic.ui.cottageDetail

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
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {
    private lateinit var  binding: FragmentCalendarBinding
    private lateinit var viewModel: CottageDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        binding.lifecycleOwner = this

        val backStackEntry = findNavController().getBackStackEntry(R.id.cottageDetailFragment)
        viewModel = ViewModelProvider(backStackEntry).get(CottageDetailViewModel::class.java)
        binding.viewModel = viewModel

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 10)

        val lastYear = Calendar.getInstance()
        lastYear.add(Calendar.YEAR, -10)

        binding.calendar.init(lastYear.time , nextYear.time, SimpleDateFormat("MMMM  YYYY", Locale.getDefault()))
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withSelectedDate(Date())

        return binding.root
    }
}