package cr.ac.una.andrezz.clases

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime
@Entity
data class Pagina(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var _uuid: String?,
    var vecesVisto: Int,
    var titulo: String,
    var fecha: LocalDateTime,
    var wikipediaTitulo: String,
    var descripcion: String,
    var imagen: thumbnail,
    var coordenadas: String,
    var url: String

    /*var titulo:String,
    var descripcion: String,
    var articulo: String,*/

): Serializable
