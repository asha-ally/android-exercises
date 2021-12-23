package com.test.movies_test.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.test.movies_test.R
import com.test.movies_test.components.CastListItem
import com.test.movies_test.data.remote.Status
import com.test.movies_test.data.remote.responses.CastModel
import com.test.movies_test.data.remote.responses.CastResponse
import com.test.movies_test.data.remote.responses.MovieDetailsResponse
import com.test.movies_test.ui.theme.blue500
import com.test.movies_test.ui.theme.dark_gray
import com.test.movies_test.ui.theme.red
import com.test.movies_test.ui.theme.white
import com.test.movies_test.utils.AppConstants
import com.test.movies_test.utils.AppConstants.API_KEY
import com.test.movies_test.viewmodel.AppViewModel


@Composable
fun DetailsScreen(
    viewModel: AppViewModel,
    back: () -> Unit,
    movieId: String
) {

    var movieDetails: MovieDetailsResponse? = null

    viewModel.getMoviesDetails(API_KEY, movieId = movieId)
    val result = viewModel.moviesDetails.collectAsState().value

    when(result.status) {
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
            result.data?.let {
                movieDetails = it
                viewModel.getMoviesCast(API_KEY, movieId = movieId)
            }
        }
        Status.ERROR -> {
            Text(
                text = "${result.message}",
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
        }
    }

    val cast = viewModel.movieCast.collectAsState().value
    when(cast.status) {
        Status.SUCCESS -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    item {
                        MovieImageSection(movieDetails, back)
                        MovieDetailsSection(movieDetails)
                        MovieDescriptionSection(movieDetails)
                        MovieCastSection(cast.data!!)
//                        MovieCastSection(cast.data?.cast)
                    }
                }
            }
        }

        Status.ERROR -> {
            AlertDialogComponent(viewModel)
            Text(
                text = "${result.message}",
                modifier = Modifier.wrapContentSize(Alignment.Center)
            )
            
        }
    }
}

@Composable
fun MovieImageSection(moviesDetails: MovieDetailsResponse?, back : () -> Unit) {
    ConstraintLayout {
        Image(
            painter = rememberImagePainter(AppConstants.IMAGE_BASEURL + moviesDetails?.backdrop_path),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(
                    shape = RoundedCornerShape(0.dp)
                )
        )
        Icon(
            painterResource(id = R.drawable.ic_back),
            tint = white,
            contentDescription = null,
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 20.dp
                )
                .clickable {
                    back.invoke()
                }
        )
    }
}

@Composable
fun MovieDetailsSection(moviesDetails: MovieDetailsResponse?) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .absolutePadding(
                top = 20.dp,
                left = 20.dp,
                right = 20.dp,
            )
    ) {

        val (
            tvMovieName, rowLike, tvDate,
            tvVotingRatio, tvGenre, rowLang
        ) = createRefs()
        Text(
            modifier = Modifier.constrainAs(tvMovieName) {},
            text = moviesDetails?.original_title.toString(),
            color = MaterialTheme.colors.onBackground,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.pt_sans_bold))
            ),
            fontSize = 23.sp,
        )

        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .constrainAs(rowLike) {
                    start.linkTo(parent.start)
                    top.linkTo(tvMovieName.bottom)
                }
        ) {
            Text(
                text = "Rating: ",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = FontFamily(Font(R.font.pt_sans_bold))
                )
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "${moviesDetails?.vote_average}/10",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = dark_gray,
                    fontFamily = FontFamily(Font(R.font.pt_sans_regular))
                )
            )
        }

        Row(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                )
                .constrainAs(tvDate) {
                    top.linkTo(rowLike.bottom)
                },
        ) {
            Text(
                text = "Release Date: ",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = FontFamily(Font(R.font.pt_sans_bold))
                )
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = moviesDetails?.release_date.toString(),
                style = TextStyle(
                    fontSize = 18.sp,
                    color = dark_gray,
                    fontFamily = FontFamily(Font(R.font.pt_sans_regular))
                )
            )
        }

        Text(
            modifier = Modifier
                .padding(top = 20.dp)
                .constrainAs(tvVotingRatio) {
                    end.linkTo(parent.end)
                    top.linkTo(tvMovieName.bottom)
                },
            text = "${moviesDetails?.vote_count} Voting",
            style = TextStyle(
                color = dark_gray,
                fontFamily = FontFamily(Font(R.font.pt_sans_regular))
            ),
            fontSize = 18.sp,
        )


        Row(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                )
                .constrainAs(tvGenre) {
                    top.linkTo(tvDate.bottom)
                },
        ) {
            Text(
                text = "Duration: ",
                style = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.pt_sans_bold))
                ),
                fontSize = 18.sp,
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "${moviesDetails?.runtime} Mins",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = FontFamily(Font(R.font.pt_sans_regular))
                )
            )
        }

        Row(
            modifier = Modifier
                .padding(top = 5.dp)
                .constrainAs(rowLang) {
                    top.linkTo(tvGenre.bottom)
                }
        ) {
            Text(
                text = "Language: ",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground,
                    fontFamily = FontFamily(Font(R.font.pt_sans_bold))
                )
            )
            Text(
                modifier = Modifier.padding(start = 5.dp),
                text = "English",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = dark_gray,
                    fontFamily = FontFamily(Font(R.font.pt_sans_regular))
                )
            )
        }
    }
}

