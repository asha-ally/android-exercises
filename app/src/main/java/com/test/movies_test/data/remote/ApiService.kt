package com.test.movies_test.data.remote

import com.test.movies_test.data.remote.responses.CastResponse
import com.test.movies_test.data.remote.responses.MovieDetailsResponse
import com.test.movies_test.data.remote.responses.MoviesResponse
import com.test.movies_test.data.remote.responses.SearchResponses
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getNowPlayingMovies(@Query("api_key") apiKey: String): MoviesResponse

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(@Query("api_key") apiKey: String): MoviesResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: String,
        @Query("api_key") apiKey: String,
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movie_id: String,
        @Query("api_key") apiKey: String
    ): CastResponse

    @GET("search/movie")
    suspend fun getsearchedmovies(
        @Query("api_key") apiKey: String,
        @Query("query") query:String
    ):SearchResponses

}