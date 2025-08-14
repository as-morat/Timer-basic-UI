package com.example.timer

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("DefaultLocale")
@Composable
fun Timer(
    totalTime: Long = 100L * 1000
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableFloatStateOf(1f) }
    var currentTime by remember { mutableFloatStateOf(totalTime.toFloat()) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(currentTime, isRunning) {
        if (currentTime > 0f && isRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
        if (currentTime < 0f) isRunning = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(260.dp)
                .onSizeChanged { size = it },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Outer Glow Arc
                drawArc(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFFFD700).copy(alpha = 0.2f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height / 2f),
                        radius = size.width.toFloat()
                    ),
                    startAngle = -215f,
                    sweepAngle = 250f,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(50.dp.toPx(), cap = StrokeCap.Round)
                )

                // Background Arc
                drawArc(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF2C3E50), // deep steel blue
                            Color(0xFFBDC3C7)  // soft silver-blue
                        )
                    ),
                    startAngle = -215f,
                    sweepAngle = 250f,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(18.dp.toPx(), cap = StrokeCap.Round)
                )

                // Progress Arc
                drawArc(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFFD700), Color(0xFFFF5722))
                    ),
                    startAngle = -215f,
                    sweepAngle = 250f * value,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(25.dp.toPx(), cap = StrokeCap.Round)
                )

                // Pointer
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = (250f * value + 145f) * (PI / 180f).toFloat()
                val radius = size.width / 2f
                val a = cos(beta) * radius
                val b = sin(beta) * radius
                drawCircle(
                    color = Color.White,
                    radius = 12f,
                    center = Offset(center.x + a, center.y + b)
                )
            }

            // Time Text
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%.1f", currentTime / 1000f),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "seconds remaining",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Buttons
            Button(
                onClick = {
                    if (currentTime <= 0L) {
                        currentTime = totalTime.toFloat()
                        isRunning = true
                    } else {
                        isRunning = !isRunning
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(50)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                if (!isRunning || currentTime <= 0L) {
                                    listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                                } else {
                                    listOf(Color(0xFFE53935), Color(0xFFEF5350))
                                }
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = if (isRunning && currentTime >= 0L) "Stop"
                        else if (!isRunning && currentTime >= 0L) "Start"
                        else "Restart",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}