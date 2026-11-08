package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.PanchangReport
import com.example.engine.SuryaPanchangEngine
import com.example.engine.TarabalamResult
import com.example.ui.components.SatyaEpistemicBadge
import com.example.ui.theme.*

@Composable
fun PanchangScreen(
    panchang: PanchangReport,
    selectedBirthNakshatra: Int,
    tarabalam: TarabalamResult,
    onSelectBirthNakshatra: (Int) -> Unit
) {
    var showNakshatraDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MarbleIvory)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SatyaEpistemicBadge(code = "CAT-E • Empirical Science")
                        Text(
                            text = "Lahiri Ayanamsha: ${panchang.ayanamshaDegrees}°",
                            color = MutedSlate,
                            fontSize = 11.sp
                        )
                    }

                    Text(
                        text = "Surya Siddhanta & Drik Panchang",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = SanctumMaroon,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = "${panchang.samvatsara} • ${panchang.ayana} • ${panchang.ritu}",
                        fontSize = 12.sp,
                        color = SacredSaffronDark,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "📍 ${panchang.location.cityName} (${panchang.location.latitude}° N, ${panchang.location.longitude}° E)",
                            fontSize = 11.sp,
                            color = CharcoalInk
                        )
                        Text(
                            text = "Sunrise: ${panchang.sunriseStr} • Sunset: ${panchang.sunsetStr}",
                            fontSize = 11.sp,
                            color = CharcoalInk
                        )
                    }
                }
            }
        }

        // 2. The 5 Angas Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "The 5 Sacred Limbs (Pancha-Anga)",
                    fontWeight = FontWeight.Bold,
                    color = SanctumMaroon,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tithi
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WarmSand),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "1. TITHI (Lunar Day)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SacredSaffronDark)
                            Text(text = panchang.tithiName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SanctumMaroon)
                            Text(text = "${panchang.paksha} (${panchang.tithiProgressPercent}% passed)", fontSize = 11.sp, color = CharcoalInk)
                            Text(text = "Ends ~${panchang.tithiEndTimeStr}", fontSize = 10.sp, color = MutedSlate)
                        }
                    }

                    // Vara
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WarmSand),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "2. VARA (Solar Day)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SacredSaffronDark)
                            Text(text = panchang.varaNameSanskrit, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SanctumMaroon)
                            Text(text = panchang.varaNameEnglish, fontSize = 11.sp, color = CharcoalInk)
                            Text(text = "Lord: Surya Narayana", fontSize = 10.sp, color = MutedSlate)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Nakshatra
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WarmSand),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "3. NAKSHATRA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SacredSaffronDark)
                            Text(text = panchang.nakshatraName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SanctumMaroon)
                            Text(text = "Pada ${panchang.nakshatraPada} • Deity: ${panchang.nakshatraDeity}", fontSize = 11.sp, color = CharcoalInk)
                            Text(text = "Ends ~${panchang.nakshatraEndTimeStr}", fontSize = 10.sp, color = MutedSlate)
                        }
                    }

                    // Yoga & Karana
                    Card(
                        colors = CardDefaults.cardColors(containerColor = WarmSand),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "4 & 5. YOGA & KARANA", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SacredSaffronDark)
                            Text(text = panchang.yogaName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SanctumMaroon)
                            Text(text = "Karana: ${panchang.karanaName}", fontSize = 11.sp, color = CharcoalInk)
                            Text(text = "Lunisolar alignment", fontSize = 10.sp, color = MutedSlate)
                        }
                    }
                }
            }
        }

        // 3. Daytime Muhurta Octants
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Auspicious & Inauspicious Muhurta Windows",
                    fontWeight = FontWeight.Bold,
                    color = SanctumMaroon,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Abhijit
                    Card(
                        colors = CardDefaults.cardColors(containerColor = EmeraldAuspiciousBg),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "✦ Abhijit Muhurta", fontWeight = FontWeight.Bold, color = EmeraldAuspicious, fontSize = 11.sp)
                            Text(text = panchang.abhijitMuhurtaStr ?: "Omitted Wednesday", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldAuspicious)
                            Text(text = "Midday grace window", fontSize = 10.sp, color = EmeraldAuspicious)
                        }
                    }

                    // Rahu Kaal
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoseRahuBg),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "☋ Rahu Kaal", fontWeight = FontWeight.Bold, color = RoseRahu, fontSize = 11.sp)
                            Text(text = panchang.rahuKaalStr, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RoseRahu)
                            Text(text = "Daily pooja continues", fontSize = 10.sp, color = RoseRahu)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Yamaganda
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AmberYamagandaBg),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "☋ Yamaganda", fontWeight = FontWeight.Bold, color = AmberYamaganda, fontSize = 11.sp)
                            Text(text = panchang.yamagandaStr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberYamaganda)
                        }
                    }

                    // Gulika Kaal
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "☋ Gulika Kaal", fontWeight = FontWeight.Bold, color = Color(0xFF334155), fontSize = 11.sp)
                            Text(text = panchang.gulikaKaalStr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                        }
                    }
                }
            }
        }

        // 4. Personalized Sanskrit Desha-Kala Sankalpa Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SanctumMaroonDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Today's Dynamic Desha-Kala Sankalpa Formula",
                            color = TempleGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = panchang.sankalpaFormulaSanskrit,
                        color = MarbleIvory,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Auto-generated using today's exact astronomical coordinates for ${panchang.location.cityName}.",
                        color = TempleGoldLight.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 5. Interactive Tarabalam Calculator
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Interactive Tarabalam Calculator",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SanctumMaroon,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Select your birth Janma Nakshatra to evaluate transit compatibility with today's transit Nakshatra (${panchang.nakshatraName}).",
                        fontSize = 11.sp,
                        color = CharcoalInk
                    )

                    // Nakshatra Selector Dropdown
                    Box {
                        OutlinedButton(
                            onClick = { showNakshatraDropdown = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Birth Nakshatra: ${SuryaPanchangEngine.NAKSHATRAS.getOrNull(selectedBirthNakshatra - 1)?.name ?: "Ashwini"}",
                                color = SanctumMaroon,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        DropdownMenu(
                            expanded = showNakshatraDropdown,
                            onDismissRequest = { showNakshatraDropdown = false }
                        ) {
                            SuryaPanchangEngine.NAKSHATRAS.forEachIndexed { idx, nak ->
                                DropdownMenuItem(
                                    text = { Text("${idx + 1}. ${nak.name} (${nak.deity})") },
                                    onClick = {
                                        onSelectBirthNakshatra(idx + 1)
                                        showNakshatraDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    // Result Display
                    Surface(
                        color = if (tarabalam.isAuspicious) EmeraldAuspiciousBg else AmberYamagandaBg,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (tarabalam.isAuspicious) EmeraldAuspicious else AmberYamaganda),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (tarabalam.isAuspicious) "✓ Auspicious: " else "• Mindful: ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (tarabalam.isAuspicious) EmeraldAuspicious else AmberYamaganda
                                )
                                Text(
                                    text = tarabalam.taraName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (tarabalam.isAuspicious) EmeraldAuspicious else AmberYamaganda
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tarabalam.description,
                                fontSize = 11.sp,
                                color = CharcoalInk
                            )
                        }
                    }
                }
            }
        }
    }
}
