package com.example

import com.example.engine.DainikPoojaRepository
import com.example.engine.GeoLocation
import com.example.engine.KarunaEngine
import com.example.engine.PractitionerMarketplace
import com.example.engine.SuryaPanchangEngine
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class ExampleUnitTest {

    @Test
    fun testSuryaPanchangCalculations() {
        val report = SuryaPanchangEngine.computePanchang(
            date = Date(),
            location = GeoLocation(
                cityName = "New Delhi",
                latitude = 28.6139,
                longitude = 77.2090
            )
        )

        assertNotNull(report)
        assertTrue(report.tithiName.isNotEmpty())
        assertTrue(report.nakshatraName.isNotEmpty())
        assertTrue(report.nakshatraPada in 1..4)
        assertTrue(report.yogaName.isNotEmpty())
        assertTrue(report.karanaName.isNotEmpty())
        assertTrue(report.varaNameSanskrit.isNotEmpty())
        assertTrue(report.ayanamshaDegrees in 23.0..25.0)

        // Verify Daytime Octants
        assertTrue(report.rahuKaalStr.isNotEmpty())
        assertTrue(report.yamagandaStr.isNotEmpty())
        assertNotNull(report.abhijitMuhurtaStr)

        // Verify Dynamic Sanskrit Desha-Kala Sankalpa
        assertTrue(report.sankalpaFormulaSanskrit.contains("ॐ तत्सत्"))
        assertTrue(report.sankalpaFormulaSanskrit.contains("दैनिकपूजां करिष्ये"))
    }

    @Test
    fun testTarabalamCalculator() {
        // Ashwini (1) to Rohini (4): diff = 3 -> (3 % 9) + 1 = 4 -> Kshema Tara (Favorable)
        val taraResult = SuryaPanchangEngine.calculateTarabalam(
            birthNakshatraIdx1Based = 1,
            currentNakshatraIdx1Based = 4
        )
        assertNotNull(taraResult)
        assertEquals("Kshema Tara", taraResult.taraName)
        assertTrue(taraResult.isAuspicious)

        // Ashwini (1) to Bharani (2): diff = 1 -> (1 % 9) + 1 = 2 -> Sampat Tara (Auspicious)
        val sampatResult = SuryaPanchangEngine.calculateTarabalam(
            birthNakshatraIdx1Based = 1,
            currentNakshatraIdx1Based = 2
        )
        assertEquals("Sampat Tara", sampatResult.taraName)
        assertTrue(sampatResult.isAuspicious)
    }

    @Test
    fun testDainikPoojaLiturgyAndSubstitutes() {
        val steps = DainikPoojaRepository.DAILY_GANESHA_POOJA_STEPS
        assertTrue(steps.isNotEmpty())
        assertEquals(5, steps.size) // Core Daily Household Pooja Steps (Achamanam, Sankalpa, Avahana, Snana, Aarti)

        // Check first step has mantra and instructions
        val firstStep = steps.first()
        assertTrue(firstStep.mantraDevanagari.isNotEmpty())
        assertTrue(firstStep.mantraKannada.isNotEmpty())
        assertTrue(firstStep.mantraTranslit.isNotEmpty())
        assertTrue(firstStep.innerSignificance.isNotEmpty())

        // Verify Mudras
        val mudras = DainikPoojaRepository.ESSENTIAL_HASTA_MUDRAS
        assertTrue(mudras.isNotEmpty())

        // Verify 16 Samskaras lifecycle data
        val samskaras = DainikPoojaRepository.SAMSKARA_LIFECYCLE
        assertTrue(samskaras.size >= 10)
    }

    @Test
    fun testKarunaEmergencyGuidanceAndDincharya() {
        val guidance = KarunaEngine.GUIDANCE_MAP
        assertTrue(guidance.isNotEmpty())
        // Tele-MANAS emergency contact must be present in ANXIETY_GRIEF card
        val anxietyCard = guidance["ANXIETY_GRIEF"]
        assertNotNull(anxietyCard)
        assertTrue(anxietyCard!!.emergencyContact?.contains("14416") == true)

        val habits = KarunaEngine.getInitialDincharyaHabits()
        assertTrue(habits.size >= 4)
        assertTrue(habits.any { it.timeSlot.contains("Brahma Muhurta") })
    }

    @Test
    fun testPractitionerMarketplaceZeroFearPledge() {
        val practitioners = PractitionerMarketplace.VERIFIED_PUJARIS
        assertTrue(practitioners.isNotEmpty())
        // Verified Purohits all carry training & badges
        assertTrue(practitioners.all { it.badges.isNotEmpty() })
        assertTrue(practitioners.all { it.feeLedger.totalInr > 0 })

        val tirthas = PractitionerMarketplace.SACRED_TIRTHAS
        assertTrue(tirthas.size >= 5)
    }
}
