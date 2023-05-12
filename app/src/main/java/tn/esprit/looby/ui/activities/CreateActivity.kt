package tn.esprit.looby.ui.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.PostService
import tn.esprit.looby.utils.URIPathHelper
import java.io.File
import java.util.*

class CreateActivity : AppCompatActivity() {

    private var postIV: ImageView? = null
    private var titelTI: TextInputEditText? = null
    private var descriptionTI: TextInputEditText? = null
    private var addPostBtn: Button? = null
    private var chooseVideoButton: FloatingActionButton? = null

    private var videoUri: Uri? = null

    companion object {
        const val GALLERY_RESULT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            123
        )

        postIV = findViewById(R.id.postIV)
        titelTI = findViewById(R.id.titleTI)
        descriptionTI = findViewById(R.id.descriptionTI)
        addPostBtn = findViewById(R.id.addPostBtn)
        chooseVideoButton = findViewById(R.id.chooseVideoButton)

        chooseVideoButton!!.setOnClickListener {
            openSystemGalleryToSelectAVideo()
        }

        addPostBtn!!.setOnClickListener {
            addPost()
        }
    }

    private fun addPost() {
        val pathFromUri = videoUri?.let { URIPathHelper().getPath(this, it) }

        val sourceFile = File(pathFromUri!!)

        val pdfname: String =
            java.lang.String.valueOf(Calendar.getInstance().timeInMillis)

        val requestBody: RequestBody = sourceFile.asRequestBody("*/*".toMediaTypeOrNull())
        val fileToUpload = MultipartBody.Part
            .createFormData("video", sourceFile.name, requestBody)
        val titleData = MultipartBody.Part.createFormData("title", titelTI?.text.toString())
        val descriptionData = MultipartBody.Part.createFormData("description", descriptionTI?.text.toString())
        val filename: RequestBody = pdfname.toRequestBody("text/plain".toMediaTypeOrNull())

        ApiService.postService.addPost(fileToUpload, titleData, descriptionData, filename)
            .enqueue(
                object : Callback<PostService.PostResponse?> {
                    override fun onResponse(
                        call: Call<PostService.PostResponse?>,
                        response: Response<PostService.PostResponse?>
                    ) {
                        if (response.code() == 200) {
                            finish()
                        } else {
                            println("status code is " + response.code())
                        }
                    }

                    override fun onFailure(
                        call: Call<PostService.PostResponse?>,
                        t: Throwable
                    ) {
                        println("HTTP ERROR")
                        t.printStackTrace()
                    }

                }
            )
    }

    private fun openSystemGalleryToSelectAVideo() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")

        try {
            startActivityForResult(intent, GALLERY_RESULT)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                baseContext,
                "No Gallery APP installed",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_RESULT) {
            videoUri = data?.data ?: return
            val bitmap = getVideoThumbnail(baseContext, videoUri!!)
            postIV?.setImageBitmap(bitmap)
            postIV?.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getVideoThumbnail(context: Context, videoUri: Uri): Bitmap? {
        val mMMR = MediaMetadataRetriever()
        mMMR.setDataSource(context, videoUri)

        return mMMR.getScaledFrameAtTime(
            -1,
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC,
            500,
            500
        )
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_down)
    }
}