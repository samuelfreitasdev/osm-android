package br.com.sf.osm_challenge.injection.app

import android.app.Application
import android.content.Context
import br.com.sf.osm_challenge.repository.DataManagerModule
import br.com.sf.osm_challenge.util.schedulers.BaseSchedulerProvider
import br.com.sf.osm_challenge.util.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DataManagerModule::class])
internal class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider.instance
    }

}