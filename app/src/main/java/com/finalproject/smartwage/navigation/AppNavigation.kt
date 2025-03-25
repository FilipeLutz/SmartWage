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
import com.finalproject.smartwage.ui.profile.ProfileScreen
import com.finalproject.smartwage.ui.settings.HelpScreen
import com.finalproject.smartwage.ui.settings.SettingsScreen
import com.finalproject.smartwage.ui.tax.TaxCreditScreen
import com.finalproject.smartwage.ui.tutorial.TutorialScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    userId: String?,
    onCameraClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination =
        if (isUserLoggedIn && userId != null)
            Destinations.Dashboard(userId).createRoute(userId)
        else Destinations.Login.route
    ) {
        composable(Destinations.Login.route) { LoginScreen(navController) }
        composable(Destinations.SignUp.route) { SignUpScreen(navController) }

        composable(
            route = Destinations.Dashboard("{userId}").route, // Dynamic route
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userIdArg = backStackEntry.arguments?.getString("userId") ?: ""
            DashboardScreen(userId = userIdArg, navController = navController)
        }

        composable(Destinations.Income.route) { IncomeTaxScreen(navController) }
        composable(Destinations.Expense.route) { ExpenseScreen(navController) }
        composable(Destinations.TaxCredit.route) { TaxCreditScreen(navController) }
        composable(Destinations.Profile.route) { ProfileScreen(navController) }
        composable(Destinations.Settings.route) { SettingsScreen(navController) }
        composable(Destinations.Help.route) { HelpScreen(navController) }
        composable(Destinations.Tutorial.route) { TutorialScreen((navController)) }
    }
}