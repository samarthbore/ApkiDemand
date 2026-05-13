package com.apkidemand.feature.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class OnboardingPage(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val gradientColors: List<Color>
)

private val pages = listOf(
    OnboardingPage(
        emoji = "📢",
        title = "Post Your Demand",
        subtitle = "Tell local sellers exactly what you need.\nAdd photos, budget & your area.",
        gradientColors = listOf(Color(0xFF6C5CE7), Color(0xFF5A4BD1))
    ),
    OnboardingPage(
        emoji = "🏪",
        title = "Get Real Offers",
        subtitle = "Nearby sellers compete to give you\nthe best price with real photos.",
        gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
    ),
    OnboardingPage(
        emoji = "🤝",
        title = "Pick & Connect",
        subtitle = "Choose your favorite offer.\nChat directly or visit the shop.",
        gradientColors = listOf(Color(0xFF16A34A), Color(0xFF15803D))
    )
)

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Page content with crossfade animation
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                fadeIn(tween(400)) + slideInHorizontally { it / 3 } togetherWith
                    fadeOut(tween(300)) + slideOutHorizontally { -it / 3 }
            },
            label = "pageTransition"
        ) { pageIndex ->
            OnboardingPageContent(pages[pageIndex])
        }

        // Bottom controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages.size) { index ->
                    val isSelected = currentPage == index
                    Box(
                        modifier = Modifier
                            .animateContentSize()
                            .height(8.dp)
                            .width(if (isSelected) 32.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.White
                                else Color.White.copy(alpha = 0.4f)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action button
            Button(
                onClick = {
                    if (currentPage < pages.size - 1) {
                        currentPage++
                    } else {
                        onFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF6C5CE7)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Text(
                    text = if (currentPage < pages.size - 1) "Next" else "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            if (currentPage < pages.size - 1) {
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(onClick = onFinished) {
                    Text(
                        text = "Skip",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = page.gradientColors + page.gradientColors.last().copy(alpha = 0.8f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Decorative circles
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = 200.dp.toPx(),
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.03f),
                radius = 300.dp.toPx(),
                center = Offset(size.width * 0.1f, size.height * 0.7f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .offset(y = (-60).dp)
        ) {
            // Emoji icon with glow
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.emoji,
                    fontSize = 56.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = page.title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = page.subtitle,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}
