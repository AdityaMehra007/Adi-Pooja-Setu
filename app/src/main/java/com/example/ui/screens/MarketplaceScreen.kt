package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.CeremonyBookingRecord
import com.example.engine.PractitionerInfo
import com.example.engine.PractitionerMarketplace
import com.example.engine.SacredTirthaRecord
import com.example.ui.theme.*

@Composable
fun MarketplaceScreen(
    userBookings: List<CeremonyBookingRecord>,
    lastBookedRef: String?,
    onDismissBookingNotice: () -> Unit,
    onBookCeremony: (PractitionerInfo) -> Unit
) {
    var selectedSection by remember { mutableStateOf("PUJARIS") } // PUJARIS or TIRTHAS
    var bookingDetailPractitioner by remember { mutableStateOf<PractitionerInfo?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MarbleIvory)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SanctumMaroon),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Seva-Setu: Verified Practitioners & Tirthas",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = MarbleIvory,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Connect with certified Vedic Purohits, Agama Archakas, and Sanskrit Scholars for sacred household milestones. Protected by the Zero-Fear Anti-Exploitation Charter.",
                        color = MarbleIvory.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    // Section Toggle Pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = if (selectedSection == "PUJARIS") TempleGold else Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { selectedSection = "PUJARIS" }
                        ) {
                            Text(
                                text = "Purohits (${PractitionerMarketplace.VERIFIED_PUJARIS.size})",
                                color = if (selectedSection == "PUJARIS") SanctumMaroonDark else MarbleIvory,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            color = if (selectedSection == "TIRTHAS") TempleGold else Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { selectedSection = "TIRTHAS" }
                        ) {
                            Text(
                                text = "Tirthas (${PractitionerMarketplace.SACRED_TIRTHAS.size})",
                                color = if (selectedSection == "TIRTHAS") SanctumMaroonDark else MarbleIvory,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }

                        Surface(
                            color = if (selectedSection == "MAP") TempleGold else Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { selectedSection = "MAP" }
                        ) {
                            Text(
                                text = "🗺️ Atlas Map (63)",
                                color = if (selectedSection == "MAP") SanctumMaroonDark else MarbleIvory,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Booking confirmation banner if just booked
        if (lastBookedRef != null) {
            item {
                Surface(
                    color = EmeraldAuspiciousBg,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldAuspicious),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✓ Slot Locked & Ceremony Confirmed!",
                                color = EmeraldAuspicious,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Booking Ref: $lastBookedRef • Synced to cloud household schedule.",
                                color = CharcoalInk,
                                fontSize = 11.sp
                            )
                        }
                        IconButton(onClick = onDismissBookingNotice) {
                            Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = EmeraldAuspicious)
                        }
                    }
                }
            }
        }

        // SECTION A: VERIFIED PUJARIS LIST
        if (selectedSection == "PUJARIS") {
            items(PractitionerMarketplace.VERIFIED_PUJARIS) { p ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TempleGold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = p.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SanctumMaroon,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = p.title,
                                    fontSize = 12.sp,
                                    color = SacredSaffronDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "📍 ${p.city}, ${p.state} • ${p.yearsExperience} yrs experience",
                                    fontSize = 11.sp,
                                    color = MutedSlate
                                )
                            }
                            Surface(
                                color = WarmSand,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, TempleGold.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "★ ${p.rating} (${p.reviewsCount})",
                                    color = SanctumMaroon,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Training & Badges
                        Surface(
                            color = WarmSand,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "🏛️ Credentials: ${p.trainingInstitution}",
                                    fontSize = 11.sp,
                                    color = CharcoalInk
                                )
                                Text(
                                    text = "Languages: ${p.languages.joinToString(", ")}",
                                    fontSize = 11.sp,
                                    color = CharcoalInk
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            p.badges.forEach { b ->
                                Surface(
                                    color = EmeraldAuspiciousBg,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "✓ $b",
                                        color = EmeraldAuspicious,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Featured: ${p.ceremonyTitle} (${p.durationMinutes} mins)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MidnightIndigo
                        )

                        // Itemized Fee Summary
                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Transparent Total Package", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SanctumMaroon)
                                    Text(text = "₹${p.feeLedger.totalInr}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SanctumMaroon)
                                }
                                Text(
                                    text = "Dakshina: ₹${p.feeLedger.dakshinaInr} • Samagri: ₹${p.feeLedger.samagriKitInr} • Travel+Platform+GST: ₹${p.feeLedger.localTravelInr + p.feeLedger.platformFeeInr + p.feeLedger.gstTaxInr}",
                                    fontSize = 10.sp,
                                    color = MutedSlate
                                )
                            }
                        }

                        Button(
                            onClick = { onBookCeremony(p) },
                            colors = ButtonDefaults.buttonColors(containerColor = SanctumMaroon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("book_ceremony_button")
                        ) {
                            Text(
                                text = "Book Verified Ceremony (Zero-Fear Guarantee)",
                                color = MarbleIvory,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // SECTION B: SACRED TIRTHAS DIRECTORY
        if (selectedSection == "TIRTHAS") {
            items(PractitionerMarketplace.SACRED_TIRTHAS) { t ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, TempleGold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = t.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SanctumMaroon,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = t.category,
                                    fontSize = 11.sp,
                                    color = SacredSaffronDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "📍 ${t.location}",
                                    fontSize = 11.sp,
                                    color = MutedSlate
                                )
                            }
                        }

                        Text(
                            text = "Presiding: ${t.deity} • Style: ${t.architecturalStyle}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = CharcoalInk
                        )

                        Text(
                            text = t.sthalaPuranaSummary,
                            fontSize = 12.sp,
                            color = CharcoalInk,
                            lineHeight = 17.sp
                        )

                        Surface(
                            color = WarmSand,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Accessibility, contentDescription = null, tint = SanctumMaroon, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = t.visitorAccessibility,
                                    fontSize = 11.sp,
                                    color = CharcoalInk
                                )
                            }
                        }
                    }
                }
            }
        }

        // SECTION C: INTERACTIVE LEAFLET ATLAS MAP
        if (selectedSection == "MAP") {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SanctumMaroonDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Interactive Tirtha Yatra Sacred Atlas",
                            color = TempleGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Topography of Grace: Explore all 12 Dwadasha Jyotirlingas (🔱) and 51 Maha Shakti Peethas (🌺). Tap any marker on the map to inspect Sati Anga, deities, mantras, and purana history.",
                            color = MarbleIvory.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
            item {
                SacredTirthaMapView()
            }
        }
    }
}

@Composable
fun SacredTirthaMapView(modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(580.dp)
            .border(1.dp, TempleGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.allowFileAccess = true
                    webViewClient = WebViewClient()
                    loadUrl("file:///android_asset/tirtha_atlas.html")
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

