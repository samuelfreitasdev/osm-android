package br.com.sf.osm_challenge.ui.maps

import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.ui.maps.MapsContract
import br.com.sf.osm_challenge.ui.maps.MapsFragment
import br.com.sf.osm_challenge.ui.maps.MapsViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class MapsModule {

    @PerFragment
    @Binds
    internal abstract fun providePresenter(mapsViewModel: MapsViewModel): MapsContract.ViewModel

    @PerFragment
    @Binds
    internal abstract fun provideView(fragment: MapsFragment): MapsContract.View

    @PerFragment
    @Binds
    internal abstract fun provideNavigator(navigator: MapsNavigator): MapsContract.Navigator

}
