package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CeremonyBookingRecord
import com.example.data.model.DisciplineProgressItem
import com.example.data.model.PoojaCompletionRecord
import com.example.data.model.PoojaScheduleItem
import com.example.data.model.ProblemSubmissionRecord
import com.example.data.model.UserProfile
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class PoojaSetuRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in before accessing PoojaSetu records.")
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        val docRef = db.collection("users").document(userId)
        return try {
            val snapshot = docRef.get().await()
            if (snapshot.exists()) snapshot.toObject(UserProfile::class.java) else null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            null
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid)
        val payload = mapOf(
            "userId" to uid,
            "displayName" to profile.displayName,
            "preferredLanguage" to profile.preferredLanguage,
            "stateRegion" to profile.stateRegion,
            "cityName" to profile.cityName,
            "calendarConvention" to profile.calendarConvention,
            "defaultRoutineMins" to profile.defaultRoutineMins,
            "seniorAccessibleMode" to profile.seniorAccessibleMode,
            "familyTraditionNote" to profile.familyTraditionNote,
            "createdAt" to (profile.createdAt ?: FieldValue.serverTimestamp()),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    fun observeCompletions(userId: String): Flow<List<PoojaCompletionRecord>> {
        val path = "users/$userId/pooja_completions"
        return db.collection("users").document(userId).collection("pooja_completions")
            .snapshots()
            .map { it.toObjects(PoojaCompletionRecord::class.java) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun recordPoojaCompletion(record: PoojaCompletionRecord) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("pooja_completions").document()
        val payload = mapOf(
            "userId" to uid,
            "poojaSlug" to record.poojaSlug,
            "poojaTitle" to record.poojaTitle,
            "durationSeconds" to record.durationSeconds,
            "personalReflection" to record.personalReflection,
            "completedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    fun observeSchedules(userId: String): Flow<List<PoojaScheduleItem>> {
        val path = "users/$userId/schedules"
        return db.collection("users").document(userId).collection("schedules")
            .snapshots()
            .map { it.toObjects(PoojaScheduleItem::class.java) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun saveSchedule(schedule: PoojaScheduleItem) {
        val uid = requireUserId()
        val docRef = if (schedule.id.isNotBlank()) {
            db.collection("users").document(uid).collection("schedules").document(schedule.id)
        } else {
            db.collection("users").document(uid).collection("schedules").document()
        }
        val payload = mapOf(
            "userId" to uid,
            "poojaSlug" to schedule.poojaSlug,
            "timeOfDay" to schedule.timeOfDay,
            "recurrenceRule" to schedule.recurrenceRule,
            "isActive" to schedule.isActive,
            "createdAt" to (schedule.createdAt ?: FieldValue.serverTimestamp()),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    fun observeDisciplineProgress(userId: String): Flow<List<DisciplineProgressItem>> {
        val path = "users/$userId/discipline_progress"
        return db.collection("users").document(userId).collection("discipline_progress")
            .snapshots()
            .map { it.toObjects(DisciplineProgressItem::class.java) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun updateDisciplineProgress(item: DisciplineProgressItem) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("discipline_progress")
            .document(item.pathwaySlug)
        val payload = mapOf(
            "userId" to uid,
            "pathwaySlug" to item.pathwaySlug,
            "pathwayTitle" to item.pathwayTitle,
            "currentDay" to item.currentDay,
            "streakDays" to item.streakDays,
            "lastCompletedAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
            throw e
        }
    }

    fun observeBookings(userId: String): Flow<List<CeremonyBookingRecord>> {
        val path = "users/$userId/bookings"
        return db.collection("users").document(userId).collection("bookings")
            .snapshots()
            .map { it.toObjects(CeremonyBookingRecord::class.java) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val exceptionToLog = firestoreError ?: (error as? Exception)
                if (exceptionToLog != null && auth.currentUser?.uid == userId) {
                    handleFirestoreError(exceptionToLog, OperationType.LIST, path)
                }
                throw error
            }
    }

    suspend fun createBooking(booking: CeremonyBookingRecord): String {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("bookings").document()
        val payload = mapOf(
            "userId" to uid,
            "practitionerId" to booking.practitionerId,
            "practitionerName" to booking.practitionerName,
            "ceremonyTitle" to booking.ceremonyTitle,
            "scheduledDate" to booking.scheduledDate,
            "totalAmountInr" to booking.totalAmountInr,
            "status" to booking.status,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
            return docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }

    suspend fun saveProblemSubmission(record: ProblemSubmissionRecord) {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("problem_guidance").document()
        val payload = mapOf(
            "userId" to uid,
            "domain" to record.domain,
            "userNote" to record.userNote,
            "practicalReferrals" to record.practicalReferrals,
            "optionalDevotionalPractice" to record.optionalDevotionalPractice,
            "createdAt" to FieldValue.serverTimestamp()
        )
        try {
            docRef.set(payload).await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            throw e
        }
    }
}
