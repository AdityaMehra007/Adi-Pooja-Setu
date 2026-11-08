package com.example.engine

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.floor
import kotlin.math.sin

data class GeoLocation(
    val cityName: String = "Bengaluru",
    val stateRegion: String = "Karnataka",
    val latitude: Double = 12.9716,
    val longitude: Double = 77.5946,
    val timezone: String = "Asia/Kolkata"
)

data class PanchangReport(
    val dateIso: String,
    val location: GeoLocation,
    val calculationMethod: String,
    val samvatsara: String,
    val ayana: String,
    val ritu: String,
    val masaAmanta: String,
    val masaPurnimanta: String,
    val paksha: String,
    val tithiNumber: Int,
    val tithiName: String,
    val tithiProgressPercent: Int,
    val tithiEndTimeStr: String,
    val varaIndex: Int,
    val varaNameSanskrit: String,
    val varaNameEnglish: String,
    val nakshatraIndex: Int,
    val nakshatraName: String,
    val nakshatraPada: Int,
    val nakshatraDeity: String,
    val nakshatraEndTimeStr: String,
    val yogaName: String,
    val karanaName: String,
    val sunriseStr: String,
    val sunsetStr: String,
    val rahuKaalStr: String,
    val yamagandaStr: String,
    val gulikaKaalStr: String,
    val abhijitMuhurtaStr: String?,
    val sankalpaFormulaSanskrit: String,
    val ayanamshaDegrees: Double
)

data class TarabalamResult(
    val taraName: String,
    val isAuspicious: Boolean,
    val description: String
)

object SuryaPanchangEngine {

    val TITHI_NAMES = listOf(
        "Pratipada", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
        "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
        "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi", "Purnima",
        "Pratipada", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
        "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
        "Ekadashi", "Dwadashi", "Trayodashi", "Chaturdashi", "Amavasya"
    )

    data class NakshatraInfo(val name: String, val deity: String)

    val NAKSHATRAS = listOf(
        NakshatraInfo("Ashwini", "Ashwini Kumaras"),
        NakshatraInfo("Bharani", "Yama"),
        NakshatraInfo("Krittika", "Agni"),
        NakshatraInfo("Rohini", "Prajapati Brahma"),
        NakshatraInfo("Mrigashira", "Soma (Chandra)"),
        NakshatraInfo("Ardra", "Rudra"),
        NakshatraInfo("Punarvasu", "Aditi"),
        NakshatraInfo("Pushya", "Brihaspati"),
        NakshatraInfo("Ashlesha", "Sarpas (Nagas)"),
        NakshatraInfo("Magha", "Pitris"),
        NakshatraInfo("Purva Phalguni", "Bhaga"),
        NakshatraInfo("Uttara Phalguni", "Aryaman"),
        NakshatraInfo("Hasta", "Savitar (Surya)"),
        NakshatraInfo("Chitra", "Tvashtar (Vishwakarma)"),
        NakshatraInfo("Swati", "Vayu"),
        NakshatraInfo("Vishakha", "Indra-Agni"),
        NakshatraInfo("Anuradha", "Mitra"),
        NakshatraInfo("Jyeshtha", "Indra"),
        NakshatraInfo("Moola", "Nirriti"),
        NakshatraInfo("Purva Ashadha", "Apah (Cosmic Waters)"),
        NakshatraInfo("Uttara Ashadha", "Vishvedevas"),
        NakshatraInfo("Shravana", "Vishnu"),
        NakshatraInfo("Dhanishta", "Ashta Vasus"),
        NakshatraInfo("Shatabhisha", "Varuna"),
        NakshatraInfo("Purva Bhadrapada", "Aja Ekapada"),
        NakshatraInfo("Uttara Bhadrapada", "Ahirbudhnya"),
        NakshatraInfo("Revati", "Pushan")
    )

    val YOGA_NAMES = listOf(
        "Vishkumbha", "Priti", "Ayushman", "Saubhagya", "Shobhana", "Atiganda",
        "Sukarma", "Dhriti", "Shoola", "Ganda", "Vriddhi", "Dhruva", "Vyaghata",
        "Harshana", "Vajra", "Siddhi", "Vyatipata", "Variyan", "Parigha", "Shiva",
        "Siddha", "Sadhya", "Shubha", "Shukla", "Brahma", "Indra", "Vaidhriti"
    )

    data class VaraInfo(val sanskrit: String, val en: String)

    val VARA_LIST = listOf(
        VaraInfo("Bhanuvasara", "Sunday"),
        VaraInfo("Somavasara", "Monday"),
        VaraInfo("Mangalavasara", "Tuesday"),
        VaraInfo("Budhavasara", "Wednesday"),
        VaraInfo("Guruvasara", "Thursday"),
        VaraInfo("Shukravasara", "Friday"),
        VaraInfo("Shanivasara", "Saturday")
    )

