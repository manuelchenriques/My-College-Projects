package com.example.battleships.views.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.battleships.R
import com.example.battleships.model.Stat
import com.example.battleships.ui.theme.TypoBattle
import kotlinx.coroutines.delay

@Composable
fun WaitingBox(text:String){
    Text(
        modifier = Modifier
            .background(Color(0x6FFFFFFF), RoundedCornerShape(10))
            .border(4.dp, Color.Black, RoundedCornerShape(10))
            .size(width = 250.dp, height = 100.dp),
        text = text,
        style = TypoBattle.h3,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary
    )
    Spacer(modifier = Modifier.height(200.dp))
    LoadingAnimation3()
}

@Composable
fun BackgroundImage(){
    Image(
        painter = painterResource(id = R.drawable.background_android),
        contentDescription = "background_image",
        modifier = Modifier.fillMaxHeight(),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun LoadingAnimation3(
    circleColor: Color = Color(0xFF4E596B),
    circleSize: Dp = 50.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {

    // 3 circles
    val circles = listOf(
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        }
    )

    circles.forEachIndexed { index, animatable ->

        LaunchedEffect(Unit) {

            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // container for circles
    Row(
        modifier = Modifier
        //.border(width = 2.dp, color = Color.Magenta)
    ) {

        // adding each circle
        circles.forEachIndexed { index, animatable ->

            // gap between the circles
            if (index != 0) {
                Spacer(modifier = Modifier.width(width = 6.dp))
            }

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(
                        color = circleColor
                            .copy(alpha = animatable.value)
                    )
            ) {
            }
        }
    }
}

@Composable
fun RankingList(ranking: List<Stat>) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(ranking) { rank ->
            Card(
                border = BorderStroke(2.dp, Color.Black),
                shape = RectangleShape,
                backgroundColor = Color(0x56FFFFFF),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Text(
                            text = rank.username,
                            style = TypoBattle.h4,
                            color = MaterialTheme.colors.primary,
                            softWrap = true
                        )
                        Text(
                            text = "Won: ${rank.gamesWon}",
                            style = TypoBattle.h4,
                            color = MaterialTheme.colors.primary,
                            softWrap = true
                        )
                        Text(
                            text = "Played: ${rank.gamesPlayed}",
                            style = TypoBattle.h4,
                            color = MaterialTheme.colors.primary,
                            softWrap = true
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GridCell(type:String?,round:Dp,squared:Dp){

    when (type){
        "Hit" -> {
            Canvas(modifier = Modifier.requiredSize(round)){
                this.drawCircle(color = Color(
                    red = 252,
                    green = 3,
                    blue = 3,
                    alpha = 255
                )
                )
            }
        }
        "Ship" -> {
            Canvas(modifier = Modifier.requiredSize(squared)){
                this.drawRoundRect(color = Color(red = 48, green = 51, blue = 54, alpha = 255))
            }
        }
        "Sunk" -> {
            Canvas(modifier = Modifier.requiredSize(squared)){
                this.drawRoundRect(color = Color(
                    red = 251,
                    green = 3,
                    blue = 3,
                    alpha = 255
                )
                )
            }
        }
        "Miss" -> {
            Canvas(modifier = Modifier.requiredSize(round)){
                this.drawCircle(color = Color(
                    red = 0,
                    green = 127,
                    blue = 255,
                    alpha = 255
                )
                )
            }
        }
    }

}

@Composable
fun UserCard(user:String?){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Card(
            border = BorderStroke(4.dp, Color.Black),
            shape = CircleShape,
            backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.CenterVertically)
        ) {
            Text(
                text = "$user's board:",
                style = TypoBattle.h3,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
            )
        }
    }
}

