//package uk.ac.tees.mad.memorylog.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.launch
//import uk.ac.tees.mad.memorylog.data.repository.RoomMemoryRepository
//import uk.ac.tees.mad.memorylog.domain.model.Memory
//import javax.inject.Inject
//
//@HiltViewModel
//class TestLocalDbViewModel @Inject constructor(
//    private val roomRepo: RoomMemoryRepository
//) : ViewModel() {
//
//    val memories = roomRepo.getAllMemories()
//        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
//
//    fun insertTestMemory() {
//        viewModelScope.launch {
//            Log.d("DB_TEST", "Inserted memory into local DB")
//            roomRepo.insert(
//                Memory(
//                    id = "test-id",
//                    title = "Test Day",
//                    description = "Testing local DB insert",
//                    date = "2025-11-05",
//                    imagePath = "/fake/path.jpg",
//                    photoPath = photoPath
//                )
//            )
//            Log.d("DB_TEST", "Inserted memory into local DB")
//        }
//    }
//}
