package com.example.topsketchup


import android.annotation.SuppressLint
import android.os.Bundle

import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import com.example.topsketchup.ui.theme.TopSketchupTheme
import io.mhssn.colorpicker.ColorPicker


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TopSketchupTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    ScratchPad()
                }
            }
        }

    }

}

data class PathState(
    val path: Path,
    val color:Color)
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingTools(drawColor:MutableState<Color>){
    Row (modifier = Modifier.padding(horizontal = 8.dp)){
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = { drawColor.value = Color.Black }) {
            Canvas(modifier = Modifier
                .width(50.dp)
                .height(50.dp), onDraw ={
                drawCircle(color = Color.Black)
            } )
        }
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = { drawColor.value = Color.Green }) {
            Canvas(modifier = Modifier
                .width(50.dp)
                .height(50.dp), onDraw ={
                drawCircle(color = Color.Green)
            } )
        }
//        ColorPicker(onPickedColor = {color ->
//            drawColor.value = color
//
//        })
    }

}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScratchPad(){
    val path = remember {
        mutableStateOf(mutableListOf<PathState>())
    }
    var visible by remember {
        mutableStateOf(false)
    }
    Scaffold (
        topBar = {
            ComposePaintAppBar(path)
        }
    ){
        PaintBody(path)
    }
}
@Composable
fun ComposePaintAppBar(

    path:MutableState<MutableList<PathState>>
) {
    val drawColor = remember {
        mutableStateOf(Color.Black)
    }
    path.value.add(PathState(Path(),drawColor.value))
    TopAppBar(
        title = {
            Text(text = "ScratchPad")
        },
        actions = {
            IconButton(onClick ={

                path.value.clear()

            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription ="Delete"
                )
            }
            IconButton(onClick ={
                val  lastItem = path.value.last()
                path.value.remove(lastItem)

            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription ="Delete"
                )
            }
        }
    )
}
@Composable
fun PaintBody(path:MutableState<MutableList<PathState>>)
{
    Box(modifier = Modifier.fillMaxSize()){
        val drawColor = remember {
            mutableStateOf(Color.Black)
        }
        path.value.add(PathState(Path(),drawColor.value))
       DrawingCanvas(drawColor,path.value)
        DrawingTools(drawColor = drawColor)
    }

}
// A composable that listen to all the movements across the XY axis from the current path to all other movements made and draws the line on each movement detected.
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    drawColor: MutableState<Color>,
    path: MutableList<PathState>
) {
    val currentPath = path.last().path
    val movePath = remember{ mutableStateOf<Offset?>(null)}

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentPath.moveTo(it.x, it.y)

                    }
                    MotionEvent.ACTION_MOVE -> {
                        movePath.value = Offset(it.x, it.y)
                    }
                    else -> {
                        movePath.value = null
                    }
                }
                true
            }
    ){
        movePath.value?.let {
            currentPath.lineTo(it.x,it.y)
            drawPath(
                path = currentPath,
                color = drawColor.value,
                style = Stroke(10f)
            )
        }
        path.forEach {
            drawPath(
                path = it.path,
                color = it.color,
                style  = Stroke(10f)
            )
        }


  }

}

@Preview(showBackground = true)
@Composable
fun MyApp(){
    TopSketchupTheme {
        ScratchPad()
    }
}

