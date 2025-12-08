package uk.ac.tees.mad.memorylog.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.memorylog.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val profile = viewModel.userProfile

    if (profile == null) {
        // Show loading while profile is fetched
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Safe values with defaults
    val name by remember { mutableStateOf(profile.name) }
    val avatarUrl by remember { mutableStateOf(profile.avatarUrl) }
    val darkTheme by remember { mutableStateOf(profile.darkTheme) }
    val autoBackup by remember { mutableStateOf(profile.autoBackup) }
    val biometricEnabled by remember { mutableStateOf(profile.biometricEnabled) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.titleLarge)


            OutlinedTextField(
                value = name,
                onValueChange = { name -> viewModel.updateProfile(name, avatarUrl) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            AsyncImage(
                model = if (avatarUrl.isNotBlank()) avatarUrl else R.drawable.default_avatar,
                contentDescription = "Avatar",
                modifier = Modifier.size(80.dp)
            )

        Divider()

        SettingToggle("Dark Theme", darkTheme) {
            viewModel.updateTheme(it)
        }

        SettingToggle("Biometric Unlock", biometricEnabled) {
            viewModel.updateBiometric(it)
        }

        SettingToggle("Auto Backup", autoBackup) {
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
