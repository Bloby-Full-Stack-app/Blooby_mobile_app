package tn.esprit.looby.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.looby.R
import tn.esprit.looby.adapters.FullPostAdapter
import tn.esprit.looby.service.ApiService
import tn.esprit.looby.service.PostService

class PostsFragment : Fragment() {

    val emptyList: List<String> = listOf()
    var mutableList: MutableList<String> = emptyList.toMutableList()
    var viewPagerVideos: ViewPager2? = null
    private var postsSL: ShimmerFrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        // FIND VIEWS
        viewPagerVideos = view.findViewById(R.id.viewPagerVideos);
        postsSL = view.findViewById(R.id.postsSL)

        postsSL!!.startShimmer()

        ApiService.postService.getPosts()
            .enqueue(
                object : Callback<PostService.PostsResponse> {
                    override fun onResponse(
                        call: Call<PostService.PostsResponse>,
                        response: Response<PostService.PostsResponse>
                    ) {
                        val posts = response.body()?.posts ?: emptyList()
                        val mutablePosts = posts.reversed().toMutableList()
                        if (response.code() == 200) {
                            viewPagerVideos!!.adapter =
                                FullPostAdapter(mutablePosts)

                            postsSL!!.stopShimmer()
                            postsSL!!.visibility = View.GONE
                        } else {
                            println("status code is " + response.code())
                        }
                    }

                    override fun onFailure(
                        call: Call<PostService.PostsResponse>,
                        t: Throwable
                    ) {
                        println("HTTP ERROR")
                        t.printStackTrace()
                    }

                }
            )

        return view
    }
}