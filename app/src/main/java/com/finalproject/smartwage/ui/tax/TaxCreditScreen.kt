package com.finalproject.smartwage.ui.tax

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
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
        viewModel.fetchTaxData()
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
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(36.dp))

                    // Tax Summary Title
                    Text(
                        text = "Tax Summary",
                        fontSize = 35.sp,
                        fontWeight = Bold,
                        color = MaterialTheme.colorScheme.primary
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