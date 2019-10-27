package com.example.api.request

data class LoginRequest(val userId: String, val friendsList: String = "", val passwordHash: String) {

}