package com.finalproject.smartwage.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.finalproject.smartwage.ui.theme.DarkBlue

@Composable
fun LoadingDialog(isLoading: Boolean) {
    if (isLoading) {
        Dialog(
            onDismissRequest = {}
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    // Semi-transparent black background
                    .background(Color.Transparent)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(100.dp),
                        color = Color.White
                    )
                }

            }
        }
    }
}