package br.com.sf.osm_challenge.ui.maps

import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.ui.main.MainContract
import javax.inject.Inject

@PerFragment
class MapsNavigator
@Inject internal constructor(private val navigator: MainContract.Navigator) : MapsContract.Navigator {
    override fun onBackPress() {
        navigator.onBackPressed()
    }

    override fun goToPlaceDetail(placeId: Int, osmType: String) {
        navigator.goToPlaceDetail(placeId, osmType)
    }
}