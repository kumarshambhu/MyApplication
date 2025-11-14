package com.shambhu.myapplication.utils


import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.shambhu.myapplication.R

class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var textView: TextView
    private lateinit var toggleButton: ImageView
    private var isExpanded = false
    private var collapsedMaxLines = 3
    private var animationDuration = 300L
    private var showToggleIfNotNeeded = false
    private var originalText: CharSequence? = null

    private var onExpandListener: ((Boolean) -> Unit)? = null

    init {
        initView()
        initAttributes(attrs)
    }

    private fun initView() {
        orientation = VERTICAL

        // Create text view
        textView = TextView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }

        // Create toggle button
        toggleButton = ImageView(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = resources.getDimensionPixelSize(R.dimen.toggle_button_margin)
            }
            setImageResource(R.drawable.ic_more_horiz)
            setOnClickListener { toggle() }
        }

        addView(textView)
        addView(toggleButton)
    }

    private fun initAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.ExpandableTextView,
                0,
                0
            )

            try {
                // Text attributes
                originalText = typedArray.getText(R.styleable.ExpandableTextView_android_text)
                textView.text = originalText

                val textColor = typedArray.getColor(
                    R.styleable.ExpandableTextView_android_textColor,
                    ContextCompat.getColor(context, android.R.color.black)
                )
                textView.setTextColor(textColor)

                val textSize = typedArray.getDimension(
                    R.styleable.ExpandableTextView_android_textSize,
                    textView.textSize
                )
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

                val textStyle = typedArray.getInt(
                    R.styleable.ExpandableTextView_android_textStyle,
                    0
                )
                textView.setTypeface(textView.typeface, textStyle)

                // ExpandableTextView specific attributes
                collapsedMaxLines = typedArray.getInt(
                    R.styleable.ExpandableTextView_collapsedMaxLines,
                    3
                )

                animationDuration = typedArray.getInt(
                    R.styleable.ExpandableTextView_animationDuration,
                    300
                ).toLong()

                showToggleIfNotNeeded = typedArray.getBoolean(
                    R.styleable.ExpandableTextView_showToggleIfNotNeeded,
                    false
                )

                // Set initial state
                textView.maxLines = collapsedMaxLines
                textView.ellipsize = TextUtils.TruncateAt.END

            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        post {
            checkTextLength()
        }
    }

    fun setText(text: CharSequence?) {
        originalText = text
        textView.text = text
        isExpanded = false
        applyToggleState()
        checkTextLength()
    }

    fun setText(@StringRes textRes: Int) {
        setText(context.getString(textRes))
    }

    fun setCollapsedMaxLines(maxLines: Int) {
        collapsedMaxLines = maxLines
        if (!isExpanded) {
            textView.maxLines = maxLines
        }
        checkTextLength()
    }

    fun expand() {
        if (!isExpanded) {
            toggle()
        }
    }

    fun collapse() {
        if (isExpanded) {
            toggle()
        }
    }

    fun isExpanded(): Boolean = isExpanded

    fun setOnExpandListener(listener: (Boolean) -> Unit) {
        this.onExpandListener = listener
    }

    fun toggle() {
        isExpanded = !isExpanded

        if (animationDuration > 0) {
            animateToggle()
        } else {
            applyToggleState()
        }

        onExpandListener?.invoke(isExpanded)
    }

    private fun animateToggle() {
        val startHeight = textView.measuredHeight
        textView.maxLines = if (isExpanded) Integer.MAX_VALUE else collapsedMaxLines
        textView.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        val endHeight = textView.measuredHeight

        val animator = ValueAnimator.ofInt(startHeight, endHeight)
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            textView.layoutParams.height = value
            textView.requestLayout()
        }

        animator.duration = animationDuration
        animator.start()

        textView.postDelayed({
            applyToggleState()
            textView.layoutParams.height = LayoutParams.WRAP_CONTENT
        }, animationDuration)
    }

    private fun applyToggleState() {
        if (isExpanded) {
            textView.maxLines = Integer.MAX_VALUE
            textView.ellipsize = null
        } else {
            textView.maxLines = collapsedMaxLines
            textView.ellipsize = TextUtils.TruncateAt.END
        }
    }

    private fun checkTextLength() {
        val lineCount = textView.lineCount
        val needsToggle = lineCount > collapsedMaxLines ||
                (textView.layout?.getEllipsisCount(collapsedMaxLines - 1) ?: 0) > 0

        toggleButton.visibility = when {
            needsToggle -> View.VISIBLE
            showToggleIfNotNeeded -> View.VISIBLE
            else -> View.GONE
        }
    }
}
