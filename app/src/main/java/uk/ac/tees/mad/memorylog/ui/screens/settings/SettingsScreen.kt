package uk.ac.tees.mad.memorylog.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.memorylog.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val profile = viewModel.userProfile

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.titleLarge)

        profile?.let {
            OutlinedTextField(
                value = it.name,
                onValueChange = { name -> viewModel.updateProfile(name, it.avatarUrl) },
                label = { Text("Name") }
            )

            AsyncImage(
                model = it.avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier.size(80.dp)
            )
        }

        Divider()

        SettingToggle("Dark Theme", profile?.darkTheme ?: false) {
            viewModel.updateTheme(it)
        }

        SettingToggle("Biometric Unlock", profile?.biometricEnabled ?: false) {
            viewModel.updateBiometric(it)
        }

        SettingToggle("Auto Backup", profile?.autoBackup ?: true) {
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


@Preview(showBackground = true, name = "MemoryLog – Settings Screen")
@Composable
fun SettingsScreenPreview() {
    var name by remember { mutableStateOf("Sarah Johnson") }
    var darkTheme by remember { mutableStateOf(true) }
    var biometric by remember { mutableStateOf(false) }
    var autoBackup by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.titleLarge)

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Avatar
        AsyncImage(
            model = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=800",
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        HorizontalDivider()

        // Toggles
        SettingToggle("Dark Theme", checked = darkTheme) { darkTheme = it }
        SettingToggle("Biometric Unlock", checked = biometric) { biometric = it }
        SettingToggle("Auto Backup", checked = autoBackup) { autoBackup = it }

        HorizontalDivider()

        // Actions
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
            Text("Logout")
        }
    }
}

