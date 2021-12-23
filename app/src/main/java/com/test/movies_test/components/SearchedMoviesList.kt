package com.test.movies_test.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.movies_test.data.remote.responses.SearchModel
import com.test.movies_test.navigation.Actions

@Composable
fun SearchedMoviesList(
    searchList: List<SearchModel>,
    action: Actions
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 15.dp
            )
    ) {


        LazyRow(
            modifier = Modifier.padding(top = 15.dp)
        ) {
            itemsIndexed(searchList) { index: Int, item: SearchModel ->
                SearchedMovieListItem(model = item, action = action)
            }
        }
    }
}