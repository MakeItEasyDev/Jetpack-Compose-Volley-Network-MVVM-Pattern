package com.jetpack.volleyrequest.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jetpack.volleyrequest.model.UserResponse
import com.jetpack.volleyrequest.repository.UserRepository
import com.jetpack.volleyrequest.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    var isLoading = mutableStateOf(false)
    private var _getUserData: MutableLiveData<List<UserResponse>> = MutableLiveData<List<UserResponse>>()
    var getUserData: LiveData<List<UserResponse>> = _getUserData
    fun getUserData(context: Context): Resource<List<UserResponse>> {
        val result = userRepository.getUserReponse(context = context)

        MediatorLiveData<Resource<List<UserResponse>>>().apply {
            addSource(result) {
                isLoading.value = true
                _getUserData.value = it.data!!
            }
            observeForever {
                Log.d("TAG", "getUserData: ")
            }
        }
        return Resource.Error("Please Wait")
    }
}