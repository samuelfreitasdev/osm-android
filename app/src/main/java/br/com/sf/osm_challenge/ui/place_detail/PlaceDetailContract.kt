package br.com.sf.osm_challenge.ui.place_detail

import br.com.sf.osm_challenge.repository.PlaceDetail
import br.com.sf.osm_challenge.ui.base.BaseNavigator
import br.com.sf.osm_challenge.ui.base.BaseView
import br.com.sf.osm_challenge.ui.base.BaseViewModel

interface PlaceDetailContract {

    interface Navigator : BaseNavigator {

        fun onBackPress()

    }

    interface View : BaseView<ViewModel> {

        fun showPlaceDetails(place: PlaceDetail)

    }

    interface ViewModel : BaseViewModel {

        fun loadPlace(placeId: Int, osmType: String)

        fun onBackPress()

    }

}
