package com.example.battleships.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.battleships.R

// Set of Material typography styles to start with
val Typography = Typography(
        body1 = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        )
        /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

private val BattleShip = FontFamily(
        Font(R.font.battleships_font)
)

val TypoBattle = Typography(
        h1 = TextStyle(
                fontFamily = BattleShip,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                fontSize = 42.sp
        ),
        h3 = TextStyle(
                fontFamily = BattleShip,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                fontSize = 25.sp
        ),
        h4 = TextStyle(
                fontFamily = BattleShip,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                fontSize = 18.sp
        )
)
