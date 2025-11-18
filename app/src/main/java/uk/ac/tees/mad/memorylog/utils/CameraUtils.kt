package uk.ac.tees.mad.memorylog.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.camera.lifecycle.ProcessCameraProvider
import kotlinx.coroutines.suspendCancellableCoroutine

@RequiresApi(Build.VERSION_CODES.P)
suspend fun ProcessCameraProvider.Companion.await(context: Context): ProcessCameraProvider =
    suspendCancellableCoroutine { cont ->
        val future = getInstance(context)
        future.addListener({
            cont.resume(future.get()) {}
        }, context.mainExecutor)
    }

private val Context.mainExecutor
    get() = androidx.core.content.ContextCompat.getMainExecutor(this)