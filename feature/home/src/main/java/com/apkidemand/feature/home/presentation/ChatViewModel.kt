package com.apkidemand.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkidemand.feature.home.data.ChatMessageEntity
import com.apkidemand.feature.home.data.ChatRepository
import com.apkidemand.feature.home.data.ChatThreadEntity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _threads = MutableStateFlow<List<ChatThreadEntity>>(emptyList())
    val threads: StateFlow<List<ChatThreadEntity>> = _threads.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessageEntity>>(emptyList())
    val messages: StateFlow<List<ChatMessageEntity>> = _messages.asStateFlow()
    
    val currentUserId: String
        get() = auth.currentUser?.uid ?: ""

    fun fetchThreads(role: String) {
        viewModelScope.launch {
            if (currentUserId.isNotEmpty()) {
                chatRepository.getChatThreads(currentUserId, role)
                    .catch { /* Handle error */ }
                    .collect { list ->
                        _threads.value = list
                    }
            }
        }
    }

    fun fetchMessages(threadId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(threadId)
                .catch { /* Handle error */ }
                .collect { list ->
                    _messages.value = list
                }
        }
    }

    fun sendMessage(threadId: String, text: String) {
        viewModelScope.launch {
            if (currentUserId.isNotEmpty()) {
                chatRepository.sendMessage(threadId, currentUserId, text)
            }
        }
    }
}
