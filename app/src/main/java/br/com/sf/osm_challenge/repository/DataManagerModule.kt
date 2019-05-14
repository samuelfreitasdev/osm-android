package br.com.sf.osm_challenge.repository

import br.com.sf.osm_challenge.repository.osm.OsmRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataManagerModule {

    @Singleton
    @Provides
    fun providePhoneQueryRepository(): OsmRepository {
        return OsmRepository()
    }

}