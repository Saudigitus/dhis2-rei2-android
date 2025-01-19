package com.saudigitus.support_module.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saudigitus.support_module.R
import com.saudigitus.support_module.ui.ManualsUiState
import org.hisp.dhis.mobile.ui.designsystem.theme.Radius
import org.hisp.dhis.mobile.ui.designsystem.theme.dropShadow

@Composable
fun ListCard(
    imageResId: Int,
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    state: ManualsUiState
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .size(width = 0.dp, height = 80.dp)
            .dropShadow(shape = RoundedCornerShape(Radius.XS))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Radius.XS),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp),
        ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )

            Column (
                modifier = Modifier.
                width(220.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            if(!state.isDownloading)
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.Black
                )
            if(state.isDownloading)
                CircularProgressIndicator()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    ListCard(imageResId = R.drawable.manual_icon, title = "Manual title here alfa omega beta", subtitle = "Manual subtitle here alfa", onClick = {}, icon = Icons.Default.ArrowDownward, state = ManualsUiState())
}