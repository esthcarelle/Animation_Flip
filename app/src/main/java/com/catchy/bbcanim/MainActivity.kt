package com.catchy.bbcanim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.catchy.bbcanim.config.CircularListConfig
import com.catchy.bbcanim.ui.CircularListState
import com.catchy.bbcanim.ui.drag
import com.catchy.bbcanim.ui.rememberCircularListState
import com.catchy.bbcanim.ui.theme.BBCAnimTheme
import kotlin.math.roundToInt

private val colors = listOf(
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Magenta,
    Color.Yellow,
    Color.Cyan,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BBCAnimTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Circular List",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxWidth()
                                .background(Color.Gray).padding(all = 32.dp),
                            color = Color.Black,
                        )
                        CircularList(
                            visibleItems = 12,
                            circularFraction = .65f,
                            overshootItems = 5,
                            modifier = Modifier.fillMaxWidth().weight(1f),
                        ) {
                            for (i in 0 until 40) {
                                ListItem(
                                    text = "Item #$i",
                                    color = colors[i % colors.size],
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(all = 8.dp)
                .clip(shape = CircleShape)
                .background(color = color)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
@Composable
fun CircularList(
    visibleItems: Int,
    modifier: Modifier = Modifier,
    state: CircularListState = rememberCircularListState(),
    circularFraction: Float = 1f,
    overshootItems: Int = 3,
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
        state.setup(
            CircularListConfig(
                contentHeight = constraints.maxHeight.toFloat(),
                numItems = placeables.size,
                visibleItems = visibleItems,
                circularFraction = circularFraction,
                overshootItems = overshootItems,
            )
        )
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            for (i in state.firstVisibleItem..state.lastVisibleItem) {
                placeables[i].placeRelative(state.offsetFor(i))
            }
        }
    }
}

@Composable
fun CircularListItem(text: String) {
    BasicTextField(
        value = text,
        onValueChange = { /* Handle text change if needed */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BBCAnimTheme {
        Greeting("Android")
    }
}