@Composable
fun MovieDescriptionSection(moviesDetails: MovieDetailsResponse?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .absolutePadding(
                top = 40.dp,
                left = 20.dp,
                right = 20.dp,
            )
    ) {
        Text(
            text = "Movie Description ",
            style = TextStyle(
                fontSize = 19.sp,
                color = MaterialTheme.colors.onBackground,
                fontFamily = FontFamily(Font(R.font.pt_sans_bold))
            )
        )
        Text(
            modifier = Modifier.padding(top = 10.dp),
            text = moviesDetails?.overview.toString(),
            style = TextStyle(
                fontSize = 16.sp,
                color = dark_gray,
                fontFamily = FontFamily(Font(R.font.pt_sans_regular))
            ),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun MovieCastSection(cast: CastResponse) {
    var isLoading by remember { mutableStateOf(false)}
    LoadingScreen(isLoading = isLoading) {
        Text(text = "Your movie is being downloaded")
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .absolutePadding(
                top = 20.dp,
                left = 20.dp,
                right = 20.dp,
            )
    ) {
        val (tvCast, tvViewAll, rowCast, btnBook) = createRefs()

        Text(
            text = "Cast",
            style = TextStyle(
                fontSize = 19.sp,
                color = dark_gray,
                fontFamily = FontFamily(Font(R.font.pt_sans_bold))
            ),
            modifier = Modifier.constrainAs(tvCast) {}
        )

        Text(
            text = "View All",
            style = TextStyle(
                fontSize = 13.sp,
                color = red,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(tvViewAll) {
                end.linkTo(parent.end)
                top.linkTo(tvCast.top)
                bottom.linkTo(tvCast.bottom)
            }
        )

        LazyRow(
            modifier = Modifier
                .padding(top = 10.dp)
                .constrainAs(rowCast) {
                    top.linkTo(tvCast.bottom)
                }
        ) {
            itemsIndexed(cast.cast) { index: Int, item: CastModel ->
                CastListItem(item)
            }
        }

        Button(
            onClick = {
                      isLoading = !isLoading
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = blue500),
            modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp)
                .fillMaxWidth()
                .constrainAs(btnBook) {
                    top.linkTo(rowCast.bottom)
                },
        ) {
            Text(
                text = "Download movie",
                color = white
            )

        }


    }

}


@Composable
fun LoadingScreen(
    isLoading: Boolean,
    content: @Composable () -> Unit
) = if (isLoading
) {
    Box(
        modifier = Modifier.fillMaxSize().background(color =MaterialTheme.colors.background )) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Loading")
            CircularProgressIndicator()
        }
    }
} else {
    content()
}



    @Composable
    fun AlertDialogComponent(viewModel: AppViewModel) {
        val openDialog = remember { mutableStateOf(true) }
        if (openDialog.value) {
            
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text("Something happened Try Again..", color = MaterialTheme.colors.onPrimary) },

    
                confirmButton = {

                    TextButton(
                        onClick = {
                            openDialog.value = false
                        }
                    ) {
                        Text("Try again", color = MaterialTheme.colors.onPrimary)

                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false

                        }
                    ) {
                        Text("Dismiss", color = MaterialTheme.colors.onPrimary)
                    }
                },
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onPrimary
            )
        }
    
}

