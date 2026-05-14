package com.apkidemand.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.apkidemand.feature.home.presentation.ChatViewModel


@Composable
fun ChatDetailScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    chatId: String,
    onBack: () -> Unit
) {
    LaunchedEffect(chatId) {
        viewModel.fetchMessages(chatId)
    }
    
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ChatHeader(chatId = chatId, onBack = onBack)
        },
        bottomBar = {
            ChatInputBar(
                text = messageText,
                onTextChange = { messageText = it },
                onSend = { 
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(chatId, messageText)
                        messageText = "" 
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Pinned Context Card
            ContextCard()

            // Chat Messages
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Today",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    OutgoingMessage(
                        text = "Hi, I'm looking for a red Banarasi silk saree with gold zari work.",
                        time = "11:32 AM",
                        isRead = true
                    )
                }

                item {
                    IncomingMessage(
                        text = "Hello! Yes, we have that in maroon with gold zari work.",
                        time = "11:34 AM"
                    )
                }

                item {
                    IncomingMediaMessage(
                        time = "11:35 AM"
                    )
                }

                item {
                    OutgoingMessage(
                        text = "This looks beautiful! Is it pure silk?",
                        time = "11:36 AM",
                        isRead = true
                    )
                }

                item {
                    IncomingMessage(
                        text = "Yes, it's 100% pure silk with fine zari work.",
                        time = "11:36 AM"
                    )
                }

                item {
                    OutgoingMessage(
                        text = "Great! What's the final price?",
                        time = "11:37 AM",
                        isRead = true
                    )
                }

                item {
                    IncomingMessage(
                        text = "₹4,500 is the final price.",
                        time = "11:38 AM"
                    )
                }

                item {
                    OutgoingMessage(
                        text = "Okay, I'll take it. What are the delivery options?",
                        time = "11:39 AM",
                        isRead = true
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatHeader(chatId: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = MaterialTheme.colorScheme.onBackground)
        }
        
        Spacer(Modifier.width(8.dp))
        
        // Avatar
        Box(modifier = Modifier.size(44.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text("RS", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            // Online Dot
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 2.dp, y = 2.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rajesh Silks", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Filled.Verified, "Verified", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
            }
            Text("Online", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Phone, "Call", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
            }
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.MoreVert, "Options", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun ContextCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=150",
                    contentDescription = "Red Banarasi Silk Saree",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text("Red Banarasi Silk Saree", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(2.dp))
                Text("Your demand", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Spacer(Modifier.width(12.dp))
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            ) {
                Text(
                    "View Details",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun OutgoingMessage(text: String, time: String, isRead: Boolean) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary, lineHeight = 20.sp)
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(time, fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                    if (isRead) {
                        Spacer(Modifier.width(4.dp))
                        Text("✓✓", fontSize = 10.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }
}

@Composable
private fun IncomingMessage(text: String, time: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Surface(
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
                Spacer(Modifier.height(6.dp))
                Text(time, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun IncomingMediaMessage(time: String) {
    val imageUrls = listOf(
        "https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&q=80&w=150",
        "https://images.unsplash.com/photo-1583391733958-d25e07fac0fa?auto=format&fit=crop&q=80&w=150",
        "https://images.unsplash.com/photo-1596455607563-ad6193f76b11?auto=format&fit=crop&q=80&w=150"
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
        Surface(
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomEnd = 16.dp, bottomStart = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    imageUrls.forEach { url ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+4", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
                
                Spacer(Modifier.height(12.dp))
                
                Text("Pure Banarasi Silk Saree", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                Text("Maroon with Gold Zari", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                Spacer(Modifier.height(8.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text("₹4,500", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(time, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun ChatInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Plus Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
            }
            
            Spacer(Modifier.width(12.dp))
            
            // Text Input
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 15.sp),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box {
                        if (text.isEmpty()) {
                            Text("Type a message...", style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp))
                        }
                        innerTextField()
                    }
                }
            )
            
            Spacer(Modifier.width(12.dp))
            
            // Emoji Button
            Icon(Icons.Outlined.Face, "Emoji", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
            
            Spacer(Modifier.width(16.dp))
            
            // Send Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onSend),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, "Send", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp).offset(x = 2.dp))
            }
        }
    }
}
