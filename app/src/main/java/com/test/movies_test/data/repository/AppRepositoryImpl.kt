package com.test.movies_test.data.repository

import android.util.Log
import com.test.movies_test.data.remote.ApiService
import com.test.movies_test.data.remote.DataSource
import com.test.movies_test.data.remote.responses.CastResponse
import com.test.movies_test.data.remote.responses.MovieDetailsResponse
import com.test.movies_test.data.remote.responses.MoviesResponse
import com.test.movies_test.data.remote.responses.SearchResponses
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

@DelicateCoroutinesApi
class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): AppRepository {
    override suspend fun getUpComingMovies(apiKey: String): Flow<DataSource<MoviesResponse>> {
        return flow {
            try {
                val result = apiService.getUpComingMovies(apiKey = apiKey)
                if(result.results.isEmpty()) {
                    emit(DataSource.empty<MoviesResponse>())
                } else {
                    emit(DataSource.success(result))
                }
            } catch (e: Exception) {
                if(e is IOException) {
                    emit(DataSource.error("No Internet Connection", null))
                } else {
                    emit(DataSource.error("Something went wrong...", null))
                }
            }
        }
    }

    override suspend fun getPlayingNowMovies(apiKey: String): Flow<DataSource<MoviesResponse>> {
        return flow {
            try {
                val result = apiService.getNowPlayingMovies(apiKey = apiKey)
                if(result.results.isEmpty()) {
                    emit(DataSource.empty<MoviesResponse>())
                } else {
                    emit(DataSource.success(result))
                }
            } catch (e: Exception) {
                if(e is IOException) {
                    emit(DataSource.error("No Internet Connection", null))
                } else {
                    Log.d("API ERROR", e.localizedMessage)
                    emit(DataSource.error("Something went wrong...", null))
                }
            }
        }
    }

    override suspend fun getMoviesDetails(apiKey: String, movieId: String): Flow<DataSource<MovieDetailsResponse>> {
        return flow {
            try {
                val result = apiService.getMovieDetails(movie_id = movieId, apiKey = apiKey)
                emit(DataSource.success(result))
            } catch (e: Exception) {
                if(e is IOException) {
                    emit(DataSource.error("No Internet Connection", null))
                } else {
                    Log.d("Movies-Details-Error", e.localizedMessage.toString())
                    emit(DataSource.error("Something went wrong...", null))
                }
            }
        }
    }

    override suspend fun getMoviesCast(
        apiKey: String,
        movieId: String
    ): Flow<DataSource<CastResponse>> {
        return flow {
            try {
                val result = apiService.getMovieCast(movie_id = movieId, apiKey = apiKey)
                emit(DataSource.success(result))
            } catch (e: Exception) {
                if(e is IOException) {
                        emit(DataSource.error("No Internet Connection", null))
                } else {
                    Log.d("Movies-Cast-Error", e.localizedMessage.toString())
                    emit(DataSource.error("Something went wrong...", null))
                }
            }
        }
    }

    override suspend fun getsearchedmovies(
        apiKey: String,
        query: String
    ): Flow<DataSource<SearchResponses>> {
        return flow {
            try {
                val result = apiService.getsearchedmovies(apiKey = apiKey,query = query  )
                emit(DataSource.success(result))
            } catch (e: Exception) {
                if(e is IOException) {
                    emit(DataSource.error("No Internet Connection", null))
                } else {
                    Log.d("Querry-Error", e.localizedMessage.toString())
                    emit(DataSource.error("Something went wrong...", null))
                }
            }

        }

    }

}