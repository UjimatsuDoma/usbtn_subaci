package prac.tanken.shigure.ui.subaci.fontman.domain

import android.content.res.AssetFileDescriptor
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import net.sf.sevenzipjbinding.ArchiveFormat
import net.sf.sevenzipjbinding.ExtractAskMode
import net.sf.sevenzipjbinding.ExtractOperationResult
import net.sf.sevenzipjbinding.IArchiveExtractCallback
import net.sf.sevenzipjbinding.IArchiveOpenCallback
import net.sf.sevenzipjbinding.IInArchive
import net.sf.sevenzipjbinding.ISequentialOutStream
import net.sf.sevenzipjbinding.SevenZip
import net.sf.sevenzipjbinding.SevenZipException
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream
import prac.tanken.shigure.ui.subaci.core.data.repository.InternalStorageRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
import javax.inject.Inject

class FontDecompressUseCase @Inject constructor(
    private val internalStorageRepository: InternalStorageRepository,
    private val resRepository: ResRepository,
) {
    companion object {
        const val TAG = "FontDecompress"

        var total = 0L
        var currentExtracted = 0L
    }

    val cacheFileZip = File(internalStorageRepository.cacheDir, "noto.7z")
    val fontsDir = File(internalStorageRepository.filesDir, "fonts")

    init {
        if (!fontsDir.isDirectory) fontsDir.delete()
        if (!fontsDir.exists()) fontsDir.mkdir()
    }

    private class SeqOutStream(
        val onProgress: (Float) -> Unit,
        val destFile: File,
    ) : ISequentialOutStream {
        @Throws(SevenZipException::class)
        override fun write(data: ByteArray): Int {
            synchronized(this) {
                if (data.isEmpty()) {
                    throw SevenZipException("null data")
                }
                Log.i(TAG, "Data to write: " + data.size)
                if (!destFile.exists()) destFile.createNewFile()
                destFile.appendBytes(data)
                currentExtracted += data.size
                val progress = currentExtracted.toFloat() / total.toFloat()
                onProgress(progress)
                return data.size
            }
        }
    }

    // The 7-zip file ~36MB on Windows
    suspend fun copySevenZipToDisk() {
        withContext(Dispatchers.IO) {
            fun getAssetFDByName(name: String) = resRepository.am.open(name)
            fun copyFromFDToDisk(input: InputStream, dest: File) {
                if (!dest.exists()) dest.createNewFile()
                input.use { input ->
                    dest.outputStream().use { output ->
                        val test = input.copyTo(output)
                        Log.d(TAG, "font file copied: $test")
                    }
                }
            }

            val sansOtfIS = getAssetFDByName("noto.7z")
            copyFromFDToDisk(sansOtfIS, cacheFileZip)
        }
    }

    // All fonts + 7-zip file ~184MB on Windows
    suspend fun decompress(onProgress: (Float) -> Unit): FontDecompressState {
        return try {
            withContext(Dispatchers.IO) {
                try {
                    if (!cacheFileZip.exists()) copySevenZipToDisk()
                    RandomAccessFileInStream(RandomAccessFile(cacheFileZip, "r")).use { inStream ->
                        SevenZip.openInArchive(null, inStream).use { archive ->
                            val archiveSimple = archive.simpleInterface
                            val totalOriginalSize = archiveSimple.archiveItems.sumOf { it.size }
                            total = totalOriginalSize
                            for (item in archiveSimple.archiveItems) {
                                val fileName = item.path.substringAfterLast("/")
                                val cacheFontFile =
                                    File(fontsDir, fileName)
                                item.extractSlow(SeqOutStream(onProgress, cacheFontFile))
                            }
                            total = 0L
                            currentExtracted = 0L
                        }
                    }
                    if (cacheFileZip.exists()) cacheFileZip.delete()
                } catch (e: FileNotFoundException) {
                    Log.e(TAG, e.message!!)
                    throw e
                } catch (e: SevenZipException) {
                    Log.e(TAG, e.message!!)
                    throw e
                } catch (e: IOException) {
                    Log.e(TAG, e.message!!)
                    throw e
                }
                return@withContext FontDecompressState.Complete
            }
        } catch (e: Exception) {
            Log.d(TAG, e.message!!)
            FontDecompressState.Failed
        }
    }
}