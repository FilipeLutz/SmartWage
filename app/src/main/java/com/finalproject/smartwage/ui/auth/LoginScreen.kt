package com.finalproject.smartwage.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.theme.SmartWageTheme
import com.finalproject.smartwage.utils.isValidEmail
import com.finalproject.smartwage.utils.isValidPassword
import com.finalproject.smartwage.viewModel.AuthViewModel
import com.finalproject.smartwage.R

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login to SmartWage", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null // Clear error when user starts typing
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible })
                {
                    val icon: Painter = if (isPasswordVisible) {
                        painterResource(id = R.drawable.view)  // Visible icon
                    } else {
                        painterResource(id = R.drawable.hidden) // Hidden icon
                    }
                    Image(
                        painter = icon,
                        contentDescription = "Toggle password visibility",
                        Modifier
                            .padding(4.dp))
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!isValidEmail(email)) {
                    errorMessage = "Please enter a valid email"
                } else if (!isValidPassword(password)) {
                    errorMessage = "Password must be at least 6 characters"
                } else {
                    isLoading = true
                    errorMessage = null

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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                navController.navigate(Destinations.SignUp.route)
            }
        ) {
            Text("Don't have an account? Sign Up")
        }

        TextButton(onClick = { navController.navigate("forgot_password") }) {
            Text("Forgot password?")
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SmartWageTheme {
        LoginScreen(rememberNavController())
    }
}