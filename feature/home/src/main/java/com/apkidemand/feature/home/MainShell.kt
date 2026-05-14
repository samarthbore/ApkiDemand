package com.apkidemand.feature.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class BottomTab(
    val label: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector
) {
    Feed("Feed", Icons.Filled.Home, Icons.Outlined.Home),
    Demand("Demand", Icons.Filled.AddCircle, Icons.Outlined.AddCircleOutline),
    Chats("Chats", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    Profile("Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainShell(
    userRole: String,
    onNavigateToPost: () -> Unit,
    onNavigateToChatDetail: (String) -> Unit,
    onLogout: () -> Unit
) {
    var currentTab by rememberSaveable { mutableStateOf(BottomTab.Feed) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToPost,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = Color.White,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Post Demand", modifier = Modifier.size(28.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                BottomTab.entries.forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (tab == BottomTab.Demand) {
                                // Currently acts as a placeholder or goes to My Demands. We'll set it as active tab
                                currentTab = tab
                            } else {
                                currentTab = tab
                            }
                        },
                        icon = {
                            if (tab == BottomTab.Chats) {
                                BadgedBox(
                                    badge = {
                                        Badge(containerColor = MaterialTheme.colorScheme.error) {
                                            Text("3")
                                        }
                                    }
                                ) {
                                    Icon(
                                        if (isSelected) tab.filledIcon else tab.outlinedIcon,
                                        contentDescription = tab.label
                                    )
                                }
                            } else {
                                Icon(
                                    if (isSelected) tab.filledIcon else tab.outlinedIcon,
                                    contentDescription = tab.label
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (currentTab) {
                BottomTab.Feed -> {
                    if (userRole == "buyer") {
                        BuyerFeedScreen()
                    } else {
                        SellerDashboardScreen(
                            onNavigateToLeads = { currentTab = BottomTab.Demand },
                            onNavigateToActiveOffers = { currentTab = BottomTab.Demand }
                        )
                    }
                }
                BottomTab.Demand -> {
                    if (userRole == "buyer") {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("My Demands Screen")
                        }
                    } else {
                        SellerFeedScreen() // Seller sees leads (demands) here
                    }
                }
                BottomTab.Chats -> ChatsScreen(userRole = userRole, onChatClick = onNavigateToChatDetail)
                BottomTab.Profile -> ProfileScreen(userRole = userRole, onLogout = onLogout)
            }
        }
    }
}
