package id.tisnahadiana.storyapp.data.remote.retrofit

import id.tisnahadiana.storyapp.data.remote.responses.LoginResponse
import id.tisnahadiana.storyapp.data.remote.responses.RegisterResponse
import id.tisnahadiana.storyapp.data.remote.responses.StoriesResponse
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null,
    ): StoriesResponse
}