package br.com.sf.osm_challenge.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.injection.PerFragment
import br.com.sf.osm_challenge.ui.base.BaseFragment
import br.com.sf.osm_challenge.ui.main.MainActivity


@PerFragment
class AboutFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAppBar()
    }

    private fun setupAppBar() {
        (activity as MainActivity)
            .customAppBar
            .apply {
                setTitle(getString(R.string.drawer_about))
                clearMenu()
                setMenuRes(R.menu.menu_empty, R.menu.menu_empty, R.menu.menu_empty)
            }
    }

    companion object {
        fun newInstance(): Fragment {
            return AboutFragment()
        }
    }
}
