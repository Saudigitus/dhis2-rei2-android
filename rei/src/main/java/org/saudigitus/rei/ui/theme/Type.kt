package org.saudigitus.rei.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.saudigitus.rei.R

private val rubikFontFamily = FontFamily(
    Font(resId = R.font.rubik_regular),
    Font(resId = R.font.rubik_light),
    Font(resId = R.font.rubik_medium),
    Font(resId = R.font.rubik_bold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = rubikFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)
