package br.com.sf.osm_challenge.ui.place_detail

import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.repository.osm.OsmRepository
import br.com.sf.osm_challenge.util.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@PerFragment
class PlaceDetailViewModel @Inject constructor(
    private var view: PlaceDetailContract.View,
    private var navigator: PlaceDetailContract.Navigator,
    private val osmRepository: OsmRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : PlaceDetailContract.ViewModel {

    private val compositeDisposable = CompositeDisposable()

    override fun loadPlace(placeId: Int, osmType: String) {

        val disposable = osmRepository.findDetails(placeId, osmType)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { view.showPlaceDetails(it) },
                { Timber.e(it) }
            )

        compositeDisposable.add(disposable)
    }

    override fun onBackPress() {
        navigator.onBackPress()
    }

    override fun destroy() {
        this.compositeDisposable.clear()
    }
}