package com.example.engine

data class TransparentFeeLedger(
    val dakshinaInr: Int,
    val samagriKitInr: Int,
    val localTravelInr: Int,
    val platformFeeInr: Int,
    val gstTaxInr: Int
) {
    val totalInr: Int get() = dakshinaInr + samagriKitInr + localTravelInr + platformFeeInr + gstTaxInr
}

data class PractitionerInfo(
    val id: String,
    val name: String,
    val title: String,
    val city: String,
    val state: String,
    val languages: List<String>,
    val traditions: List<String>,
    val yearsExperience: Int,
    val trainingInstitution: String,
    val badges: List<String>,
    val ceremonyTitle: String,
    val durationMinutes: Int,
    val feeLedger: TransparentFeeLedger,
    val rating: Double,
    val reviewsCount: Int
)

data class SacredTirthaRecord(
    val name: String,
    val category: String, // JYOTIRLINGA, SHAKTI_PEETHA, CHAR_DHAM, PANCHA_BHOOTA, DIVYA_DESAM
    val location: String,
    val deity: String,
    val architecturalStyle: String,
    val historicalPeriod: String,
    val sthalaPuranaSummary: String,
    val visitorAccessibility: String
)

object PractitionerMarketplace {

    val VERIFIED_PUJARIS = listOf(
        PractitionerInfo(
            id = "puj-101",
            name = "Vidwan Srihari Bhatta Ghanapathi",
            title = "Rigveda & Smarta Grihya Purohit",
            city = "Bengaluru",
            state = "Karnataka",
            languages = listOf("Kannada", "Sanskrit", "Telugu", "English"),
            traditions = listOf("Smarta (Bodhayana / Apastamba)", "Hoysala-Karnataka Sampradaya"),
            yearsExperience = 18,
            trainingInstitution = "Sri Sringeri Sharada Peetham Veda Pathashala (Ghana-Anta certification)",
            badges = listOf("Identity KYC Verified", "Veda Pathashala Audited", "Anti-Exploitation Zero-Fear Charter"),
            ceremonyTitle = "Griha Pravesh & Ganapati Homa",
            durationMinutes = 120,
            feeLedger = TransparentFeeLedger(dakshinaInr = 3500, samagriKitInr = 1200, localTravelInr = 300, platformFeeInr = 250, gstTaxInr = 45),
            rating = 4.96,
            reviewsCount = 142
        ),
        PractitionerInfo(
            id = "puj-102",
            name = "Acharya Vishwanath Shastri",
            title = "Shukla Yajurveda & Kashi Dharmashastra Scholar",
            city = "Varanasi / Online Pan-India",
            state = "Uttar Pradesh",
            languages = listOf("Hindi", "Sanskrit", "Bhojpuri", "English"),
            traditions = listOf("Madhyandina Shukla Yajurveda", "North Indian Purnimanta Vidhi"),
            yearsExperience = 24,
            trainingInstitution = "Sampurnanand Sanskrit Vishwavidyalaya, Varanasi (Acharya Degree)",
            badges = listOf("Identity KYC Verified", "University Degree Audited", "Anti-Exploitation Zero-Fear Charter"),
            ceremonyTitle = "Rudrabhishek & Satyanarayan Katha",
            durationMinutes = 90,
            feeLedger = TransparentFeeLedger(dakshinaInr = 2800, samagriKitInr = 950, localTravelInr = 250, platformFeeInr = 200, gstTaxInr = 36),
            rating = 4.94,
            reviewsCount = 219
        ),
        PractitionerInfo(
            id = "puj-103",
            name = "Dr. Aparna Chakravarty, Ph.D.",
            title = "Shaiva-Shakta Agama & Tantra Historian",
            city = "Kolkata / Online Pan-India",
            state = "West Bengal",
            languages = listOf("Bengali", "Sanskrit", "Hindi", "English"),
            traditions = listOf("Kashmiri Shaivism (Trika)", "Bengal Shakta Agama"),
            yearsExperience = 15,
            trainingInstitution = "Jadavpur University & Asiatic Society Manuscript Fellowship",
            badges = listOf("Identity KYC Verified", "Academic Publications Verified", "Educational Only — Zero Superstition"),
            ceremonyTitle = "1-on-1 Session: Understanding Tantra, Yantra & Chandi Meaning",
            durationMinutes = 60,
            feeLedger = TransparentFeeLedger(dakshinaInr = 1800, samagriKitInr = 0, localTravelInr = 0, platformFeeInr = 150, gstTaxInr = 27),
            rating = 4.98,
            reviewsCount = 87
        )
    )

