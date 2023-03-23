package com.microsoft.fluentuidemo.demos

import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.microsoft.fluentui.theme.FluentTheme
import com.microsoft.fluentui.theme.FluentTheme.aliasTokens
import com.microsoft.fluentui.theme.token.AliasTokens
import com.microsoft.fluentui.theme.token.FluentIcon
import com.microsoft.fluentui.theme.token.FluentStyle
import com.microsoft.fluentui.theme.token.controlTokens.ButtonSize
import com.microsoft.fluentui.theme.token.controlTokens.ButtonStyle
import com.microsoft.fluentui.theme.token.controlTokens.SnackbarStyle
import com.microsoft.fluentui.tokenized.controls.Button
import com.microsoft.fluentui.tokenized.controls.ToggleSwitch
import com.microsoft.fluentui.tokenized.listitem.ChevronOrientation
import com.microsoft.fluentui.tokenized.listitem.ListItem
import com.microsoft.fluentui.tokenized.notification.NotificationDuration
import com.microsoft.fluentui.tokenized.notification.NotificationResult
import com.microsoft.fluentui.tokenized.notification.Snackbar
import com.microsoft.fluentui.tokenized.notification.SnackbarQueue
import com.microsoft.fluentui.tokenized.segmentedcontrols.PillBar
import com.microsoft.fluentui.tokenized.segmentedcontrols.PillMetaData
import com.microsoft.fluentuidemo.DemoActivity
import com.microsoft.fluentuidemo.R
import kotlinx.coroutines.launch

class V2SnackbarActivity : DemoActivity() {
    override val contentLayoutId: Int
        get() = R.layout.v2_activity_compose
    override val contentNeedsScrollableContainer: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val composeView = findViewById<ComposeView>(R.id.compose_here)
        val context = this

        // Tags used for testing
        val MODIFIABLE_PARAMETER_SECTION = "Modifiable Parameters"
        val ICON_PARAM = "Icon Param"
        val SUBTITLE_PARAM = "Subtitle Param"
        val ACTION_BUTTON_PARAM = "Action Button Param"
        val DISMISS_BUTTON_PARAM = "Dismiss Button Param"
        val SHOW_SNACKBAR = "Show Snackbar"
        val DISMISS_SNACKBAR = "Dismiss Snackbar"

