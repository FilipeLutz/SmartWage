package com.finalproject.smartwage.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType.Companion.Email
import androidx.compose.ui.text.input.KeyboardType.Companion.Password
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.finalproject.smartwage.R
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.dialogs.ErrorMessageDialog
import com.finalproject.smartwage.ui.components.dialogs.MessageType
import com.finalproject.smartwage.ui.components.dialogs.PasswordErrorDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.utils.PasswordValidationError
import com.finalproject.smartwage.utils.isValidEmail
import com.finalproject.smartwage.utils.validatePassword
import com.finalproject.smartwage.viewModel.AuthViewModel

// This is a login screen for the SmartWage app.
@Composable
fun LoginScreen(
    // This function is responsible for displaying the login screen UI and handling user interactions.
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    // State variables to hold user input and UI state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var messageType by remember { mutableStateOf(MessageType.TEXT) }
    var passwordErrors by remember { mutableStateOf<List<PasswordValidationError>>(emptyList()) }

    // Function to show messages with correct type
    fun showMessage(newMessage: String, type: MessageType) {
        if (message != newMessage) {
            message = newMessage
            messageType = type
        }
    }

    // Main UI Layout
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Logo Image
        Image(
            painter = painterResource(
                id = R.drawable.homelogo
            ),
            contentDescription = "Home Logo",
            modifier = Modifier
                .size(250.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text(
                    "Email", fontSize = 20.sp
                )
            },
            textStyle = TextStyle(
                fontSize = 24.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = Email
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollState(),
                    enabled = true
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    "Password",
                    fontSize = 20.sp
                )
            },
            textStyle = TextStyle(
                fontSize = 24.sp
            ),
            singleLine = true,
            visualTransformation =
                // Toggle password visibility
                if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(
                    // This icon button is used to toggle the visibility of the password
                    onClick = { isPasswordVisible = !isPasswordVisible }) {
                    val icon = if (isPasswordVisible) painterResource(id = R.drawable.view)
                    else painterResource(id = R.drawable.hidden)
                    // Display the icon based on the password visibility state
                    Image(
                        painter = icon,
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable(
                                onClick = { isPasswordVisible = !isPasswordVisible },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = Password
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
                .scrollable(
                    orientation = Orientation.Horizontal,
                    state = rememberScrollState(),
                    enabled = true
                )
                .clickable(
                    onClick = { isPasswordVisible = !isPasswordVisible },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )

        )

        Spacer(modifier = Modifier.height(8.dp))

        // Forgot Password TextButton
        Row(
            horizontalArrangement = End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp)
        ) {
            TextButton(
                modifier = Modifier
                    .clickable(
                        onClick = {},
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                onClick = {
                    if (!isValidEmail(email)) {
                        showMessage("Please enter a valid email", MessageType.ERROR)
                    } else {
                        viewModel.sendPasswordResetEmail(email)
                        showMessage("Password reset email sent!", MessageType.TEXT)
                    }
                }
            ) {
                Text(
                    "Forgot password?",
                    fontSize = 18.sp,
                    color = DarkBlue,
                    fontWeight = Bold,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                if (!isValidEmail(email)) {
                                    showMessage("Please enter a valid email", MessageType.ERROR)
                                } else {
                                    viewModel.sendPasswordResetEmail(email)
                                    showMessage("Password reset email sent!", MessageType.TEXT)
                                }
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Login Button
        Button(
            onClick = {
                // Validate email format
                if (!isValidEmail(email)) {
                    showMessage("Please enter a valid email", MessageType.ERROR)
                    return@Button
                }

                /*

                // Ceck if email is registered
                viewModel.isEmailRegistered(email) { isRegistered ->
                    if (!isRegistered) {
                        showMessage("Email is not registered", MessageType.ERROR)
                        return@isEmailRegistered
                    }
                }

                */

                // Validate password
                passwordErrors = validatePassword(password)
                if (passwordErrors.isNotEmpty()) {
                    return@Button // Stops execution if password has errors
                }

                // If email and password are valid, proceed with login
                isLoading = true
                message = null

                viewModel.login(email, password) { success ->
                    isLoading = false
                    if (success) {
                        val userId = viewModel.user.value?.id ?: ""
                        navController.navigate(Destinations.Dashboard(userId).createRoute(userId)) {
                            popUpTo(Destinations.Login.route) { inclusive = true }
                        }
                    } else {
                        showMessage("Email or Password wrong", MessageType.ERROR)
                    }
                }
            },
            colors = buttonColors(containerColor = DarkBlue),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        ) {
            Text(
                "LOGIN",
                fontSize = 22.sp,
                fontWeight = Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Sign up row with clickable TextButton
        Row(
            horizontalArrangement = Center,
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "Don't have an account?",
                fontSize = 20.sp,
                fontWeight = SemiBold
            )

            TextButton(
                modifier = Modifier
                    .clickable(
                        onClick = {},
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                onClick = { navController.navigate(Destinations.SignUp.route) }
            ) {
                Text(
                    "SIGN UP",
                    fontSize = 22.sp,
                    color = DarkBlue,
                    fontWeight = Bold,
                    modifier = Modifier
                        .clickable(
                            onClick = { navController.navigate(Destinations.SignUp.route) },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }
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