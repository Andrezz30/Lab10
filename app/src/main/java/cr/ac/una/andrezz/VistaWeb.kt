package cr.ac.una.andrezz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VistaWeb.newInstance] factory method to
 * create an instance of this fragment.
 */
class VistaWeb : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vista_web, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView: WebView = view.findViewById(R.id.webView)
        var url = activity?.intent?.getStringExtra("url") ?: "https://defaulturl.com"
        if(url=="https://defaulturl.com"){
            url = arguments?.getString("url") ?: "https://defaulturl.com"
        }

        webView.loadUrl(url)
        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
        webView.settings.javaScriptEnabled = true


        Log.d("WebViewActivity", "URL recibida: $url")

        if (!url.isNullOrBlank()) {
            webView.loadUrl(url)
        } else {
            Log.e("WebViewActivity", "URL es null o está vacía")
            //Toast.makeText(this, "URL inválida. No se puede cargar la página.", Toast.LENGTH_SHORT).show()
        }

        val volverBoton = view.findViewById<Button>(R.id.volverBoton)
        volverBoton.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }





    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VistaWeb.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VistaWeb().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        // Mostrar la barra de navegación
        (activity as AppCompatActivity).supportActionBar?.show()
    }
}
/*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class VistaWeb : Acty() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_vista_web)

        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
        webView.settings.javaScriptEnabled = true

        val url = intent.getStringExtra("url")
        Log.d("WebViewActivity", "URL recibida: $url")

        if (!url.isNullOrBlank()) {
            webView.loadUrl(url)
        } else {
            Log.e("WebViewActivity", "URL es null o está vacía")
            Toast.makeText(this, "URL inválida. No se puede cargar la página.", Toast.LENGTH_SHORT).show()
        }

        val volverBoton = findViewById<Button>(R.id.volverBoton)
        volverBoton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}*/