        composeView.setContent {
            FluentTheme {
                val snackbarQueue = remember { SnackbarQueue() }

                val scope = rememberCoroutineScope()
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var icon: Boolean by rememberSaveable { mutableStateOf(false) }
                    var actionLabel: String? by rememberSaveable { mutableStateOf(null) }
                    var subtitle: String? by rememberSaveable { mutableStateOf(null) }
                    var style: SnackbarStyle by rememberSaveable { mutableStateOf(SnackbarStyle.Neutral) }
                    var duration: NotificationDuration by rememberSaveable {
                        mutableStateOf(
                            NotificationDuration.SHORT
                        )
                    }
                    var dismissEnabled by rememberSaveable { mutableStateOf(false) }

                    ListItem.SectionHeader(
                        title = LocalContext.current.resources.getString(R.string.app_modifiable_parameters),
                        enableChevron = true,
                        enableContentOpenCloseTransition = true,
                        chevronOrientation = ChevronOrientation(90f, 0f),
                        modifier = Modifier.testTag(MODIFIABLE_PARAMETER_SECTION)
                    ) {
                        LazyColumn(Modifier.fillMaxHeight(0.5F)) {
                            item {
                                PillBar(
                                    mutableListOf(
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_indefinite),
                                            onClick = {
                                                duration = NotificationDuration.INDEFINITE
                                            },
                                            selected = duration == NotificationDuration.INDEFINITE
                                        ),
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_long),
                                            onClick = {
                                                duration = NotificationDuration.LONG
                                            },
                                            selected = duration == NotificationDuration.LONG
                                        ),
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_short),
                                            onClick = {
                                                duration = NotificationDuration.SHORT
                                            },
                                            selected = duration == NotificationDuration.SHORT
                                        )
                                    ), style = FluentStyle.Neutral,
                                    showBackground = true
                                )
                            }

                            item {
                                Spacer(
                                    Modifier
                                        .height(8.dp)
                                        .fillMaxWidth()
                                        .background(aliasTokens.neutralBackgroundColor[AliasTokens.NeutralBackgroundColorTokens.Background1].value())
                                )
                            }

                            item {
                                PillBar(
                                    mutableListOf(
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_neutral),
                                            onClick = {
                                                style = SnackbarStyle.Neutral
                                            },
                                            selected = style == SnackbarStyle.Neutral
                                        ),
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_contrast),
                                            onClick = {
                                                style = SnackbarStyle.Contrast
                                            },
                                            selected = style == SnackbarStyle.Contrast
                                        ),
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_accent),
                                            onClick = {
                                                style = SnackbarStyle.Accent
                                            },
                                            selected = style == SnackbarStyle.Accent
                                        ),
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_warning),
                                            onClick = {
                                                style = SnackbarStyle.Warning
                                            },
                                            selected = style == SnackbarStyle.Warning
                                        ),
                                        PillMetaData(
                                            text = LocalContext.current.resources.getString(R.string.fluentui_danger),
                                            onClick = {
                                                style = SnackbarStyle.Danger
                                            },
                                            selected = style == SnackbarStyle.Danger
                                        )
                                    ), style = FluentStyle.Neutral,
                                    showBackground = true
                                )
                            }

                            item {
                                ListItem.Item(
                                    text = "Icon",
                                    subText = if (!icon)
                                        LocalContext.current.resources.getString(R.string.fluentui_disabled)
                                    else
                                        LocalContext.current.resources.getString(R.string.fluentui_enabled),
                                    trailingAccessoryView = {
                                        ToggleSwitch(
                                            onValueChange = {
                                                icon = it
                                            },
                                            checkedState = icon,
                                            modifier = Modifier.testTag(ICON_PARAM)
                                        )
                                    }
                                )
                            }

                            item {
                                val subTitleText =
                                    LocalContext.current.resources.getString(R.string.fluentui_subtitle)
                                ListItem.Item(
                                    text = subTitleText,
                                    subText = if (subtitle.isNullOrBlank())
                                        LocalContext.current.resources.getString(R.string.fluentui_disabled)
                                    else
                                        LocalContext.current.resources.getString(R.string.fluentui_enabled),
                                    trailingAccessoryView = {
                                        ToggleSwitch(
                                            onValueChange = {
                                                if (subtitle.isNullOrBlank()) {
                                                    subtitle = subTitleText
                                                } else {
                                                    subtitle = null
                                                }
                                            },
                                            checkedState = !subtitle.isNullOrBlank(),
                                            modifier = Modifier.testTag(SUBTITLE_PARAM)
                                        )
                                    }
                                )
                            }

                            item {
                                ListItem.Item(
                                    text = "Action Label",
                                    subText = if (actionLabel.isNullOrBlank())
                                        LocalContext.current.resources.getString(R.string.fluentui_disabled)
                                    else
                                        LocalContext.current.resources.getString(R.string.fluentui_enabled),
                                    trailingAccessoryView = {
                                        ToggleSwitch(
                                            onValueChange = {
                                                if (actionLabel.isNullOrBlank()) {
                                                    actionLabel = "Action"
                                                } else {
                                                    actionLabel = null
                                                }
                                            },
                                            checkedState = !actionLabel.isNullOrBlank(),
                                            modifier = Modifier.testTag(ACTION_BUTTON_PARAM)
                                        )
                                    }
                                )
                            }

                            item {
                                ListItem.Item(
                                    text = "Dismiss Button",
                                    subText = if (!dismissEnabled)
                                        LocalContext.current.resources.getString(R.string.fluentui_disabled)
                                    else
                                        LocalContext.current.resources.getString(R.string.fluentui_enabled),
                                    trailingAccessoryView = {
                                        ToggleSwitch(
                                            onValueChange = {
                                                dismissEnabled = it
                                            },
                                            checkedState = dismissEnabled,
                                            modifier = Modifier.testTag(DISMISS_BUTTON_PARAM)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    val result: NotificationResult = snackbarQueue.enqueue(
                                        "Hello from Fluent",
                                        style = style,
                                        icon = if (icon) FluentIcon(Icons.Outlined.ShoppingCart) else null,
                                        actionText = actionLabel,
                                        subTitle = subtitle,
                                        duration = duration,
                                        enableDismiss = dismissEnabled
                                    )

                                    when (result) {
                                        NotificationResult.TIMEOUT -> Toast.makeText(
                                            context,
                                            "Fluent Snackbar Timed Out",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        NotificationResult.CLICKED -> Toast.makeText(
                                            context,
                                            "Fluent Snackbar Clicked",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        NotificationResult.DISMISSED -> Toast.makeText(
                                            context,
                                            "Fluent Snackbar Dismissed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            text = "Show Snackbar",
                            size = ButtonSize.Small,
                            style = ButtonStyle.OutlinedButton,
                            modifier = Modifier.testTag(SHOW_SNACKBAR)
                        )

                        Button(
                            onClick = {
                                snackbarQueue.currentSnackbar?.dismiss()
                            },
                            text = "Dismiss Snackbar",
                            size = ButtonSize.Small,
                            style = ButtonStyle.OutlinedButton,
                            modifier = Modifier.testTag(DISMISS_SNACKBAR)
                        )
                    }
                    Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Snackbar(snackbarQueue, Modifier.padding(bottom = 12.dp))
                    }
                }
            }
        }
    }
}