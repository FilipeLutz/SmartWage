package com.finalproject.smartwage.ui.components.dialogs

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.Red

@Composable
fun RevenueDialog(
    onDismiss: () -> Unit
) {

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Open Revenue Website",
                    fontSize = 24.sp,
                    fontWeight = Bold
                )
            }
        },
        text = {
            Text(
                text = "You are about to leave the app and visit the Revenue.ie",
                fontSize = 20.sp,
                fontWeight = SemiBold,
                style = TextStyle(lineHeight = 30.sp)
            )
        },
        confirmButton = {
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ){

                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "CANCEL",
                        fontSize = 20.sp,
                        fontWeight = SemiBold,
                        color = Red
                    )
                }

                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = "https://www.revenue.ie/en/home.aspx".toUri()
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = "OK",
                        fontSize = 20.sp,
                        fontWeight = SemiBold,
                        color = DarkBlue
                    )
                }
            }
        },
    )
}