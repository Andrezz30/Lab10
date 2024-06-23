package cr.ac.una.andrezz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class FrecuentesFragment : Fragment() {
    private lateinit var buscarFrecuentes: Button
    lateinit var numeroCuadro: EditText
    var numeroFrecuentes: String = "0"      // se inicializa en 1 para que no de error en la busqueda
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buscarFrecuentes = view.findViewById(R.id.VerBoton)
        numeroCuadro = view.findViewById(R.id.NumeroFrecuentes)

        // Se crea el evento para buscar los frecuentes, se obtiene el numero de frecuentes
        buscarFrecuentes.setOnClickListener {
            numeroFrecuentes = numeroCuadro.text.toString()

            val bundle = Bundle()
            bundle.putString("numeroFrecuentes", numeroFrecuentes) // Agrega el valor al Bundle
            val fragment = ListaFrecuentesFragment()
            fragment.arguments = bundle // Pasa el Bundle al nuevo fragmento
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.home_content, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frecuentes, container, false)
    }
}