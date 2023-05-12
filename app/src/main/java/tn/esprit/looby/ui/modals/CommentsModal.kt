package tn.esprit.looby.ui.modals

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.adapters.CommentAdapter
import tn.esprit.looby.models.Comment
import tn.esprit.looby.models.Post
import tn.esprit.looby.models.User
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.CommentService
import tn.esprit.looby.utils.Constants
import java.util.*

class CommentsModal : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "CommentsModal"
    }

    fun newInstance(
        post: Post?
    ): CommentsModal {
        val args = Bundle()
        args.putSerializable("post", post)
        val fragment = CommentsModal()
        fragment.arguments = args
        return fragment
    }

    var commentsRV: RecyclerView? = null
    var addButton: Button? = null
    var commentTIET: TextInputEditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_comments, container, false)

        commentsRV = view.findViewById(R.id.commentsRV)
        addButton = view.findViewById(R.id.addButton)
        commentTIET = view.findViewById(R.id.commentTIET)

        val post = arguments?.getSerializable("post") as Post?

        val commentsList: MutableList<Comment> = post!!.comments as MutableList<Comment>

        commentsRV!!.adapter = CommentAdapter(commentsList)
        commentsRV!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val sharedPreferences =
            requireContext().getSharedPreferences(Constants.SHARED_PREF_SESSION, Context.MODE_PRIVATE)
        val userData = sharedPreferences.getString("USER_DATA", null)

        val user: User? = if (userData != null) {
            Gson().fromJson(userData, User::class.java)
        } else {
            null
        }

        addButton!!.setOnClickListener {
            val comment = Comment(
                Calendar.getInstance().time,
                commentTIET!!.text.toString(),
                post._id,
                user!!._id
            )

            ApiService.commentService.addComment(
                CommentService.CommentBody(
                    comment.idPost!!,
                    comment.idUser!!,
                    comment.description!!
                )
            ).enqueue(object : Callback<CommentService.CommentResponse> {
                override fun onResponse(
                    call: Call<CommentService.CommentResponse>,
                    response: Response<CommentService.CommentResponse>
                ) {
                    if (response.code() == 200) {
                        commentsList.add(comment)
                        commentTIET!!.setText("")
                        commentsRV!!.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(
                    call: Call<CommentService.CommentResponse>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                }
            })
        }

        return view
    }
}