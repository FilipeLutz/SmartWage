package com.finalproject.smartwage.ui.components.cards

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.finalproject.smartwage.ui.theme.Black
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.White
import com.finalproject.smartwage.ui.tutorial.TutorialVideo

@Composable
fun TutorialCard(video: TutorialVideo, onCardClick: () -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Card with video thumbnail and play icon overlay
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(onClick = onCardClick),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Load thumbnail
                val thumbnailBitmap = remember(video.fileName) {
                    loadVideoThumbnail(context, video.fileName)
                }

                // Thumbnail or placeholder
                if (thumbnailBitmap != null) {
                    Image(
                        bitmap = thumbnailBitmap.asImageBitmap(),
                        contentDescription = "Video thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightGray)
                    )
                }

                // Play icon overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .background(
                            color = Black.copy(alpha = 0.6f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Title below the card
        Text(
            text = video.title,
            color = DarkBlue,
            fontSize = 16.sp,
            fontWeight = Bold,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

// Load video thumbnail from resources
private fun loadVideoThumbnail(context: Context, fileName: String): Bitmap? {
    return try {
        val resourceName = fileName.removeSuffix(".mp4")
        val uri = "android.resource://${context.packageName}/raw/$resourceName".toUri()

        MediaMetadataRetriever().run {
            setDataSource(context, uri)
            val bitmap = frameAtTime
            release()
            bitmap
        }
    } catch (_: Exception) {
        null
    }
}