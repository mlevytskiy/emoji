package com.example.api.response

import com.example.model.App

data class LoginResponse(val token: String, val friendsList: List<Friend>, val myApps: List<App>)