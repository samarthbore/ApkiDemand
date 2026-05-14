package com.apkidemand.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apkidemand.feature.home.data.DemandEntity
import com.apkidemand.feature.home.data.MarketRepository
import com.apkidemand.feature.home.data.OfferEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val marketRepository: MarketRepository
) : ViewModel() {

    private val _demands = MutableStateFlow<List<DemandEntity>>(emptyList())
    val demands: StateFlow<List<DemandEntity>> = _demands.asStateFlow()
    
    private val _offers = MutableStateFlow<List<OfferEntity>>(emptyList())
    val offers: StateFlow<List<OfferEntity>> = _offers.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // By default load everything
        fetchDemands("All")
    }

    fun fetchDemands(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            marketRepository.getDemandsFlow(category)
                .catch { e ->
                    _error.value = e.localizedMessage
                    _isLoading.value = false
                }
                .collect { list ->
                    _demands.value = list
                    _isLoading.value = false
                }
        }
    }
    
    // For Buyer Feed (Offers matched to their demands or just global offers for now)
    // We simulate by fetching all offers if we don't have a specific demand context
    // Realistically, buyer feed shows offers they received.
    // For now, let's keep it simple.
    
    fun clearError() {
        _error.value = null
    }
}