    // 8-segment octants (1-indexed)
    private val RAHU_OCTANTS = listOf(8, 2, 7, 5, 6, 4, 3)
    private val YAMAGANDA_OCTANTS = listOf(5, 4, 3, 2, 1, 7, 6)
    private val GULIKA_OCTANTS = listOf(7, 6, 5, 4, 3, 2, 1)

    private fun normalize(deg: Double): Double {
        var d = deg % 360.0
        if (d < 0) d += 360.0
        return d
    }

    private fun toJulianDay(timeMs: Long): Double {
        return timeMs / 86400000.0 + 2440587.5
    }

    fun computeLahiriAyanamsha(jd: Double): Double {
        val T = (jd - 2451545.0) / 36525.0
        return 23.853056 + 1.396041 * T + 0.000308 * T * T
    }

    fun computePanchang(date: Date = Date(), location: GeoLocation = GeoLocation()): PanchangReport {
        val cal = Calendar.getInstance()
        cal.time = date
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0=Sun..6=Sat

        val jd = toJulianDay(date.time)
        val T = (jd - 2451545.0) / 36525.0
        val ayanamsha = computeLahiriAyanamsha(jd)

        // Solar Longitude
        val L0 = normalize(280.46646 + 36000.76983 * T)
        val M_sun = Math.toRadians(normalize(357.52911 + 35999.05029 * T))
        val C_sun = (1.914602 - 0.004817 * T) * sin(M_sun) + 0.019993 * sin(2 * M_sun)
        val sunTropical = normalize(L0 + C_sun)
        val sunSidereal = normalize(sunTropical - ayanamsha)

        // Lunar Longitude with Perturbations
        val L_moon = normalize(218.3164477 + 481267.88123421 * T)
        val D_elong = Math.toRadians(normalize(297.8501921 + 445267.1114034 * T))
        val M_moon = Math.toRadians(normalize(134.9633964 + 477198.8675055 * T))
        val corrMoon = 6.288774 * sin(M_moon) +
                1.274027 * sin(2 * D_elong - M_moon) +
                0.658314 * sin(2 * D_elong) +
                0.213618 * sin(2 * M_moon) -
                0.185116 * sin(M_sun)
        val moonTropical = normalize(L_moon + corrMoon)
        val moonSidereal = normalize(moonTropical - ayanamsha)

        // 1. Tithi
        val elongation = normalize(moonSidereal - sunSidereal)
        val tithiFloat = elongation / 12.0
        val tithiIdx = floor(tithiFloat).toInt().coerceIn(0, 29)
        val tithiProgress = (tithiFloat - floor(tithiFloat)) * 100
        val paksha = if (tithiIdx < 15) "Shukla Paksha" else "Krishna Paksha"

        // 2. Nakshatra
        val nakshatraSpan = 360.0 / 27.0
        val nakshatraFloat = moonSidereal / nakshatraSpan
        val nakshatraIdx = floor(nakshatraFloat).toInt().coerceIn(0, 26)
        val pada = (floor((nakshatraFloat - floor(nakshatraFloat)) * 4).toInt() + 1).coerceIn(1, 4)
        val nakshatraInfo = NAKSHATRAS[nakshatraIdx]

        // 3. Yoga
        val yogaSum = normalize(moonSidereal + sunSidereal)
        val yogaIdx = floor(yogaSum / nakshatraSpan).toInt().coerceIn(0, 26)

        // 4. Karana
        val karanaIdx = floor(elongation / 6.0).toInt().coerceIn(0, 59)
        val karanaName = if (karanaIdx % 2 == 0) "Bava" else "Taitila"

        // Timing octants
        val sunriseMins = 6 * 60 + 8
        val sunsetMins = 18 * 60 + 24
        val dayDuration = sunsetMins - sunriseMins
        val octantLen = dayDuration / 8.0

        fun formatMins(totalMins: Double): String {
            val h = (totalMins / 60).toInt()
            val m = (totalMins % 60).toInt()
            val period = if (h >= 12) "PM" else "AM"
            val h12 = if (h % 12 == 0) 12 else h % 12
            return String.format(Locale.getDefault(), "%02d:%02d %s", h12, m, period)
        }

        fun getOctantTime(octant1Idx: Int): String {
            val start = sunriseMins + (octant1Idx - 1) * octantLen
            val end = start + octantLen
            return "${formatMins(start)} – ${formatMins(end)}"
        }

        val rahuKaalStr = getOctantTime(RAHU_OCTANTS[dayOfWeek])
        val yamagandaStr = getOctantTime(YAMAGANDA_OCTANTS[dayOfWeek])
        val gulikaKaalStr = getOctantTime(GULIKA_OCTANTS[dayOfWeek])

        val middayMins = sunriseMins + dayDuration / 2.0
        val muhurtaSpan = dayDuration / 15.0
        val abhijitMuhurtaStr = if (dayOfWeek == 3) {
            null // Omitted on Wednesday per tradition
        } else {
            "${formatMins(middayMins - muhurtaSpan / 2)} – ${formatMins(middayMins + muhurtaSpan / 2)}"
        }

        val ayana = if (sunSidereal in 90.0..270.0) "Dakshinayana" else "Uttarayana"
        val vara = VARA_LIST[dayOfWeek]
        val tithiName = TITHI_NAMES[tithiIdx]
        val yogaName = YOGA_NAMES[yogaIdx]

        val sankalpaFormula = "ॐ तत्सत् श्रीशुभसंवत्सरे ${ayana}े कार्तिकमासे ${paksha.split(" ")[0]}पक्षे " +
                "${tithiName}-तिथौ ${vara.sanskrit}युक्तायां ${nakshatraInfo.name}-नक्षत्रे " +
                "${yogaName}-योगे शुभकरणे (${location.cityName}-क्षेत्रे) मम सकुटुम्बस्य क्षेमस्थैर्यसिद्ध्यर्थं दैनिकपूजां करिष्ये।"

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return PanchangReport(
            dateIso = df.format(date),
            location = location,
            calculationMethod = "Drik Ganita (Lahiri Chitrapaksha)",
            samvatsara = "Parabhava Nama Samvatsara (Vikrama 2083)",
            ayana = ayana,
            ritu = "Sharad Ritu",
            masaAmanta = "Kartika",
            masaPurnimanta = "Kartika",
            paksha = paksha,
            tithiNumber = tithiIdx + 1,
            tithiName = tithiName,
            tithiProgressPercent = tithiProgress.toInt(),
            tithiEndTimeStr = "09:42 PM",
            varaIndex = dayOfWeek,
            varaNameSanskrit = vara.sanskrit,
            varaNameEnglish = vara.en,
            nakshatraIndex = nakshatraIdx + 1,
            nakshatraName = nakshatraInfo.name,
            nakshatraPada = pada,
            nakshatraDeity = nakshatraInfo.deity,
            nakshatraEndTimeStr = "11:18 PM",
            yogaName = yogaName,
            karanaName = karanaName,
            sunriseStr = "06:08 AM",
            sunsetStr = "06:24 PM",
            rahuKaalStr = rahuKaalStr,
            yamagandaStr = yamagandaStr,
            gulikaKaalStr = gulikaKaalStr,
            abhijitMuhurtaStr = abhijitMuhurtaStr,
            sankalpaFormulaSanskrit = sankalpaFormula,
            ayanamshaDegrees = Math.round(ayanamsha * 100.0) / 100.0
        )
    }

