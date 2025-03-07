package com.finalproject.smartwage.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.dialogs.ErrorMessageDialog
import com.finalproject.smartwage.ui.components.dialogs.LoadingDialog
import com.finalproject.smartwage.ui.components.dialogs.MessageType
import com.finalproject.smartwage.ui.components.dialogs.PasswordErrorDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.PasswordValidationError
import com.finalproject.smartwage.utils.isValidEmail
import com.finalproject.smartwage.utils.validatePassword
import com.finalproject.smartwage.viewModel.AuthViewModel

@Composable
fun SignUpScreen(
    navController: NavController, viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var messageType by remember { mutableStateOf(MessageType.TEXT) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordErrors by remember { mutableStateOf<List<PasswordValidationError>>(emptyList()) }
    val emailExists by viewModel.emailExists.collectAsState()

    // Function to show messages
    fun showMessage(newMessage: String, type: MessageType) {
        if (message != newMessage) {
            message = newMessage
            messageType = type
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.homelogo),
            contentDescription = "Home Logo",
            Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Create an Account", fontSize = 26.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name *", fontSize = 18.sp) },
            textStyle = TextStyle(fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email *", fontSize = 18.sp) },
            textStyle = TextStyle(fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Phone Number Field (Optional)
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number (Optional)", fontSize = 18.sp) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(fontSize = 24.sp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password *", fontSize = 18.sp) },
            textStyle = TextStyle(fontSize = 24.sp),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    val icon = if (isPasswordVisible) painterResource(id = R.drawable.view)
                    else painterResource(id = R.drawable.hidden)
                    Image(painter = icon, contentDescription = "Toggle password visibility", Modifier.padding(end = 12.dp))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password *", fontSize = 18.sp) },
            textStyle = TextStyle(fontSize = 24.sp),
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    val icon = if (isConfirmPasswordVisible) painterResource(id = R.drawable.view)
                    else painterResource(id = R.drawable.hidden)
                    Image(painter = icon, contentDescription = "Toggle password visibility", Modifier.padding(end = 12.dp))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Sign-Up Button
        Button(
            onClick = {
                if (email.isBlank() || name.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    showMessage("All fields with * are required!", MessageType.ERROR)
                    return@Button
                }

                // Validate email format
                if (!isValidEmail(email)) {
                    showMessage("Please enter a valid email", MessageType.ERROR)
                    return@Button
                }

                if (password != confirmPassword) {
                    showMessage("Passwords do not match", MessageType.ERROR)
                    return@Button
                }

                passwordErrors = validatePassword(password)
                if (passwordErrors.isNotEmpty()) {
                    return@Button
                }

                viewModel.isEmailRegistered(email)
                if (emailExists == true) {
                    showMessage("Email is already in use", MessageType.ERROR)
                    return@Button
                }

                isLoading = true
                message = null

                viewModel.signUp(name, email, password, phoneNumber) { success ->
                    isLoading = false
                    if (success) {
                        navController.navigate(Destinations.Login.route)
                    } else {
                        showMessage("Sign-up failed, please try again.", MessageType.ERROR)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            modifier = Modifier.width(300.dp).height(50.dp)
        ) {
            Text("SIGN UP", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Login Navigation
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account?", fontSize = 20.sp)
            TextButton(onClick = { navController.navigate(Destinations.Login.route) }) {
                Text("LOGIN", fontSize = 22.sp, color = DarkBlue)
            }
        }

        // Show Loading Indicator
        if (isLoading) {
            LoadingDialog(isLoading = true)
        }
    }

    // Show General Error Message Dialog
    if (message != null && passwordErrors.isEmpty()) {
        ErrorMessageDialog(
            message = message,
            messageType = messageType,
            onDismiss = { message = null }
        )
    }

    // Show Password Error Dialog
    if (passwordErrors.isNotEmpty()) {
        PasswordErrorDialog(
            errors = passwordErrors,
            onDismiss = { passwordErrors = emptyList() }
        )
    }
}
