package ru.skypaws.features.ui.topBarWithNavMenu

import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.skypaws.mobileapp.domain.model.UserDomain
import ru.skypaws.presentation.R
import ru.skypaws.presentation.ui.theme.SkyPawsTheme
import java.io.ByteArrayInputStream

@Composable
fun NavigationHeader(
   user: UserDomain
) {
    val photo = user.photo
    val bitmap = if (photo != null) {
        val imageBytes = Base64.decode(photo, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(imageBytes)
        remember { BitmapFactory.decodeStream(inputStream) }
    } else {
        null
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 12.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(end = 8.dp),
        ) {
            Text(
                color = Color.White,
                text = "${user.surname ?: ""} ${user.name ?: ""}",
                fontSize = 16.sp,
                maxLines = 3
            )
            Text(
                color = Color.White,
                text = user.position ?: "",
                fontSize = 12.sp
            )
        }

        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20)),
            painter = if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                painterResource(id = R.drawable.ic_launcher_foreground)
            },
            contentDescription = "photo",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "dark"
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "light"
)
@Composable
fun NavigationHeaderPreview() {
    // данные пользователя (full name, position, photo)
    val listOfUserData = listOf(
        "Иванов Иван",
        "пилот",
        null
    )
    val photo = listOfUserData[2]
    val bitmap = if (photo != null) {
        val imageBytes = Base64.decode(photo, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(imageBytes)
        remember { BitmapFactory.decodeStream(inputStream) }
    } else {
        null
    }

    SkyPawsTheme {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 12.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(end = 8.dp),
            ) {
                Text(
                    color = Color.White,
                    text = listOfUserData[0] ?: "",
                    fontSize = 16.sp,
                    maxLines = 3
                )
                Text(
                    color = Color.White,
                    text = listOfUserData[1] ?: "",
                    fontSize = 12.sp
                )
            }

            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(20)),
                painter = if (bitmap != null) {
                    BitmapPainter(bitmap.asImageBitmap())
                } else {
                    painterResource(id = R.drawable.ic_launcher_foreground)
                },
                contentDescription = "photo",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}