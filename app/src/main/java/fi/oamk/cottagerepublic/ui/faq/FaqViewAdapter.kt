package fi.oamk.cottagerepublic.ui.faq

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.cottagerepublic.R
import fi.oamk.cottagerepublic.data.FAQDataModel

class MyQuestionRecyclerViewAdapter(
    private val values: List<FAQDataModel>
) : RecyclerView.Adapter<MyQuestionRecyclerViewAdapter.ViewHolder>() {

    private var  mExpandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_account_faq_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.questionView.text = item.question
        holder.answerView.text = item.answer

        val isExpanded = position == mExpandedPosition
        holder.answerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        // This is used to animate the the arrow button
        holder.questionView.isActivated = (isExpanded)
        holder.itemView.isActivated = isExpanded


        holder.itemView.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position

            notifyItemChanged(position)
        }
    }
    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionView: TextView = view.findViewById(R.id.question)
        val answerView: TextView = view.findViewById(R.id.answer)

        override fun toString(): String {
            return super.toString() + " '" + questionView.text + "'"
        }
    }
}