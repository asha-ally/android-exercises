package com.test.movies_test.data.remote.responses

data class CastResponse(
    var id: String,
    var cast: List<CastModel>
)

data class CastModel(
    var name: String,
    var profile_path: String?,
    var original_name: String,
)