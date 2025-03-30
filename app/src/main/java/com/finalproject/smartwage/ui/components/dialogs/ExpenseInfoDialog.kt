package com.finalproject.smartwage.ui.components.dialogs

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue
import java.util.Locale

@Composable
fun ExpenseInfoDialog(
    onDismiss: () -> Unit,
    category: String,
    onLinkClick: (Context) -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = "Eligibility Information",
                    fontSize = 24.sp,
                    fontWeight = Bold
                )
            }
        },
        text = {
            val text = buildAnnotatedString {
                append(
                    when (category.uppercase(Locale.getDefault())) {
                        "RENT TAX CREDIT" -> "Check if you qualify for the Rent Tax Credit by reviewing the qualifying conditions on "
                        "TUITION FEE RELIEF" -> "Learn more about eligibility requirements for Tuition Fee Relief on "
                        else -> "Additional information about this expense category on "
                    }
                )
                pushStringAnnotation(
                    tag = "URL",
                    annotation = when (category.uppercase(Locale.getDefault())) {
                        "RENT TAX CREDIT" -> "https://www.revenue.ie/rent-credit"
                        "TUITION FEE RELIEF" -> "https://www.revenue.ie/tuition-fees"
                        else -> "https://www.revenue.ie"
                    }
                )
                withStyle(
                    style = SpanStyle(
                        fontWeight = Bold,
                        color = DarkBlue,
                        textDecoration = Underline
                    )
                ) {
                    append("Revenue.ie")
                }
                pop()
            }

            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = SemiBold,
                style = TextStyle(lineHeight = 25.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onLinkClick(context)
                        onDismiss()
                    }
            )
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(DarkBlue),
                onClick = onDismiss,
            ) {
                Text(
                    "OK",
                    fontSize = 18.sp
                )
            }
        }
    )
}