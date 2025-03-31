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
import com.finalproject.smartwage.ui.settings.AboutAppScreen
import com.finalproject.smartwage.ui.settings.SettingsScreen
import com.finalproject.smartwage.ui.tax.TaxCreditScreen
import com.finalproject.smartwage.ui.tutorial.FullVideoScreen
import com.finalproject.smartwage.ui.tutorial.TutorialScreen

/**
 * AppNavigation is a Composable function that sets up the navigation for the app.
 * It defines the navigation graph and handles the navigation between different screens.
 *
 * @param navController The NavHostController for managing navigation.
 * @param isUserLoggedIn A boolean indicating whether the user is logged in or not.
 * @param userId The user ID of the logged-in user.
 */

@Composable
fun AppNavigation(
    // Parameters
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    userId: String?
) {
    // Set up the navigation host
    NavHost(
        navController = navController,
        // Start destination based on user login status
        startDestination =
        if (isUserLoggedIn && userId != null)
            Destinations.Dashboard(userId).createRoute(userId)
        else Destinations.Login.route
    ) {
        // Define the navigation graph
        composable(Destinations.Login.route) { LoginScreen(navController) }
        composable(Destinations.SignUp.route) { SignUpScreen(navController) }

        // Dashboard screen
        composable(
            // Dynamic route for the dashboard
            route = Destinations.Dashboard("{userId}").route, // Dynamic route
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extract the userId from the backStackEntry arguments
            val userIdArg = backStackEntry.arguments?.getString("userId") ?: ""
            DashboardScreen(userId = userIdArg, navController = navController)
        }

        // Other screens
        composable(Destinations.Income.route) { IncomeTaxScreen(navController) }
        composable(Destinations.Expense.route) { ExpenseScreen(navController) }
        composable(Destinations.TaxCredit.route) { TaxCreditScreen(navController) }
        composable(Destinations.Profile.route) { ProfileScreen(navController) }
        composable(Destinations.Settings.route) { SettingsScreen(navController) }
        composable(Destinations.AboutApp.route) { AboutAppScreen(navController) }
        composable(Destinations.Tutorial.route) { TutorialScreen(navController) }
        // Fullscreen video screen
        composable("fullscreen/{videoName}") { backStackEntry ->
            // Extract the videoName from the backStackEntry arguments
            FullVideoScreen(
                videoName = backStackEntry.arguments?.getString("videoName"),
                onBack = { navController.popBackStack() }
            )
        }
    }
}