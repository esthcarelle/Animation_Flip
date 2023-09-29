package com.catchy.bbcanim.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.verticalDrag
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.catchy.bbcanim.config.CircularListConfig
import com.catchy.bbcanim.ui.theme.BBCAnimTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CircularListStateImpl(
    currentOffset: Float = 0f,
) : CircularListState {

    private val animatable = Animatable(currentOffset)
    private var itemHeight = 0f
    private var config = CircularListConfig()
    private var initialOffset = 0f

    private val minOffset: Float
        get() = -(config.numItems - 1) * itemHeight

    override val verticalOffset: Float
        get() = animatable.value

    override val firstVisibleItem: Int
        get() = ((-verticalOffset - initialOffset) / itemHeight).toInt().coerceAtLeast(0)

    override val lastVisibleItem: Int
        get() = (((-verticalOffset - initialOffset) / itemHeight).toInt() + config.visibleItems)
            .coerceAtMost(config.numItems - 1)

    override suspend fun snapTo(value: Float) {
        animatable.snapTo(value.coerceIn(-(config.numItems - 1) * itemHeight, 0f))
    }

    override suspend fun decayTo(velocity: Float, value: Float) {
        // TBD
    }

    override suspend fun stop() {
        animatable.stop()
    }

    override fun setup(config: CircularListConfig) {
        this.config = config
        itemHeight = config.contentHeight / config.visibleItems
        initialOffset = (config.contentHeight - itemHeight) / 2f
    }

    override fun offsetFor(index: Int): IntOffset {
        val y = (verticalOffset + initialOffset + index * itemHeight)
        return IntOffset(
            x = 0,
            y = y.roundToInt()
        )
    }

    companion object {
        val Saver = Saver<CircularListStateImpl, List<Any>>(
            save = { listOf(it.verticalOffset) },
            restore = {
                CircularListStateImpl(it[0] as Float)
            }
        )
    }
}

// 4
@Composable
fun rememberCircularListState(): CircularListState {
    val state = rememberSaveable(saver = CircularListStateImpl.Saver) {
        CircularListStateImpl()
    }
    return state
}

fun Modifier.drag(
    state: CircularListState,
) = pointerInput(Unit) {
    val decay = splineBasedDecay<Float>(this)
    coroutineScope {
        while (true) {
            val pointerId = awaitPointerEventScope { awaitFirstDown().id }
            state.stop()
            val tracker = VelocityTracker()
            awaitPointerEventScope {
                verticalDrag(pointerId) { change ->
                    val verticalDragOffset = state.verticalOffset + change.positionChange().y
                    launch {
                        state.snapTo(verticalDragOffset)
                    }
                    tracker.addPosition(change.uptimeMillis, change.position)
                    change.consumePositionChange()
                }
            }
            val velocity = tracker.calculateVelocity().y
            val targetValue = decay.calculateTargetValue(state.verticalOffset, velocity)
            launch {
                state.decayTo(velocity, targetValue)
            }
        }
    }
}


 val colors = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Magenta,
    Color.Yellow,
    Color.Cyan,
)

@Preview(showBackground = true, widthDp = 420)
@Composable
fun PreviewCircularList5() {
    BBCAnimTheme() {
        Surface {
            CircularList(
                visibleItems = 12,
                circularFraction = .65f,
                modifier = Modifier.fillMaxSize(),
            ) {

            }
        }
    }
}