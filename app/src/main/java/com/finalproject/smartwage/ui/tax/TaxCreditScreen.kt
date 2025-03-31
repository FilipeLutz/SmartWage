package com.finalproject.smartwage.ui.tax

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.TaxDataMessage
import com.finalproject.smartwage.ui.components.cards.TaxExplanationCard
import com.finalproject.smartwage.ui.components.cards.TaxSummaryCard
import com.finalproject.smartwage.viewModel.TaxViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * TaxCreditScreen is a Composable function that displays the tax summary and related information.
 * It uses a ViewModel to fetch and manage tax data, and it provides a user interface for the user
 * to view their tax information.
 *
 * @param navController The NavController used for navigation within the app.
 */

@Composable
fun TaxCreditScreen(
    // Parameter to navigate between screens
    navController: NavController
) {
    // ViewModel to manage tax data
    val viewModel: TaxViewModel = hiltViewModel()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()
    val paye by viewModel.paye.collectAsState()
    val usc by viewModel.usc.collectAsState()
    val prsi by viewModel.prsi.collectAsState()
    val taxPaid by viewModel.taxPaid.collectAsState()
    val rentTaxCredit by viewModel.rentTaxCredit.collectAsState()
    val tuitionFeeRelief by viewModel.tuitionFeeRelief.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val expectedTax by viewModel.expectedTax.collectAsState()
    val expectedPAYE by viewModel.expectedPAYE.collectAsState()
    val expectedUSC by viewModel.expectedUSC.collectAsState()
    val expectedPRSI by viewModel.expectedPRSI.collectAsState()

    // Fetch tax data when the user ID changes
    LaunchedEffect(userId) {
        viewModel.fetchTaxData()
    }

    // Scaffold to provide a basic layout structure
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        // Surface to provide a background snd padding for the screen
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            // Column to arrange UI elements vertically
            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                // Row to arrange the back button and title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = CenterVertically
                ) {
                    // Box to contain the back button
                    Box(
                        modifier = Modifier
                            .padding(start = 22.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { navController.popBackStack() }
                            )
                    ) {
                        // Back button icon
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(50.dp))

                    // Tax Summary Title
                    Text(
                        text = "Tax Summary",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // LazyColumn to display the tax summary and explanation cards
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                ) {
                    item {
                        // if the total income and expenses are 0, show a message
                        if (totalIncome == 0.0 && totalExpenses == 0.0) {
                            // Text message indicating no tax data available
                            TaxDataMessage(navController)

                        } else {
                            // Else show the tax summary card
                            TaxSummaryCard(
                                totalIncome, paye, usc, prsi, taxPaid,
                                expectedPAYE, expectedUSC, expectedPRSI, expectedTax,
                                rentTaxCredit, tuitionFeeRelief
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        // Explanation card for tax credits
                        TaxExplanationCard()
                    }

                    item {
                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }
            }
        }
    }
}