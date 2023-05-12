package tn.esprit.looby.adapters

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import tn.esprit.looby.R
import tn.esprit.looby.models.Post
import tn.esprit.looby.utils.Constants

internal class PostsGridAdapter(
    private val context: Context,
    private val posts: List<Post>
) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun getCount(): Int {
        return posts.count()
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val post = posts[position]
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.single_post_small, null)
        }

        val videoView: VideoView = convertView!!.findViewById(R.id.videoView)
        val postTitleTV: TextView = convertView.findViewById(R.id.postTitleTV)
        val progressBar: ProgressBar = convertView.findViewById(R.id.progressBar)

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

        return convertView
    }
}