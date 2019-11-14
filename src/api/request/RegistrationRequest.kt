package com.example.api.request

data class RegistrationRequest(val userId: Int, val friendsList: String = "", val createdPasswordHash: String,
                               val displayName: String, val country: String) {
}