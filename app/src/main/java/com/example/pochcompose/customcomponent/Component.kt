package com.example.pochcompose.customcomponent

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pochcompose.R
import timber.log.Timber

enum class StarsTypes {
    FILLED,
    HALFFILLED,
    EMPTY

}

@SuppressLint("RememberReturnType")
@Composable
fun HeroRating(
    modifier: Modifier,
    rating: Double,
    scaleFactor: Float = 2f
) {

    val result = RatingCalculation(rating = rating)
    val starPathString = stringResource(R.string.star_rating)
    val starPath = remember {
        PathParser().parsePathString(starPathString).toPath()
    }
    val starPathBounds = remember {
        starPath.getBounds()
    }
//    HalfFilledStar(starPath, starPathBounds, scaleFactor = scaleFactor)
//    FilledStar(starPathPath = starPath, starPathBounds = starPathBounds, scaleFactor = scaleFactor)
//    EmptyFilledStar(
//        starPathPath = starPath,
//        starPathBounds = starPathBounds,
//        scaleFactor = scaleFactor
//    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {

        result[StarsTypes.FILLED.name]?.let {
            repeat(it) {
                FilledStar(
                    starPathPath = starPath,
                    starPathBounds = starPathBounds,
                    scaleFactor = scaleFactor
                )
            }
        }

       result[StarsTypes.HALFFILLED.name]?.let {
            repeat(it) {
                HalfFilledStar(
                    starPathPath = starPath,
                    starPathBounds = starPathBounds,
                    scaleFactor = scaleFactor
                )
            }
        }
        result[StarsTypes.EMPTY.name]?.let {
            repeat(it) {
                EmptyFilledStar(
                    starPathPath = starPath,
                    starPathBounds = starPathBounds,
                    scaleFactor = scaleFactor
                )
            }
        }

    }
}

@Composable
fun FilledStar(
    starPathPath: Path,
    starPathBounds: Rect,
    scaleFactor: Float
) {

    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        val canvasSize = this.size
        scale(scale = scaleFactor) {
            val pathWidth = starPathBounds.width
            val pathHeight = starPathBounds.height
            val pathLeft = (canvasSize.width / 2f) - (pathWidth / 1.6f)
            val pathTop = (canvasSize.height / 2f) - (pathHeight / 1.6f)

            translate(left = pathLeft, top = pathTop) {
                drawPath(
                    path = starPathPath,
                    color = Color.Yellow
                )
            }
        }
    }

}

@Composable
fun EmptyFilledStar(
    starPathPath: Path,
    starPathBounds: Rect,
    scaleFactor: Float
) {

    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        val canvasSize = this.size
        scale(scale = scaleFactor) {
            val pathWidth = starPathBounds.width
            val pathHeight = starPathBounds.height
            val pathLeft = (canvasSize.width / 2f) - (pathWidth / 1.6f)
            val pathTop = (canvasSize.height / 2f) - (pathHeight / 1.6f)

            translate(left = pathLeft, top = pathTop) {
                drawPath(
                    path = starPathPath,
                    color = Color.LightGray
                )
            }
        }
    }

}

@Composable
fun HalfFilledStar(
    starPathPath: Path,
    starPathBounds: Rect,
    scaleFactor: Float
) {

    Canvas(
        modifier = Modifier
            .size(24.dp)
    ) {
        val canvasSize = this.size
        scale(scale = scaleFactor) {
            val pathWidth = starPathBounds.width
            val pathHeight = starPathBounds.height
            val pathLeft = (canvasSize.width / 2f) - (pathWidth / 1.7f)
            val pathTop = (canvasSize.height / 2f) - (pathHeight / 1.7f)

            translate(left = pathLeft, top = pathTop) {
                drawPath(
                    path = starPathPath,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
                clipPath(
                    path = starPathPath
                ) {
                    drawRect(
                        color = Color.Yellow,
                        size = Size(
                            width = starPathBounds.maxDimension / 1.7f,
                            height = starPathBounds.maxDimension * 2
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun RatingCalculation(rating: Double): Map<String, Int> {
    val maxStars by remember { mutableStateOf(5) }
    var fillStarts by remember { mutableStateOf(0) }
    var halfFilledStarts by remember { mutableStateOf(0) }
    var emptyStarts by remember { mutableStateOf(0) }

    LaunchedEffect(rating) {
        var (firstNumber, secondNumber) = rating.toString()
            .split(".")
            .map {
                it.toInt()
            }

        // 4.5   (first num to 1,2,3,4,5  & second: 1,2,3,4,5,6,7,8,9)
        if (firstNumber in 0..5 && secondNumber in 0..9) {
            fillStarts = firstNumber

            if (secondNumber in 1..5) {
                halfFilledStarts++
            }

            if (secondNumber in 6..9) {
                fillStarts++
            }

            if (firstNumber == 5 && secondNumber > 0) {
                emptyStarts = 5
                fillStarts = 0
                halfFilledStarts = 0
            }
        } else {
            Timber.i("Rating Widget Invalid rating numbers")
        }
    }
    emptyStarts = maxStars - (fillStarts + halfFilledStarts)
    return mapOf(
        StarsTypes.FILLED.name to fillStarts,
        StarsTypes.HALFFILLED.name to halfFilledStarts,
        StarsTypes.EMPTY.name to emptyStarts
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultFillRater() {
    val starPathString = stringResource(R.string.star_rating)
    val starPath = remember {
        PathParser().parsePathString(starPathString).toPath()
    }
    val starPathBounds = remember {
        starPath.getBounds()
    }
    EmptyFilledStar(starPath, starPathBounds, scaleFactor = 0.5f)
}