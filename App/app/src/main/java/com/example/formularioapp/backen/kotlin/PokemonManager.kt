package com.example.formularioapp.backen.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PokemonManager {

    suspend fun obtenerPokemons(inicio: Int, fin: Int): List<String> {

        val cantidad = fin - inicio + 1
        val offset = inicio - 1

        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getPokemons(cantidad, offset)

                response.results.map { pokemon ->
                    pokemon.name.replaceFirstChar { it.uppercase() }
                }

            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}