package com.example.topsketchup



import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter


class MainActivity : ComponentActivity() {
    private val action: MutableState<Pair<Boolean, Pair<Float, Float>>?> = mutableStateOf(null)
    private val path = Path()


    private val collectList = mutableListOf<Pair<Boolean, Pair<Float, Float>>>()


    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Canvas(modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            action.value = Pair(true, Pair(it.x, it.y))
                            path.moveTo(it.x, it.y)

                        }
                        MotionEvent.ACTION_MOVE -> {
                            action.value = Pair(false, Pair(it.x, it.y))
                            path.lineTo(it.x, it.y)

                        }
                        MotionEvent.ACTION_UP -> {
                        }
                        else -> false
                    }
                    true
                }
            ) {
                action.value?.let {

                    collectList.add(it)
                    drawPath(
                        path = collectList.toPath(),
                        color = Color.Green,
                        alpha = 1f,
                        style = Stroke(10f))




                }
            }
        }
    }

    fun List<Pair<Boolean, Pair<Float, Float>>>.toPath(): Path {
        val path = Path()
        forEach {
            if(it.first) {
                path.moveTo(it.second.first, it.second.second)
            } else {
                path.lineTo(it.second.first, it.second.second)
            }
        }

        return path
    }
}

