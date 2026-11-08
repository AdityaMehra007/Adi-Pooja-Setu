package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.CivilisationEntry
import com.example.ui.components.SatyaEpistemicBadge
import com.example.ui.theme.*

@Composable
fun CivilisationScreen(
    articles: List<CivilisationEntry>,
    selectedPillar: String,
    onSelectPillar: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MarbleIvory)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Header Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SanctumMaroon),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "SARASWATI × SATYA Civilisation Engine",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = MarbleIvory,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "The Living Archive of Indian Civilisation. Every record separates Primary Textual Witness, Traditional Commentary, and Modern Scientific / Historical Evaluation.",
                        color = MarbleIvory.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    // Filter Pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "ALL" to "All Pillars",
                            "VEDAS_UPANISHADS" to "Vedas & Upanishads",
                            "TANTRA_AGHORA" to "Tantra & Aghora",
                            "ANCIENT_SCIENCES" to "Ancient Sciences"
                        ).forEach { (key, label) ->
                            val isSel = selectedPillar == key
                            Surface(
                                color = if (isSel) TempleGold else Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { onSelectPillar(key) }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) SanctumMaroonDark else MarbleIvory,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Knowledge Articles List
        items(articles) { art ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            SatyaEpistemicBadge(code = art.epistemicCode)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = art.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SanctumMaroon,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = art.subtitle,
                                fontSize = 11.sp,
                                color = SacredSaffronDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Citation: ${art.locusCitation}",
                                fontSize = 10.sp,
                                color = MutedSlate
                            )
                        }
                    }

                    // Sanskrit Original Text Box
                    Surface(
                        color = MarbleIvory,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = art.sanskritOriginal,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SanctumMaroon,
                                fontFamily = FontFamily.Serif,
                                lineHeight = 22.sp
                            )
                            Text(
                                text = art.transliteration,
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic,
                                color = CharcoalInk,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // 3-Column Epistemic Breakdown
                    Card(colors = CardDefaults.cardColors(containerColor = WarmSand), shape = RoundedCornerShape(10.dp)) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "1. Primary Textual Statement (CAT-A)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SanctumMaroon)
                            Text(text = art.primaryTextStatement, fontSize = 11.sp, color = CharcoalInk, lineHeight = 16.sp)
                        }
                    }

                    Card(colors = CardDefaults.cardColors(containerColor = WarmSand), shape = RoundedCornerShape(10.dp)) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "2. Traditional Commentary (CAT-C)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SacredSaffronDark)
                            Text(text = art.traditionalCommentary, fontSize = 11.sp, color = CharcoalInk, lineHeight = 16.sp)
                        }
                    }

                    Card(colors = CardDefaults.cardColors(containerColor = EmeraldAuspiciousBg), shape = RoundedCornerShape(10.dp)) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "3. Modern Scientific / Historical Evaluation (CAT-B / CAT-E)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldAuspicious)
                            Text(text = art.modernScientificEvaluation, fontSize = 11.sp, color = CharcoalInk, lineHeight = 16.sp)
                        }
                    }

                    // Safety Notice if applicable
                    if (art.safetyNotice != null) {
                        Surface(
                            color = AmberYamagandaBg,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberYamaganda.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = art.safetyNotice,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = AmberYamaganda,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
