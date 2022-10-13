package com.adoyo.mediaplayer

import android.app.Application
import android.net.Uri
import android.provider.MediaStore

data class Metadata(
    val fileName: String,
)

interface MetaDataReader {
    fun getMetadataFromUri(contentUri: Uri): Metadata?
}

class MetadataReaderImpl(
    private val app: Application
) : MetaDataReader {
    override fun getMetadataFromUri(contentUri: Uri): Metadata? {
        if (contentUri.scheme != "content") {
            return null
        }
        val fileName = app.contentResolver
            .query(
                contentUri,
                arrayOf(MediaStore.Video.VideoColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                val index = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                cursor.getString(index)
            }
        return fileName?.let { fullFileName ->
            Metadata(
                fileName = Uri.parse(fullFileName).lastPathSegment ?: return null
            )
        }
    }

}