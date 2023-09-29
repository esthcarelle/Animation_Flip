package com.catchy.bbcanim.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset
import com.catchy.bbcanim.config.CircularListConfig

@Stable
interface CircularListState {
    val verticalOffset: Float
    val firstVisibleItem: Int
    val lastVisibleItem: Int

    suspend fun snapTo(value: Float)
    suspend fun decayTo(velocity: Float, value: Float)
    suspend fun stop()
    fun offsetFor(index: Int): IntOffset
    fun setup(config: CircularListConfig)
}