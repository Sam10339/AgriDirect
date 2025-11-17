package com.example.agridirect20

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A reusable primary action button used throughout the AgriDirect app.
 *
 * Applies the app's primary color scheme for consistent UI styling.
 * Wraps  Button with predefined colors and predictable behavior.
 *
 * Usage Example:
 * ```
 * AgriPrimaryButton(
 *     onClick = { /* handle action */ }
 * ) {
 *     Text("Submit")
 * }
 * ```
 */
@Composable
fun AgriPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        content()
    }
}
