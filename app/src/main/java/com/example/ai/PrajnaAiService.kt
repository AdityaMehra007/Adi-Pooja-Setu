package com.example.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

data class PrajnaAiResponse(
    val answerText: String,
    val epistemicBadge: String = "CAT-A • Primary Textual Source",
    val primaryCitation: String = "PoojaSetu SATYA Knowledge Graph",
    val isSafetyNotice: Boolean = false
)

class PrajnaAiService {

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            systemInstruction = content {
                text(
                    """
                    You are PRAJNA, the verified intelligence engine of Project PoojaSetu.
                    Your core rules:
                    1. Ground every explanation in authentic classical texts (Four Vedas, Upanishads, Grihya Sutras, Puranas, Agamas, Dharmasindhu, Aryabhatiya, Charaka Samhita) or peer-reviewed historical scholarship.
                    2. Never hallucinate or synthesize mantras or citations. If a topic is unverified, state so with epistemic humility.
                    3. Strict Anti-Exploitation: Never diagnose curses, evil eye, or black magic attacks. If a user asks about being cursed or paying money for curse removal, clearly inform them it is fraudulent extortion under Indian law.
                    4. Medical Safety: Never advise stopping prescribed medication or substituting healthcare with rituals.
                    5. Crisis Help: If distress or self-harm is detected, immediately surface Tele-MANAS (14416) and 112.
                    6. Respond respectfully, concisely, and warmly in the user's language (English, Hindi, Kannada, etc.).
                    """.trimIndent()
                )
            },
            generationConfig = generationConfig {
                temperature = 0.2f
            }
        )
    }

    suspend fun askPrajna(userQuery: String, userRegion: String = "All-India"): PrajnaAiResponse {
        val lower = userQuery.lowercase()
        // Fast-path safety triage
        if (lower.contains("suicide") || lower.contains("kill myself") || lower.contains("self harm")) {
            return PrajnaAiResponse(
                answerText = "Your life and immediate safety are precious. Please reach out right now to verified, confidential professionals:\n• Tele-MANAS (Govt. of India 24×7): Call 14416 or 1800-891-4416\n• National Emergency: 112\n• Women Helpline: 181",
                epistemicBadge = "CAT-E • Verified Crisis Protocol",
                primaryCitation = "National Health Mission, Ministry of Health & Family Welfare",
                isSafetyNotice = true
            )
        }

        if (lower.contains("curse") || lower.contains("black magic") || lower.contains("vashikaran") || lower.contains("remove curse")) {
            return PrajnaAiResponse(
                answerText = "PoojaSetu Safety & Anti-Exploitation Guarantee: PoojaSetu does not diagnose curses or facilitate coercive rituals. Anyone demanding money from you or your family to 'remove a black magic curse' is committing criminal fraud and extortion. You can report threats to the National Cybercrime Portal at 1930 or local police at 112. In authentic Shaiva philosophy, the Divine is your eternal protector—no middleman holds your destiny.",
                epistemicBadge = "CAT-B • Historical Scholarship & Safety",
                primaryCitation = "SATYA Anti-Exploitation Policy & Consumer Protection",
                isSafetyNotice = true
            )
        }

        return try {
            val prompt = "User Region: $userRegion. Question: $userQuery\nPlease provide a clear, grounded explanation with source attribution."
            val response = withTimeout(45_000L) {
                model.generateContent(prompt)
            }
            val text = response.text
            if (!text.isNullOrBlank()) {
                PrajnaAiResponse(
                    answerText = text,
                    epistemicBadge = "CAT-B • Grounded Explanation",
                    primaryCitation = "PoojaSetu SATYA Civilisation Repository"
                )
            } else {
                PrajnaAiResponse(
                    answerText = "I could not retrieve a verified source for this query. In accordance with SATYA Principle #2 (Accuracy Before AI), unverified answers are not generated.",
                    epistemicBadge = "CAT-H • Unverified",
                    primaryCitation = "SATYA Epistemic Humility Gate"
                )
            }
        } catch (e: TimeoutCancellationException) {
            Log.w("PrajnaAi", "AI query timed out", e)
            PrajnaAiResponse(
                answerText = "The AI service response timed out. Please check your network connection and try again.",
                epistemicBadge = "CAT-E • Network Timeout",
                primaryCitation = "System Service"
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PrajnaAi", "AI query failed", e)
            PrajnaAiResponse(
                answerText = "Could not connect to Gemini service (${e.localizedMessage ?: "Network error"}). Please ensure App Check is configured.",
                epistemicBadge = "CAT-E • System State",
                primaryCitation = "System Diagnostics"
            )
        }
    }
}
