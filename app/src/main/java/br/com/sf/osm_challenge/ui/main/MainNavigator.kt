package br.com.sf.osm_challenge.ui.main

import androidx.fragment.app.FragmentTransaction
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.injection.PerActivity
import br.com.sf.osm_challenge.ui.about.AboutFragment
import br.com.sf.osm_challenge.ui.maps.MapsFragment
import br.com.sf.osm_challenge.ui.place_detail.PlaceDetailFragment
import javax.inject.Inject

@PerActivity
class MainNavigator @Inject
internal constructor(private val mainActivity: MainActivity) : MainContract.Navigator {

    private val TAG_DETAILS = "tag_details"
    private val TAG_MASTER = "tag_master"

    override fun goToHome() {
        clearMaster()

        mainActivity
            .customAppBar
            .setState(MainContract.Navigator.State.SINGLE_COLUMN_DETAILS)
        mainActivity
            .containersLayout
            .state = MainContract.Navigator.State.SINGLE_COLUMN_DETAILS

        val fragment = MapsFragment.newInstance()

        mainActivity
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main__frame_details, fragment, TAG_DETAILS)
            .commitNow()
    }

    override fun goToPlaceDetail(placeOsmId: Int, osmType: String) {
        clearDetails()

        mainActivity
            .customAppBar
            .setState(MainContract.Navigator.State.SINGLE_COLUMN_DETAILS)
        mainActivity
            .containersLayout
            .state = MainContract.Navigator.State.SINGLE_COLUMN_DETAILS

        val fragment = PlaceDetailFragment.newInstance(placeOsmId, osmType)

        mainActivity
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main__frame_details, fragment, TAG_DETAILS)
            .commitNow()
    }

    override fun goToAbout() {
        clearDetails()
        mainActivity
            .customAppBar
            .setState(MainContract.Navigator.State.SINGLE_COLUMN_MASTER)
        mainActivity
            .containersLayout
            .state = MainContract.Navigator.State.SINGLE_COLUMN_MASTER

        val fragment = AboutFragment.newInstance()

        mainActivity
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_main__frame_master, fragment, TAG_MASTER)
            .commitNow()
    }

    private fun clearDetails(): Boolean {
        val details = mainActivity.supportFragmentManager.findFragmentByTag(TAG_DETAILS)
        if (details != null) {
            mainActivity.supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(details)
                .commitNow()
            return true
        }
        return false
    }

    private fun clearMaster() {
        val master = mainActivity.supportFragmentManager.findFragmentByTag(TAG_MASTER)
        if (master != null) {
            mainActivity.supportFragmentManager.beginTransaction().remove(master).commitNow()
        }
    }

    override fun onBackPressed(): Boolean {
        val state = mainActivity.containersLayout.state
        if (state == MainContract.Navigator.State.TWO_COLUMNS_WITH_DETAILS && !mainActivity.containersLayout.hasTwoColumns()) {
            if (clearDetails()) {
                mainActivity.containersLayout.state = MainContract.Navigator.State.TWO_COLUMNS_EMPTY
                return true
            }
        }
        return false
    }
}
