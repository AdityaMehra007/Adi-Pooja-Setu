package com.example.engine

data class SubstituteRule(
    val allowed: Boolean,
    val guidance: String,
    val textualAuthority: String,
    val epistemicCategory: String = "CAT-A • Primary Textual Source"
)

data class GuidedPoojaStep(
    val stepNumber: Int,
    val upacharaName: String,
    val titleEn: String,
    val titleHi: String,
    val titleKn: String,
    val mantraDevanagari: String,
    val mantraKannada: String,
    val mantraTranslit: String,
    val mantraCitation: String,
    val practicalInstruction: String,
    val innerSignificance: String,
    val requiredItem: String,
    val substituteRule: SubstituteRule,
    val associatedMudraName: String? = null
)

data class HastaMudraGuide(
    val name: String,
    val sanskritName: String,
    val purpose: String,
    val visualDescription: String,
    val stepUsage: String
)

data class SamskaraLifecycleItem(
    val name: String,
    val sanskritName: String,
    val stage: String, // Childhood, Education, Marriage, Ancestral
    val significance: String,
    val traditionalTiming: String
)

object DainikPoojaRepository {

    val DAILY_GANESHA_POOJA_STEPS = listOf(
        GuidedPoojaStep(
            stepNumber = 1,
            upacharaName = "Achamanam & Deepa Prajwalanam",
            titleEn = "Lighting the Sacred Lamp (Deepa) & Purification",
            titleHi = "आचमन एवं दीप प्रज्वलन",
            titleKn = "ಆಚಮನ ಮತ್ತು ದೀಪ ಪ್ರಜ್ವಲನ",
            mantraDevanagari = "शुभं करोति कल्याणमारोग्यं धनसम्पदः । शत्रुबुद्धिविनाशाय दीपज्योतिर्नमोऽस्तु ते ॥",
            mantraKannada = "ಶುಭಂ ಕರೋತಿ ಕಲ್ಯಾಣಮಾರೋಗ್ಯಂ ಧನಸಂಪದಃ । ಶತ್ರುಬುದ್ಧಿವಿನಾಶಾಯ ದೀಪಜ್ಯೋತಿರ್ನಮೋಸ್ತು ತೇ ॥",
            mantraTranslit = "śubhaṁ karoti kalyāṇamārogyaṁ dhanasampadaḥ | śatrubuddhivināśāya dīpajyotirnamo'stu te ||",
            mantraCitation = "Smarta / Puranic Deepa Stuti (Verified Household Liturgy)",
            practicalInstruction = "Sit facing East or North. Light the clay or brass diya using cow ghee or sesame oil. Sip a few drops of water from the spoon (Achamanam) reciting Om Keshavaya Namah, Om Narayanaya Namah, Om Madhavaya Namah.",
            innerSignificance = "The flame signifies illuminating spiritual wisdom (Jñana) dispelling darkness and inner mental distraction before worship begins.",
            requiredItem = "Brass/Clay Diya with Ghee/Sesame Oil & Cotton Wick",
            substituteRule = SubstituteRule(
                allowed = true,
                guidance = "If open flame is prohibited (e.g. apartment regulations, dorm, hospital), meditate mentally upon Agni/Surya (Manasa Deepa) or use a clean symbolic electric diya.",
                textualAuthority = "Shiva Purana (Vidyeshwara Samhita) & Grihya Sutra Manasa-Pooja allowances"
            ),
            associatedMudraName = "Anjali Mudra"
        ),
        GuidedPoojaStep(
            stepNumber = 2,
            upacharaName = "Desha-Kala Sankalpa",
            titleEn = "Sacred Intention & Cosmic Coordinates (Sankalpa)",
            titleHi = "देश-काल संकल्प",
            titleKn = "ದೇಶ-ಕಾಲ ಸಂಕಲ್ಪ",
            mantraDevanagari = "ॐ तत्सत् श्रीशुभसंवत्सरे अद्य शुभतिथौ शुभनक्षत्रे श्रीमहागणपतिप्रीत्यर्थं यथाशक्ति दैनिकपूजां करिष्ये ॥",
            mantraKannada = "ಓಂ ತತ್ಸತ್ ಶ್ರೀಶುಭಸಂವತ್ಸರೇ ಅದ್ಯ ಶುಭತಿಥೌ ಶುಭನಕ್ಷತ್ರೇ ಶ್ರೀಮಹಾಗಣಪತಿಪ್ರೀತ್ಯರ್ಥಂ ಯಥಾಶಕ್ತಿ ದೈನಿಕಪೂಜಾಂ ಕರಿಷ್ಯೇ ॥",
            mantraTranslit = "oṁ tatsat śrīśubhasaṁvatsare adya śubhatithau śubhanakṣatre śrīmahāgaṇapatiprītyarthaṁ yathāśakti dainikapūjāṁ kariṣye ||",
            mantraCitation = "Baudhayana / Apastamba Grihya Kalpa Sankalpa Vidhi",
            practicalInstruction = "Hold a pinch of Akshata (unbroken turmeric-coated rice) and a few drops of water in your right palm. Recite the intention and gently release the water into the Arghya plate.",
            innerSignificance = "Sankalpa mindfully anchors the practitioner in astronomical space-time (Ayana, Masa, Tithi, Nakshatra) and awakens conscious dedication.",
            requiredItem = "Akshata (Turmeric Rice) & Copper Kalash Water",
            substituteRule = SubstituteRule(
                allowed = true,
                guidance = "If Akshata is unavailable, plain pure drinking water (Shuddha Jala) or a clean flower petal held in the palm suffices.",
                textualAuthority = "Dharmasindhu — Pratinidhi Dravya (Substitute Materials) Section"
            ),
            associatedMudraName = "Sankalpa Mudra"
        ),
        GuidedPoojaStep(
            stepNumber = 3,
            upacharaName = "Dhyanam & Avahanam",
            titleEn = "Invocation of Lord Mahaganapati",
            titleHi = "श्री महागणपति ध्यान एवं आवाहन",
            titleKn = "ಶ್ರೀ ಮಹಾಗಣಪತಿ ಧ್ಯಾನ ಮತ್ತು ಆವಾಹನ",
            mantraDevanagari = "ॐ गणानां त्वा गणपतिं हवामहे कविं कवीनामुपमश्रवस्तमम् । ज्येष्ठराजं ब्रह्मणां ब्रह्मणस्पत आ नः शृण्वन्नूतिभिः सीद सादनम् ॥",
            mantraKannada = "ಓಂ ಗಣಾನಾಂ ತ್ವಾ ಗಣಪತಿಂ ಹವಾಮಹೇ ಕವಿಂ ಕವೀನಾಮುಪಮಶ್ರವಸ್ತಮಮ್ । ಜ್ಯೇಷ್ಠರಾಜಂ ಬ್ರಹ್ಮಣಾಂ ಬ್ರಹ್ಮಣಸ್ಪತ ಆ ನಃ ಶೃಣ್ವನ್ನೂತಿಭಿಃ ಸೀದ ಸಾದನಮ್ ॥",
            mantraTranslit = "oṁ gaṇānāṁ tvā gaṇapatiṁ havāmahe kaviṁ kavīnāmupamaśravastamam | jyeṣṭharājaṁ brahmaṇāṁ brahmaṇaspata ā naḥ śṛṇvannūtibhiḥ sīda sādanam ||",
            mantraCitation = "Rigveda Samhita 2.23.1 (Rishi Gritsamada, Trishtubh/Jagati)",
            practicalInstruction = "Fold palms in Anjali / Avahani Mudra. Visualize the benevolent, wisdom-bestowing form of Lord Ganesha and offer Durva grass or red hibiscus flowers. (Note: Traditionally, Tulasi is never offered to Ganesha).",
            innerSignificance = "Invoking the Supreme Leader of cosmic faculties, welcoming higher intelligence to preside over the sacred space of our heart.",
            requiredItem = "Durva Grass (3/5/21 blades) or Red Flowers",
            substituteRule = SubstituteRule(
                allowed = true,
                guidance = "If Durva grass or fresh flowers are absent, offer Akshata with devotion. Gita 9.26 affirms: 'Patram Pushpam Phalam Toyam' — offered with pure heart is accepted.",
                textualAuthority = "Rigveda 2.23.1 & Bhagavad Gita 9.26"
            ),
            associatedMudraName = "Avahani Mudra"
        ),
        GuidedPoojaStep(
            stepNumber = 4,
            upacharaName = "Panchamrita / Shuddhodaka Snanam",
            titleEn = "Bathing the Deity with Pure Water & Offerings",
            titleHi = "शुद्धोदक स्नान एवं गन्ध-अक्षत",
            titleKn = "ಶುದ್ಧೋದಕ ಸ್ನಾನ ಮತ್ತು ಗಂಧ-ಅಕ್ಷತೆ",
            mantraDevanagari = "मन्दाकिन्यास्तु यत्तोयं सर्वपापहरं शुभम् । तदिदं कल्पितं देव स्नानार्थं प्रतिगृह्यताम् ॥ ॐ गं गणपतये नमः — स्नानीयं जलं समर्पयामि ॥",
            mantraKannada = "ಮಂದಾಕಿನ್ಯಾಸ್ತು ಯತ್ತೋಯಂ ಸರ್ವಪಾಪಹರಂ ಶುಭಮ್ । ತದಿದಂ ಕಲ್ಪಿತಂ ದೇವ ಸ್ನಾನಾರ್ಥಂ ಪ್ರತಿಗೃಹ್ಯತಾಮ್ ॥ ಓಂ ಗಂ ಗಣಪತಯೇ ನಮಃ — ಸ್ನಾನೀಯಂ ಜಲಂ ಸಮರ್ಪಯಾಮಿ ॥",
            mantraTranslit = "mandākinyāstu yattoyaṁ sarvapāpaharaṁ śubham | tadidaṁ kalpitaṁ deva snānārthaṁ pratigṛhyatām || oṁ gaṁ gaṇapataye namaḥ — snānīyaṁ jalaṁ samarpayāmi ||",
            mantraCitation = "Padma Purana / Panchopachara Kalpa",
            practicalInstruction = "Offer a few drops of clean water with a spoon or flower into the plate in front of the Murti. Apply sandalwood paste (Chandana) and Kumkum gently to the forehead.",
            innerSignificance = "Water represents the sacred Mandakini river purifying mental impurities; sandalwood brings cooling peace.",
            requiredItem = "Pure Water Spoon & Sandalwood Paste",
            substituteRule = SubstituteRule(
                allowed = true,
                guidance = "If fresh sandalwood is unavailable, clean water or a pinch of turmeric powder mixed with water can be offered with gratitude.",
                textualAuthority = "Grihya Parishesha — Substitute Materials"
            ),
            associatedMudraName = "Dhenu Mudra"
        ),
        GuidedPoojaStep(
            stepNumber = 5,
            upacharaName = "Dhoopa, Deepa, Naivedyam & Mangala Aarti",
            titleEn = "Incense, Food Offering & Camphor Aarti",
            titleHi = "धूप, नैवेद्य एवं कर्पूर मंगल आरती",
            titleKn = "ಧೂಪ, ನೈವೇದ್ಯ ಮತ್ತು ಮಂಗಳಾರತಿ",
            mantraDevanagari = "वक्रतुण्ड महाकाय सूर्यकोटि समप्रभ । निर्विघ्नं कुरु मे देव सर्वकार्येषु सर्वदा ॥ ॐ गं गणपतये नमः — धूप-दीप-नैवेद्य-आरार्तिकं समर्पयामि ॥",
            mantraKannada = "ವಕ್ರತುಂಡ ಮಹಾಕಾಯ ಸೂರ್ಯಕೋಟಿ ಸಮಪ್ರಭ । ನಿರ್ವಿಘ್ನಂ ಕುರು ಮೇ ದೇವ ಸರ್ವಕಾರ್ಯೇಷು ಸರ್ವದಾ ॥ ಓಂ ಗಂ ಗಣಪತಯೇ ನಮಃ — ಧೂಪ-ದೀಪ-ನೈವೇದ್ಯ-ಆರಾರ್ತಿಕಂ ಸಮರ್ಪಯಾಮಿ ॥",
            mantraTranslit = "vakratuṇḍa mahākāya sūryakoṭi samaprabha | nirvighnaṁ kuru me deva sarvakāryeṣu sarvadā || oṁ gaṁ gaṇapataye namaḥ — dhūpa-dīpa-naivedya-ārārtikaṁ samarpayāmi ||",
            mantraCitation = "Mudgala Purana & Traditional Aarti Sangraha",
            practicalInstruction = "Wave natural incense. Offer fruit, jaggery, or soaked gram as Naivedyam with Prana-mudras. Conclude with waving the camphor aarti in clockwise circles.",
            innerSignificance = "Recognizing that all sustenance comes from the Divine before partaking as blessed Prasada.",
            requiredItem = "Jaggery (Guda), Banana, or Fresh Fruit + Camphor",
            substituteRule = SubstituteRule(
                allowed = true,
                guidance = "If cooked sweets or fruits are unavailable, a spoonful of milk, raisins, rock sugar (Mishri), or plain clean drinking water fulfills the Naivedyam requirement.",
                textualAuthority = "Taittiriya Upanishad (Anna Brahma) & Smarta Vidhi"
            ),
            associatedMudraName = "Prana-Apana-Vyana Mudra"
        )
    )

