package br.com.minhaempresa.teethkids.datastore


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserRegistrationUiState(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val password: String = "",
    val resume: String = "",
    val address1: String = "",
    val address2: String = "",
    val address3: String = ""
)

class UserRegistrationViewModel : ViewModel() {

    // Expose screen UI state
    private val _uiState = MutableStateFlow(UserRegistrationUiState())
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

    fun updateAddress1(address1: String) {
        _uiState.update { currentState ->
            currentState.copy(address1 = address1)
        }
    }

    fun updateAddress2(address2: String) {
        _uiState.update { currentState ->
            currentState.copy(address2 = address2)
        }
    }

    fun updateAddress3(address3: String) {
        _uiState.update { currentState ->
            currentState.copy(address1 = address3)
        }
    }


    // Add additional functions for user registration logic, such as validation and submitting the data.
}