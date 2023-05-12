package tn.esprit.looby.ui.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.models.Post
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.PostService
import tn.esprit.looby.utils.Constants

class PostActivity : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var postTitleTV: TextView? = null
    private var postDescriptionTV: TextView? = null
    private var deleteButton: TextView? = null
    private var reportButton: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        initialize()
    }

    private fun bindViews() {
        videoView = findViewById(R.id.videoView)
        postTitleTV = findViewById(R.id.postTitleTV)
        postDescriptionTV = findViewById(R.id.postDescriptionTV)
        deleteButton = findViewById(R.id.deleteButton)
        reportButton = findViewById(R.id.reportButton)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initialize() {
        bindViews()

        val post = intent.getSerializableExtra("post") as Post?

        videoView!!.setVideoURI(Uri.parse(Constants.BASE_URL_VIDEOS + post!!.videoFilename))
        videoView!!.setOnPreparedListener { mp ->
            progressBar!!.visibility = View.GONE
            mp.start()
            val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
            val screenRatio: Float = videoView!!.width / videoView!!.height.toFloat()
            val scale = videoRatio / screenRatio
            if (scale >= 1f) {
                videoView!!.scaleX = scale
            } else {
                videoView!!.scaleY = 1f / scale
            }
        }
        videoView!!.setOnCompletionListener { mp -> mp.start() }

        postTitleTV!!.text = post.title
        postDescriptionTV!!.text = post.description

        deleteButton!!.setOnClickListener {
            deletePost(post._id)
        }

        reportButton!!.setOnClickListener {
            Toast.makeText(applicationContext, "Coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePost(_id: String) {
        ApiService.postService.deletePost(_id)?.enqueue(
            object : Callback<PostService.MessageResponse?> {
                override fun onResponse(
                    call: Call<PostService.MessageResponse?>,
                    response: Response<PostService.MessageResponse?>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(applicationContext, "Post Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("BODY", "id code is $_id")
                        println("status code is " + response.code())
                    }
                }

                override fun onFailure(
                    call: Call<PostService.MessageResponse?>,
                    t: Throwable
                ) {
                    println("HTTP ERROR")
                        t.printStackTrace()
                }
            }
        )
    }
}