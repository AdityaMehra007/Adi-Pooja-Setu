package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.theme.*

@Composable
fun SatyaEpistemicBadge(code: String, modifier: Modifier = Modifier) {
    val (bg, textColor, borderCol, label) = when {
        code.contains("CAT-A") -> Quadruple(SanctumMaroon.copy(alpha = 0.12f), SanctumMaroon, SanctumMaroon.copy(alpha = 0.35f), "CAT-A • Primary Text")
        code.contains("CAT-B") -> Quadruple(MidnightIndigo.copy(alpha = 0.12f), MidnightIndigo, MidnightIndigo.copy(alpha = 0.35f), "CAT-B • Historical Scholarship")
        code.contains("CAT-C") -> Quadruple(SacredSaffron.copy(alpha = 0.15f), SacredSaffronDark, SacredSaffron.copy(alpha = 0.4f), "CAT-C • Traditional Sampradaya")
        code.contains("CAT-E") -> Quadruple(EmeraldAuspiciousBg, EmeraldAuspicious, EmeraldAuspicious.copy(alpha = 0.4f), "CAT-E • Empirical Science")
        code.contains("CAT-G") -> Quadruple(Color(0xFFE2E8F0), Color(0xFF334155), Color(0xFF94A3B8), "CAT-G • Cultural Folklore")
        else -> Quadruple(AmberYamagandaBg, AmberYamaganda, AmberYamaganda.copy(alpha = 0.4f), "CAT-D • Multiple Views")
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(50),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderCol),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun TopSacredHeader(
    preferredLanguage: String,
    stateRegion: String,
    cityName: String,
    seniorMode: Boolean,
    onLanguageChange: (String) -> Unit,
    onRegionChange: (String, String) -> Unit,
    onSeniorModeToggle: () -> Unit,
    onSignOut: () -> Unit
) {
    var showRegionMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SanctumMaroon)
            .statusBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(TempleGold)
                ) {
                    Text(
                        text = "ॐ",
                        color = SanctumMaroonDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "POOJASETU",
                            color = MarbleIvory,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "SOVEREIGN",
                            color = TempleGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "$cityName • $stateRegion",
                        color = MarbleIvory.copy(alpha = 0.8f),
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Senior Mode Toggle
                Surface(
                    color = if (seniorMode) SacredSaffron else SanctumMaroonDark,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f)),
                    modifier = Modifier.clickable { onSeniorModeToggle() }
                ) {
                    Text(
                        text = if (seniorMode) "A+ ON" else "A+",
                        color = MarbleIvory,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }

                // Region Selector Button
                Box {
                    Surface(
                        color = SanctumMaroonDark,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f)),
                        modifier = Modifier.clickable { showRegionMenu = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = "Select Region",
                                tint = TempleGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = stateRegion.take(6),
                                color = MarbleIvory,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRegionMenu,
                        onDismissRequest = { showRegionMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Karnataka (Amanta / Bengaluru)") },
                            onClick = {
                                onRegionChange("Karnataka", "Bengaluru")
                                showRegionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Uttar Pradesh (Purnimanta / Varanasi)") },
                            onClick = {
                                onRegionChange("Uttar Pradesh", "Varanasi")
                                showRegionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Tamil Nadu (Solar / Chennai)") },
                            onClick = {
                                onRegionChange("Tamil Nadu", "Chennai")
                                showRegionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Maharashtra (Amanta / Pune)") },
                            onClick = {
                                onRegionChange("Maharashtra", "Pune")
                                showRegionMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("West Bengal (Solar / Kolkata)") },
                            onClick = {
                                onRegionChange("West Bengal", "Kolkata")
                                showRegionMenu = false
                            }
                        )
                    }
                }

                // Sign Out
                IconButton(
                    onClick = onSignOut,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Sign Out",
                        tint = TempleGoldLight,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Secondary Language Pills Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Language:",
                color = TempleGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            listOf("en" to "EN", "hi" to "हिन्दी", "kn" to "ಕನ್ನಡ").forEach { (code, label) ->
                val isSelected = preferredLanguage == code
                Surface(
                    color = if (isSelected) TempleGold else SanctumMaroonDark,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { onLanguageChange(code) }
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) SanctumMaroonDark else MarbleIvory,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomSacredNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = SanctumMaroon,
        contentColor = MarbleIvory,
        tonalElevation = 8.dp
    ) {
        val tabs = listOf(
            Triple(AppTab.HOME_POOJA, Icons.Default.Brightness7, "Pooja"),
            Triple(AppTab.PANCHANG, Icons.Default.CalendarMonth, "Panchang"),
            Triple(AppTab.MARKETPLACE, Icons.Default.Handshake, "Seva-Setu"),
            Triple(AppTab.CIVILISATION, Icons.Default.MenuBook, "Civilisation"),
            Triple(AppTab.KARUNA_GURUKUL, Icons.Default.CompassCalibration, "Guidance")
        )

        tabs.forEach { (tab, icon, label) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) SanctumMaroonDark else MarbleIvory.copy(alpha = 0.8f)
                    )
                },
                label = {
                    Text(
                        text = label,
                        color = if (isSelected) TempleGold else MarbleIvory.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = TempleGold
                )
            )
        }
    }
}
