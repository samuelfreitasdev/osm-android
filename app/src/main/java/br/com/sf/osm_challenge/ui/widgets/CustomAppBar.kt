package br.com.sf.osm_challenge.ui.widgets

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.ui.main.MainContract
import com.google.android.material.appbar.AppBarLayout

class CustomAppBar : AppBarLayout {

    private var toolbarGeneral: Toolbar? = null

    private var toolbarSpecific: Toolbar? = null

    private var space: View? = null

    private var state: MainContract.Navigator.State? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.view_main_toolbar, this, true)

        toolbarGeneral = findViewById(R.id.view_main_toolbar__toolbar_general)
        toolbarSpecific = findViewById(R.id.view_main_toolbar__toolbar_specific)
        space = findViewById(R.id.view_main_toolbar__space_toolbar)

        if (toolbarGeneral != null) {
            toolbarGeneral!!.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        } else {
            toolbarSpecific!!.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        }
    }

    fun hasGeneralToolbar(): Boolean {
        return toolbarGeneral != null
    }

    fun setOnNavigationClickListener(onNavigationClickListener: View.OnClickListener) {
        if (toolbarGeneral != null) {
            toolbarGeneral!!.setNavigationOnClickListener(onNavigationClickListener)
        } else {
            toolbarSpecific!!.setNavigationOnClickListener(onNavigationClickListener)
        }
    }

    fun setNavigationItem(resId: Int) {
        if (toolbarGeneral != null) {
            toolbarGeneral!!.setNavigationIcon(resId)
        } else {
            toolbarSpecific!!.setNavigationIcon(resId)
        }
    }

    fun setOnItemClickListener(listener: Toolbar.OnMenuItemClickListener) {
        if (toolbarGeneral != null) {
            toolbarGeneral!!.setOnMenuItemClickListener(listener)
        }
        if (toolbarSpecific != null) {
            toolbarSpecific!!.setOnMenuItemClickListener(listener)
        }
    }

    fun setTitle(title: String) {
        toolbarSpecific!!.title = title
    }

    fun clearMenu() {
        toolbarSpecific!!.menu.clear()
        if (toolbarGeneral != null) {
            toolbarGeneral!!.menu.clear()
        }
    }

    fun setMenuGeneralRes(@MenuRes menuGeneral: Int) {
        if (toolbarGeneral != null) {
            toolbarGeneral!!.menu.clear()
            toolbarGeneral!!.inflateMenu(menuGeneral)
        }
    }

    fun setMenuRes(@MenuRes menuGeneral: Int, @MenuRes menuSpecific: Int, @MenuRes menuMerged: Int) {
        toolbarSpecific!!.menu.clear()
        if (toolbarGeneral != null) {
            toolbarGeneral!!.menu.clear()
            toolbarGeneral!!.inflateMenu(menuGeneral)
            toolbarSpecific!!.inflateMenu(menuSpecific)
        } else {
            toolbarSpecific!!.inflateMenu(menuMerged)
        }
    }

    fun setState(state: MainContract.Navigator.State) {
        this.state = state
        when (state) {
            MainContract.Navigator.State.SINGLE_COLUMN_MASTER, MainContract.Navigator.State.SINGLE_COLUMN_DETAILS -> if (space != null) {
                space!!.visibility = View.GONE
            }
            MainContract.Navigator.State.TWO_COLUMNS_EMPTY, MainContract.Navigator.State.TWO_COLUMNS_WITH_DETAILS -> if (space != null) {
                space!!.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState())
        bundle.putString(STATE_TITLE, toolbarSpecific!!.title.toString())
        bundle.putString(STATE_TOOLBAR_STATE, state!!.name)
        return bundle
    }

    public override fun onRestoreInstanceState(parcelable: Parcelable?) {
        var parcelable = parcelable
        if (parcelable is Bundle) {
            val bundle = parcelable as Bundle?
            toolbarSpecific!!.title = bundle!!.getString(STATE_TITLE)
            setState(MainContract.Navigator.State.valueOf(bundle.getString(STATE_TOOLBAR_STATE)))
            parcelable = bundle.getParcelable(STATE_SUPER)
        }
        super.onRestoreInstanceState(parcelable)
    }

    companion object {
        private const val STATE_SUPER = "state_super"
        private const val STATE_TITLE = "state_title"
        private const val STATE_TOOLBAR_STATE = "state_toolbar_state"
    }
}
