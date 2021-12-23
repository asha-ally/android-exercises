package com.test.movies_test.data.remote.responses

data class SearchResponses(
    val results: List<SearchModel>
)

data class SearchModel(
    var id: String,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var poster_path: String,
    var vote_average: String
)