    val ESSENTIAL_HASTA_MUDRAS = listOf(
        HastaMudraGuide(
            name = "Anjali Mudra",
            sanskritName = "अञ्जलि मुद्रा",
            purpose = "Salutation, humility, centering awareness",
            visualDescription = "Both palms joined flat together in front of the chest/heart with fingertips pointing upward.",
            stepUsage = "Used during Prathana, Dhyana, and Namaskara."
        ),
        HastaMudraGuide(
            name = "Avahani Mudra",
            sanskritName = "आवाहनी मुद्रा",
            purpose = "Invoking the deity's presence",
            visualDescription = "Both hands cupped together with thumbs bent inwards towards the palms, inviting grace.",
            stepUsage = "Used during Step 3 (Avahanam) to invoke the divine form."
        ),
        HastaMudraGuide(
            name = "Dhenu Mudra (Amritikarana)",
            sanskritName = "धेनु मुद्रा (अमृतीकरण)",
            purpose = "Transforming offerings into celestial nectar",
            visualDescription = "Interlocking fingers so that the little finger of the right hand touches the ring finger of the left, and index touches middle, resembling a cow's udder.",
            stepUsage = "Used over water, Panchamrita, and Naivedyam offerings."
        ),
        HastaMudraGuide(
            name = "Matsya Mudra",
            sanskritName = "मत्स्य मुद्रा",
            purpose = "Protection and sanctification of water",
            visualDescription = "Placing the right palm flat on the back of the left hand with thumbs outstretched like the fins of a fish.",
            stepUsage = "Shown over the water Kalash during purification."
        ),
        HastaMudraGuide(
            name = "Shankha Mudra",
            sanskritName = "शङ्ख मुद्रा",
            purpose = "Resonance with the primordial sound (Om)",
            visualDescription = "Wrapping the four fingers of the right hand around the left thumb, while the right thumb touches the left index finger, resembling a conch shell.",
            stepUsage = "Shown during Aarti and bell ringing."
        ),
        HastaMudraGuide(
            name = "Naivedya Pancha-Prana Mudras",
            sanskritName = "पञ्चप्राण मुद्रा",
            purpose = "Offering food to the 5 vital currents (Prana, Apana, Vyana, Udana, Samana)",
            visualDescription = "Using ring, little and thumb for Prana; index, middle and thumb for Apana; ring, middle and thumb for Vyana; all fingers for Udana; palm for Samana.",
            stepUsage = "Performed while waving hand toward the altar during Naivedyam."
        )
    )

