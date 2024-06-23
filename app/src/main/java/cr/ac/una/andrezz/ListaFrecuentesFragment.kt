package cr.ac.una.andrezz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cr.ac.una.andrezz.adapter.MyListaFrecuentesAdapter
import cr.ac.una.andrezz.clases.Pagina
import cr.ac.una.andrezz.dao.PageDAO
import cr.ac.una.andrezz.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaFrecuentesFragment : Fragment() {
    private lateinit var paginasDao: PageDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        paginasDao = AppDatabase.getInstance(requireContext()).ubicacionDao()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listView = view.findViewById<ListView>(R.id.listaFrecuentes)
        var numeroFrecuentes = arguments?.getString("numeroFrecuentes")?.toInt()
            //?: obtenerNumeroFrecuentes(requireContext()) // Recupera el valor del Bundle o SharedPreferences

        Log.d("NumeroFrecuentes", numeroFrecuentes.toString())
        if(numeroFrecuentes ==null) {
            numeroFrecuentes= obtenerNumeroFrecuentes(requireContext())
            guardarNumeroFrecuentes(
                requireContext(),
                numeroFrecuentes
            ) // Guarda el valor para persistencia
        }else{
            guardarNumeroFrecuentes(
                requireContext(),
                numeroFrecuentes!!
            ) // Guarda el valor para persistencia
        }
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val ubicaciones = withContext(Dispatchers.Default) {
                    paginasDao.getAll(numeroFrecuentes!!)
                }
                val adapter = MyListaFrecuentesAdapter(requireContext(), ubicaciones as List<Pagina>)
                listView.adapter = adapter

                listView.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem = adapter.getItem(position) as Pagina
                    val bundle = Bundle()
                    Log.d("Titulo", selectedItem.titulo)
                    val url = selectedItem.url
                    bundle.putString("url", url)
                    val fragment = VistaWeb()
                    fragment.arguments = bundle
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.home_content, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            } catch (e: Exception) {
                Log.e("TAG1", "Error al cargar datos desde la base de datos: ${e.message}")
            }
        }
    }

    private fun guardarNumeroFrecuentes(context: Context, valor: Int) {
        val sharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("numeroFrecuentes", valor)
        editor.apply()
    }

    private fun obtenerNumeroFrecuentes(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("numeroFrecuentes", 1)
    }
}
