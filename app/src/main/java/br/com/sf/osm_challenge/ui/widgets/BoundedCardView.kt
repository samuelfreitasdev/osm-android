package br.com.sf.osm_challenge.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import br.com.sf.osm_challenge.R

class BoundedCardView : CardView {

    private val boundedWidth: Int
    private val boundedHeight: Int

    constructor(context: Context) : super(context) {
        boundedWidth = 0
        boundedHeight = 0
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        @SuppressLint("CustomViewStyleable") val a = context.obtainStyledAttributes(attrs, R.styleable.BoundedView)
        boundedWidth = a.getDimensionPixelSize(R.styleable.BoundedView_bounded_width, 0)
        boundedHeight = a.getDimensionPixelSize(R.styleable.BoundedView_bounded_height, 0)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        // Adjust width as necessary
        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        if (boundedWidth in 1..(measuredWidth - 1)) {
            val measureMode = View.MeasureSpec.getMode(widthMeasureSpec)
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(boundedWidth, measureMode)
        }
        // Adjust height as necessary
        val measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec)
        if (boundedHeight in 1..(measuredHeight - 1)) {
            val measureMode = View.MeasureSpec.getMode(heightMeasureSpec)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(boundedHeight, measureMode)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}
