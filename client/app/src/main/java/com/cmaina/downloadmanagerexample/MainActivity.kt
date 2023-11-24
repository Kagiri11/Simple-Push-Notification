package com.cmaina.downloadmanagerexample

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cmaina.downloadmanagerexample.ui.theme.DownloadManagerExampleTheme
import java.io.File
import java.util.*

class MainActivity : ComponentActivity() {

    private var downloadID: Long = 0L
    private var isDownloadFininshed: Boolean = false
    private lateinit var dwnldManager: DownloadManager
    private var downloadProgress: Int = 0
    val myFile = File("kyoskReceipt.txt")

    val url = "https://images.unsplash.com/photo-1656479816742-f53fb677f809?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1974&q=80"
    val fileName = url.substring(url.lastIndexOf('/') + 1).substring(0, 1).uppercase(Locale.ROOT)

    override fun onCreate(savedInstanceState: Bundle?) {
        dwnldManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        super.onCreate(savedInstanceState)
        setContent {
            DownloadManagerExampleTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = {
                            startDownload(provideDownloadRequest(url))
                        }
                    ) {
                        Text(text = "Download")
                    }
                }
            }
        }
    }

    private fun provideDownloadRequest(imageDownloadUrl: String): DownloadManager.Request {
        return DownloadManager.Request(Uri.parse(imageDownloadUrl))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Kyosk Receipt")
            .setDescription("Downloading Kyosk order receipt")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOCUMENTS, File.separator)
    }

    private fun startDownload(request: DownloadManager.Request) {
        downloadID = dwnldManager.enqueue(request)
        trackDownload()
    }

    private fun trackDownload() {
        Log.d("DonwloadThing", "trackDowload area")
        while (!isDownloadFininshed) {
            Log.d("DonwloadThing", "track download area isdownload finished")
            val cursor = dwnldManager.query(DownloadManager.Query().setFilterById(downloadID))
            if (cursor.moveToFirst()) {
                Log.d("DonwloadThing", "Cursor has moved to first")
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val status = cursor.getInt(columnIndex)
                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        Log.d("DonwloadThing", "Download has failed")
                    }
                    DownloadManager.STATUS_PAUSED -> {
                        Log.d("DonwloadThing", "Download is paused")
                    }
                    DownloadManager.STATUS_RUNNING -> {
                        Log.d("DonwloadThing", "Download is running")
                        val cursorColumnIndex =
                            cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                        val total = cursor.getLong(cursorColumnIndex)
                        if (total >= 0) {
                            val columnIndexDownloadedSoFar =
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                            val downloadedBit = cursor.getLong(columnIndexDownloadedSoFar)
                            downloadProgress = ((downloadedBit * 100L) / total).toInt()
                        }
                    }
                    DownloadManager.STATUS_PENDING -> {
                        Log.d("DonwloadThing", "Download is pending")
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        Log.d("DonwloadThing", "Download has completed")
                        downloadProgress = 100
                        isDownloadFininshed = true
                        Toast.makeText(this, "Download is complete", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello $name!")
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        DownloadManagerExampleTheme {
            Greeting("Android")
        }
    }
}
