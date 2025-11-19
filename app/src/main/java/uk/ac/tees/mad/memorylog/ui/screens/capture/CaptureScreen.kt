package uk.ac.tees.mad.memorylog.ui.screens.capture

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.memorylog.utils.FileUtils
import uk.ac.tees.mad.memorylog.utils.await
import uk.ac.tees.mad.memorylog.viewmodel.CaptureViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CaptureScreen(
    onPhotoSaved: (String) -> Unit,
    viewModel: CaptureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> permissionGranted = granted }
    )

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            launcher.launch(android.Manifest.permission.CAMERA)
        }
    }

    if (permissionGranted) {
        CameraPreviewAndCaptureUI(onPhotoSaved, viewModel)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Waiting for camera permission…")
        }
    }
}

//    val context = LocalContext.current
//    val imageCapture = remember { ImageCapture.Builder().build() }
//
//    var previewView: PreviewView? by remember { mutableStateOf(null) }
//
//    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
//
////    RequestCameraPermission {
//        CameraPreviewAndCaptureUI(onPhotoSaved, viewModel)
////    }
//
//    LaunchedEffect(true) {
//        val cameraProvider = ProcessCameraProvider.await(context)
//        val preview = Preview.Builder().build().also {
//            it.setSurfaceProvider(previewView?.surfaceProvider)
//        }
//
//        cameraProvider.unbindAll()
//        cameraProvider.bindToLifecycle(
//            lifecycleOwner,
//            androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
//            preview,
//            imageCapture
//        )
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//
//        AndroidView(
//            modifier = Modifier.weight(1f),
//            factory = {
//                PreviewView(it).apply {
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT
//                    )
//                }.also { previewView = it }
//            }
//        )
//
//        Button(
//            modifier = Modifier.fillMaxWidth(),
//            onClick = {
//                takePhoto(context, imageCapture) { path ->
//                    viewModel.onPhotoCaptured(path)
//                    onPhotoSaved(path)
//                }
//            }
//        ) {
//            Text("Capture Photo")
//        }
//    }
//}

fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onSaved: (String) -> Unit
) {
    val photoFile = FileUtils.createImageFile(context)

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions, ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("PhotoPath", "Saved at: ${photoFile.absolutePath}")
                onSaved(photoFile.absolutePath)
            }
        })
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
private fun CameraPreviewAndCaptureUI(
    onPhotoSaved: (String) -> Unit,
    viewModel: CaptureViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    var lensFacing by remember { mutableStateOf(androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA) }
    var flashMode by remember { mutableStateOf(ImageCapture.FLASH_MODE_OFF) }

    val imageCapture = remember { ImageCapture.Builder()
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .setFlashMode(flashMode)
        .build()
    }

    // Check permission state
    val hasCameraPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    if (!hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Waiting for camera permission…")
        }
        return
    }

    // CameraX Setup
    LaunchedEffect(lensFacing, flashMode, previewView) {
        if (previewView != null) {
            val cameraProvider = ProcessCameraProvider.await(context)

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView?.surfaceProvider)
            }

            imageCapture.flashMode = flashMode

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                lensFacing,
                preview,
                imageCapture
            )
        }
    }

    // UI Layout
    Box(modifier = Modifier.fillMaxSize()) {

        // Camera Preview
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }.also { previewView = it }
            }
        )

        // Top Controls Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Flash Toggle
            Button(onClick = {
                flashMode = when (flashMode) {
                    ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                    ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                    else -> ImageCapture.FLASH_MODE_OFF
                }
            }) {
                Text(
                    when (flashMode) {
                        ImageCapture.FLASH_MODE_OFF -> "Flash Off"
                        ImageCapture.FLASH_MODE_ON -> "Flash On"
                        else -> "Flash Auto"
                    }
                )
            }

            // Switch Camera
            Button(onClick = {
                lensFacing =
                    if (lensFacing == androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA)
                        androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
                    else androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
            }) {
                Text("Switch")
            }
        }

        // Round Capture Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .size(80.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(androidx.compose.material3.MaterialTheme.colorScheme.primary)
                .clickable {
                    takePhoto(context, imageCapture) { path ->
                        viewModel.onPhotoCaptured(path)
                        onPhotoSaved(path)
                    }
                }
        )
    }
}