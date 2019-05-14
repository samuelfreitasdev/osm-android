package br.com.sf.osm_challenge.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.ui.base.BaseActivity
import br.com.sf.osm_challenge.ui.widgets.ContainersLayout
import br.com.sf.osm_challenge.ui.widgets.CustomAppBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    internal lateinit var presenter: MainContract.ViewModel

    @Inject
    lateinit var navigator: MainContract.Navigator

    @BindView(R.id.activity_main__nav)
    internal lateinit var navigationView: NavigationView

    @Nullable
    @JvmField
    @BindView(R.id.activity_main__nav_side)
    internal var navigationSideView: NavigationView? = null

    @Nullable
    @JvmField
    @BindView(R.id.activity_main__insets)
    internal var insetsView: ViewGroup? = null

    @BindView(R.id.activity_main__drawer)
    internal lateinit var drawer: DrawerLayout

    @BindView(R.id.activity_main__custom_appbar)
    lateinit var customAppBar: CustomAppBar

    @BindView(R.id.activity_main__containers_layout)
    lateinit var containersLayout: ContainersLayout

    private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        unbinder = ButterKnife.bind(this)

        if (insetsView != null && navigationSideView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(insetsView!!) { v, insets ->
                (insetsView!!.layoutParams as ViewGroup.MarginLayoutParams).topMargin = insets.systemWindowInsetTop
                (insetsView!!.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
                    insets.systemWindowInsetBottom
                insetsView!!.requestLayout()
                (navigationSideView!!.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
                    -insets.systemWindowInsetTop
                navigationSideView!!.requestLayout()
                insets.consumeSystemWindowInsets()
            }
            navigationSideView!!.setNavigationItemSelectedListener(this)
        }

        navigationView.setNavigationItemSelectedListener(this)
        customAppBar.setOnNavigationClickListener(View.OnClickListener { v -> toggleDrawer() })

//        if (savedInstanceState == null) {
        this.presenter.clickHome()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_home -> presenter.clickHome()
            R.id.drawer_about -> presenter.clickAbout()
        }
        drawer.closeDrawers()
        return true
    }

    override fun highlightHome() {
        navigationView.setCheckedItem(R.id.drawer_home)
        if (navigationSideView != null) {
            navigationSideView!!.setCheckedItem(R.id.drawer_home)
        }
    }

    override fun highlightAbout() {
        navigationView.setCheckedItem(R.id.drawer_about)
        if (navigationSideView != null) {
            navigationSideView!!.setCheckedItem(R.id.drawer_about)
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (!navigator.onBackPressed()) {
                finish()
            }
        }
    }

    override fun closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.postDelayed({ drawer.closeDrawer(GravityCompat.START) }, 100)
        }
    }

    override fun openDrawer() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    override fun toggleDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START)
        }
    }

}
