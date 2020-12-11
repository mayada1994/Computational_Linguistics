package com.mayada1994.ngram.widgets

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.mayada1994.ngram.R

class AutoCompleteDropDown : AppCompatAutoCompleteTextView {

    private var isInputValid = true
    var firstFocus = true
    private var mInitialText = ""
    private var isPopup = false
    var position = ListView.INVALID_POSITION

    constructor(context: Context) : super(context)
    constructor(arg0: Context, arg1: AttributeSet?) : super(arg0, arg1)
    constructor(arg0: Context, arg1: AttributeSet?, arg2: Int) : super(arg0, arg1, arg2)

    fun isInputValid(): Boolean {
        return isInputValid
    }

    fun setInputValid(inputValid: Boolean) {
        isInputValid = inputValid
    }

    var initialText: String
        get() = mInitialText
        set(mInitialText) {
            this.mInitialText = mInitialText
            setText(mInitialText)
        }

    override fun enoughToFilter(): Boolean {
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int,
                                previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            performFiltering("", 0)
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
            keyListener = null
            dismissDropDown()
        } else {
            isPopup = false
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (isPopup) {
                    dismissDropDown()
                } else {
                    requestFocus()
                    showDropDown()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun showDropDown() {
        super.showDropDown()
        isPopup = true
    }

    override fun dismissDropDown() {
        super.dismissDropDown()
        isPopup = false
    }

    override fun setCompoundDrawablesWithIntrinsicBounds(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        var right: Drawable? = right
        val dropdownIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_drop_down)
        if (dropdownIcon != null) {
            right = dropdownIcon
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
    }

}