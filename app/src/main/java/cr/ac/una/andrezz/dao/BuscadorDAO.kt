package cr.ac.una.andrezz.dao

import cr.ac.una.andrezz.clases.pages
import retrofit2.http.GET
import retrofit2.http.Path

interface BuscadorDAO {
    @GET("page/related/{title}")
    suspend fun Buscar(@Path("title") title: String): pages
}