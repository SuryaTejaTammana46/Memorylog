package uk.ac.tees.mad.memorylog.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CaptureViewModel @Inject constructor() : ViewModel() {

    var lastCapturedPath: String? = null
        private set

    fun onPhotoCaptured(path: String) {
        lastCapturedPath = path
    }
}