package br.com.sf.osm_challenge.ui.widgets

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofFloat
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import br.com.sf.osm_challenge.R
import br.com.sf.osm_challenge.ui.main.MainContract
import br.com.sf.osm_challenge.ui.util.ViewUtils

class ContainersLayout : FrameLayout {

    private var spaceMaster: View? = null

    private var spaceDetails: View? = null

    internal lateinit var frameMaster: ViewGroup

    internal lateinit var frameDetails: ViewGroup

    var state: MainContract.Navigator.State? = null
        set(state) {
            field = state
            when (state) {
                MainContract.Navigator.State.SINGLE_COLUMN_MASTER -> singleColumnMaster()
                MainContract.Navigator.State.SINGLE_COLUMN_DETAILS -> singleColumnDetails()
                MainContract.Navigator.State.TWO_COLUMNS_EMPTY -> twoColumnsEmpty()
                MainContract.Navigator.State.TWO_COLUMNS_WITH_DETAILS -> twoColumnsWithDetails()
            }
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_main_containers, this, true)

        spaceMaster = findViewById(R.id.activity_main__space_master)
        spaceDetails = findViewById(R.id.activity_main__space_details)
        frameMaster = findViewById(R.id.activity_main__frame_master)
        frameDetails = findViewById(R.id.activity_main__frame_details)
    }

    fun hasTwoColumns(): Boolean {
        return spaceMaster != null && spaceDetails != null
    }

    private fun singleColumnMaster() {
        if (hasTwoColumns()) {
            spaceMaster!!.visibility = View.GONE
            spaceDetails!!.visibility = View.GONE
            frameDetails.visibility = View.GONE
        } else {
            animateOutFrameDetails()
        }
        frameMaster.visibility = View.VISIBLE
    }

    private fun singleColumnDetails() {
        if (hasTwoColumns()) {
            spaceMaster!!.visibility = View.GONE
            spaceDetails!!.visibility = View.GONE
        }
        frameMaster.visibility = View.GONE
        frameDetails.visibility = View.VISIBLE
    }

    private fun twoColumnsEmpty() {
        if (hasTwoColumns()) {
            spaceMaster!!.visibility = View.VISIBLE
            spaceDetails!!.visibility = View.VISIBLE
            frameDetails.visibility = View.VISIBLE
        } else {
            animateOutFrameDetails()
        }
        frameMaster.visibility = View.VISIBLE
    }

    private fun twoColumnsWithDetails() {
        if (hasTwoColumns()) {
            spaceMaster!!.visibility = View.VISIBLE
            spaceDetails!!.visibility = View.VISIBLE
            frameMaster.visibility = View.VISIBLE
            frameDetails.visibility = View.VISIBLE
        } else {
            animateInFrameDetails()
        }
    }

    private fun animateInFrameDetails() {
        frameDetails.visibility = View.VISIBLE
        ViewUtils.onLaidOut(frameDetails) {
            val alpha = ObjectAnimator.ofFloat(frameDetails, View.ALPHA, 0.4f, 1f)
            val translate = ofFloat(frameDetails, View.TRANSLATION_Y, frameDetails.height * 0.3f, 0f)

            val set = AnimatorSet()
            set.playTogether(alpha, translate)
            set.duration = ANIM_DURATION.toLong()
            set.interpolator = LinearOutSlowInInterpolator()
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    frameMaster.visibility = View.GONE
                }
            })
            set.start()
        }
    }

    private fun animateOutFrameDetails() {
        ViewUtils.onLaidOut(frameDetails) {
            if (frameDetails.isShown) {
                val alpha = ObjectAnimator.ofFloat(frameDetails, View.ALPHA, 1f, 0f)
                val translate = ofFloat(frameDetails, View.TRANSLATION_Y, 0f, frameDetails.height * 0.3f)

                val set = AnimatorSet()
                set.playTogether(alpha, translate)
                set.duration = ANIM_DURATION.toLong()
                set.interpolator = FastOutLinearInInterpolator()
                set.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        frameDetails.alpha = 1f
                        frameDetails.translationY = 0f
                        frameDetails.visibility = View.GONE
                    }
                })
                set.start()
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(STATE_SUPER, super.onSaveInstanceState())
        bundle.putString(STATE_CONTAINERS_STATE, this.state!!.name)
        return bundle
    }

    public override fun onRestoreInstanceState(parcelable: Parcelable?) {
        var parcelable = parcelable
        if (parcelable is Bundle) {
            val bundle = parcelable as Bundle?
            state = MainContract.Navigator.State.valueOf(bundle!!.getString(STATE_CONTAINERS_STATE))
            parcelable = bundle.getParcelable(STATE_SUPER)
        }
        super.onRestoreInstanceState(parcelable)
    }

    companion object {

        const val ANIM_DURATION = 250

        private const val STATE_SUPER = "state_super"
        private const val STATE_CONTAINERS_STATE = "state_containers_state"
    }
}
