package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp

@IgnoreExtraProperties
data class UserProfile(
    @DocumentId val id: String = "",
    val userId: String = "",
    val displayName: String = "",
    val preferredLanguage: String = "en",
    val stateRegion: String = "Karnataka",
    val cityName: String = "Bengaluru",
    val calendarConvention: String = "Amanta",
    val defaultRoutineMins: Long = 10L,
    val seniorAccessibleMode: Boolean = false,
    val familyTraditionNote: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PoojaCompletionRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val poojaSlug: String = "",
    val poojaTitle: String = "",
    val durationSeconds: Long = 0L,
    val personalReflection: String = "",
    @ServerTimestamp val completedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PoojaScheduleItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val poojaSlug: String = "",
    val timeOfDay: String = "06:15",
    val recurrenceRule: String = "DAILY",
    val isActive: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class DisciplineProgressItem(
    @DocumentId val id: String = "",
    val userId: String = "",
    val pathwaySlug: String = "",
    val pathwayTitle: String = "",
    val currentDay: Long = 1L,
    val streakDays: Long = 1L,
    @ServerTimestamp val lastCompletedAt: Timestamp? = null
)

@IgnoreExtraProperties
data class CeremonyBookingRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val practitionerId: String = "",
    val practitionerName: String = "",
    val ceremonyTitle: String = "",
    val scheduledDate: String = "",
    val totalAmountInr: Long = 0L,
    val status: String = "CONFIRMED_SLOT_LOCKED",
    @ServerTimestamp val createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class ProblemSubmissionRecord(
    @DocumentId val id: String = "",
    val userId: String = "",
    val domain: String = "",
    val userNote: String = "",
    val practicalReferrals: String = "",
    val optionalDevotionalPractice: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)
