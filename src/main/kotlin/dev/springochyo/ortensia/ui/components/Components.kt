package dev.springochyo.ortensia.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ══════════════════════════════════════════════════════════════════
// CAELESTIA COMPONENTS
// All colors taken exclusively from MaterialTheme.colorScheme
// ══════════════════════════════════════════════════════════════════

// ── Hoverable card ───────────────────────────────────────────────

@Composable
fun CaelestiaCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isHovered) MaterialTheme.colorScheme.outlineVariant
                      else MaterialTheme.colorScheme.outline,
        animationSpec = tween(180),
        label = "card_border"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isHovered)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(180),
        label = "card_bg"
    )

    Card(
        onClick = onClick,
        modifier = modifier.hoverable(interactionSource),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(width = 1.dp, color = borderColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isHovered) 8.dp else 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(20.dp), content = content)
    }
}

// ── Back button ──────────────────────────────────────────────────

@Composable
fun CaelestiaBackButton(
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isHovered) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        animationSpec = tween(160),
        label = "back_border"
    )

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .heightIn(min = 44.dp)
            .hoverable(interactionSource),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.5.dp, borderColor),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

// ── Main menu button with hover border ───────────────────────────

@Composable
fun CaelestiaMenuButton(
    icon: String,
    label: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val borderColor by animateColorAsState(
        targetValue = if (isHovered) MaterialTheme.colorScheme.primary
                      else MaterialTheme.colorScheme.outline,
        animationSpec = tween(180),
        label = "menu_border"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isHovered)
            MaterialTheme.colorScheme.surfaceVariant
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(180),
        label = "menu_bg"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .hoverable(interactionSource),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            hoveredElevation  = 0.dp
        ),
        interactionSource = interactionSource
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(icon, fontSize = 28.sp, modifier = Modifier.padding(end = 16.dp))
            Column {
                Text(label, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// ── Screen header ────────────────────────────────────────────────

@Composable
fun CaelestiaHeader(
    title: String,
    subtitle: String = "",
    backLabel: String,
    onBack: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                        MaterialTheme.colorScheme.background.copy(alpha = 0f)
                    )
                )
            )
    ) {
        // Left accent bar
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(3.dp)
                .height(if (onBack != null && subtitle.isNotEmpty()) 100.dp else 70.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (onBack != null) {
                CaelestiaBackButton(
                    onClick = onBack,
                    label = backLabel,
                    modifier = Modifier.wrapContentWidth()
                )
                Spacer(Modifier.height(2.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 0.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), thickness = 1.dp)
}

// ── Text field ───────────────────────────────────────────────────

@Composable
fun CaelestiaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor    = MaterialTheme.colorScheme.outline,
            errorBorderColor        = MaterialTheme.colorScheme.error,
            focusedLabelColor       = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor     = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLabelColor         = MaterialTheme.colorScheme.error,
            cursorColor             = MaterialTheme.colorScheme.primary,
            focusedTextColor        = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor      = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    )
}

// ── Buttons ──────────────────────────────────────────────────────

@Composable
fun CaelestiaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor   = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.outline
        )
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
fun CaelestiaOutlinedBtn(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Text(text, fontSize = 14.sp)
    }
}

// ── Section label ────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.5.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier.padding(bottom = 8.dp)
    )
}

// ── Stat control row ─────────────────────────────────────────────

@Composable
fun StatControlRow(
    statName: String,
    currentValue: Short,
    minValue: Short,
    maxValue: Short,
    description: String = "",
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                statName,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
            if (description.isNotEmpty()) {
                Text(
                    description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                "$minValue – $maxValue",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
        Spacer(Modifier.width(12.dp))

        FilledTonalIconButton(
            onClick = onDecrement,
            enabled = currentValue > minValue,
            modifier = Modifier.size(36.dp)
        ) {
            Text("−", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .widthIn(min = 52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                currentValue.toString(),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        FilledTonalIconButton(
            onClick = onIncrement,
            enabled = currentValue < maxValue,
            modifier = Modifier.size(36.dp)
        ) {
            Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

// Keep legacy aliases so old screens compile without change
@Deprecated("Use CaelestiaCard", ReplaceWith("CaelestiaCard(onClick, modifier, content)"))
@Composable
fun OrtensiaCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = CaelestiaCard(onClick, modifier, content)

@Deprecated("Use CaelestiaHeader", ReplaceWith("CaelestiaHeader(...)"))
@Composable
fun GradientHeader(
    title: String,
    subtitle: String = "",
    backLabel: String = "Назад",
    onBack: (() -> Unit)? = null
) = CaelestiaHeader(title, subtitle, backLabel, onBack)

@Deprecated("Use CaelestiaTextField", ReplaceWith("CaelestiaTextField(...)"))
@Composable
fun OrtensiaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) = CaelestiaTextField(value, onValueChange, label, modifier, singleLine)

@Deprecated("Use CaelestiaButton", ReplaceWith("CaelestiaButton(...)"))
@Composable
fun OrtensiaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) = CaelestiaButton(text, onClick, modifier, enabled)

@Deprecated("Use CaelestiaOutlinedBtn", ReplaceWith("CaelestiaOutlinedBtn(...)"))
@Composable
fun OrtensiaOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) = CaelestiaOutlinedBtn(text, onClick, modifier)
