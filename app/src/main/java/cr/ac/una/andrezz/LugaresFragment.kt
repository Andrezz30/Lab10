package cr.ac.una.andrezz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cr.ac.una.andrezz.adapter.LugaresAdapter
import cr.ac.una.andrezz.clases.Pagina
import cr.ac.una.andrezz.dao.PageDAO
import cr.ac.una.andrezz.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LugaresFragment : Fragment() {
    private lateinit var paginasDao: PageDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lugares, container, false)

        paginasDao = AppDatabase.getInstance(requireContext()).ubicacionDao()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listaPaginas)

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val ubicaciones = withContext(Dispatchers.Default) {
                    paginasDao.getAll() // Obtener los datos de la base de datos
                }
                val adapter = LugaresAdapter(requireContext(),ubicaciones as List<Pagina>)
                listView.adapter = adapter

                listView.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem = adapter.getItem(position) as Pagina
                    val bundle = Bundle()
                    Log.d("Titulo", selectedItem.titulo)
                    var url = selectedItem.url
                    bundle.putString("url", url)
                    val fragment = VistaWeb()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            } catch (e: Exception) {
                // Manejar errores adecuadamente, como mostrar un mensaje de error al usuario
                Log.e("TAG1", "Error al cargar datos desde la base de datos: ${e.message}")
            }
        }
    }
}