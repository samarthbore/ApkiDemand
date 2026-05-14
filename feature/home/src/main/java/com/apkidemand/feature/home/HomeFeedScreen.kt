package com.apkidemand.feature.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.apkidemand.feature.home.presentation.HomeViewModel

// ═══════════════════════════════════════════════════════════
//  BUYER FEED — Shows seller offers/deals nearby
// ═══════════════════════════════════════════════════════════


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyerFeedScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onOfferClick: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val offers by viewModel.offers.collectAsState()
    
    val mappedOffers = offers.map { entity ->
        OfferItem(
            id = entity.id,
            shopName = entity.shopName,
            shopInitials = entity.shopInitials,
            sellerName = "Seller",
            title = entity.title,
            description = entity.description,
            category = "Fashion", // Hardcoded for this specific app niche
            categoryEmoji = "👗",
            price = entity.price,
            originalPrice = null,
            discountBadge = null,
            isNegotiable = entity.isNegotiable,
            areaName = "Local",
            cityName = "City",
            distance = "Nearby",
            rating = 4.5,
            totalOffers = 10,
            imageUrl = entity.imageUrls.firstOrNull() ?: "https://images.unsplash.com/photo-1610189013233-030cdb46a2ee?auto=format&fit=crop&q=80&w=400",
            timeAgo = "Just now",
            isVerified = true
        )
    }

    val filtered = if (selectedCategory == "All") mappedOffers
    else mappedOffers.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.LocationOn, "Location", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Your Location", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp), color = MaterialTheme.colorScheme.onBackground)
                            Icon(Icons.Filled.KeyboardArrowDown, "Change", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                        }
                        Text("Local Area", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                                Icon(Icons.Filled.LocationOn, "Radius", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(10.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("2.5 km radius", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                
                // Right: Actions
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Search, "Search", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        BadgedBox(badge = { Badge(containerColor = MaterialTheme.colorScheme.primary) { Text("2", color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp) } }) {
                            Icon(Icons.Filled.Notifications, "Notifications", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
                        }
                    }
                    Box(modifier = Modifier.size(38.dp)) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1599566150163-29194dcaad36?auto=format&fit=crop&q=80&w=150",
                            contentDescription = "Profile",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00E676))
                                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                        )
                    }
                }
            }
        }

        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEDE8FF)), // Light purple background for the bag icon like the image
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✨", modifier = Modifier.align(Alignment.TopStart).padding(4.dp), fontSize = 10.sp)
                            Icon(Icons.Filled.ShoppingBag, "Demand", tint = Color(0xFF6C5CE7), modifier = Modifier.size(22.dp))
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text("Post local shopkeepers in", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.height(2.dp))
                            Text("Busyland & nearby markets", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondary // Gold color
                    ) {
                        Text(
                            "+ Post Your Demand",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Category chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categoryFilters) { (name, emoji) ->
                    val isSelected = selectedCategory == name
                    val bgColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = bgColor,
                        onClick = { selectedCategory = name },
                        modifier = Modifier.height(40.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            if (name == "More") {
                                Icon(Icons.Filled.GridView, "More", tint = textColor, modifier = Modifier.size(16.dp))
                            } else {
                                Text(emoji, fontSize = 16.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(name, color = textColor, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Nearby Sellers Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Nearby Sellers", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp), color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.width(12.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text("${filtered.size} Active", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium), color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Sort by", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.clickable { }.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Popular", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Icon(Icons.Filled.KeyboardArrowDown, "Sort", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Offer Cards
        items(filtered, key = { it.id }) { offer ->
            OfferCardHorizontal(offer = offer, onClick = { onOfferClick(offer.id) }, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
        }
    }
}

@Composable
fun OfferCardHorizontal(offer: OfferItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.height(160.dp).padding(8.dp)) {
            // Left Image Section
            Box(
                modifier = Modifier
                    .width(130.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = offer.imageUrl,
                    contentDescription = offer.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Category Overlay
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(offer.categoryEmoji, fontSize = 10.sp)
                        Spacer(Modifier.width(4.dp))
                        Text(offer.category, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Right Content Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp, top = 14.dp, bottom = 14.dp, end = 12.dp)
            ) {
                // Top Row: Title, Time, Heart
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = offer.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(offer.timeAgo, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Icon(Icons.Outlined.FavoriteBorder, "Save", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(4.dp))

                // Description
                Text(
                    text = offer.description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(8.dp))

                // Pricing
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹${offer.price}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary) // Golden price
                    if (offer.originalPrice != null) {
                        Spacer(Modifier.width(6.dp))
                        Text("₹${offer.originalPrice}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough))
                    }
                    if (offer.discountBadge != null) {
                        Spacer(Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text("Negotiable", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Bottom Row: Shop Info & Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Shop Avatar & Details
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(offer.shopInitials, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(offer.shopName, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                                if (offer.isVerified) {
                                    Spacer(Modifier.width(4.dp))
                                    Icon(Icons.Filled.Verified, "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(12.dp))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, "Rating", tint = Color(0xFFFFB300), modifier = Modifier.size(10.dp))
                                Spacer(Modifier.width(2.dp))
                                Text("${offer.rating} (${offer.totalOffers}) · ${offer.areaName} · ${offer.distance}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    // Action Button
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("View Details", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onPrimary)
                            Spacer(Modifier.width(4.dp))
                            Icon(Icons.Filled.ArrowForward, "Go", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(12.dp))
                        }
                    }
                }
            }
        }
    }
}


// ═══════════════════════════════════════════════════════════
//  SELLER FEED — Shows buyer demands nearby
// ═══════════════════════════════════════════════════════════

@Composable
fun SellerFeedScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onDemandClick: (String) -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val demands by viewModel.demands.collectAsState()
    
    val mappedDemands = demands.map { entity ->
        DemandItem(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            category = entity.category.ifEmpty { "Fashion" },
            categoryEmoji = "👗",
            buyerName = entity.userName,
            buyerInitials = entity.userInitials,
            areaName = entity.areaName,
            cityName = entity.cityName,
            budgetMin = null,
            budgetMax = entity.budget,
            offerCount = entity.offerCount,
            isUrgent = false,
            timeAgo = "Just now",
            photoCount = entity.imageUrls.size
        )
    }

    val filtered = if (selectedCategory == "All") mappedDemands
    else mappedDemands.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.LocationOn, "Location", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Column {
                            Text("Your Location", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Local Area", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = {}) { Icon(Icons.Filled.Search, "Search", tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                        IconButton(onClick = {}) {
                            BadgedBox(badge = { Badge(containerColor = MaterialTheme.colorScheme.error) { Text("5") } }) {
                                Icon(Icons.Filled.Notifications, "Notifications", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text("🏪 Demands near you", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Medium)
            }
        }

        item {
            LazyRow(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categoryFilters) { (name, emoji) ->
                    val isSelected = selectedCategory == name
                    val bgColor by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        tween(200), label = "chip")
                    val textColor by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        tween(200), label = "chipText")
                    Surface(shape = RoundedCornerShape(12.dp), color = bgColor,
                        onClick = { selectedCategory = name }, modifier = Modifier.height(40.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 14.dp)) {
                            Text(emoji, fontSize = 16.sp); Spacer(Modifier.width(6.dp))
                            Text(name, color = textColor, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("What Buyers Need", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("${filtered.size} active", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        items(filtered, key = { it.id }) { demand ->
            DemandCard(demand = demand, onClick = { onDemandClick(demand.id) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp))
        }
    }
}

@Composable
fun DemandCard(demand: DemandItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.secondaryContainer) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(demand.categoryEmoji, fontSize = 12.sp); Spacer(Modifier.width(4.dp))
                            Text(demand.category, style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Medium)
                        }
                    }
                    if (demand.isUrgent) {
                        Surface(shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.errorContainer) {
                            Text("⚡ Urgent", style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
                Text(demand.timeAgo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(10.dp))
            Text(demand.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(4.dp))
            Text(demand.description, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 20.sp)
            Spacer(Modifier.height(10.dp))
            if (demand.budgetMin != null || demand.budgetMax != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💰", fontSize = 14.sp); Spacer(Modifier.width(6.dp))
                    Text(
                        text = when {
                            demand.budgetMin != null && demand.budgetMax != null -> "₹${demand.budgetMin} – ₹${demand.budgetMax}"
                            demand.budgetMax != null -> "Up to ₹${demand.budgetMax}"
                            else -> "₹${demand.budgetMin}+"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(28.dp).clip(CircleShape)
                        .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary))),
                        contentAlignment = Alignment.Center) {
                        Text(demand.buyerInitials, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(demand.buyerName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                        Text("${demand.areaName}, ${demand.cityName}", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                }
                Surface(shape = RoundedCornerShape(10.dp),
                    color = if (demand.offerCount > 0) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocalOffer, "Offers", modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(Modifier.width(4.dp))
                        Text("${demand.offerCount} offers", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        }
    }
}
