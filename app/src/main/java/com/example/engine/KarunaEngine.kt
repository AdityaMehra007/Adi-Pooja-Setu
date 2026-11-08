package com.example.engine

data class GuidanceDomainCard(
    val domainKey: String,
    val title: String,
    val practicalResource: String,
    val devotionalSupport: String,
    val antiExploitationWarning: String,
    val emergencyContact: String? = null
)

data class DincharyaHabit(
    val id: String,
    val timeSlot: String,
    val sanskritTerm: String,
    val title: String,
    val classicalSource: String,
    val scientificNote: String,
    val isCompleted: Boolean = false
)

object KarunaEngine {

    val GUIDANCE_MAP = mapOf(
        "ANXIETY_GRIEF" to GuidanceDomainCard(
            domainKey = "ANXIETY_GRIEF",
            title = "Emotional Stress, Grief, Loneliness or Overwhelm",
            practicalResource = "1. Connect with a licensed mental health professional or call India's free 24×7 Tele-MANAS helpline (14416 / 1800-891-4416). Speak with trusted family members.",
            devotionalSupport = "2. Optional peaceful practice: 10-minute evening Deepa meditation, slow Nadi Shodhana breathing (6 breaths/min), and contemplating Bhagavad Gita 2.14 & 2.56 on equanimity.",
            antiExploitationWarning = "Protection Notice: Never pay any astrologer or ritualist claiming your emotional distress is due to an 'evil eye' or 'curse' from a relative.",
            emergencyContact = "Tele-MANAS: 14416 • National Emergency: 112"
        ),
        "OCCULT_FEAR_PROTECTION" to GuidanceDomainCard(
            domainKey = "OCCULT_FEAR_PROTECTION",
            title = "Fear of Curses, 'Black Magic' Threats or Extortion",
            practicalResource = "1. Demands for money to 'remove a Tantric curse' constitute criminal extortion and fraud under Indian law. Block the perpetrator and report immediately to Cybercrime (1930) or local police (112).",
            devotionalSupport = "2. Optional courage & bhakti practice: Light a ghee diya at home for Lord Ganesha or Shiva. In classical Shaiva philosophy, the Divine is your eternal protector—no middleman holds your destiny hostage.",
            antiExploitationWarning = "SATYA Guarantee: PoojaSetu strictly bans practitioners from diagnosing curses, selling Vashikaran, or offering black magic removal packages.",
            emergencyContact = "National Cybercrime Helpline: 1930 • Police: 112"
        ),
        "FAMILY_CONFLICT" to GuidanceDomainCard(
            domainKey = "FAMILY_CONFLICT",
            title = "Household Disharmony, Marital Strain or Family Friction",
            practicalResource = "1. Seek neutral mediation, family counseling, or establish calm boundaries. Separate practical financial or lifestyle issues from spiritual blame.",
            devotionalSupport = "2. Optional shared household routine: Light an evening lamp together with the Rigvedic Samjnana Sukta (RV 10.191.2: 'sam gacchadhvam sam vadadhvam' — Walk together, speak with harmony).",
            antiExploitationWarning = "Ethical Rule: Never allow anyone to blame a spouse, in-law, or newborn of bringing 'bad luck' or 'inauspiciousness' to the home."
        ),
        "CAREER_FINANCE" to GuidanceDomainCard(
            domainKey = "CAREER_FINANCE",
            title = "Job Search, Financial Debt or Exam Pressure",
            practicalResource = "1. Audit monthly expenses, restructure high-interest debt, and divide study/work into focused 90-minute deep-work blocks (Ekagrata).",
            devotionalSupport = "2. Optional morning focus: Recite Aditya Hridaya Stotra or Saraswati Vandana to cultivate clarity, perseverance, and disciplined action (Purushartha).",
            antiExploitationWarning = "Ethical Rule: No Yantra, gemstone, or costly Homa can guarantee financial returns without personal effort."
        ),
        "HEALTH_ILLNESS" to GuidanceDomainCard(
            domainKey = "HEALTH_ILLNESS",
            title = "Chronic Illness Recovery or Caring for an Elderly Parent",
            practicalResource = "1. Medical Priority: Strictly follow your licensed doctor's diagnosis, therapy, and prescription schedules. Use Senior Mode in PoojaSetu for low-vision/audio comfort.",
            devotionalSupport = "2. Optional comfort: Classical Dharma texts explicitly exempt unwell individuals from physical ritual rules and encourage Manasa Pooja (mental prayer) and listening to Vishnu Sahasranama.",
            antiExploitationWarning = "Medical Safety: Never discontinue prescribed medications (insulin, cardiac, oncology, psychiatric) for any unverified ritual or herbal remedy."
        )
    )

    fun getInitialDincharyaHabits(): List<DincharyaHabit> = listOf(
        DincharyaHabit(
            id = "hab_1",
            timeSlot = "05:30 AM (Brahma Muhurta)",
            sanskritTerm = "Ushah-Pana & Smarana",
            title = "Morning Awakening, Warm Hydration & Gratitude",
            classicalSource = "Ashtanga Hridaya, Sutrasthana Ch. 2 (Dincharya)",
            scientificNote = "Optimizes cortisol awakening response (CAR) and aids morning peristalsis."
        ),
        DincharyaHabit(
            id = "hab_2",
            timeSlot = "06:00 AM",
            sanskritTerm = "Asana & Nadi Shodhana",
            title = "15-Min Mobility & Alternate-Nostril Breathing (6 breaths/min)",
            classicalSource = "Hatha Yoga Pradipika 2.7–10 & Yoga Sutra 2.49",
            scientificNote = "Clinical RCTs show increased heart rate variability (HRV) and autonomic balance."
        ),
        DincharyaHabit(
            id = "hab_3",
            timeSlot = "06:30 AM",
            sanskritTerm = "Dainik Devata Pooja",
            title = "10-Min Household Pooja & Desha-Kala Sankalpa",
            classicalSource = "Smarta / Agama Grihya Kalpa",
            scientificNote = "Structured daily ritual reduces anticipatory anxiety and improves cognitive focus."
        ),
        DincharyaHabit(
            id = "hab_4",
            timeSlot = "08:30 PM",
            sanskritTerm = "Swadhyaya & Atma-Niriksana",
            title = "20-Min Sacred/Philosophical Reading & Reflection",
            classicalSource = "Yoga Sutra 2.44 (Swadhyaya)",
            scientificNote = "Replaces high-dopamine blue-light scrolling with relaxing cognitive engagement."
        )
    )
}
