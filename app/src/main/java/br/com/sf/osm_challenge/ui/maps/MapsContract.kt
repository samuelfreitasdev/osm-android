package br.com.sf.osm_challenge.ui.maps

import br.com.sf.osm_challenge.repository.BoundingBox
import br.com.sf.osm_challenge.repository.POI
import br.com.sf.osm_challenge.ui.base.BaseNavigator
import br.com.sf.osm_challenge.ui.base.BaseView
import br.com.sf.osm_challenge.ui.base.BaseViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

interface MapsContract {

    interface Navigator : BaseNavigator {

        fun onBackPress()

        fun goToPlaceDetail(placeId: Int, osmType: String)

    }

    interface View : BaseView<ViewModel> {

        fun setMapPosition(point: GeoPoint, marker: Marker)

        fun showPOIMarkers(pois: List<POI>)

        fun clearMarkers()

        fun showLoadingIndicator(show: Boolean)

    }

    interface ViewModel : BaseViewModel {

        fun findPOI(position: BoundingBox, term: String)

        fun clickOnPlaceDetail(placeId: Int, osmType: String)

    }

}
