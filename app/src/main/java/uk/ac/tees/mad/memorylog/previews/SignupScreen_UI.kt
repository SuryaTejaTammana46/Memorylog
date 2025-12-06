package uk.ac.tees.mad.memorylog.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.memorylog.ui.screens.auth.components.AppTextField
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme

@Composable
fun SignupScreen_UI() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center)
                .scrollable(scrollState, Orientation.Vertical),
        ) {
            Text(
                "Create Account",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )
            Spacer(Modifier.height(8.dp))

            AppTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Sign up")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_SignupScreen_UI() {
    MemoryLogTheme {
        SignupScreen_UI()
    }
}
