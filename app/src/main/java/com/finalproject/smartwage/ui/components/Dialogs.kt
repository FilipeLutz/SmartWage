package com.finalproject.smartwage.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.finalproject.smartwage.ui.components.cards.PasswordErrorCard
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.PasswordValidationError
import kotlinx.coroutines.delay

@Composable
fun LoadingDialog(isLoading: Boolean) {
    if (isLoading) {
        Dialog(
            onDismissRequest = {}
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
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

@Composable
fun ErrorMessageDialog(
    message: String?,
    messageType: MessageType,
    onDismiss: () -> Unit
) {

    val textColor = when (messageType) {
        MessageType.TEXT -> Color.Black
        MessageType.SUCCESS -> Color.Green
        MessageType.ERROR -> Color.Red
        MessageType.WARNING -> Color.Yellow
        MessageType.INFO -> Color.White
    }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .width(IntrinsicSize.Max) // Key change: Use IntrinsicSize.Min
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth() // Important: fillMaxWidth inside IntrinsicSize
                    .padding(16.dp) // Add padding around the text
            ) {
                Text(
                    text = message ?: "",
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.wrapContentWidth() // Wrap text width if needed
                )
            }
        }
    }

    if (!message.isNullOrEmpty()) {
        LaunchedEffect(message) {
            delay(2000)
            onDismiss()
        }
    }
}

@Composable
fun PasswordErrorDialog(
    errors: List<PasswordValidationError>,
    onDismiss: () -> Unit
) {
    if (errors.isNotEmpty()) {
        Dialog(onDismissRequest = onDismiss) {
            PasswordErrorCard(errors = errors, onDismiss = onDismiss)
        }
    }
}

enum class MessageType {
    TEXT,
    SUCCESS,
    ERROR,
    WARNING,
    INFO
}

@SuppressLint("DefaultLocale")
@Composable
fun TaxResultDialog(
    calculatedTax: Triple<Double, Double, Double>?,
    onDismiss: () -> Unit
) {
    val totalTax = calculatedTax?.let {
        it.first + it.second + it.third
    } ?: 0.0

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Show Total Tax
                Text(
                    text = "Tax to Pay: €${String.format("%.2f", totalTax)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Updated Tax Explanation
                Text(
                    text = "Tax Rate Information",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "• PAYE: 20% on the first €44,000, 40% on any excess income.",
                        fontSize = 19.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• USC: Calculated based on the following brackets:",
                        fontSize = 19.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "   - 0.5% on the first €12,012",
                        fontSize = 18.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "   - 2% on the next €15,370",
                        fontSize = 18.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "   - 3% on the next €42,662",
                        fontSize = 18.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "   - 8% on anything above that",
                        fontSize = 18.sp, color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• PRSI: 4% flat tax if your income exceeds €18,304.",
                        fontSize = 19.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "• Tax Credit: A €4,000 reduction is applied to the total PAYE tax owed.",
                        fontSize = 19.sp,
                        color = DarkBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "• There is no tax relief calculated in this tax rate. For a complete tax calculation, please add your incomes and expenses.",
                    fontSize = 19.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .width(80.dp)
                            .height(50.dp)
                    ) {
                        Text("Ok", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}