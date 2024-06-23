package cr.ac.una.andrezz.converter



import androidx.room.TypeConverter
import cr.ac.una.andrezz.clases.thumbnail
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Converters {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let {
            LocalDateTime.parse(it, formatter)
        }
    }
    @TypeConverter
    fun fromThumbnail(thumbnail: thumbnail?): ByteArray? {
        if (thumbnail == null) return null

        val byteArrayOutputStream = ByteArrayOutputStream()
        try {
            ObjectOutputStream(byteArrayOutputStream).use { it.writeObject(thumbnail) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return byteArrayOutputStream.toByteArray()
    }

    @TypeConverter
    fun toThumbnail(byteArray: ByteArray?): thumbnail? {
        if (byteArray == null) return null

        val byteArrayInputStream = ByteArrayInputStream(byteArray)
        var thumbnail: thumbnail? = null
        try {
            ObjectInputStream(byteArrayInputStream).use { thumbnail = it.readObject() as thumbnail }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return thumbnail
    }
}