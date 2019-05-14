package br.com.sf.osm_challenge.ui.place_detail

import br.com.sf.osm_challenge.injection.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class PlaceDetailModule {

    @PerFragment
    @Binds
    internal abstract fun provideViewModel(placeDetailViewModel: PlaceDetailViewModel): PlaceDetailContract.ViewModel

    @PerFragment
    @Binds
    internal abstract fun provideView(fragment: PlaceDetailFragment): PlaceDetailContract.View

    @PerFragment
    @Binds
    internal abstract fun provideNavigator(navigator: PlaceDetailNavigator): PlaceDetailContract.Navigator

}
