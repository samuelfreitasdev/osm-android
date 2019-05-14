package br.com.sf.osm_challenge.injection.app

import br.com.sf.osm_challenge.injection.PerActivity
import br.com.sf.osm_challenge.ui.main.MainActivity
import br.com.sf.osm_challenge.ui.main.MainActivityModule
import br.com.sf.osm_challenge.ui.main.MainActivityProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class, MainActivityProvider::class])
    internal abstract fun bindMainActivity(): MainActivity

}
