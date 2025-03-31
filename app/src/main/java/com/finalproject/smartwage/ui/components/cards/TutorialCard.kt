package com.finalproject.smartwage.ui.components.cards

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.finalproject.smartwage.ui.theme.Black
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.White
import com.finalproject.smartwage.ui.tutorial.TutorialVideo

/**
 * TutorialCard is a Composable function that displays a card with a video thumbnail and title.
 * It allows the user to click on the card to play the video.
 *
 * @param video The TutorialVideo object containing video details.
 * @param onCardClick The callback function to be invoked when the card is clicked.
 */

@Composable
fun TutorialCard(
    // Parameters
    video: TutorialVideo,
    onCardClick: () -> Unit
) {
    // Get the current context
    val context = LocalContext.current

    // Column to hold the card and title
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        // Card with video thumbnail and play icon overlay
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable(
                    onClick = onCardClick
                )
                .border(
                    width = 1.dp,
                    color = DarkBlue,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            elevation = cardElevation(4.dp)
        ) {
            // Box to hold the thumbnail and play icon
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Load thumbnail
                val thumbnailBitmap = remember(video.fileName) {
                    // Load the video thumbnail using MediaMetadataRetriever
                    loadVideoThumbnail(context, video.fileName)
                }

                // Thumbnail or placeholder
                if (thumbnailBitmap != null) {
                    // Display the thumbnail
                    Image(
                        bitmap = thumbnailBitmap.asImageBitmap(),
                        contentDescription = "Video thumbnail",
                        contentScale = Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } else {
                    // Placeholder if thumbnail loading fails
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(LightGray)
                    )
                }

                // Play icon overlay
                Box(
                    modifier = Modifier
                        .align(Center)
                        .size(48.dp)
                        .background(
                            color = Black.copy(
                                alpha = 0.6f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Center
                ) {
                    // Play icon
                    Icon(
                        imageVector = Default.PlayArrow,
                        contentDescription = "Play",
                        tint = White,
                        modifier = Modifier
                            .size(32.dp)
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
            textDecoration = Underline,
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .wrapContentWidth(CenterHorizontally)
        )
    }
}

// Function to load video thumbnail using MediaMetadataRetriever
private fun loadVideoThumbnail(
    // Parameters
    context: Context,
    fileName: String
): Bitmap? {
    // Try to load the video thumbnail
    return try {
        // Create URI for the video resource
        val resourceName = fileName.removeSuffix(".mp4")
        // Get the URI for the video resource
        val uri = "android.resource://${context.packageName}/raw/$resourceName".toUri()
        // Create MediaMetadataRetriever instance
        MediaMetadataRetriever().run {
            setDataSource(context, uri)
            val bitmap = frameAtTime
            release()
            bitmap
        }
        // Handle exceptions
    } catch (_: Exception) {
        null
    }
}