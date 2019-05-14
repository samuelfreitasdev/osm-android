package br.com.sf.osm_challenge.ui.main

import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.ui.maps.MapsFragment
import br.com.sf.osm_challenge.ui.maps.MapsModule
import br.com.sf.osm_challenge.ui.place_detail.PlaceDetailFragment
import br.com.sf.osm_challenge.ui.place_detail.PlaceDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityProvider {

    @PerFragment
    @ContributesAndroidInjector(modules = [MapsModule::class])
    internal abstract fun provideMapsFragment(): MapsFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [PlaceDetailModule::class])
    internal abstract fun providePlaceDetailFragment(): PlaceDetailFragment

}
