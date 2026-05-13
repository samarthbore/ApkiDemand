package com.apkidemand.app

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.apkidemand.feature.auth.OnboardingScreen
import com.apkidemand.feature.auth.OtpVerificationScreen
import com.apkidemand.feature.auth.PhoneInputScreen
import com.apkidemand.feature.auth.ProfileSetupScreen
import com.apkidemand.feature.home.ChatDetailScreen
import com.apkidemand.feature.home.MainShell
import com.apkidemand.feature.postdemand.PostDemandScreen
import kotlinx.coroutines.delay

@Composable
fun ApkiDemandNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splash"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("onboarding") {
            OnboardingScreen(
                onFinished = {
                    navController.navigate("phone_input") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("phone_input") {
            PhoneInputScreen(
                onSendOtp = { phone ->
                    navController.navigate("otp_verify/$phone")
                }
            )
        }

        composable(
            route = "otp_verify/{phone}",
            arguments = listOf(navArgument("phone") { type = NavType.StringType })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpVerificationScreen(
                phoneNumber = phone,
                onVerified = {
                    navController.navigate("profile_setup") {
                        popUpTo("phone_input") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("profile_setup") {
            ProfileSetupScreen(
                onComplete = { role ->
                    navController.navigate("main/$role") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ── Main App (with bottom nav) ──
        composable(
            route = "main/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "buyer"
            MainShell(
                userRole = role,
                onNavigateToPost = {
                    navController.navigate("post_demand/$role")
                },
                onNavigateToChatDetail = { chatId ->
                    navController.navigate("chat_detail/$chatId")
                },
                onLogout = {
                    navController.navigate("onboarding") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ── Post Demand / Offer ──
        composable(
            route = "post_demand/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "buyer"
            PostDemandScreen(
                userRole = role,
                onBack = { navController.popBackStack() },
                onPosted = { navController.popBackStack() }
            )
        }

        // ── Chat Detail ──
        composable(
            route = "chat_detail/{chatId}",
            arguments = listOf(navArgument("chatId") { type = NavType.StringType })
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            ChatDetailScreen(
                chatId = chatId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF6C5CE7), Color(0xFF5A4BD1), Color(0xFF3D3580))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(600)) + scaleIn(tween(600))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(100.dp).scale(scale)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AD", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(24.dp))
                Text("Apki Demand", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Text("Market Ka Offer", fontSize = 16.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}
