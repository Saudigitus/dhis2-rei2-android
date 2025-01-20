package com.saudigitus.support_module.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorComponent(error: String, type: String, comp: String, date: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .border(1.dp, Color.Red, RoundedCornerShape(8.dp)) // Red border
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFCDD2))
                .padding(8.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$type $comp",
                    fontSize = 10.sp,
                    color = Color.Red
                )
            }
            Text(
                text = error,
                fontSize = 12.sp,
                color = Color.Black
            )
        }


    }
}
@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    ErrorComponent("Error It is a long established fact reader", "FK", "server","2012-15-54")
}