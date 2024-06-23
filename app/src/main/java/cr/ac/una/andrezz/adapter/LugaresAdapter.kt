package cr.ac.una.andrezz.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import cr.ac.una.andrezz.R
import cr.ac.una.andrezz.clases.Pagina

class LugaresAdapter(context: Context, lugares: List<Pagina>) :
    ArrayAdapter<Pagina>(context,0,lugares) {


    @SuppressLint("MissingInflatedId", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_lugar, parent, false)
        val nombre = view.findViewById<TextView>(R.id.nombreView)
        val descripcion = view.findViewById<TextView>(R.id.descripcionView)
        val imagen = view.findViewById<ImageView>(R.id.imagenView)
        val fechayhora = view.findViewById<TextView>(R.id.fechaHoraView)

        val lugar = getItem(position)

        nombre.text = lugar?.titulo
        Log.d("Titulo", "Se coloca titulo")

        val extractText = lugar?.descripcion ?: "Sin extracto"
        descripcion.text = if (extractText.length > 300) extractText.substring(0, 300) + "..." else extractText
        Log.d("Descripcion", "Se coloca descripcion")

        lugar?.imagen?.source?.let { url ->
            Glide.with(context)
                .load(url)
                .into(imagen)
        }

        Log.d("Imagen", "Se coloca imagen")


        fechayhora.text = lugar?.fecha.toString()
        Log.d("FEcha", "Se coloca fecha")


        return view
    }
}