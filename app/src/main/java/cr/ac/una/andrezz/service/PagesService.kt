package cr.ac.una.andrezz.service

import com.google.gson.GsonBuilder
import cr.ac.una.andrezz.dao.PageDAO
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PagesService {

    val gson = GsonBuilder().setPrettyPrinting().create()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://es.wikipedia.org/api/rest_v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiWikiService = retrofit.create(PageDAO::class.java)
}