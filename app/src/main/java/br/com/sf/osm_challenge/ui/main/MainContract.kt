package br.com.sf.osm_challenge.ui.main

import br.com.sf.osm_challenge.ui.base.BaseNavigator
import br.com.sf.osm_challenge.ui.base.BaseView
import br.com.sf.osm_challenge.ui.base.BaseViewModel

interface MainContract {

    interface Navigator : BaseNavigator {

        enum class State {
            SINGLE_COLUMN_MASTER, SINGLE_COLUMN_DETAILS, TWO_COLUMNS_EMPTY, TWO_COLUMNS_WITH_DETAILS
        }

        fun goToHome()

        fun goToPlaceDetail(placeOsmId: Int, osmType: String)

        fun goToAbout()

        fun onBackPressed(): Boolean

    }

    interface View : BaseView<ViewModel> {


        fun highlightHome()

        fun highlightAbout()

        fun closeDrawer()

        fun openDrawer()

        fun toggleDrawer()

    }

    interface ViewModel : BaseViewModel {

        fun clickHome()

        fun clickAbout()

    }

}
