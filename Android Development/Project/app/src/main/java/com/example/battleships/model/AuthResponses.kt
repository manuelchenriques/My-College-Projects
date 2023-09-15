package com.example.battleships.model

sealed class AuthResponses {
    object SignInSuccessful: AuthResponses()
    object SignInFailed: AuthResponses()
    object RegisterSuccessful: AuthResponses()
    object RegisterFailed: AuthResponses()
    object BadCredentials: AuthResponses()
}