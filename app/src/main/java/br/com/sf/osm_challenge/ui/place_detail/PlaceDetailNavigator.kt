package br.com.sf.osm_challenge.ui.place_detail

import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.ui.main.MainContract
import javax.inject.Inject

@PerFragment
class PlaceDetailNavigator
@Inject internal constructor(private val navigator: MainContract.Navigator) : PlaceDetailContract.Navigator {

    override fun onBackPress() {
        navigator.goToHome()
    }

}