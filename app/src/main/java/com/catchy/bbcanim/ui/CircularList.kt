package com.catchy.bbcanim.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import com.catchy.bbcanim.config.CircularListConfig


@Composable
fun CircularList(
    visibleItems: Int,
    modifier: Modifier = Modifier,
    // 1
    state: CircularListState = rememberCircularListState(),
    circularFraction: Float = 1f,
    content: @Composable () -> Unit,
) {
    check(visibleItems > 0) { "Visible items must be positive" }
    check(circularFraction > 0f) { "Circular fraction must be positive" }

    Layout(
        modifier = modifier.clipToBounds().drag(state),
        content = content,
    ) { measurables, constraints ->
        val itemHeight = constraints.maxHeight / visibleItems
        val itemConstraints = Constraints.fixed(width = constraints.maxWidth, height = itemHeight)
        val placeables = measurables.map { measurable -> measurable.measure(itemConstraints) }
        // 2
        state.setup(
            CircularListConfig(
                contentHeight = constraints.maxHeight.toFloat(),
                numItems = placeables.size,
                visibleItems = visibleItems,
                circularFraction = circularFraction,
            )
        )
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            // 3
            for (i in state.firstVisibleItem..state.lastVisibleItem) {
                // 4
                placeables[i].placeRelative(state.offsetFor(i))
            }
        }
    }
}