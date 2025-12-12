package uk.ac.tees.mad.memorylog.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _dailyImage = MutableStateFlow<String?>(null)
    val dailyImage: StateFlow<String?> = _dailyImage

    private val _dailyQuote = MutableStateFlow<String?>(null)
    val dailyQuote: StateFlow<String?> = _dailyQuote

    fun fetchDailyInspiration() = viewModelScope.launch {
        val prefs = context.getSharedPreferences("splash_cache", Context.MODE_PRIVATE)

        try {
            val apiKey = "YOUR_PEXELS_API_KEY"
            val url = URL("https://api.pexels.com/v1/curated?per_page=1")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                setRequestProperty("Authorization", apiKey)
                connectTimeout = 4000
                readTimeout = 4000
            }

            val response = connection.inputStream.bufferedReader().readText()
            val json = JSONObject(response)
            val imageUrl = json.getJSONArray("photos")
                .getJSONObject(0)
                .getJSONObject("src")
                .getString("medium")

            // Save image locally
            prefs.edit().putString("last_image", imageUrl).apply()
            _dailyImage.value = imageUrl
        } catch (e: Exception) {
            // Offline fallback
            _dailyImage.value = prefs.getString("last_image", null)
        }

        // --- Optional: Daily quote (can come from any free quote API) ---
        try {
            val quoteResponse = URL("https://zenquotes.io/api/random").readText()
            val quoteJson = JSONObject(quoteResponse.trim().removeSurrounding("[", "]"))
            val quote = quoteJson.getString("q")
            val author = quoteJson.getString("a")
            val fullQuote = "\"$quote\" – $author"

            prefs.edit().putString("last_quote", fullQuote).apply()
            _dailyQuote.value = fullQuote
        } catch (e: Exception) {
            _dailyQuote.value = prefs.getString("last_quote", "Reflect. Remember. Reconnect.")
        }
    }
}
