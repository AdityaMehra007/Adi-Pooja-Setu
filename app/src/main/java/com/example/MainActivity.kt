package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.data.repository.PoojaSetuRepository
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.components.BottomSacredNavBar
import com.example.ui.components.TopSacredHeader
import com.example.ui.screens.*
import com.example.ui.theme.MarbleIvory
import com.example.ui.theme.PoojaSetuTheme
import com.example.util.configureAppCheck
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check initialization with launch intent before any UI or Firebase calls
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            PoojaSetuTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val auth = Firebase.auth
    var currentUser by remember { mutableStateOf(auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { fAuth ->
            currentUser = fAuth.currentUser
        }
        auth.addAuthStateListener(listener)
        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null) {
        AuthScreen(
            onAuthSuccess = { currentUser = Firebase.auth.currentUser }
        )
    } else {
        val uid = currentUser!!.uid
        key(uid) {
            MainFeatureContainer(
                currentUserId = uid,
                onSignOut = {
                    auth.signOut()
                    currentUser = null
                }
            )
        }
    }
}

@Composable
fun MainFeatureContainer(
    currentUserId: String,
    onSignOut: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext
    val viewModel: MainViewModel = viewModel(
        key = currentUserId,
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val dbId = context.getString(R.string.firestore_database_id)
                val firestore = FirebaseFirestore.getInstance(dbId)
                val repository = PoojaSetuRepository(firestore)
                return MainViewModel(repository, currentUserId) as T
            }
        }
    )

    val currentTab by viewModel.currentTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val panchangReport by viewModel.panchangReport.collectAsState()
    val currentStepIndex by viewModel.currentStepIndex.collectAsState()
    val completedSteps by viewModel.completedStepNumbers.collectAsState()
    val showMissingMaterialModal by viewModel.showMissingMaterialModal.collectAsState()
    val selectedDurationMins by viewModel.selectedRoutineDurationMins.collectAsState()
    val poojaCompletedCelebration by viewModel.poojaCompletedCelebration.collectAsState()

    val selectedBirthNakshatra by viewModel.selectedBirthNakshatra.collectAsState()
    val tarabalamResult by viewModel.tarabalamResult.collectAsState()

    val userBookings by viewModel.bookingsFlow.collectAsState()
    val lastBookedRef by viewModel.lastBookedRef.collectAsState()

    val selectedCivPillar by viewModel.selectedCivPillar.collectAsState()

    val selectedProblemDomain by viewModel.selectedProblemDomain.collectAsState()
    val problemUserNote by viewModel.problemUserNote.collectAsState()
    val guidanceResultShown by viewModel.guidanceResultShown.collectAsState()
    val dincharyaHabits by viewModel.dincharyaHabits.collectAsState()

    val aiQuery by viewModel.aiQuery.collectAsState()
    val aiIsLoading by viewModel.aiIsLoading.collectAsState()
    val aiResponse by viewModel.aiResponse.collectAsState()

    Scaffold(
        topBar = {
            TopSacredHeader(
                preferredLanguage = userProfile.preferredLanguage,
                stateRegion = userProfile.stateRegion,
                cityName = userProfile.cityName,
                seniorMode = userProfile.seniorAccessibleMode,
                onLanguageChange = { viewModel.updateLanguage(it) },
                onRegionChange = { region, city -> viewModel.updateRegion(region, city) },
                onSeniorModeToggle = { viewModel.toggleSeniorMode() },
                onSignOut = onSignOut
            )
        },
        bottomBar = {
            BottomSacredNavBar(
                currentTab = currentTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MarbleIvory)
        ) {
            when (currentTab) {
                AppTab.HOME_POOJA -> {
                    DailyPoojaScreen(
                        userProfile = userProfile,
                        panchangReport = panchangReport,
                        poojaSteps = viewModel.poojaSteps,
                        currentStepIndex = currentStepIndex,
                        completedSteps = completedSteps,
                        showMissingMaterialModal = showMissingMaterialModal,
                        selectedDurationMins = selectedDurationMins,
                        poojaCompletedCelebration = poojaCompletedCelebration,
                        onSelectStep = { viewModel.selectStep(it) },
                        onToggleMissingMaterial = { viewModel.toggleMissingMaterialModal() },
                        onSetDuration = { viewModel.setRoutineDuration(it) },
                        onNextStep = { viewModel.markCurrentStepCompleteAndNext() },
                        onPreviousStep = { viewModel.selectStep(currentStepIndex - 1) },
                        onResetPooja = { viewModel.resetPooja() }
                    )
                }
                AppTab.PANCHANG -> {
                    PanchangScreen(
                        panchang = panchangReport,
                        selectedBirthNakshatra = selectedBirthNakshatra,
                        tarabalam = tarabalamResult,
                        onSelectBirthNakshatra = { viewModel.updateTarabalam(it) }
                    )
                }
                AppTab.MARKETPLACE -> {
                    MarketplaceScreen(
                        userBookings = userBookings,
                        lastBookedRef = lastBookedRef,
                        onDismissBookingNotice = { viewModel.dismissBookingConfirmation() },
                        onBookCeremony = { viewModel.bookCeremony(it) }
                    )
                }
                AppTab.CIVILISATION -> {
                    CivilisationScreen(
                        articles = viewModel.filteredArticles,
                        selectedPillar = selectedCivPillar,
                        onSelectPillar = { viewModel.setCivPillarFilter(it) }
                    )
                }
                AppTab.KARUNA_GURUKUL -> {
                    KarunaGurukulScreen(
                        selectedProblemDomain = selectedProblemDomain,
                        problemUserNote = problemUserNote,
                        guidanceResultShown = guidanceResultShown,
                        dincharyaHabits = dincharyaHabits,
                        aiQuery = aiQuery,
                        aiIsLoading = aiIsLoading,
                        aiResponse = aiResponse,
                        onSelectProblemDomain = { viewModel.setProblemDomain(it) },
                        onUpdateProblemNote = { viewModel.updateProblemNote(it) },
                        onSubmitProblem = { viewModel.submitProblemForRecord() },
                        onToggleDincharyaHabit = { viewModel.toggleDincharyaHabit(it) },
                        onAiQueryChange = { viewModel.setAiQuery(it) },
                        onAskAi = { viewModel.askPrajnaAi(it) }
                    )
                }
            }
        }
    }
}
