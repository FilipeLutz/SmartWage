package com.finalproject.smartwage.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.viewModel.AuthViewModel
import com.finalproject.smartwage.ui.theme.DarkBlue

@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.homelogo),
            contentDescription = "Home Logo",
            Modifier
                .size(250.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Create an Account",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            textStyle = TextStyle(
                fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            textStyle = TextStyle(
                fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            textStyle = TextStyle(
                fontSize = 24.sp),
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
                            .padding(end = 12.dp)
                    )
                }
            },
            textStyle = TextStyle(
                fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible })
                {
                    val icon: Painter = if (isConfirmPasswordVisible) {
                        painterResource(id = R.drawable.view)  // Visible icon
                    } else {
                        painterResource(id = R.drawable.hidden) // Hidden icon
                    }
                    Image(
                        painter = icon,
                        contentDescription = "Toggle password visibility",
                        Modifier
                            .padding(end = 12.dp)
                    )
                }
            },
            textStyle = TextStyle(
                fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (password != confirmPassword) {
                    errorMessage = "Passwords do not match"
                    return@Button
                }

                viewModel.signUp(name, email, password, phoneNumber) { success ->
                    if (success) {
                        navController.navigate("dashboard")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors( containerColor = DarkBlue),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
        ) {
            Text(
                "SIGN UP",
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ){

            Text(
                "Already have an account?",
                fontSize = 20.sp,
            )

            TextButton(
                onClick = {
                    navController.navigate(Destinations.Login.route)
                }
            ) {
                Text(
                    "LOGIN",
                    fontSize = 22.sp,
                    color = DarkBlue
                )
            }
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        }
    }
}