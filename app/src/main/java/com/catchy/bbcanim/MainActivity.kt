package com.catchy.bbcanim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.catchy.bbcanim.ui.theme.BBCAnimTheme
import kotlin.math.roundToInt

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
                    CircularList(listOf("1","2","3","4","5","6","7","8","9"))
                }
            }
        }
    }
}

@Composable
fun CircularList(items: List<String>) {
    var scrollOffset by remember { mutableStateOf(0f) }
    val maxScrollOffset = items.size * 150f // Adjust as needed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .clip(MaterialTheme.shapes.large)
                .background(Color.Gray)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, delta ->
                        scrollOffset -= delta
                        if (scrollOffset < 0) {
                            scrollOffset = maxScrollOffset
                        } else if (scrollOffset > maxScrollOffset) {
                            scrollOffset = 0f
                        }
                    }
                }
        ) {
            items.forEachIndexed { index, item ->
                val angle = (2 * Math.PI * index / items.size).toFloat()
                val x = 150 * Math.cos(angle.toDouble()).toFloat() + scrollOffset
                val y = 0f // Adjust the vertical position as needed

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                        .size(50.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    BasicTextField(
                        value = item,
                        onValueChange = {},
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                    )
                }
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