package com.apkidemand.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(
    onNavigateToLeads: () -> Unit,
    onNavigateToActiveOffers: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Seller Dashboard",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardStatCard(
                modifier = Modifier.weight(1f),
                title = "Active Leads",
                value = "12",
                icon = Icons.Filled.ListAlt,
                color = MaterialTheme.colorScheme.primary,
                onClick = onNavigateToLeads
            )
            DashboardStatCard(
                modifier = Modifier.weight(1f),
                title = "Active Offers",
                value = "4",
                icon = Icons.Filled.LocalOffer,
                color = MaterialTheme.colorScheme.secondary,
                onClick = onNavigateToActiveOffers
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        DashboardStatCard(
            modifier = Modifier.fillMaxWidth(),
            title = "Total Sales Views",
            value = "342",
            icon = Icons.Filled.Analytics,
            color = Color(0xFF0369A1),
            onClick = {}
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Recent Activity",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Activity List Placeholder
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.LocalOffer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("New Offer Submitted", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text("For: Men's Formal Shirts", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                        }
                        Text("2h ago", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardStatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = color.copy(alpha = 0.8f))
        }
    }
}
