package uk.ac.tees.mad.memorylog.ui.screens.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

@Composable
fun BiometricLogin(onSuccess: () -> Unit) {
    val context = LocalContext.current
    val activity = context as FragmentActivity

    LaunchedEffect(Unit) {
        val biometricManager = BiometricManager.from(context)
        if (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS) {
            val executor = ContextCompat.getMainExecutor(context)
            val prompt = BiometricPrompt(activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        onSuccess()
                    }
                })
            val info = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock MemoryLog")
                .setSubtitle("Use fingerprint or face to unlock")
                .setNegativeButtonText("Cancel")
                .build()
            prompt.authenticate(info)
        }
    }
}
