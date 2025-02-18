@file:Suppress("DEPRECATION")

package com.finalproject.smartwage.ui.auth

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.isValidEmail
import com.finalproject.smartwage.utils.isValidPassword
import com.finalproject.smartwage.viewModel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val resetEmailSent by viewModel.resetEmailSent.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Column for the login screen
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize(),
    ) {

        // Logo
        Image(
            painter = painterResource(
                id = R.drawable.homelogo
            ),
            contentDescription = "Home Logo",
            Modifier
                .size(250.dp),
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Column for the email and password fields
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
        ) {
            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = {
                    Text (
                        "Email",
                        fontSize = 18.sp
                    )
                },
                textStyle = TextStyle(
                    fontSize = 24.sp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Show error message if email is invalid
            errorMessage?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = {
                    Text(
                        "Password",
                        fontSize = 18.sp,
                    )
                },
                textStyle = TextStyle(
                    fontSize = 24.sp),
                // Hide or show password
                visualTransformation =
                if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible }
                    ) {
                        val icon: Painter = if (isPasswordVisible) {
                            painterResource(id = R.drawable.view)  // Visible icon
                        } else {
                            painterResource(id = R.drawable.hidden) // Hidden icon
                        }
                        // Icon for password visibility
                        Image(
                            painter = icon,
                            contentDescription = "Toggle password visibility",
                            Modifier
                                .padding(end = 12.dp)
                        )
                    }
                },
                // Keyboard type for password
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )

            // Show confirmation message when reset email is sent
            if (resetEmailSent) {
                LaunchedEffect(Unit) {
                    Toast.makeText(
                        context, "Password reset email sent!",
                            Toast.LENGTH_LONG).show()
                }
            }

            // Show error message if email sending fails
            errorMessage?.let { message ->
                LaunchedEffect(message) {
                    Toast.makeText(context, message,
                        Toast.LENGTH_LONG).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row for forgot password
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
        ) {
            // Forgot password button
            TextButton(
                onClick = {
                    if (email.isNotBlank()) {
                        viewModel.sendPasswordResetEmail(email)
                        Toast.makeText(context, "Password reset email sent!",
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Please enter your email",
                            Toast.LENGTH_LONG).show()
                    }
                }
            ) {
                Text(
                    "Forgot password?",
                    fontSize = 18.sp,
                    color = DarkBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Login button
        Button(
            // On click, check if email and password are valid
            onClick = {
                if (!isValidEmail(email)) {
                    errorMessage = "Please enter a valid email"
                } else if (!isValidPassword(password)) {
                    errorMessage = "Password must be at least 8 characters"
                } else {
                    isLoading = true
                    errorMessage = null
                    // Call login function from view model
                    viewModel.login(email, password) { success ->
                        isLoading = false
                        if (success) {
                            val userId = viewModel.user.value?.id ?: ""
                            navController.navigate(Destinations.Dashboard(userId).createRoute(userId))
                        } else {
                            errorMessage = "Login failed. Please try again."
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkBlue
            ),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
        ) {
            Text(
                "LOGIN",
                fontSize = 22.sp,
            )
        }

        // Show loading indicator while logging in
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Row for sign up
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ){
            // Text for sign up
            Text(
                "Don't have an account?",
                fontSize = 20.sp,
            )
            // Sign up button
            TextButton(
                onClick = {
                    navController.navigate(Destinations.SignUp.route)
                }
            ) {
                Text(
                    "SIGN UP",
                    fontSize = 22.sp,
                    color = DarkBlue
                )
            }
        }
    }
}