package cr.ac.una.andrezz.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import cr.ac.una.andrezz.MainActivity
import cr.ac.una.andrezz.R
import cr.ac.una.andrezz.VistaWeb
import cr.ac.una.andrezz.clases.Pagina
import cr.ac.una.andrezz.controller.PageController
import cr.ac.una.andrezz.dao.PageDAO
import cr.ac.una.andrezz.db.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.LocalDateTime
import kotlin.properties.Delegates

var auxName = ""
var auxProb = 0
class LocationService : Service() {

    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var buttonPendingIntent: PendingIntent
    private lateinit var   buttonIntent :Intent
    private var contNotificacion = 2
    private var placeName: String? = null
    val pageController = PageController();

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        buttonIntent = Intent(this, VistaWeb::class.java).apply {
            action = "OPEN_FRAGMENT"
            putExtra("param_key", "param_value")
        }
        // Abrir el archivo local.properties
        /*val properties = Properties()
        val inputStream: InputStream = FileInputStream("local.properties")
        properties.load(inputStream)*/


        buttonPendingIntent = PendingIntent.getActivity(this, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        Places.initialize(applicationContext, getString(R.string.google_maps_api_key))
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        this.startForeground(1, createNotification("Service running"))

        requestLocationUpdates()
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "locationServiceChannel",
            "Location Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(message: String): Notification {
        return NotificationCompat.Builder(this, "locationServiceChannel")
            .setContentTitle("Location Service")
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 0L
        ).apply {
            setMinUpdateDistanceMeters(50.0f)
        }.build()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.locations.forEach { location ->
                getPlaceName(location.latitude, location.longitude)
                latitude = location.latitude
                longitude = location.longitude
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getPlaceName(latitude: Double, longitude: Double) {
        val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
        val placesClient: PlacesClient = Places.createClient(this)

        val placeResponse = placesClient.findCurrentPlace(request)
        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                val topPlaces = response.placeLikelihoods
                    .sortedByDescending { it.likelihood }
                    .take(1)

                topPlaces.forEach { placeLikelihood ->
                    placeName = placeLikelihood.place.name ?: "Unknown"
                    val message = "Lugar: $placeName, Probabilidad: ${placeLikelihood.likelihood}"
                    //sendNotification(message, placeName)
                    Log.d("LocationService", message)
                    // Call searchWikipediaAndNotify with the place name
                    searchWikipediaAndNotify(placeName!!)
                }
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Log.e("LocationService", "Lugar no encontrado: ${exception.statusCode}")
                }
            }
        }
    }
    private fun sendNotification(message: String, wikipediaUrl: String) {
        val notificationId = contNotificacion++
        if(wikipediaUrl!="") {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("url", wikipediaUrl)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                notificationId, // Use notificationId as the requestCode to ensure uniqueness
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val notification = NotificationCompat.Builder(this, "locationServiceChannel")
                .setContentTitle(placeName)
                .setContentText(message)
                .setSmallIcon(R.mipmap.icono)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)


        }else{
            /*val intent = Intent(this, MainActivity::class.java).apply {
                //putExtra("url", wikipediaUrl)
                //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this,
                notificationId, // Use notificationId as the requestCode to ensure uniqueness
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )*/
            val notification = NotificationCompat.Builder(this, "locationServiceChannel")
                .setContentTitle(placeName)
                .setContentText(message)
                .setSmallIcon(R.mipmap.icono)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(notificationId, notification)
        }
    }
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private fun searchWikipediaAndNotify(placeName: String) {
        lateinit var movimientoDao: PageDAO
        movimientoDao = AppDatabase.getInstance(this).ubicacionDao()
        val formattedQuery = placeName.replace(" ", "_")
        serviceScope.launch {
            try {
                val resultadoBusqueda = withContext(Dispatchers.IO) {
                    pageController.Buscar(formattedQuery)
                }

                if (resultadoBusqueda.isNotEmpty()) {
                    val firstResultTitle = resultadoBusqueda.first().title
                    val wikipediaUrl = "https://es.wikipedia.org/wiki/$firstResultTitle"
                    sendNotification("Latitud: $latitude \nLongitud: $longitude\nArticulo:Si", wikipediaUrl)
                    Log.d("ResultadoBusqueda", "Se encontró información: $resultadoBusqueda")
                    val current = LocalDateTime.now()
                     var aux = false
                    lateinit var pagina: Pagina
                    val listaLugares = withContext(Dispatchers.Default) {
                        movimientoDao.getAll()
                    }


                    if (!listaLugares.isNullOrEmpty()) {
                        for (i in listaLugares){
                            if (i != null) {
                                if(i.titulo == placeName){
                                    movimientoDao.update(Pagina(i.id,i._uuid,i.vecesVisto+1,i.titulo,i.fecha,i.descripcion,i.imagen,i.coordenadas,i.url))
                                    Log.d("Base de datos", "Se ACTUALIZO con exito en la base de datos")
                                    aux = true
                                    break
                                }
                            }
                        }
                        if (aux==false){
                            pagina = Pagina(
                                null, null, 1, placeName, current, resultadoBusqueda.first().extract,
                                resultadoBusqueda.first().thumbnail, "Latitud:$latitude, Longitud:$longitude",
                                wikipediaUrl)
                            movimientoDao.insert(pagina)
                            Log.d("Base de datos", "Se guarda con exito en la base de datos")
                        }


                    }else{
                        pagina = Pagina(
                            null, null, 1, placeName, current, resultadoBusqueda.first().extract,
                            resultadoBusqueda.first().thumbnail, "latitude,longitude",wikipediaUrl
                        )
                            movimientoDao.insert(pagina)

                        Log.d("Base de datos", "Se guarda con exito en la base de datos")
                    }

                } else {
                    Log.d("ResultadoBusqueda", "No se encontró información")
                }
            } catch (e: HttpException) {
                sendNotification("Latitud: $latitude \nLongitud: $longitude\n Articulo:No", "")
                Log.e("HTTP_ERROR", "No se encontró información. Error: ${e.message}")
            } catch (e: Exception) {
                Log.e("ERROR", "No se encontró información. Error: ${e.message}")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
