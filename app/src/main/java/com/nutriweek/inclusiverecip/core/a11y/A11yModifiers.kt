package com.nutriweek.inclusiverecip.core.a11y

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

fun Modifier.touchTarget() = this.defaultMinSize(minHeight = A11yDefaults.MinTouchTarget)

fun Modifier.a11yDescription(desc: String) = this.semantics { contentDescription = desc }

@Composable
fun focusOutline(): BorderStroke = BorderStroke(A11yDefaults.FocusOutlineWidth, Color.White)

@Composable
fun LargeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.touchTarget().focusable(),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        border = focusOutline(),
        colors = ButtonDefaults.outlinedButtonColors()
    ) { Text(text) }
}