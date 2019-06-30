package com.wiseassblog.androiddata.data.movementdatabase

import android.content.res.AssetManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wiseassblog.common.MovementRepositoryException
import com.wiseassblog.common.ResultWrapper
import com.wiseassblog.domain.domainmodel.Movement
import com.wiseassblog.domain.repository.IMovementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovementDatabaseImpl(private val assets: AssetManager) : IMovementRepository {
    override suspend fun getMovements(): ResultWrapper<Exception, List<Movement>> = withContext(Dispatchers.IO) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

        val jsonString = assets.open("movements.json")
            .bufferedReader()
            .use {
                it.readText()
            }

        val type = Types.newParameterizedType(List::class.java, Movement::class.java)
        val adapter = moshi.adapter<List<Movement>>(type)

        ResultWrapper.build { adapter.fromJson(jsonString) ?: throw MovementRepositoryException }
    }

    override suspend fun getMovementById(movementId: String): ResultWrapper<Exception, Movement> =
        withContext(Dispatchers.IO) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            val jsonString = assets.open("movements.json")
                .bufferedReader()
                .use {
                    it.readText()
                }

            val type = Types.newParameterizedType(List::class.java, Movement::class.java)
            val adapter = moshi.adapter<List<Movement>>(type)

            var movement: Movement? = null

            adapter.fromJson(jsonString)
                ?.forEach {
                    if (it.name == movementId) movement = it
                }


            ResultWrapper.build { movement ?: throw MovementRepositoryException }
        }

}