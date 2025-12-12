package uk.ac.tees.mad.memorylog.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import uk.ac.tees.mad.memorylog.domain.model.UserProfile
import uk.ac.tees.mad.memorylog.ui.screens.calender.components.ProfileHeader
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme
import uk.ac.tees.mad.memorylog.ui.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel(), onLogout: () -> Unit) {
    val uiState by viewModel.userProfile.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHost) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Failed to load profile")
                    }
                }

                is UiState.Success -> {
                    val profile = (uiState as UiState.Success<UserProfile?>).data

                    if (profile == null) {
                        Text("No user data")
                        return@Box
                    }
                    Log.d("PROFILE", profile.toString())
                    profile?.let {
                        var name by remember { mutableStateOf(it.name) }
//                var avatarUrl by remember { mutableStateOf(it.avatarUrl) }
//                var darkTheme by remember { mutableStateOf(it.darkTheme) }
//                var autoBackup by remember { mutableStateOf(it.autoBackup) }
//                var biometricEnabled by remember { mutableStateOf(it.biometricEnabled) }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {

                            ProfileHeader("Settings")

                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xff4a4a4a))
                                    .align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profile.avatarUrl,
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White
                                )
                            }

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Your Name") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = {
                                    viewModel.updateProfile(name)
                                    scope.launch {
                                        snackbarHost.showSnackbar("Name updated successfully")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Save Name")
                            }

                            Spacer(Modifier.height(18.dp))

                            SwitchRow(
                                title = "Dark Theme",
                                checked = profile.darkTheme,
                                onCheckedChange = viewModel::updateTheme
                            )

                            SwitchRow(
                                title = "Auto Backup",
                                checked = profile.autoBackup,
                                onCheckedChange = viewModel::updateAutoBackup
                            )

//                    SwitchRow(
//                        title = "Biometric Unlock",
//                        checked = profile.biometricEnabled,
//                        onCheckedChange = viewModel::updateBiometric
//                    )

                            Button(
                                onClick = {
                                    viewModel.logout()
                                    onLogout()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Logout")
                            }
                        }
                    }
                }

                else -> {}
            }

        }
    }
}

@Composable
private fun SwitchRow(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingToggle(title: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title)
        Switch(checked = checked, onCheckedChange = onToggle)
    }
}

@Preview(showBackground = true, name = "MemoryLog – Settings Screen")
@Composable
fun SettingsScreenPreview() {
    MemoryLogTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Text("Settings", style = MaterialTheme.typography.titleLarge)

            // Profile Section
            Text("Profile", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = "Emma Wilson",
                onValueChange = {},
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800",
                contentDescription = "Profile Avatar",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            HorizontalDivider()

            // Toggle Settings
            SettingToggle("Dark Theme", checked = true) {}
            SettingToggle("Biometric Unlock", checked = false) {}
            SettingToggle("Auto Backup", checked = true) {}

            HorizontalDivider()

            // Action Buttons
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Cache")
            }

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}

