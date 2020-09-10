/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.microsoft.fluentui.tablayout

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.microsoft.fluentui.R
import com.microsoft.fluentui.theming.FluentUIContextThemeWrapper
import com.microsoft.fluentui.view.TemplateView

/**
 * [TabLayout] is used for loading a Template of fluentUI TabLayout.
 * The template uses already existing Android Design Support Library [TabLayout].*/
class TabLayout : TemplateView {
    enum class TabType {
        STANDARD, SWITCH, PILLS
    }

    /*This [tabLayout] stores the Android Design Support Library [TabLayout] attached to the given template.*/
    var tabLayout: TabLayout? = null
        private set

    /*This [tabType] stores the type of TabLayout. It supports [TabType.STANDARD], [TabType.SWITCH], [TabType.PILLS]*/
    var tabType: TabType? = null
        set(value) {
            if (field == value)
                return
            field = value
            updateTemplate()
        }

    private var tabLayoutContainer: ViewGroup? = null

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(FluentUIContextThemeWrapper(context), attrs, defStyleAttr) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TabLayout)
        val tabTypeOrdinal = styledAttributes.getInt(R.styleable.TabLayout_tabType, TabType.STANDARD.ordinal)
        tabType = TabType.values()[tabTypeOrdinal]
        styledAttributes.recycle()
    }

    override val templateId: Int
        get() = R.layout.view_tab_layout

    override fun onTemplateLoaded() {
        super.onTemplateLoaded()
        tabLayoutContainer = findViewInTemplateById(R.id.tab_layout_container)
        tabLayout = findViewInTemplateById(R.id.tab_layout)
    }

    /**
     * This function updates the given template on the basics of [tabType].*/
    fun updateTemplate() {
        val tabLayout = tabLayout ?: return
        var paddingHorizontal = resources.getDimension(R.dimen.fluentui_tab_padding_horizontal).toInt()
        val paddingVertical = resources.getDimension(R.dimen.fluentui_tab_padding_vertical).toInt()
        when (tabType) {
            TabType.STANDARD -> {
                tabLayout.tabMode = TabLayout.MODE_FIXED
                tabLayout.layoutParams.width = LayoutParams.MATCH_PARENT
                tabLayout.setBackgroundResource(R.drawable.tab_layout_background)
            }
            TabType.SWITCH -> {
                tabLayout.tabMode = TabLayout.MODE_FIXED
                tabLayout.layoutParams.width = LayoutParams.WRAP_CONTENT
                tabLayout.setBackgroundResource(R.drawable.tab_layout_background)
            }
            TabType.PILLS -> {
                tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
                tabLayout.layoutParams.width = LayoutParams.MATCH_PARENT
                tabLayout.setBackgroundResource(0)
                updateMargin()
                paddingHorizontal = 0
            }
        }
        tabLayoutContainer?.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
    }

    override fun onAttachedToWindow() {
        updateTemplate()
        super.onAttachedToWindow()
    }

    /**
     * Updates the right margin for the tabs in [tabLayout]. Used for [TabType.PILLS]
     * */
    private fun updateMargin() {
        val tabLayout = tabLayout ?: return
        val viewGroup = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabLayout.tabCount - 1) {
            val tab: View = viewGroup.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.rightMargin = resources.getDimension(R.dimen.fluentui_tab_margin).toInt()
            tab.layoutParams = layoutParams
        }
        tabLayout.requestLayout()
    }
}