package cr.ac.una.andrezz

//import androidx.fragment.app.commit
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        reemplazarFragmento(Home(), "WikiLocation")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)



        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.FOREGROUND_SERVICE), LOCATION_PERMISSION_REQUEST_CODE)
        }
        val url = intent.getStringExtra("url")
        if (url != null) {
            val fragment = VistaWeb.newInstance(url, "param2") // reemplaza "param2" con lo que necesites
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_content, fragment) // reemplaza "fragment_container" con tu ID de contenedor real
                .commit()
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //startLocationService()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val title: Int
        lateinit var fragment: Fragment
        when (menuItem.itemId) {
            R.id.home_fragment -> {
                title = R.string.home_fragment
                fragment = Home()
            }
            R.id.buscador_fragment -> {
                title = R.string.buscador_fragment
                fragment = ListControlFinancieroFragment()
            }
            R.id.lugares_b_d -> {
                title = R.string.menu_directions
                fragment = LugaresFragment()
            }
            R.id.frecuente_fragment -> {
                title = R.string.frecuente_fragment
                fragment = ListaFrecuentesFragment()
            }
            R.id.config_fragment -> {
                title = R.string.config_fragment
                fragment = FrecuentesFragment()
            }

            else -> {
                throw IllegalArgumentException("menu option not implemented!!")
            }
        }

        reemplazarFragmento(fragment, getString(title))
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun reemplazarFragmento(fragment: Fragment, title: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragment)
            .commit()
        setTitle(title)
    }

}
