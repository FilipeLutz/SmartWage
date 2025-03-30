package com.finalproject.smartwage.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.ui.theme.PurpleGrey80
import java.text.NumberFormat.getNumberInstance
import java.util.Locale

// DashboardCards is a composable function that creates a card with a label, value, and icon.
@Composable
fun DashboardCards(
    // Parameters:
    label: String,
    value: Double,
    iconRes: Int,
    navController: NavController,
    destination: String,
) {

    // Format the value to 2 decimal places using UK locale
    val numberFormat = getNumberInstance(Locale.UK)
        .apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    // Format the value to a string
    val formattedValue = numberFormat.format(value)

    // Create a card with a clickable modifier
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp)
            .clickable(
                onClick = { navController.navigate(destination) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = cardColors(PurpleGrey80),
        elevation = cardElevation(defaultElevation = 6.dp)
    ) {
        // Create a row inside the card
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            // Create a column for the label and value
            Column {
                // Create a text for the label
                Text(
                    text = label,
                    fontSize = 26.sp,
                    fontWeight = Bold,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))
                // Create a text for the value
                Text(
                    text = "€$formattedValue",
                    fontSize = 22.sp,
                    fontWeight = Bold,
                    color = colorScheme.primary
                )
            }
            // Create an image for the icon
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                colorFilter = tint(DarkBlue),
                modifier = Modifier
                    .size(40.dp)
            )
        }
    }
}