package cr.ac.una.andrezz.clases

import java.io.Serializable

data class page (
    var title: String,
    var thumbnail: thumbnail,
    var titles: titles,
    var extract :String,
    var url: String? = null,
): Serializable