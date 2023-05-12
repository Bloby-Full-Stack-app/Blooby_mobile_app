package tn.esprit.looby.adapters;

import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.looby.R
import tn.esprit.looby.models.Post
import tn.esprit.looby.ui.activities.PostActivity
import tn.esprit.looby.utils.Constants

class PostAdapter(var items: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.SearchPostViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_post_small, parent, false)
        return SearchPostViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchPostViewHolder, position: Int) {
        holder.bindView(items[position])
    }

    override fun getItemCount(): Int = items.size

    class SearchPostViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val videoView: VideoView = itemView.findViewById(R.id.videoView)
        private val postTitleTV: TextView = itemView.findViewById(R.id.postTitleTV)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bindView(post: Post) {

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PostActivity::class.java)
                intent.putExtra("post", post)
                itemView.context.startActivity(intent)
            }

            videoView.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE)
            videoView.setVideoURI(Uri.parse(Constants.BASE_URL_VIDEOS + post.videoFilename))
            videoView.setOnPreparedListener { mp ->
                progressBar.visibility = View.GONE
                mp.setVolume(0F, 0F)
                mp.start()
                val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
                val screenRatio: Float = videoView.width / videoView.height.toFloat()
                val scale = videoRatio / screenRatio
                if (scale >= 1f) {
                    videoView.scaleX = scale
                } else {
                    videoView.scaleY = 1f / scale
                }
            }

            postTitleTV.text = post.title
        }
    }
}