    val SAMSKARA_LIFECYCLE = listOf(
        SamskaraLifecycleItem("Garbhadhana", "गर्भाधान", "Conception", "Mindful consecration of marriage for conceiving life", "Pre-natal"),
        SamskaraLifecycleItem("Simantonnayana", "सीमान्तोन्नयन", "Pregnancy", "Baby shower & mental cheerfulness for the expectant mother", "4th–8th month of pregnancy"),
        SamskaraLifecycleItem("Namakarana", "नामकरण", "Childhood", "Formal naming of the newborn child with Vedic blessings", "11th or 12th day after birth"),
        SamskaraLifecycleItem("Annaprashana", "अन्नप्राशन", "Childhood", "First solid sacred food offering (Kheer/payasam)", "6th month"),
        SamskaraLifecycleItem("Chudakarana (Mundan)", "चूड़ाकरण", "Childhood", "First haircut purifying neonatal past-life ties", "1st or 3rd year"),
        SamskaraLifecycleItem("Aksharabhyasam / Vidyarambha", "विद्यारम्भ", "Education", "Initiation into the alphabet, Saraswati worship & reading", "5th year (Vijayadashami)"),
        SamskaraLifecycleItem("Upanayana (Yajnopavita)", "उपनयन", "Education", "Investiture with sacred thread & Gayatri mantra initiation", "8th–12th year"),
        SamskaraLifecycleItem("Vivaha Samskara", "विवाह", "Adulthood", "Sacred matrimony uniting two souls and families with Saptapadi", "Adulthood"),
        SamskaraLifecycleItem("Griha Pravesh", "गृहप्रवेश", "Household", "Vastu Shanti and welcoming Agni into a new home", "Before occupying new home"),
        SamskaraLifecycleItem("Shashtiabdapoorthi", "षष्ट्यब्दपूर्ति", "Elderhood", "Renewal of vows and thanksgiving on reaching 60th birthday", "60th Year"),
        SamskaraLifecycleItem("Antyeshti & Pitru Shraddha", "अन्त्येष्टि एवं श्राद्ध", "Bereavement & Ancestral", "Final rites with dignity & annual gratitude offering to ancestors", "Upon passing & Pitru Paksha")
    )
}