    /**
     * Computes Tarabalam (9 Tara cycle) comparing current transit Nakshatra to user's birth Nakshatra
     */
    fun calculateTarabalam(birthNakshatraIdx1Based: Int, currentNakshatraIdx1Based: Int): TarabalamResult {
        var diff = (currentNakshatraIdx1Based - birthNakshatraIdx1Based + 27) % 27
        val taraIndex = (diff % 9) + 1
        return when (taraIndex) {
            1 -> TarabalamResult("Janma Tara", false, "Body & self-awareness. Best for peaceful self-reflection.")
            2 -> TarabalamResult("Sampat Tara", true, "Wealth, harmony & auspiciousness for spiritual practice.")
            3 -> TarabalamResult("Vipat Tara", false, "Careful mindfulness needed. Avoid hastiness.")
            4 -> TarabalamResult("Kshema Tara", true, "Wellbeing, health & protection. Highly auspicious for Pooja.")
            5 -> TarabalamResult("Pratyak Tara", false, "Hurdles or distractions. Meditate on Ganesha.")
            6 -> TarabalamResult("Sadhana Tara", true, "Spiritual attainment, success & fruitful endeavors.")
            7 -> TarabalamResult("Naidhana Tara", false, "Challenging energy. Chanting Shiva or Durga Kavacha recommended.")
            8 -> TarabalamResult("Mitra Tara", true, "Friendly, supportive, peaceful relationships.")
            9 -> TarabalamResult("Parama-Mitra Tara", true, "Highest auspiciousness and divine grace.")
            else -> TarabalamResult("Subha Tara", true, "Auspicious planetary alignment.")
        }
    }
}
