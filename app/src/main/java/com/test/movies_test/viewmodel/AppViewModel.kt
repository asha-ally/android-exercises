package com.test.movies_test.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.movies_test.data.remote.DataSource
import com.test.movies_test.data.remote.Status
import com.test.movies_test.data.remote.responses.CastResponse
import com.test.movies_test.data.remote.responses.MovieDetailsResponse
import com.test.movies_test.data.remote.responses.MoviesResponse
import com.test.movies_test.data.remote.responses.SearchResponses
import com.test.movies_test.data.repository.AppRepository
import com.test.movies_test.utils.AppConstants.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appRepository: AppRepository
): ViewModel() {

    private val _upcomingMovies = MutableStateFlow<DataSource<MoviesResponse>>(DataSource.loading(null))
    val upcomingMovies get() = _upcomingMovies

    fun getUpcomingMovies(apiKey: String) {
        viewModelScope.launch {
            appRepository.getUpComingMovies( apiKey).collect {
                when(it.status) {
                    Status.EMPTY -> { _upcomingMovies.emit(DataSource.empty()) }
                    Status.SUCCESS -> { _upcomingMovies.emit(DataSource.success(it.data)) }
                    else -> { _upcomingMovies.emit(DataSource.error(it.message.toString(), null)) }
                }
            }
        }
    }

    private val _playNowMovies = MutableStateFlow<DataSource<MoviesResponse>>(DataSource.loading(null))
    val playNowMovies get() = _playNowMovies

    fun getPlayNowMovies(apiKey: String) {
        viewModelScope.launch {
            appRepository.getPlayingNowMovies(apiKey).collect {
                when(it.status) {
                    Status.SUCCESS -> {
                        _playNowMovies.emit(DataSource.success(it.data))
                    }
                    else -> {
                        _playNowMovies.emit(DataSource.error(it.message.toString(), null))
                    }
                }
            }
        }
    }

    private val _moviesDetails = MutableStateFlow<DataSource<MovieDetailsResponse>>(DataSource.loading(null))
    val moviesDetails get() = _moviesDetails
    fun getMoviesDetails(apiKey: String, movieId: String) {
        viewModelScope.launch {
            appRepository.getMoviesDetails(apiKey, movieId).collect {
                when(it.status) {
                    Status.SUCCESS -> {
                        _moviesDetails.emit(DataSource.success(it.data))
                    }
                    else -> {
                        _moviesDetails.emit(DataSource.error(it.message.toString(), null))
                    }
                }
            }
        }
    }


    private val _movieCast = MutableStateFlow<DataSource<CastResponse>>(DataSource.loading(null))
    val movieCast get() = _movieCast
    fun getMoviesCast(apiKey: String, movieId: String) {
        viewModelScope.launch {
            appRepository.getMoviesCast(apiKey, movieId).collect {
                when(it.status) {
                    Status.SUCCESS -> {
                        _movieCast.emit(DataSource.success(it.data))
                    }
                    else -> {
                        _movieCast.emit(DataSource.error(it.message.toString(), null))
                    }
                }
            }
        }
    }

    val query = mutableStateOf("")

    init {
        newSearch(apiKey = API_KEY,query.toString())
    }



    private val _search = MutableStateFlow<DataSource<SearchResponses>>(DataSource.loading(null))
    val search get() =_search
    fun newSearch(apiKey: String,query: String){
            viewModelScope.launch {
                appRepository.getsearchedmovies(apiKey, query).collect {
                    when(it.status) {
                        Status.SUCCESS -> {
                            _search.emit(DataSource.success(it.data))
                        }
                        else -> {
                            _search.emit(DataSource.error(it.message.toString(), null))
                        }
                    }
                }
            }
        }




    fun onQueryChanged(query: String){
        this.query.value = query

    }

}
private val _reload = mutableStateOf("")
val reload get() = _reload
fun getreload(){

}