    val SACRED_TIRTHAS = listOf(
        SacredTirthaRecord(
            name = "Kashi Vishwanath",
            category = "12 Jyotirlinga & Moksha Puri",
            location = "Varanasi, Uttar Pradesh",
            deity = "Lord Shiva (Vishveshwara)",
            architecturalStyle = "Nagara Temple Architecture (Reconstructed by Ahilyabai Holkar 1780)",
            historicalPeriod = "Continuous sacred site documented since Vedic / Skanda Purana era",
            sthalaPuranaSummary = "The eternal city standing upon Shiva's Trishula; center of Advaitic and Pauranic spiritual liberation.",
            visitorAccessibility = "Wheelchair accessible through Kashi Vishwanath Corridor, electric golf carts for seniors."
        ),
        SacredTirthaRecord(
            name = "Kedarnath Temple",
            category = "12 Jyotirlinga & Chota Char Dham",
            location = "Rudraprayag, Uttarakhand (3583m altitude)",
            deity = "Lord Shiva (Sadashiva)",
            architecturalStyle = "Katyuri / Nagara Massive Granite Stone Masonry",
            historicalPeriod = "Attributed to the Pandavas; revitalized by Adi Shankaracharya (8th century CE)",
            sthalaPuranaSummary = "The hump of Lord Shiva emerging in the Himalayas following the Mahabharata war.",
            visitorAccessibility = "Helicopter services, palanquins, and pony assistance available from Gaurikund."
        ),
        SacredTirthaRecord(
            name = "Brihadisvara Temple",
            category = "Great Living Chola Temple (UNESCO)",
            location = "Thanjavur, Tamil Nadu",
            deity = "Lord Shiva (Peruvudaiyar)",
            architecturalStyle = "Pure Classical Dravidian (Raja Raja Chola I, consecrated 1010 CE)",
            historicalPeriod = "Early 11th Century CE (Chola Empire zenith)",
            sthalaPuranaSummary = "Monolithic 80-tonne granite Kumbam apex capstone; exceptional acoustic mandapas and bronze casting.",
            visitorAccessibility = "Flat courtyard with ramps and battery buggies for elderly pilgrims."
        ),
        SacredTirthaRecord(
            name = "Kamakhya Temple",
            category = "51 Shakti Peethas (Yoni Peetha)",
            location = "Guwahati, Assam (Nilachal Hill)",
            deity = "Goddess Kamakhya (Mahamaya)",
            architecturalStyle = "Nilachal Style (Hemispherical dome atop cruciform base)",
            historicalPeriod = "Koch Kingdom reconstruction (1565 CE) over ancient Kamarupa foundations",
            sthalaPuranaSummary = "Primary seat of Shakta Tantra celebrating the natural creative power of Mother Earth during Ambubachi Mela.",
            visitorAccessibility = "Special senior citizen darshan queue and step-free access paths."
        ),
        SacredTirthaRecord(
            name = "Chidambaram Nataraja",
            category = "Pancha Bhoota Sthalam (Akasha / Space)",
            location = "Chidambaram, Tamil Nadu",
            deity = "Lord Nataraja & Chidambara Rahasyam",
            architecturalStyle = "Ancient Dravidian with Golden Gopuram (Chola & Pandya)",
            historicalPeriod = "Documented since Sangam era; patronized by Cholas",
            sthalaPuranaSummary = "The cosmic dance of Shiva in the hall of consciousness; worship of the formless Space element.",
            visitorAccessibility = "Flat stone walkways, traditional temple priest assistance."
        )
    )
}
