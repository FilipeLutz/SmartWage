package com.finalproject.smartwage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.finalproject.smartwage.ui.auth.LoginScreen
import com.finalproject.smartwage.ui.auth.SignUpScreen
import com.finalproject.smartwage.ui.dashboard.DashboardScreen
import com.finalproject.smartwage.ui.expense.ExpenseScreen
import com.finalproject.smartwage.ui.income.IncomeTaxScreen
import com.finalproject.smartwage.ui.tax.TaxCreditScreen
import com.finalproject.smartwage.ui.profile.ProfileScreen
import com.finalproject.smartwage.ui.settings.HelpScreen
import com.finalproject.smartwage.ui.settings.SettingsScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Login.route
    ) {
        // Login Screen
        composable(Destinations.Login.route) { LoginScreen(navController) }

        // SignUp Screen
        composable(Destinations.SignUp.route) { SignUpScreen(navController) }

        // Dashboard Screen
        composable(
            route = Destinations.Dashboard("").route, // Use the route template
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            DashboardScreen(userId = userId)
        }

        // Other Screens
        composable(Destinations.Income.route) { IncomeTaxScreen(navController) }
        composable(Destinations.Expense.route) { ExpenseScreen(navController) }
        composable(Destinations.TaxCredit.route) { TaxCreditScreen(navController) }
        composable(Destinations.Profile.route) { ProfileScreen(navController) }
        composable(Destinations.Settings.route) { SettingsScreen(navController) }
        composable(Destinations.Help.route) { HelpScreen(navController) }
    }
}