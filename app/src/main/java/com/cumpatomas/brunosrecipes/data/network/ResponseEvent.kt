package com.cumpatomas.brunosrecipes.data.network

sealed class ResponseEvent <out T> {
    data class Success <T> (val data: T): ResponseEvent<T>()
    data class Error <T> (val exception: Exception): ResponseEvent<T>()
}