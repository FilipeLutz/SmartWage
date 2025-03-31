package com.finalproject.smartwage.ui.tutorial

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Start
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.finalproject.smartwage.R
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.White

/**
 * FullVideoScreen is a Composable function that displays a full-screen video player using ExoPlayer.
 * It takes a video name and a callback function to handle the back navigation.
 *
 * @param videoName The name of the video file to be played (without the .mp4 extension).
 * @param onBack A lambda function to be called when the back button is pressed.
 */

@OptIn(UnstableApi::class)
@Composable
fun FullVideoScreen(
    // Parameter to receive the video name
    videoName: String?,
    onBack: () -> Unit
) {
    // Get the current context
    val context = LocalContext.current
    // Prepare the ExoPlayer instance
    val player = remember {
        // ExoPlayer is a powerful media player library for Android
        ExoPlayer.Builder(context)
            .build()
            // Set the player to be in full-screen mode
            .apply {
                playWhenReady = true
                repeatMode = Player.REPEAT_MODE_OFF
            }
    }
    // Use DisposableEffect to manage the lifecycle of the player
    DisposableEffect(videoName) {
        // Release the player when the Composable is disposed
        if (videoName != null) {
            // Set the media item to be played
            val resourceName = videoName.removeSuffix(".mp4")
            // Create a URI for the video resource
            val uri = "android.resource://${context.packageName}/raw/$resourceName".toUri()
            // Prepare the player with the media item
            player.setMediaItem(MediaItem.fromUri(uri))
            // Prepare the player for playback
            player.prepare()
            // Start playing the video
            player.play()
        }

        onDispose {
            // Release the player when the Composable is disposed
            player.release()
        }
    }
    // Box to hold the video player and back button
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // AndroidView to display the ExoPlayer's PlayerView
        AndroidView(
            factory = { context ->
                // Create a PlayerView to display the video
                PlayerView(context).apply {
                    // Set the player to the PlayerView
                    this.player = player
                    useController = true
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )

        Row(
            horizontalArrangement = Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    top = 5.dp
                )
        ) {
            // IconButton to handle back navigation
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .size(30.dp)
                    .background(
                        White,
                        shape = CircleShape
                    )
                    .clickable(
                        onClick = onBack,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            ) {
                // Icon for back navigation
                Icon(
                    painter = painterResource(id = R.drawable.close),
                    contentDescription = "Back",
                    tint = DarkBlue,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable(
                            onClick = onBack,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }
        }
    }
}