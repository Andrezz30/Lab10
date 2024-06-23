package cr.ac.una.andrezz.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cr.ac.una.andrezz.clases.Pagina
import cr.ac.una.andrezz.converter.Converters
import cr.ac.una.andrezz.dao.PageDAO

@Database(entities = [Pagina::class], version = 7)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun ubicacionDao(): PageDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return try {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "DB-Pagina"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            } catch (ex: Exception) {
                // Manejar excepciones durante la creación de la base de datos
                throw ex
            }
        }
    }
}
