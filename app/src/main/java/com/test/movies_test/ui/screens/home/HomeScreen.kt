@file:Suppress("PreviewAnnotationInFunctionWithParameters")
package com.test.movies_test.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.movies_test.R
import com.test.movies_test.components.PlayingNowMoviesList
import com.test.movies_test.components.SearchedMoviesList
import com.test.movies_test.components.UpComingMoviesListItem
import com.test.movies_test.data.remote.Status
import com.test.movies_test.data.remote.responses.MovieModel
import com.test.movies_test.data.remote.responses.SearchModel
import com.test.movies_test.navigation.Actions
import com.test.movies_test.ui.theme.dark_gray
import com.test.movies_test.utils.AppConstants.API_KEY
import com.test.movies_test.viewmodel.AppViewModel

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    actions: Actions,
) {
    viewModel.getPlayNowMovies(API_KEY)
    val playMovies = arrayListOf<MovieModel>()
    val playNowResult = viewModel.playNowMovies.collectAsState().value
    when(playNowResult.status) {
        Status.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Status.SUCCESS -> {
            playNowResult.data?.results?.let {
                playMovies.addAll(it)
                viewModel.getUpcomingMovies(API_KEY)
            }
        }
        Status.EMPTY -> {
            Text(
                modifier = Modifier.wrapContentSize(Alignment.Center),
                text = "No Popular Movies Found..",
                color = MaterialTheme.colors.onBackground
            )
        }
        Status.ERROR -> {
            Text(
                modifier = Modifier.wrapContentSize(Alignment.Center),
                text = "${playNowResult.message}",
                color = MaterialTheme.colors.onBackground
            )
        }
    }

    val upcomingMovies = viewModel.upcomingMovies.collectAsState().value
    when(upcomingMovies.status) {
        Status.SUCCESS -> {
            upcomingMovies.data?.results?.let { upcomingMoviesData ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .absolutePadding(
                                top = 20.dp,
                                left = 20.dp,
                                right = 20.dp,
                            )
                    ) {
                        item {

                            var query = viewModel.query.value
                            val keyboardController = LocalSoftwareKeyboardController.current
                            val searchedmovies = arrayListOf<SearchModel>()
                            val searchedMoviesResult = viewModel.search.collectAsState().value
                            when (searchedMoviesResult.status){
                                Status.LOADING -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                                Status.SUCCESS -> {
                                    searchedMoviesResult.data?.results?.let {
                                        searchedmovies.addAll(it)
                                        viewModel.getUpcomingMovies(API_KEY)
                                    }
                                }
                                Status.EMPTY -> {
                                    Text(
                                        modifier = Modifier.wrapContentSize(Alignment.Center),
                                        text = "Search something different",
                                        color = MaterialTheme.colors.onBackground
                                    )
                                }
                                Status.ERROR -> {
                                    Text(
                                        modifier = Modifier.wrapContentSize(Alignment.Center),
                                        text = "${playNowResult.message}",
                                        color = MaterialTheme.colors.onBackground
                                    )
                                }
                            }

                            Surface(
                                elevation= 8.dp,
                                modifier = Modifier.fillMaxWidth(),
                                color=MaterialTheme.colors.surface
                            ) {
                                Row(
                                    modifier=Modifier.fillMaxWidth()
                                ) {

                                    TextField(
                                        value = query,
                                        onValueChange = {
                                                value -> viewModel.onQueryChanged(value)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = {Text(text="Search")},
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Search),

                                        textStyle = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 18.sp),
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Search,
                                                contentDescription ="",
                                                modifier = Modifier
                                                    .padding(15.dp)
                                                    .size(24.dp)
                                            )

                                        },
                                        singleLine = true,
                                        shape = RectangleShape,
                                        colors = TextFieldDefaults.textFieldColors(
                                            textColor = MaterialTheme.colors.onBackground,
                                            cursorColor = MaterialTheme.colors.onPrimary,
                                            leadingIconColor = MaterialTheme.colors.onPrimary,
                                            trailingIconColor = MaterialTheme.colors.onPrimary,
                                            backgroundColor = MaterialTheme.colors.background,
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            disabledIndicatorColor = Color.Transparent
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                       viewModel.newSearch(API_KEY,query)
                                                keyboardController!!.hide()
                                            },

                                            )
                                    )



                                }

                            }

                            SearchedMoviesList(searchList = searchedmovies, action = actions )
                            PlayingNowMoviesList(playMovies, actions)
                        }

                        item {
                            Spacer(modifier = Modifier.padding(top = 40.dp))
                            Text(
                                text = "Upcoming Movies",
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.pt_sans_bold)),
                                    color = dark_gray
                                )
                            )
                        }

                        itemsIndexed(upcomingMoviesData) { index: Int, item: MovieModel ->
                            Spacer(modifier = Modifier.padding(top = 10.dp))
                            UpComingMoviesListItem(model = item, actions = actions)
                        }


                    }
                }
            }
        }
        Status.ERROR -> {
            Text(
                modifier = Modifier.wrapContentSize(Alignment.Center),
                text = "No Movies Found",
                color = MaterialTheme.colors.onBackground
            )
        }
    }
}







