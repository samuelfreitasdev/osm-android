package br.com.sf.osm_challenge.ui.place_detail

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.repository.PlaceDetail
import br.com.sf.osm_challenge.ui.base.DaggerFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.IconOverlay
import javax.inject.Inject


@PerFragment
class PlaceDetailFragment : DaggerFragment(), PlaceDetailContract.View {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.appBarLayout)
    internal lateinit var appBarLayout: AppBarLayout

    @BindView(R.id.collapsingToolbar)
    internal lateinit var collapsingToolbar: CollapsingToolbarLayout

    @BindView(R.id.map)
    lateinit var mapView: MapView

    @BindView(R.id.tvTitle)
    lateinit var tvTitle: TextView

    @BindView(R.id.tvPosition)
    lateinit var tvPosition: TextView

    @BindView(R.id.llAddressContainer)
    lateinit var llAddressContainer: LinearLayout

    @Inject
    internal lateinit var viewModel: PlaceDetailContract.ViewModel

    private var unbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(br.com.sf.osm_challenge.R.layout.fragment_place_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)

        toolbar.setNavigationOnClickListener { viewModel.onBackPress() }

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapsingToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsingToolbar)) {
                toolbar.navigationIcon?.setColorFilter(
                    ResourcesCompat.getColor(resources, R.color.material_white, activity!!.theme),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                toolbar.navigationIcon?.setColorFilter(
                    ResourcesCompat.getColor(resources, R.color.material_black, activity!!.theme),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val placeId = arguments!!.getInt(PARAM1, 0)
        val osmType = arguments!!.getString(PARAM2, "")
        viewModel.loadPlace(placeId, osmType)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    override fun onDestroy() {
        viewModel.destroy()
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    override fun showPlaceDetails(place: PlaceDetail) {

        val center = GeoPoint(
            place.center.coordinates[1],
            place.center.coordinates[0]
        )
        mapView.controller.animateTo(center, 17.5, 40)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.setMultiTouchControls(true)
        mapView.setHasTransientState(true)
        mapView.overlays.add(
            IconOverlay(
                center,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_location_on_red_900_48dp, activity!!.theme)
            )
        )

        tvTitle.text = place.localName
        tvPosition.text = place.center.coordinates[0].toString() + ", " + place.center.coordinates[1]

        for (address in place.address) {
            val view = layoutInflater.inflate(R.layout.item_address_line, null)!!
            view.findViewById<TextView>(R.id.tvText).text = address.localName
            view.tag = Math.random().toString()

            llAddressContainer.addView(view)
        }
    }

    companion object {
        private const val PARAM1 = "PARAM1"
        private const val PARAM2 = "PARAM2"

        fun newInstance(placeId: Int, osmType: String): Fragment {

            val arguments = Bundle().apply {
                putInt(PARAM1, placeId)
                putString(PARAM2, osmType)
            }

            return PlaceDetailFragment()
                .apply { this.arguments = arguments }
        }
    }
}
