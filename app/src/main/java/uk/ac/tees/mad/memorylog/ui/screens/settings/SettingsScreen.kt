package uk.ac.tees.mad.memorylog.ui.screens.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.memorylog.R
import uk.ac.tees.mad.memorylog.domain.model.UserProfile
import uk.ac.tees.mad.memorylog.ui.screens.calender.components.ProfileHeader
import uk.ac.tees.mad.memorylog.ui.screens.uistate.UiState
import uk.ac.tees.mad.memorylog.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.userProfile.collectAsState()

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

            Log.d("PROFILE",profile.toString())
            profile?.let {
                var name by remember { mutableStateOf(it.name) }
                var avatarUrl by remember { mutableStateOf(it.avatarUrl) }
                var darkTheme by remember { mutableStateOf(it.darkTheme) }
                var autoBackup by remember { mutableStateOf(it.autoBackup) }
                var biometricEnabled by remember { mutableStateOf(it.biometricEnabled) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    ProfileHeader("Settings")
                    Text("Profile", style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = name,
                        onValueChange = { newName ->
                            name = newName
                            viewModel.updateProfile(name, avatarUrl)
                        },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    AsyncImage(
                        model = if (avatarUrl.isNotBlank()) avatarUrl else R.drawable.img,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(80.dp)
                    )

                    Divider()

                    SettingToggle ("Dark Theme", darkTheme) {
                    darkTheme = it
                    viewModel.updateTheme(it)
                }

                    SettingToggle("Biometric Unlock", biometricEnabled) {
                        biometricEnabled = it
                        viewModel.updateBiometric(it)
                    }

                    SettingToggle("Auto Backup", autoBackup) {
                        autoBackup = it
                        viewModel.updateAutoBackup(it)
                    }

                    Divider()

                    Button(onClick = { viewModel.clearCache() }) {
                        Text("Clear Cache")
                    }

                    Button(onClick = { viewModel.logout() }) {
                        Text("Logout")
                    }
                }
            }
        }

        else -> Unit
    }
}

@Composable
fun SettingToggle(title: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) { Text(title)
        Switch (checked = checked, onCheckedChange = onToggle) }
}