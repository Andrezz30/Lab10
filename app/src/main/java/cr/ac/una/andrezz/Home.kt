package cr.ac.una.andrezz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cr.ac.una.andrezz.service.LocationService


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val botonStart = view.findViewById<Button>(R.id.start_button)
        val botonStop = view.findViewById<Button>(R.id.stop_button)
        botonStart.setOnClickListener {
            val serviceIntent = Intent(requireContext(), LocationService::class.java)
            ContextCompat.startForegroundService(requireContext(), serviceIntent)
        }
        botonStop.setOnClickListener {
            val serviceIntent = Intent(requireContext(), LocationService::class.java)
            requireActivity().stopService(serviceIntent)

        }

        return view
    }
}