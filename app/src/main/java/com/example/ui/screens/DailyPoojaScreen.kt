package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.engine.DainikPoojaRepository
import com.example.engine.GuidedPoojaStep
import com.example.engine.PanchangReport
import com.example.ui.components.SatyaEpistemicBadge
import com.example.ui.theme.*

@Composable
fun DailyPoojaScreen(
    userProfile: UserProfile,
    panchangReport: PanchangReport,
    poojaSteps: List<GuidedPoojaStep>,
    currentStepIndex: Int,
    completedSteps: Set<Int>,
    showMissingMaterialModal: Boolean,
    selectedDurationMins: Int,
    poojaCompletedCelebration: Boolean,
    onSelectStep: (Int) -> Unit,
    onToggleMissingMaterial: () -> Unit,
    onSetDuration: (Int) -> Unit,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onResetPooja: () -> Unit
) {
    var showSamskarasDialog by remember { mutableStateOf(false) }
    var showMandirGuideDialog by remember { mutableStateOf(false) }

    val currentStep = poojaSteps.getOrElse(currentStepIndex) { poojaSteps[0] }
    val isSenior = userProfile.seniorAccessibleMode
    val titleText = when (userProfile.preferredLanguage) {
        "hi" -> currentStep.titleHi
        "kn" -> currentStep.titleKn
        else -> currentStep.titleEn
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MarbleIvory)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Live Panchang & Daily Pooja Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SanctumMaroon),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = TempleGold.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "Drik Ganita • ${panchangReport.location.stateRegion}",
                                color = TempleGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "${panchangReport.tithiName} • ${panchangReport.paksha.split(" ")[0]}",
                            color = MarbleIvory,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Today's Guided Household Pooja",
                        color = MarbleIvory,
                        fontSize = if (isSenior) 22.sp else 19.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Text(
                        text = "Step-by-step authentic Panchopachara with verified Vedic mantras, inner meaning, and zero-fear missing material alternatives.",
                        color = MarbleIvory.copy(alpha = 0.85f),
                        fontSize = if (isSenior) 14.sp else 12.sp,
                        lineHeight = 18.sp
                    )

                    // Routine Duration Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Format:", color = TempleGold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        listOf(5 to "5m Quick", 10 to "10m Daily", 15 to "15m Full").forEach { (mins, label) ->
                            val isSel = selectedDurationMins == mins
                            Surface(
                                color = if (isSel) SacredSaffron else Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) Color.White else TempleGold.copy(alpha = 0.3f)),
                                modifier = Modifier.clickable { onSetDuration(mins) }
                            ) {
                                Text(
                                    text = label,
                                    color = MarbleIvory,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    // Secondary Quick Actions: 16 Samskaras & Home Mandir
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showSamskarasDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TempleGoldLight),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "16 Samskaras Guide", fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = { showMandirGuideDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TempleGoldLight),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Ghar Mandir Setup", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // 2. Step Sequence Progress Indicator
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Liturgy Steps Sequence",
                            fontWeight = FontWeight.Bold,
                            color = SanctumMaroon,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "${completedSteps.size} of ${poojaSteps.size} Completed",
                            color = SacredSaffronDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        poojaSteps.forEachIndexed { idx, s ->
                            val isDone = completedSteps.contains(s.stepNumber)
                            val isCurrent = idx == currentStepIndex
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isCurrent -> SanctumMaroon
                                            isDone -> EmeraldAuspicious
                                            else -> WarmSand
                                        }
                                    )
                                    .clickable { onSelectStep(idx) }
                            ) {
                                Text(
                                    text = if (isDone) "✓" else "${s.stepNumber}",
                                    color = if (isCurrent || isDone) MarbleIvory else CharcoalInk,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Active Step Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Header of the step
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    color = SanctumMaroon,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "STEP ${currentStep.stepNumber} OF ${poojaSteps.size}",
                                        color = TempleGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                SatyaEpistemicBadge(code = currentStep.substituteRule.epistemicCategory)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = titleText,
                                fontSize = if (isSenior) 20.sp else 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = SanctumMaroon,
                                fontFamily = FontFamily.Serif
                            )
                            Text(
                                text = "Source: ${currentStep.mantraCitation}",
                                fontSize = 11.sp,
                                color = MutedSlate
                            )
                        }

                        // Missing item button
                        Surface(
                            color = AmberYamagandaBg,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberYamaganda.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .clickable { onToggleMissingMaterial() }
                                .testTag("missing_item_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = "Missing Item Substitute",
                                    tint = AmberYamaganda,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Missing Item?",
                                    color = AmberYamaganda,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Missing Material Substitute Drawer
                    if (showMissingMaterialModal) {
                        Surface(
                            color = AmberYamagandaBg,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberYamaganda),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = AmberYamaganda,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Text-Verified Substitute (Pratinidhi Dravya)",
                                        color = AmberYamaganda,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = currentStep.substituteRule.guidance,
                                    color = CharcoalInk,
                                    fontSize = if (isSenior) 14.sp else 12.sp,
                                    lineHeight = 18.sp
                                )
                                Text(
                                    text = "Textual Authority: ${currentStep.substituteRule.textualAuthority}",
                                    color = MutedSlate,
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                    }

                    // Sacred Mantra Box
                    Surface(
                        color = MarbleIvory,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sacred Mantra",
                                    color = SacredSaffronDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Chandas Checked",
                                    color = MutedSlate,
                                    fontSize = 10.sp
                                )
                            }

                            // Devanagari
                            Text(
                                text = currentStep.mantraDevanagari,
                                color = SanctumMaroon,
                                fontSize = if (isSenior) 20.sp else 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Serif,
                                lineHeight = 26.sp
                            )

                            // Kannada script if chosen
                            if (userProfile.preferredLanguage == "kn") {
                                Text(
                                    text = currentStep.mantraKannada,
                                    color = SanctumMaroonDark,
                                    fontSize = if (isSenior) 18.sp else 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 24.sp
                                )
                            }

                            // ISO Transliteration
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, Color(0xFFE2E8F0))
                            ) {
                                Text(
                                    text = currentStep.mantraTranslit,
                                    color = CharcoalInk,
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    // Practical Instructions & Inner Meaning
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Instruction
                        Card(
                            colors = CardDefaults.cardColors(containerColor = WarmSand),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Action (Vidhi)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SanctumMaroon
                                )
                                Text(
                                    text = currentStep.practicalInstruction,
                                    fontSize = if (isSenior) 13.sp else 11.sp,
                                    color = CharcoalInk,
                                    lineHeight = 16.sp
                                )
                                Text(
                                    text = "Material: ${currentStep.requiredItem}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SacredSaffronDark
                                )
                            }
                        }

                        // Inner Meaning
                        Card(
                            colors = CardDefaults.cardColors(containerColor = WarmSand),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Inner Meaning (Bhavana)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MidnightIndigo
                                )
                                Text(
                                    text = currentStep.innerSignificance,
                                    fontSize = if (isSenior) 13.sp else 11.sp,
                                    color = CharcoalInk,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Associated Hasta Mudra info chip
                    if (currentStep.associatedMudraName != null) {
                        Surface(
                            color = TempleGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Associated Mudra: ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SanctumMaroon
                                )
                                Text(
                                    text = currentStep.associatedMudraName,
                                    fontSize = 11.sp,
                                    color = SanctumMaroonDark
                                )
                            }
                        }
                    }

                    // Step Navigation Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onPreviousStep,
                            enabled = currentStepIndex > 0,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("previous_step_button")
                        ) {
                            Text(text = "← Previous")
                        }

                        Button(
                            onClick = onNextStep,
                            colors = ButtonDefaults.buttonColors(containerColor = SanctumMaroon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("next_step_button")
                        ) {
                            Text(
                                text = if (currentStepIndex == poojaSteps.size - 1) "✓ Complete Pooja" else "Complete & Next →",
                                color = MarbleIvory,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Pooja Celebration Dialog
    if (poojaCompletedCelebration) {
        AlertDialog(
            onDismissRequest = onResetPooja,
            title = {
                Text(
                    text = "🌸 Pooja Sampoornam!",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = SanctumMaroon
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "May the grace of Mahaganapati bring peace, mental clarity, and auspiciousness to you and your household.",
                        fontSize = 14.sp,
                        color = CharcoalInk
                    )
                    Text(
                        text = "Your daily pooja completion has been recorded in your cloud spiritual journal.",
                        fontSize = 12.sp,
                        color = MutedSlate
                    )
                    Surface(
                        color = MarbleIvory,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "ॐ शान्तिः शान्तिः शान्तिः ॥",
                            color = SanctumMaroon,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onResetPooja,
                    colors = ButtonDefaults.buttonColors(containerColor = SanctumMaroon)
                ) {
                    Text("Return to Sanctuary")
                }
            }
        )
    }

    // 16 Samskaras Dialog
    if (showSamskarasDialog) {
        AlertDialog(
            onDismissRequest = { showSamskarasDialog = false },
            title = {
                Text(
                    text = "The 16 Vedic Samskaras (Lifecycle Milestones)",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = SanctumMaroon,
                    fontSize = 17.sp
                )
            },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    itemsIndexed(DainikPoojaRepository.SAMSKARA_LIFECYCLE) { _, s ->
                        Card(colors = CardDefaults.cardColors(containerColor = WarmSand)) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(text = "${s.name} (${s.sanskritName})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SanctumMaroon)
                                Text(text = "Stage: ${s.stage} • Timing: ${s.traditionalTiming}", fontSize = 11.sp, color = SacredSaffronDark)
                                Text(text = s.significance, fontSize = 11.sp, color = CharcoalInk)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSamskarasDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Home Mandir Setup Dialog
    if (showMandirGuideDialog) {
        AlertDialog(
            onDismissRequest = { showMandirGuideDialog = false },
            title = {
                Text(
                    text = "Ghar Mandir (Home Altar) Setup Guide",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = SanctumMaroon,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "• Orientation: Place the altar in the Northeast (Ishanya), East, or North facing direction.", fontSize = 12.sp)
                    Text(text = "• Murti Size: Traditional Grihya texts recommend household idols be thumb-sized to 6 inches, avoiding rigid temple Agama obligations.", fontSize = 12.sp)
                    Text(text = "• Deepa Safety: Keep the oil/ghee lamp in the Southeast or East corner on a sturdy metal tray away from flammable fabrics.", fontSize = 12.sp)
                    Text(text = "• Eco Visarjana: For earthen festival murtis, dissolve gracefully in a bucket at home and water home plants.", fontSize = 12.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showMandirGuideDialog = false }) {
                    Text("Understood")
                }
            }
        )
    }
}
