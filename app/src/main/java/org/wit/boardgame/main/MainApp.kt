package org.wit.boardgame.main
import android.app.Application
import org.wit.boardgame.activities.data.model.LoggedInUser
import org.wit.boardgame.models.PlacemarkJSONStore
import org.wit.boardgame.models.PlacemarkStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var placemarks: PlacemarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        placemarks = PlacemarkJSONStore(applicationContext)
        i("On Board started")
    }
}