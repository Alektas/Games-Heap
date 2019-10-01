package alektas.gamesheap

import alektas.gamesheap.di.AppComponent
import alektas.gamesheap.di.DaggerAppComponent
import android.app.Application

class App: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }
}