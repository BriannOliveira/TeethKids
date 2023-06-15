package br.com.minhaempresa.teethkids.signUp.model


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserRegistrationUiState(
    val  name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val resume: String = "",
    val addressone: String = "",
    val addresstwo: String = "",
    val addressthree: String = "",
    val fcmToken: String = ""

)

class UserRegistrationViewModel : ViewModel() {

    // Expose screen UI state
    private var _uiState = MutableStateFlow(UserRegistrationUiState())
    val uiState: StateFlow<UserRegistrationUiState> = _uiState.asStateFlow()

    // Handle business logic
    fun updateName(name: String) {
        _uiState.update { currentState ->
            currentState.copy(name = name)
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update { currentState ->
            currentState.copy(phone = phone)
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email)
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    fun updateResume(resume: String) {
        _uiState.update { currentState ->
            currentState.copy(resume = resume)
        }
    }

    fun updateAddressOne(address: String) {
        _uiState.update { currentState ->
            currentState.copy(addressone = address)
        }
    }

    fun updateAddressTwo(address: String) {
        _uiState.update { currentState ->
            currentState.copy(addresstwo = address)
        }
    }

    fun updateAddressThree(address: String) {
        _uiState.update { currentState ->
            currentState.copy(addressthree = address)
        }
    }

    fun updateFcmToken(fcmToken: String){
        _uiState.update { currenState ->
            currenState.copy(fcmToken = fcmToken)
        }
    }


    // Add additional functions for user registration logic, such as validation and submitting the data.
}