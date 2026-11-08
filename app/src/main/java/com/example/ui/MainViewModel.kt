package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.PrajnaAiResponse
import com.example.ai.PrajnaAiService
import com.example.data.model.CeremonyBookingRecord
import com.example.data.model.DisciplineProgressItem
import com.example.data.model.PoojaCompletionRecord
import com.example.data.model.ProblemSubmissionRecord
import com.example.data.model.UserProfile
import com.example.data.repository.PoojaSetuRepository
import com.example.engine.CivilisationEngine
import com.example.engine.CivilisationEntry
import com.example.engine.DainikPoojaRepository
import com.example.engine.DincharyaHabit
import com.example.engine.GeoLocation
import com.example.engine.GuidedPoojaStep
import com.example.engine.KarunaEngine
import com.example.engine.PanchangReport
import com.example.engine.PractitionerInfo
import com.example.engine.PractitionerMarketplace
import com.example.engine.SuryaPanchangEngine
import com.example.engine.TarabalamResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

enum class AppTab(val title: String) {
    HOME_POOJA("Daily Pooja"),
    PANCHANG("Drik Panchang"),
    MARKETPLACE("Seva-Setu"),
    CIVILISATION("Civilisation"),
    KARUNA_GURUKUL("Karuna & Gurukul")
}

