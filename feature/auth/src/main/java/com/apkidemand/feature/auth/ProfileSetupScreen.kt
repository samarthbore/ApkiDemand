package com.apkidemand.feature.auth

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class RoleOption(
    val id: String,
    val emoji: String,
    val title: String,
    val description: String
)

private val roles = listOf(
    RoleOption("buyer", "🛒", "Buyer", "I want to find products\n& get offers from sellers"),
    RoleOption("seller", "🏪", "Seller", "I want to see what buyers\nneed & send them offers")
)

private val cities = listOf(
    "Amravati", "Nagpur", "Nashik", "Aurangabad", "Pune",
    "Kolhapur", "Solapur", "Akola", "Latur", "Nanded",
    "Jalgaon", "Sangli", "Wardha", "Chandrapur", "Yavatmal"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onComplete: (role: String) -> Unit,
    onBack: () -> Unit
) {
    var displayName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("buyer") }
    var cityDropdownExpanded by remember { mutableStateOf(false) }

    val isValid = displayName.length >= 2 && selectedCity.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6C5CE7), Color(0xFF5A4BD1))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (displayName.isNotEmpty()) displayName.take(2).uppercase() else "👤",
                    fontSize = if (displayName.isNotEmpty()) 28.sp else 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Set up your profile",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tell us about yourself",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name
            OutlinedTextField(
                value = displayName,
                onValueChange = { if (it.length <= 80) displayName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Your Name") },
                placeholder = { Text("e.g. Rahul Sharma") },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C5CE7),
                    focusedLabelColor = Color(0xFF6C5CE7),
                    cursorColor = Color(0xFF6C5CE7)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // City
            ExposedDropdownMenuBox(
                expanded = cityDropdownExpanded,
                onExpandedChange = { cityDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    label = { Text("Your City") },
                    placeholder = { Text("Select your city") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded) },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6C5CE7),
                        focusedLabelColor = Color(0xFF6C5CE7)
                    )
                )
                ExposedDropdownMenu(
                    expanded = cityDropdownExpanded,
                    onDismissRequest = { cityDropdownExpanded = false }
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = { selectedCity = city; cityDropdownExpanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Role selection - only buyer or seller
            Text(
                text = "I am a...",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                roles.forEach { role ->
                    val isSelected = selectedRole == role.id
                    val borderColor by animateColorAsState(
                        if (isSelected) Color(0xFF6C5CE7) else MaterialTheme.colorScheme.outline,
                        tween(200), label = "roleBorder"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = borderColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .background(
                                if (isSelected) Color(0xFF6C5CE7).copy(alpha = 0.08f)
                                else MaterialTheme.colorScheme.surface
                            )
                            .clickable { selectedRole = role.id }
                            .padding(vertical = 20.dp, horizontal = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = role.emoji, fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = role.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isSelected) Color(0xFF6C5CE7)
                            else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = role.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val btnColor by animateColorAsState(
                if (isValid) Color(0xFF6C5CE7) else Color(0xFFD1D5DB),
                tween(300), label = "btnColor"
            )
            Button(
                onClick = { if (isValid) onComplete(selectedRole) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = btnColor, contentColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(if (isValid) 6.dp else 0.dp)
            ) {
                Icon(Icons.Filled.Check, null, Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Complete Setup", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
