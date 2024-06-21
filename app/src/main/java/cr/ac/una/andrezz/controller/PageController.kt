package cr.ac.una.andrezz.controller

import cr.ac.una.andrezz.clases.page
import cr.ac.una.andrezz.service.PagesService

class PageController {
    var pagesService = PagesService()

    suspend fun  Buscar(terminoBusqueda: String):ArrayList<page>{
        return pagesService.apiWikiService.Buscar(terminoBusqueda).pages as ArrayList<page>
    }
}