class MainViewModel(
    private val repository: PoojaSetuRepository,
    val currentUserId: String
) : ViewModel() {

    private val prajnaService = PrajnaAiService()

    // Active Navigation
    private val _currentTab = MutableStateFlow(AppTab.HOME_POOJA)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // User Profile
    private val _userProfile = MutableStateFlow(
        UserProfile(
            userId = currentUserId,
            displayName = "Devotee",
            preferredLanguage = "en",
            stateRegion = "Karnataka",
            cityName = "Bengaluru",
            calendarConvention = "Amanta",
            defaultRoutineMins = 10L,
            seniorAccessibleMode = false,
            familyTraditionNote = "Smarta daily household puja tradition"
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Panchang State
    private val _panchangReport = MutableStateFlow(
        SuryaPanchangEngine.computePanchang(Date(), GeoLocation("Bengaluru", "Karnataka", 12.9716, 77.5946))
    )
    val panchangReport: StateFlow<PanchangReport> = _panchangReport.asStateFlow()

    // Tarabalam State
    private val _selectedBirthNakshatra = MutableStateFlow(1) // 1 = Ashwini
    val selectedBirthNakshatra: StateFlow<Int> = _selectedBirthNakshatra.asStateFlow()

    private val _tarabalamResult = MutableStateFlow(
        SuryaPanchangEngine.calculateTarabalam(1, _panchangReport.value.nakshatraIndex)
    )
    val tarabalamResult: StateFlow<TarabalamResult> = _tarabalamResult.asStateFlow()

    // Guided Pooja Player State
    val poojaSteps: List<GuidedPoojaStep> = DainikPoojaRepository.DAILY_GANESHA_POOJA_STEPS

    private val _currentStepIndex = MutableStateFlow(0)
    val currentStepIndex: StateFlow<Int> = _currentStepIndex.asStateFlow()

    private val _completedStepNumbers = MutableStateFlow<Set<Int>>(emptySet())
    val completedStepNumbers: StateFlow<Set<Int>> = _completedStepNumbers.asStateFlow()

    private val _showMissingMaterialModal = MutableStateFlow(false)
    val showMissingMaterialModal: StateFlow<Boolean> = _showMissingMaterialModal.asStateFlow()

    private val _selectedRoutineDurationMins = MutableStateFlow(10)
    val selectedRoutineDurationMins: StateFlow<Int> = _selectedRoutineDurationMins.asStateFlow()

    private val _poojaCompletedCelebration = MutableStateFlow(false)
    val poojaCompletedCelebration: StateFlow<Boolean> = _poojaCompletedCelebration.asStateFlow()

    // Realtime Firestore Collections
    val completionsFlow = repository.observeCompletions(currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    val bookingsFlow = repository.observeBookings(currentUserId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

    // Civilisation & SATYA state
    private val _selectedCivPillar = MutableStateFlow("ALL")
    val selectedCivPillar: StateFlow<String> = _selectedCivPillar.asStateFlow()

    val filteredArticles: List<CivilisationEntry>
        get() = if (_selectedCivPillar.value == "ALL") {
            CivilisationEngine.ARTICLES
        } else {
            CivilisationEngine.ARTICLES.filter { it.pillar == _selectedCivPillar.value }
        }

    // Karuna Guidance State
    private val _selectedProblemDomain = MutableStateFlow("ANXIETY_GRIEF")
    val selectedProblemDomain: StateFlow<String> = _selectedProblemDomain.asStateFlow()

    private val _problemUserNote = MutableStateFlow("")
    val problemUserNote: StateFlow<String> = _problemUserNote.asStateFlow()

    private val _guidanceResultShown = MutableStateFlow(true)
    val guidanceResultShown: StateFlow<Boolean> = _guidanceResultShown.asStateFlow()

    // Dincharya Habits
    private val _dincharyaHabits = MutableStateFlow(KarunaEngine.getInitialDincharyaHabits())
    val dincharyaHabits: StateFlow<List<DincharyaHabit>> = _dincharyaHabits.asStateFlow()

    // PRAJNA AI Chat
    private val _aiQuery = MutableStateFlow("")
    val aiQuery: StateFlow<String> = _aiQuery.asStateFlow()

    private val _aiIsLoading = MutableStateFlow(false)
    val aiIsLoading: StateFlow<Boolean> = _aiIsLoading.asStateFlow()

    private val _aiResponse = MutableStateFlow<PrajnaAiResponse?>(
        PrajnaAiResponse(
            answerText = "Namaste! I am PRAJNA, your source-verified guide. In Vedic and Agama practice, every ritual action (Upachara) has an outer physical purification and an inner conscious intention (Bhavana). Ask me about any mantra, missing ritual item, Panchang calculation, or ancient Indian science.",
            epistemicBadge = "CAT-A • Primary Textual Source",
            primaryCitation = "PoojaSetu SATYA Knowledge Graph"
        )
    )
    val aiResponse: StateFlow<PrajnaAiResponse?> = _aiResponse.asStateFlow()

    // Marketplace Booking Confirmation
    private val _lastBookedRef = MutableStateFlow<String?>(null)
    val lastBookedRef: StateFlow<String?> = _lastBookedRef.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val profile = repository.getUserProfile(currentUserId)
            if (profile != null) {
                _userProfile.value = profile
                updateLocationAndPanchang(profile.cityName, profile.stateRegion)
            } else {
                repository.saveUserProfile(_userProfile.value)
            }
        }
    }

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun updateLanguage(lang: String) {
        _userProfile.value = _userProfile.value.copy(preferredLanguage = lang)
        viewModelScope.launch {
            repository.saveUserProfile(_userProfile.value)
        }
    }

    fun toggleSeniorMode() {
        val next = !_userProfile.value.seniorAccessibleMode
        _userProfile.value = _userProfile.value.copy(seniorAccessibleMode = next)
        viewModelScope.launch {
            repository.saveUserProfile(_userProfile.value)
        }
    }

    fun updateRegion(region: String, city: String) {
        _userProfile.value = _userProfile.value.copy(stateRegion = region, cityName = city)
        updateLocationAndPanchang(city, region)
        viewModelScope.launch {
            repository.saveUserProfile(_userProfile.value)
        }
    }

    fun updateLocationAndPanchang(cityName: String, stateRegion: String) {
        val geo = when (stateRegion.uppercase()) {
            "TAMIL NADU" -> GeoLocation(cityName, stateRegion, 13.0827, 80.2707)
            "MAHARASHTRA" -> GeoLocation(cityName, stateRegion, 18.5204, 73.8567)
            "UTTAR PRADESH" -> GeoLocation(cityName, stateRegion, 25.3176, 82.9739)
            "WEST BENGAL" -> GeoLocation(cityName, stateRegion, 22.5726, 88.3639)
            else -> GeoLocation(cityName, stateRegion, 12.9716, 77.5946)
        }
        _panchangReport.value = SuryaPanchangEngine.computePanchang(Date(), geo)
        updateTarabalam(_selectedBirthNakshatra.value)
    }

    fun updateTarabalam(birthNakshatraIdx: Int) {
        _selectedBirthNakshatra.value = birthNakshatraIdx
        _tarabalamResult.value = SuryaPanchangEngine.calculateTarabalam(
            birthNakshatraIdx,
            _panchangReport.value.nakshatraIndex
        )
    }

    fun selectStep(index: Int) {
        _currentStepIndex.value = index.coerceIn(0, poojaSteps.size - 1)
        _showMissingMaterialModal.value = false
    }

    fun toggleMissingMaterialModal() {
        _showMissingMaterialModal.value = !_showMissingMaterialModal.value
    }

    fun setRoutineDuration(mins: Int) {
        _selectedRoutineDurationMins.value = mins
    }

    fun markCurrentStepCompleteAndNext() {
        val stepNum = poojaSteps[_currentStepIndex.value].stepNumber
        _completedStepNumbers.value = _completedStepNumbers.value + stepNum

        if (_currentStepIndex.value < poojaSteps.size - 1) {
            _currentStepIndex.value += 1
            _showMissingMaterialModal.value = false
        } else {
            // Pooja Completed!
            _poojaCompletedCelebration.value = true
            viewModelScope.launch {
                repository.recordPoojaCompletion(
                    PoojaCompletionRecord(
                        userId = currentUserId,
                        poojaSlug = "daily-ganesha",
                        poojaTitle = "Daily Mahaganapati Pooja",
                        durationSeconds = (_selectedRoutineDurationMins.value * 60).toLong(),
                        personalReflection = "Completed step sequence with peace and focus."
                    )
                )
            }
        }
    }

    fun resetPooja() {
        _currentStepIndex.value = 0
        _completedStepNumbers.value = emptySet()
        _poojaCompletedCelebration.value = false
        _showMissingMaterialModal.value = false
    }

    fun bookCeremony(practitioner: PractitionerInfo) {
        viewModelScope.launch {
            val booking = CeremonyBookingRecord(
                userId = currentUserId,
                practitionerId = practitioner.id,
                practitionerName = practitioner.name,
                ceremonyTitle = practitioner.ceremonyTitle,
                scheduledDate = "Upcoming Auspicious Muhurta",
                totalAmountInr = practitioner.feeLedger.totalInr.toLong(),
                status = "CONFIRMED_SLOT_LOCKED"
            )
            val ref = repository.createBooking(booking)
            _lastBookedRef.value = ref
        }
    }

    fun dismissBookingConfirmation() {
        _lastBookedRef.value = null
    }

    fun setCivPillarFilter(pillar: String) {
        _selectedCivPillar.value = pillar
    }

    fun setProblemDomain(domain: String) {
        _selectedProblemDomain.value = domain
        _guidanceResultShown.value = true
    }

    fun updateProblemNote(note: String) {
        _problemUserNote.value = note
    }

    fun submitProblemForRecord() {
        viewModelScope.launch {
            val domain = _selectedProblemDomain.value
            val guidance = KarunaEngine.GUIDANCE_MAP[domain]
            if (guidance != null) {
                repository.saveProblemSubmission(
                    ProblemSubmissionRecord(
                        userId = currentUserId,
                        domain = domain,
                        userNote = _problemUserNote.value,
                        practicalReferrals = guidance.practicalResource,
                        optionalDevotionalPractice = guidance.devotionalSupport
                    )
                )
            }
        }
    }

    fun toggleDincharyaHabit(habitId: String) {
        _dincharyaHabits.value = _dincharyaHabits.value.map {
            if (it.id == habitId) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    fun setAiQuery(q: String) {
        _aiQuery.value = q
    }

    fun askPrajnaAi(customQuery: String? = null) {
        val queryToRun = customQuery ?: _aiQuery.value
        if (queryToRun.isBlank()) return

        _aiIsLoading.value = true
        viewModelScope.launch {
            val resp = prajnaService.askPrajna(queryToRun, _userProfile.value.stateRegion)
            _aiResponse.value = resp
            _aiIsLoading.value = false
            _aiQuery.value = ""
        }
    }
}
