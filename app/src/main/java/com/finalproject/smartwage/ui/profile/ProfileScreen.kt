package com.finalproject.smartwage.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.finalproject.smartwage.navigation.Destinations
import com.finalproject.smartwage.ui.components.DashboardBottomBar
import com.finalproject.smartwage.ui.components.DashboardTopBar
import com.finalproject.smartwage.ui.components.dialogs.DeleteAccountDialog
import com.finalproject.smartwage.ui.components.dialogs.ErrorMessageDialog
import com.finalproject.smartwage.ui.components.dialogs.MessageType
import com.finalproject.smartwage.ui.components.dialogs.PhotoDialog
import com.finalproject.smartwage.ui.theme.DarkBlue
import com.finalproject.smartwage.viewModel.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

/**
 * ProfileScreen is a Composable function that displays the user's profile information and allows
 * them to update their profile picture, name, and phone number. It also provides options to delete
 * the account and reset the password.
 *
 * @param navController The NavController used for navigation.
 * @param viewModel The ProfileViewModel instance for managing user data.
 */

@Composable
fun ProfileScreen(
    // Parameters
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    // State variables
    val userState by viewModel.user.collectAsState()
    var imageUriState by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var messageType by remember { mutableStateOf<MessageType>(MessageType.TEXT) }
    var showImageDialog by remember { mutableStateOf(false) }
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val hasProfilePicture = imageUriState != null

    // Initial values for name and phone number
    val initialName = userState?.name ?: ""
    val initialPhoneNumber = userState?.phoneNumber ?: ""

    // Function to show messages
    fun showMessage(newMessage: String, type: MessageType) {
        message = newMessage
        messageType = type
    }

    // Observe user state changes to update the image URI
    LaunchedEffect(userState) {
        userState?.profilePicture?.let { uriString ->
            imageUriState = uriString.toUri()
        }
    }

    // Image picker and camera launchers
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUriState = it
            viewModel.updateProfilePicture(it)
        }
    }

    // Function to create an image file for the camera
    fun createImageFile(): Uri? {
        return try {
            // Create an image file name
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${System.currentTimeMillis()}_",
                ".jpg",
                storageDir
            )
            // Save a file: path for use with FileProvider
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (_: IOException) {
            null
        }
    }

    // Camera launcher
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        // If the image was successfully captured, update the image URI
        if (success) {
            // Update the image URI in the ViewModel
            currentPhotoUri?.let { uri ->
                imageUriState = uri
                viewModel.updateProfilePicture(uri)
            }
        }
    }

    // Permission launcher for camera access
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // If permission is granted, launch the camera
        if (isGranted) {
            currentPhotoUri?.let { uri ->
                takePictureLauncher.launch(uri)
            }
        } else {
            // Show a message if permission is denied
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to take a picture
    fun takePicture() {
        currentPhotoUri = createImageFile()
        currentPhotoUri?.let { uri ->
            // Check if the camera permission is granted
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePictureLauncher.launch(uri)
            } else {
                // Request camera permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } ?: run {
            // Show a message if the image file could not be created
            Toast.makeText(context, "Error creating image file", Toast.LENGTH_SHORT).show()
        }
    }

    // Scaffold to provide the top bar, bottom bar, and padding
    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        // Surface to hold the content with a background color
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background)
        ) {
            userState?.let { currentUser ->
                // State variables for name and phone number
                var name by remember { mutableStateOf(currentUser.name) }
                var phoneNumber by remember { mutableStateOf(currentUser.phoneNumber) }

                // Check if the save button should be enabled
                val isSaveButtonEnabled =
                    name != initialName || phoneNumber != initialPhoneNumber || imageUriState?.toString() != currentUser.profilePicture

                // Column to hold the profile screen content
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row for the title
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Center
                    ) {
                        // Title Text
                        Text(
                            text = "User Profile",
                            fontSize = 35.sp,
                            fontWeight = Bold,
                            color = colorScheme.primary,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // Profile Picture Section
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        // Display the profile picture or default icon
                        if (hasProfilePicture && imageUriState != null) {
                            // Load the image using AsyncImage
                            AsyncImage(
                                model = imageUriState,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .fillMaxSize(),
                            )
                        } else {
                            // Default profile icon
                            Icon(
                                imageVector = Default.Person,
                                contentDescription = "Default Profile Icon",
                                tint = White,
                                modifier = Modifier
                                    .size(120.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Text for "Add Photo" or "Edit Photo/Delete" options
                    if (!hasProfilePicture) {
                        // Text for "Add Photo" when there's no photo
                        Text(
                            text = "Add Photo",
                            fontSize = 16.sp,
                            fontWeight = SemiBold,
                            textDecoration = Underline,
                            color = DarkBlue,
                            modifier = Modifier
                                .clickable {
                                    showImageDialog = true
                                }
                                .padding(top = 8.dp)
                        )
                    } else {
                        // Row with "Edit Photo" and "Delete" Texts when there's a photo
                        Row(
                            horizontalArrangement = Center,
                            verticalAlignment = CenterVertically,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            // Edit Photo Text
                            Text(
                                text = "Edit Photo",
                                textDecoration = Underline,
                                fontWeight = SemiBold,
                                fontSize = 16.sp,
                                color = DarkBlue,
                                modifier = Modifier
                                    .clickable {
                                        showImageDialog = true
                                    }
                                    .padding(end = 2.dp)
                            )

                            // Slash ("/")
                            Text(
                                text = "/",
                                fontSize = 16.sp,
                                fontWeight = SemiBold,
                                color = DarkGray,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                            )

                            // Delete Photo Text
                            Text(
                                text = "Delete Photo",
                                color = Red,
                                fontSize = 16.sp,
                                fontWeight = SemiBold,
                                textDecoration = Underline,
                                modifier = Modifier
                                    .clickable {
                                        viewModel.deleteProfilePicture()
                                        imageUriState = null
                                    }
                                    .padding(start = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Column to hold the input fields
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth(),
                        verticalArrangement = spacedBy(5.dp)
                    ) {

                        // Name Field
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 24.sp
                            ),
                            label = {
                                Text(
                                    "Name",
                                    fontSize = 18.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .scrollable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberScrollState(),
                                    enabled = true
                                )
                        )

                        // Email (Non-editable)
                        OutlinedTextField(
                            value = currentUser.email,
                            onValueChange = {},
                            singleLine = true,
                            label = {
                                Text(
                                    "Email",
                                    fontSize = 18.sp
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 24.sp
                            ),
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .scrollable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberScrollState(),
                                    enabled = true
                                )
                        )

                        // Phone Number Field
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = {
                                Text(
                                    "Phone Number",
                                    fontSize = 18.sp
                                )
                            },
                            textStyle = TextStyle(
                                fontSize = 24.sp
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = Number
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .scrollable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberScrollState(),
                                    enabled = true
                                )
                        )
                    }

                    // Row for "Update Password" button
                    Row(
                        horizontalArrangement = End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp)
                    ) {
                        // Update Password Button
                        TextButton(
                            onClick = {
                                // Send password reset email
                                viewModel.sendPasswordResetEmail(currentUser.email)
                                // Show success message
                                showMessage(
                                    "A password reset email has been sent to your email address!",
                                    MessageType.TEXT
                                )
                            }
                        ) {
                            // Text for "Update Password"
                            Text(
                                "Update Password",
                                fontSize = 18.sp,
                                textDecoration = Underline,
                                color = DarkBlue,
                                fontWeight = Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Column to hold the save and delete buttons
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = spacedBy(15.dp),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        // Save Button to update the profile
                        Button(
                            onClick = {
                                // Update the user profile with new values
                                val updatedUser = currentUser.copy(
                                    name = name,
                                    phoneNumber = phoneNumber,
                                    profilePicture = imageUriState?.toString()
                                        ?: currentUser.profilePicture
                                )
                                // Call the ViewModel to update the user
                                viewModel.updateUser(updatedUser)
                                // Show success message
                                showMessage("Profile updated successfully.", MessageType.TEXT)
                                coroutineScope.launch {
                                    // Show a snackbar message
                                    snackbarHostState.showSnackbar("Profile Updated")
                                }
                            },
                            colors = buttonColors(DarkBlue),
                            enabled = isSaveButtonEnabled,
                            modifier = Modifier
                                .width(250.dp)
                                .height(50.dp)
                        ) {
                            // Text for "Save Changes"
                            Text(
                                "SAVE CHANGES",
                                color = White,
                                fontSize = 22.sp,
                                fontWeight = Bold
                            )
                        }

                        // Delete Account Button
                        OutlinedButton(
                            onClick = {
                                // Show delete confirmation dialog
                                showDeleteConfirmation = true
                            },
                            border = BorderStroke(
                                width = 2.dp,
                                color = Red
                            ),
                            modifier = Modifier
                                .width(250.dp)
                                .height(50.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {}
                        ) {
                            // Text for "Delete Account"
                            Text(
                                "DELETE ACCOUNT",
                                color = Red,
                                fontSize = 22.sp,
                                fontWeight = Bold
                            )
                        }
                    }
                }
            }

            // Delete Account Dialog if showDeleteConfirmation is true
            if (showDeleteConfirmation) {
                DeleteAccountDialog(
                    onDismiss = { showDeleteConfirmation = false },
                    onConfirm = {
                        // Call the ViewModel to delete the user account
                        viewModel.deleteUserAccount(
                            onSuccess = {
                                // Show success message
                                navController.navigate(Destinations.Login.route) {
                                    popUpTo(0)
                                }
                            },
                            onFailure = {
                                // Show error message
                                showMessage(
                                    "Failed to delete account. Please try again.",
                                    MessageType.ERROR
                                )
                            }
                        )
                        // Navigate to the login screen
                        navController.navigate(Destinations.Login.route) {
                            popUpTo(0)
                        }
                    }
                )
            }

            // Message Dialog
            message?.let {
                ErrorMessageDialog(
                    message = it,
                    messageType = messageType,
                    onDismiss = { message = null }
                )
            }

            // if showImageDialog is true, show the PhotoDialog
            if (showImageDialog) {
                PhotoDialog(
                    onDismiss = { showImageDialog = false },
                    onCameraClick = {
                        // Launch the camera
                        showImageDialog = false
                        takePicture()
                    },
                    onGalleryClick = {
                        // Launch the image picker
                        showImageDialog = false
                        pickImageLauncher.launch("image/*")
                    }
                )
            }
        }
    }
}