package com.finalproject.smartwage.ui.tax

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.cards.TaxDataMessage
import com.finalproject.smartwage.ui.components.cards.TaxExplanationCard
import com.finalproject.smartwage.ui.components.cards.TaxSummaryCard
import com.finalproject.smartwage.viewModel.TaxViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TaxCreditScreen(
    navController: NavController
) {
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

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.fetchTaxData()
        }
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Tax Summary",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                ) {
                    item {
                        if (totalIncome == 0.0 && totalExpenses == 0.0) {

                            TaxDataMessage(navController)
                        } else {

                            TaxSummaryCard(
                                totalIncome, paye, usc, prsi, taxPaid,
                                expectedPAYE, expectedUSC, expectedPRSI, expectedTax,
                                rentTaxCredit, tuitionFeeRelief
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
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