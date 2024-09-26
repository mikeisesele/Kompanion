package com.michael.kompanion.extensions

import android.graphics.Color
import android.annotation.SuppressLint
import androidx.annotation.PluralsRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.clickable(onClick: (() -> Unit)? = null): Modifier {
    return onClick?.let { block ->
        clickable(onClick = block)
    } ?: this
}

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.clickableWithRipple(
    onClick: (() -> Unit)? = null,
    rippleColor: Color// Custom ripple color
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this.then(
        if (onClick != null) {
            Modifier.clickable(
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick,
            )
        } else {
            Modifier
        },
    )
}

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.wrapOrFillHeight(maxHeightFactor: Float): Modifier = layout { measurable, constraints ->
    val placeable =
        measurable.measure(constraints.copy(maxHeight = (constraints.maxHeight * maxHeightFactor).toInt()))
    val maxHeight =
        if (placeable.height == constraints.maxHeight) {
            (constraints.maxHeight * maxHeightFactor).toInt()
        } else {
            placeable.height
        }
    val maxWidth = constraints.maxWidth
    layout(maxWidth, maxHeight) {
        placeable.placeRelative(0, 0)
    }
}

inline fun Modifier.applyIf(
    condition: Boolean,
    crossinline other: Modifier.() -> Modifier,
) = if (condition) other() else this

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun pluralResource(
    @PluralsRes id: Int,
    count: Int,
    vararg formatArgs: Any? = emptyArray(),
) = LocalContext.current.resources.getQuantityString(id, count, *formatArgs)

@Composable
fun getSimplePluralResource(
    @PluralsRes id: Int,
    count: Int,
) = pluralResource(id = id, count = count, count)

// consume click events to prevent items behind the component being clicked
fun Modifier.consumeClicks(enabled: Boolean = true) = if (enabled) {
    clickable(
        interactionSource = MutableInteractionSource(),
        indication = null,
        onClick = {},
    )
} else {
    this
}

