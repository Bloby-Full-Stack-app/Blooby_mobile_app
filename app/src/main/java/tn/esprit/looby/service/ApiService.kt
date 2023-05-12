package tn.esprit.looby.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.looby.utils.Constants


object ApiService {

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    val userService: UserService by lazy {
        retrofit().create(UserService::class.java)
    }

    val postService: PostService by lazy {
        retrofit().create(PostService::class.java)
    }

    val musicService: MusicService by lazy {
        retrofit().create(MusicService::class.java)
    }

    val chatService: ChatService by lazy {
        retrofit().create(ChatService::class.java)
    }

    val likeService: LikeService by lazy {
        retrofit().create(LikeService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit().create(CommentService::class.java)
    }

    val recordService: RecordService by lazy {
        retrofit().create(RecordService::class.java)
    }

}