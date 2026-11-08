package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.PrajnaAiResponse
import com.example.engine.DincharyaHabit
import com.example.engine.KarunaEngine
import com.example.ui.components.SatyaEpistemicBadge
import com.example.ui.theme.*

@Composable
fun KarunaGurukulScreen(
    selectedProblemDomain: String,
    problemUserNote: String,
    guidanceResultShown: Boolean,
    dincharyaHabits: List<DincharyaHabit>,
    aiQuery: String,
    aiIsLoading: Boolean,
    aiResponse: PrajnaAiResponse?,
    onSelectProblemDomain: (String) -> Unit,
    onUpdateProblemNote: (String) -> Unit,
    onSubmitProblem: () -> Unit,
    onToggleDincharyaHabit: (String) -> Unit,
    onAiQueryChange: (String) -> Unit,
    onAskAi: (String?) -> Unit
) {
    val activeGuidance = KarunaEngine.GUIDANCE_MAP[selectedProblemDomain] ?: KarunaEngine.GUIDANCE_MAP.values.first()

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
                        text = "KARUNA Guidance & GURUKUL Academy",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = MarbleIvory,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "A safe sanctuary for personal struggles, circadian discipline (Dincharya), and source-grounded answers from PRAJNA AI.",
                        color = MarbleIvory.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    // Helplines alert strip
                    Surface(
                        color = Color.White.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = TempleGoldLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Emergency: 112 • Tele-MANAS: 14416 • Cybercrime: 1930",
                                color = MarbleIvory,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // 2. KARUNA: "What Are You Going Through?" Safe Triage
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
                        text = "🧭 What Are You Going Through?",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SanctumMaroon,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Choose your current situation to see practical action alongside non-harmful devotional comfort:",
                        fontSize = 11.sp,
                        color = CharcoalInk
                    )

                    // Domain Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "ANXIETY_GRIEF" to "Stress / Grief",
                            "OCCULT_FEAR_PROTECTION" to "Curse / Scam Defense",
                            "FAMILY_CONFLICT" to "Family Conflict",
                            "CAREER_FINANCE" to "Career / Finance"
                        ).forEach { (k, label) ->
                            val isSel = selectedProblemDomain == k
                            Surface(
                                color = if (isSel) SanctumMaroon else WarmSand,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { onSelectProblemDomain(k) }
                            ) {
                                Text(
                                    text = label,
                                    color = if (isSel) MarbleIvory else CharcoalInk,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Guidance Output Card
                    Surface(
                        color = MarbleIvory,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = activeGuidance.title,
                                fontWeight = FontWeight.Bold,
                                color = SanctumMaroon,
                                fontSize = 14.sp,
                                fontFamily = FontFamily.Serif
                            )

                            Card(colors = CardDefaults.cardColors(containerColor = WarmSand)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "🧭 Practical & Real-World Step", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MidnightIndigo)
                                    Text(text = activeGuidance.practicalResource, fontSize = 11.sp, color = CharcoalInk, lineHeight = 16.sp)
                                }
                            }

                            Card(colors = CardDefaults.cardColors(containerColor = WarmSand)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "🪔 Optional Peaceful Devotional Support", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SacredSaffronDark)
                                    Text(text = activeGuidance.devotionalSupport, fontSize = 11.sp, color = CharcoalInk, lineHeight = 16.sp)
                                }
                            }

                            Surface(color = AmberYamagandaBg, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "🛡️ ${activeGuidance.antiExploitationWarning}",
                                    color = AmberYamaganda,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    // Private Note Input & Submit
                    OutlinedTextField(
                        value = problemUserNote,
                        onValueChange = onUpdateProblemNote,
                        placeholder = { Text("Optional private reflection note (stored securely in your account)...", fontSize = 11.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("problem_note_input"),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3
                    )

                    Button(
                        onClick = onSubmitProblem,
                        colors = ButtonDefaults.buttonColors(containerColor = SacredSaffron),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_problem_button")
                    ) {
                        Text("Save Note to Private Cloud Journal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // 3. GURUKUL: Dincharya Circadian Habit Tracker
        item {
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Daily Dincharya Circadian Tracker",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SanctumMaroon,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "${dincharyaHabits.count { it.isCompleted }} / ${dincharyaHabits.size} Done",
                            color = SacredSaffronDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Classical daily regimen from Ashtanga Hridaya aligned with modern circadian neuroscience:",
                        fontSize = 11.sp,
                        color = MutedSlate
                    )

                    dincharyaHabits.forEach { habit ->
                        Surface(
                            color = if (habit.isCompleted) EmeraldAuspiciousBg else WarmSand,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (habit.isCompleted) EmeraldAuspicious else TempleGold.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleDincharyaHabit(habit.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Checkbox(
                                    checked = habit.isCompleted,
                                    onCheckedChange = { onToggleDincharyaHabit(habit.id) },
                                    colors = CheckboxDefaults.colors(checkedColor = EmeraldAuspicious)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = habit.timeSlot, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SacredSaffronDark)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "• ${habit.sanskritTerm}", fontSize = 10.sp, color = MutedSlate)
                                    }
                                    Text(text = habit.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SanctumMaroon)
                                    Text(text = "📜 Source: ${habit.classicalSource}", fontSize = 10.sp, color = CharcoalInk)
                                    Text(text = "🔬 Science: ${habit.scientificNote}", fontSize = 10.sp, color = EmeraldAuspicious)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. PRAJNA AI Assistant (Zero-Hallucination Mode via Firebase AI Logic)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MidnightIndigo),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRAJNA Source-Grounded AI",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MarbleIvory,
                            fontFamily = FontFamily.Serif
                        )
                        SatyaEpistemicBadge(code = "CAT-A • Primary Text")
                    }
                    Text(
                        text = "Powered by Gemini 3.8 Flash via Firebase AI Logic. Strictly verified by the SATYA epistemic database. Zero fabricated mantras.",
                        color = MarbleIvory.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )

                    // Quick Prompt Chips
                    Text(text = "Try asking:", color = TempleGold, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            "Why do Smarta and Vaishnava Ekadashi dates differ?",
                            "I do not have Durva grass for Ganesha Pooja, what is the substitute?",
                            "What is the mathematical meaning of the 8 Vedic Ashtavikritis?"
                        ).forEach { prompt ->
                            Surface(
                                color = Color.White.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, TempleGold.copy(alpha = 0.3f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAskAi(prompt) }
                            ) {
                                Text(
                                    text = "💬 \"$prompt\"",
                                    color = MarbleIvory,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Query Input & Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = aiQuery,
                            onValueChange = onAiQueryChange,
                            placeholder = { Text("Ask about any mantra, ritual step, or science...", color = Color.LightGray, fontSize = 11.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = TempleGold,
                                unfocusedBorderColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ai_query_input")
                        )

                        Button(
                            onClick = { onAskAi(null) },
                            enabled = !aiIsLoading && aiQuery.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = TempleGold),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("ask_ai_button")
                        ) {
                            if (aiIsLoading) {
                                CircularProgressIndicator(color = SanctumMaroonDark, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Ask", color = SanctumMaroonDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Response Card
                    if (aiResponse != null) {
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "Prajna Grounded Answer:", color = TempleGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    SatyaEpistemicBadge(code = aiResponse.epistemicBadge)
                                }
                                Text(
                                    text = aiResponse.answerText,
                                    color = MarbleIvory,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                                Text(
                                    text = "📜 Verified Citation: ${aiResponse.primaryCitation}",
                                    color = TempleGoldLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
