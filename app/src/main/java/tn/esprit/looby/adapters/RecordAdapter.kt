package tn.esprit.looby.adapters;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.looby.R
import tn.esprit.looby.models.Record
import java.text.SimpleDateFormat

class RecordAdapter(var items: MutableList<Record>) :
    RecyclerView.Adapter<RecordAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_comment, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ConversationViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val titleTV: TextView = itemView.findViewById(R.id.titleTV)
        private val descriptionTV: TextView = itemView.findViewById(R.id.descriptionTV)

        fun bindView(item: Record) {

            itemView.setOnClickListener {

            }

            titleTV.text = item.user!!.firstname + " " + item.user!!.lastname
            descriptionTV.text =
                "Was here at : " + SimpleDateFormat("dd-MM-yyyy HH:MM").format(item.date)
        }
    }
}