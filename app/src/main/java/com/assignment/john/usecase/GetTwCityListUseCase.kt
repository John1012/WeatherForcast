package com.assignment.john.usecase

import android.content.Context
import com.assignment.john.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

@Serializable
data class CityInfo(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val state: String
)

@Serializable
data class Coord(
    val lat: Double,
    val lon: Double
)

class GetTwCityListUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun call(): List<String> = withContext(Dispatchers.Default) {
        return@withContext try {
            val inputStream = context.resources.openRawResource(R.raw.city_list)
            val cityInfoList = Json.decodeFromStream<List<CityInfo>>(inputStream)
            cityInfoList
                .filter { it.country == "TW" }
                .map { it.name }
        } catch (e: Exception) {
            emptyList()
        }
    }
}