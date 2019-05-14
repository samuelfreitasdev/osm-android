package br.com.sf.osm_challenge.ui.maps

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.repository.POI
import br.com.sf.osm_challenge.repository.Point
import br.com.sf.osm_challenge.ui.base.DaggerFragment
import br.com.sf.osm_challenge.ui.main.MainActivity
import br.com.sf.osm_challenge.util.hideKeyboard
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import permissions.dispatcher.*
import javax.inject.Inject


@RuntimePermissions
@PerFragment
class MapsFragment : DaggerFragment(), MapsContract.View, Marker.OnMarkerClickListener {

    @BindView(R.id.map)
    lateinit var mapView: MapView

    @BindView(R.id.etSearch)
    lateinit var etSearch: EditText

    @BindView(R.id.pbLoadingIndicator)
    lateinit var pbLoadingIndicator: ProgressBar

    @Inject
    internal lateinit var viewModel: MapsContract.ViewModel

    private var poiList: List<POI> = ArrayList()

    private var unbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(br.com.sf.osm_challenge.R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unbinder = ButterKnife.bind(this, view)

        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        mapView.setMultiTouchControls(true)
        mapView.setHasTransientState(true)
        mapView.controller.setZoom(9.5)

        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            val position = getCurrentLocation()

            val boundingBox = br.com.sf.osm_challenge.repository.BoundingBox(
                Point(mapView.boundingBox.latNorth, mapView.boundingBox.lonWest),
                Point(mapView.boundingBox.latSouth, mapView.boundingBox.lonEast)
            )

            if (actionId == EditorInfo.IME_ACTION_GO) {
                viewModel.findPOI(boundingBox, etSearch.text.toString())
            }
            return@setOnEditorActionListener false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupMapInitialPositionWithPermissionCheck()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
    }

    override fun onDestroy() {
        viewModel.destroy()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun setupMapInitialPosition() {
        getCurrentLocation().run {
            val position = GeoPoint(this.latitude, this.longitude)
            setMapPosition(position, Marker(mapView))
        }
    }

    @OnShowRationale(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun showRationaleForCamera(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_location_rationale, request)
    }

    @OnPermissionDenied(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun onCameraDenied() {
        Toast.makeText(context, R.string.permission_location_denied, Toast.LENGTH_SHORT).show()
    }

    @OnNeverAskAgain(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    fun onCameraNeverAskAgain() {
        Toast.makeText(context, R.string.permission_location_never_askagain, Toast.LENGTH_SHORT).show()
    }

    @OnClick(R.id.imDrawerButton)
    fun toggleDrawer() {
        (activity as MainActivity).toggleDrawer()
        (activity as MainActivity).hideKeyboard()
    }

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(activity!!)
            .setPositiveButton(
                R.string.permission_button_allow
            ) { _, _ -> request.proceed() }
            .setNegativeButton(
                R.string.permission_button_deny
            ) { _, _ -> request.cancel() }
            .setCancelable(false)
            .setMessage(messageResId)
            .show()
    }

    override fun setMapPosition(point: GeoPoint, marker: Marker) {
        mapView.controller.setCenter(point)

        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        mapView.overlays.add(marker)
    }

    override fun showPOIMarkers(pois: List<POI>) {
        if (!pois.isEmpty()) {

            this.poiList = pois

            pois.first().run {
                mapView.controller.zoomTo(16)
                mapView.controller.setCenter(GeoPoint(this.lat, this.lon))
            }

            pois.map { mapPoiToMarker(it) }
                .run {
                    mapView.overlays.addAll(this)
                }

            mapView.invalidate()
        } else {
            Toast.makeText(context, R.string.maps_empty_message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun mapPoiToMarker(poi: POI): Marker {

        val marker = Marker(mapView)

        marker.position = GeoPoint(poi.lat, poi.lon)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_location_on_red_900_48dp, activity!!.theme)
        marker.title = poi.displayName.split(",")[0]
        marker.id = poi.osmId
        marker.subDescription = poi.displayName

        marker.setOnMarkerClickListener(this)

        return marker
    }

    override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {
        val poi = this.poiList.find { it.osmId == marker!!.id }

        viewModel.clickOnPlaceDetail(
            marker!!.id.toInt(),
            poi!!.osmType
        )
        return true
    }

    override fun showLoadingIndicator(show: Boolean) {
        if (show) {
            pbLoadingIndicator.visibility = View.VISIBLE
        } else {
            pbLoadingIndicator.visibility = View.GONE
        }
    }

    override fun clearMarkers() {
        mapView.overlays.clear()
        setupMapInitialPosition()
        mapView.invalidate()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(): Location {
        val locationManager = getSystemService(context!!, LocationManager::class.java)

        val gpsLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (gpsLocation != null) {
            return gpsLocation
        }

        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!
    }

    companion object {
        fun newInstance(): MapsFragment {
            return MapsFragment()
        }
    }
}
