package br.com.sf.osm_challenge.ui.maps

import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.repository.BoundingBox
import br.com.sf.osm_challenge.repository.POI
import br.com.sf.osm_challenge.repository.Point
import br.com.sf.osm_challenge.repository.osm.OsmRepository
import br.com.sf.osm_challenge.util.schedulers.BaseSchedulerProvider
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

@PerFragment
class MapsViewModel @Inject constructor(
    private var view: MapsContract.View,
    private var navigator: MapsContract.Navigator,
    private val osmRepository: OsmRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : MapsContract.ViewModel {

    private val compositeDisposable = CompositeDisposable()

    override fun findPOI(position: BoundingBox, term: String) {

        view.showLoadingIndicator(true)
        view.clearMarkers()

        val closestPOIs = osmRepository.findPOICloseTo(term, position)
        val reversePOI = findPOISReverse(term)

        val disposable = Flowable.merge(closestPOIs, reversePOI)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { view.showPOIMarkers(it); view.showLoadingIndicator(false) },
                { Timber.e(it); view.showLoadingIndicator(false) })

        compositeDisposable.add(disposable)
    }

    private fun findPOISReverse(term: String): Flowable<List<POI>> {
        getLatLon(term)?.run {
            return osmRepository.findReverse(this)
        }
        return Flowable.empty()
    }

    private fun getLatLon(term: String): Point? {
        val pattern = Pattern.compile("(-?\\d+(\\.\\d+)?),(\\s*(-?\\d+(\\.\\d+)?))")
        val matcher = pattern.matcher(term)

        if (matcher.find()) {
            val lat = matcher.group(1).orEmpty()
            val lon = matcher.group(3).orEmpty()

            return Point(lat.toDouble(), lon.toDouble())
        }
        return null
    }

    override fun clickOnPlaceDetail(placeId: Int, osmType: String) {
        navigator.goToPlaceDetail(placeId, osmType)
    }

    override fun destroy() {
        this.compositeDisposable.clear()
    }
}