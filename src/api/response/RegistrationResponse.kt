package com.example.api.response

data class RegistrationResponse(val token: String, val friendsList: List<Friend>) {
}