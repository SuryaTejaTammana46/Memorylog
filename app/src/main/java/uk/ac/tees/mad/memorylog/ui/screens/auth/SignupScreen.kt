package uk.ac.tees.mad.memorylog.ui.screens.auth

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.memorylog.ui.screens.auth.components.AppTextField
import uk.ac.tees.mad.memorylog.ui.viewmodel.AuthViewModel
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.memorylog.ui.theme.MemoryLogTheme

@Composable
fun SignupScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollableState = rememberScrollState()

    LaunchedEffect(uiState.success) {
        if (uiState.success) onSignupSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center)
                .scrollable(
                    state = scrollableState,
                    orientation = Orientation.Vertical
                ),
        ) {
            Text(
                "Create Account",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(16.dp))

            // Email
            AppTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChanged,
                placeholder = "Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            //Password
            AppTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                placeholder = "Password",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )
            Spacer(Modifier.height(8.dp))
            AppTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged,
                placeholder = "Confirm Password",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
            )
            Spacer(Modifier.height(16.dp))

            //Signup
            Button(
                onClick = { viewModel.doSignUp() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading,
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp),
            ) {
                Text(if (uiState.loading) "Creating..." else "Sign up")
            }

            Spacer(Modifier.height(8.dp))
            //Login
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Already have an account? ",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium

                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Login",
                    modifier = Modifier.clickable(onClick = onNavigateToLogin),
                    color = MaterialTheme.colorScheme.onBackground,
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            //show error message if present
            uiState.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(text = it.toString(), color = MaterialTheme.colorScheme.error)
            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewSignupScreen() {
    MemoryLogTheme {
        SignupScreen(
            onSignupSuccess = {},
            onNavigateToLogin = {}
        )
    }
}

