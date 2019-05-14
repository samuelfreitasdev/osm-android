package br.com.sf.osm_challenge.ui.main

import br.com.sf.osm_challenge.injection.PerActivity
import javax.inject.Inject

@PerActivity
internal class MainViewModel @Inject
constructor(private var view: MainContract.View, private var navigator: MainContract.Navigator) :
    MainContract.ViewModel {

    override fun clickHome() {
        view.highlightHome()
        navigator.goToHome()
    }

    override fun clickAbout() {
        view.highlightAbout()
        navigator.goToAbout()
    }

    override fun destroy() {
    }
}
