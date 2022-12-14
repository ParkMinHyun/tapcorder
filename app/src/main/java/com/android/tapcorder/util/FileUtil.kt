package com.android.tapcorder.util

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.android.tapcorder.App
import com.android.tapcorder.data.audio.AudioData
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

class FileUtil {

    companion object {
        private const val DEFAULT_FILE_NAME = "지나간 음성"
        val TEMP_FILE_DIR = App.getContext().getExternalFilesDir("/")!!.absolutePath
        val SAVE_FILE_DIR = Environment.getExternalStorageDirectory().toString() + "/DCIM/MoMember/"

        @SuppressLint("SimpleDateFormat")
        fun createTempFilePath() = "$TEMP_FILE_DIR/${SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss").format(Date())}.mp3"

        fun createSaveFilePath(): String {
            var index = 1
            while (true) {
                val fileName = "$SAVE_FILE_DIR/$DEFAULT_FILE_NAME$index.mp3"
                if (!File(fileName).exists()) {
                    return fileName
                }
                index++
            }
        }

        fun deleteFilePath(filePath: String) {
            val path = Paths.get(filePath)
            try {
                Files.delete(path)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun renameFile(filePath: String, from: String, to: String): File {
            val src = File(filePath, from)
            val dest = File(filePath, to)
            if (src.exists()) {
                src.renameTo(dest)
            }
            return dest
        }

        fun getContentUri(audioData: AudioData): Uri? {
            val context = App.getContext()
            val audioFilePath = SAVE_FILE_DIR + "/" + audioData.name
            val internalFile = File(audioFilePath)
            return FileProvider.getUriForFile(context, "${context.packageName}.provider", internalFile)
        }
    }
}