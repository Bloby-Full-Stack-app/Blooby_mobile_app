package tn.esprit.looby.adapters;

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.looby.R
import tn.esprit.looby.models.Music
import tn.esprit.looby.ui.activities.MusicActivity
import tn.esprit.looby.utils.Constants
import tn.esprit.looby.utils.ImageLoader


class MusicAdapter(var items: MutableList<Music>) :
    RecyclerView.Adapter<MusicAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_search, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SearchViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val imageIV: ImageView = itemView.findViewById(R.id.imageIV)
        private val descriptionTV: TextView = itemView.findViewById(R.id.titleTV)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bindView(music: Music) {

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MusicActivity::class.java)
                intent.putExtra("music", music)
                itemView.context.startActivity(intent)
            }

            ImageLoader.setImageFromUrl(
                imageIV,
                progressBar,
                Constants.BASE_URL_MUSIC + music.imageFilename
            )

            descriptionTV.text = music.title
        }
    }
}