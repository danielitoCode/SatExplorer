package com.elitec.satexplorer

import android.app.Application
import com.elitec.satexplorer.feature.satellite.di.satelliteModule
import com.elitec.satexplorer.feature.tracking.di.trackingModule
import com.elitec.satexplorer.feature.visualization.di.visualizationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SatExplorerApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SatExplorerApp)
            modules(
                visualizationModule,
                trackingModule,
                satelliteModule
            )
        }
    }
}
