package com.apkidemand.feature.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    onVerified: () -> Unit,
    onBack: () -> Unit
) {
    var otpValue by remember { mutableStateOf("") }
    var timeLeft by remember { mutableIntStateOf(30) }
    var canResend by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Countdown timer
    LaunchedEffect(canResend) {
        if (!canResend) {
            timeLeft = 30
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            canResend = true
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Auto-verify when 6 digits entered
    LaunchedEffect(otpValue) {
        if (otpValue.length == 6) {
            isVerifying = true
            delay(1500) // Simulate verification
            isVerifying = false
            onVerified()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6C5CE7), Color(0xFF5A4BD1))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔐",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Verify OTP",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter the 6-digit code sent to\n+91 $phoneNumber",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // OTP Input Boxes
            BasicTextField(
                value = otpValue,
                onValueChange = { value ->
                    if (value.length <= 6 && value.all { it.isDigit() }) {
                        otpValue = value
                        isError = false
                    }
                },
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                decorationBox = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        repeat(6) { index ->
                            val char = otpValue.getOrNull(index)
                            val isFocused = otpValue.length == index
                            val borderColor by animateColorAsState(
                                targetValue = when {
                                    isError -> MaterialTheme.colorScheme.error
                                    isFocused -> Color(0xFF6C5CE7)
                                    char != null -> Color(0xFF6C5CE7).copy(alpha = 0.5f)
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                animationSpec = tween(200),
                                label = "border"
                            )

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        width = if (isFocused) 2.dp else 1.5.dp,
                                        color = borderColor,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        if (char != null) Color(0xFF6C5CE7).copy(alpha = 0.05f)
                                        else MaterialTheme.colorScheme.surface
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (char != null) {
                                    Text(
                                        text = char.toString(),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF6C5CE7)
                                    )
                                } else if (isFocused) {
                                    Box(
                                        modifier = Modifier
                                            .width(2.dp)
                                            .height(24.dp)
                                            .background(Color(0xFF6C5CE7))
                                    )
                                }
                            }
                        }
                    }
                }
            )

            if (isError) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Invalid OTP. Please try again.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Verifying indicator
            if (isVerifying) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Color(0xFF6C5CE7)
                    )
                    Text(
                        text = "Verifying...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6C5CE7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Resend section
            if (canResend) {
                TextButton(
                    onClick = {
                        canResend = false
                        otpValue = ""
                    }
                ) {
                    Text(
                        text = "Resend OTP",
                        color = Color(0xFF6C5CE7),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Text(
                    text = "Resend code in ${timeLeft}s",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}
