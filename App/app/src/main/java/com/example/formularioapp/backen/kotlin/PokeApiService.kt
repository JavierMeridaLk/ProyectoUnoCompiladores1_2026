package com.example.formularioapp.backen.kotlin

import retrofit2.http.GET
import retrofit2.http.Path

data class PokemonResult(
    val name: String,
    val url: String
)

data class PokemonListResponse(
    val results: List<PokemonResult>
)

interface PokeApiService {

    // Obtener lista por rango
    @GET("pokemon")
    suspend fun getPokemons(
        @retrofit2.http.Query("limit") limit: Int,
        @retrofit2.http.Query("offset") offset: Int
    ): PokemonListResponse
}