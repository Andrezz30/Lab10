package cr.ac.una.andrezz.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cr.ac.una.andrezz.clases.Pagina

@Dao
interface PageDAO {
    @Insert
    fun insert(entity: Pagina)

    @Update
    fun update(entity: Pagina)

    @Query("SELECT * FROM Pagina")
    fun getAll(): List<Pagina?>?

    @Query("SELECT * FROM Pagina ORDER BY vecesVisto DESC LIMIT :limit")
    suspend fun getAll(limit: Int): List<Pagina>

    @Query("DELETE FROM Pagina")
    suspend fun deleteAll()
}