package fi.oamk.cottagerepublic.ui.faq

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.FAQ
import fi.oamk.cottagerepublic.databinding.FragmentAccountFaqBinding

class FaqFragment : Fragment() {
    private lateinit var binding: FragmentAccountFaqBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_faq, container, false)

        binding.faq.adapter = MyQuestionRecyclerViewAdapter(FAQ.items)

        return binding.root
    }
}