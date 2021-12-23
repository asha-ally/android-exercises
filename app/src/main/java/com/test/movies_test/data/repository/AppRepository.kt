package com.test.movies_test.data.repository

import com.test.movies_test.data.remote.DataSource
import com.test.movies_test.data.remote.responses.CastResponse
import com.test.movies_test.data.remote.responses.MovieDetailsResponse
import com.test.movies_test.data.remote.responses.MoviesResponse
import com.test.movies_test.data.remote.responses.SearchResponses
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun getUpComingMovies(apiKey: String): Flow<DataSource<MoviesResponse>>
    suspend fun getPlayingNowMovies(apiKey: String): Flow<DataSource<MoviesResponse>>
    suspend fun getMoviesDetails(apiKey: String, movieId: String): Flow<DataSource<MovieDetailsResponse>>
    suspend fun getMoviesCast(apiKey: String, movieId: String): Flow<DataSource<CastResponse>>
    suspend fun getsearchedmovies(apiKey: String,query :String) :Flow<DataSource<SearchResponses>>
}