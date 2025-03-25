package com.finalproject.smartwage.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.input.KeyboardType
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
import com.finalproject.smartwage.ui.theme.White
import com.finalproject.smartwage.viewModel.ProfileViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
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

    val initialName = userState?.name ?: ""
    val initialPhoneNumber = userState?.phoneNumber ?: ""

    fun showMessage(newMessage: String, type: MessageType) {
        message = newMessage
        messageType = type
    }

    LaunchedEffect(userState) {
        userState?.profilePicture?.let { uriString ->
            imageUriState = uriString.toUri()
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUriState = it
            viewModel.updateProfilePicture(it)
        }
    }

    fun createImageFile(): Uri? {
        return try {
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${System.currentTimeMillis()}_",
                ".jpg",
                storageDir
            )
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (_: IOException) {
            null
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoUri?.let { uri ->
                imageUriState = uri
                viewModel.updateProfilePicture(uri)
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            currentPhotoUri?.let { uri ->
                takePictureLauncher.launch(uri)
            }
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        currentPhotoUri = createImageFile()
        currentPhotoUri?.let { uri ->
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePictureLauncher.launch(uri)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } ?: run {
            Toast.makeText(context, "Error creating image file", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { DashboardTopBar(navController) },
        bottomBar = { DashboardBottomBar(navController) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            userState?.let { currentUser ->
                var name by remember { mutableStateOf(currentUser.name) }
                var phoneNumber by remember { mutableStateOf(currentUser.phoneNumber) }

                val isSaveButtonEnabled =
                    name != initialName || phoneNumber != initialPhoneNumber || imageUriState?.toString() != currentUser.profilePicture

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "User Profile",
                            fontSize = 35.sp,
                            fontWeight = Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))

                    // Profile Picture Section
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        if (hasProfilePicture && imageUriState != null) {
                            AsyncImage(
                                model = imageUriState,
                                contentDescription = "Profile Picture",
                                modifier = Modifier.fillMaxSize(),
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile Icon",
                                tint = White,
                                modifier = Modifier.size(120.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // "Add Photo" Text
                    if (!hasProfilePicture) {
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
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
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
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            // Delete Photo Text
                            Text(
                                text = "Delete Photo",
                                color = Color.Red,
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

                    Spacer(modifier = Modifier.height(15.dp))

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        // Name Field
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            singleLine = true,
                            textStyle = TextStyle(fontSize = 24.sp),
                            label = { Text("Name", fontSize = 18.sp) },
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
                            label = { Text("Email", fontSize = 18.sp) },
                            textStyle = TextStyle(fontSize = 24.sp),
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
                            label = { Text("Phone Number", fontSize = 18.sp) },
                            textStyle = TextStyle(fontSize = 24.sp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .scrollable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberScrollState(),
                                    enabled = true
                                )
                        )
                    }

                    // Update Password Button
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp)
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.sendPasswordResetEmail(currentUser.email)
                                showMessage(
                                    "A password reset email has been sent to your email address!",
                                    MessageType.TEXT
                                )
                            }
                        ) {
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

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Save Button to update the profile
                        Button(
                            onClick = {
                                val updatedUser = currentUser.copy(
                                    name = name,
                                    phoneNumber = phoneNumber,
                                    profilePicture = imageUriState?.toString() ?: currentUser.profilePicture
                                )
                                viewModel.updateUser(updatedUser)
                                showMessage("Profile updated successfully.", MessageType.TEXT)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Profile Updated")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(DarkBlue),
                            enabled = isSaveButtonEnabled,
                            modifier = Modifier
                                .width(250.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                "SAVE UPDATES",
                                color = White,
                                fontSize = 22.sp,
                                fontWeight = Bold
                            )
                        }

                        // Delete Account Button
                        Button(
                            onClick = {
                                showDeleteConfirmation = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier
                                .width(250.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                "DELETE ACCOUNT",
                                color = White,
                                fontSize = 18.sp,
                                fontWeight = Bold
                            )
                        }
                    }
                }
            }

            // Delete Account Dialog
            if (showDeleteConfirmation) {
                DeleteAccountDialog(
                    onDismiss = { showDeleteConfirmation = false },
                    onConfirm = {
                        viewModel.deleteUserAccount(
                            onSuccess = {
                                navController.navigate(Destinations.Login.route) {
                                    popUpTo(0)
                                }
                            },
                            onFailure = {
                                showMessage("Failed to delete account. Please try again.", MessageType.ERROR)
                            }
                        )
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

            if (showImageDialog) {
                PhotoDialog(
                    onDismiss = { showImageDialog = false },
                    onCameraClick = {
                        showImageDialog = false
                        takePicture()
                    },
                    onGalleryClick = {
                        showImageDialog = false
                        pickImageLauncher.launch("image/*")
                    }
                )
            }
        }
    }
}