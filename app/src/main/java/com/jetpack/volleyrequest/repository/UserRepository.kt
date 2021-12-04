package com.jetpack.volleyrequest.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetpack.volleyrequest.model.UserResponse
import com.jetpack.volleyrequest.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor() {
    fun getUserReponse(context: Context): MutableLiveData<Resource<List<UserResponse>>> {
        val _setUserData: MutableLiveData<Resource<List<UserResponse>>> = MutableLiveData<Resource<List<UserResponse>>>()
        val url = "https://jsonplaceholder.typicode.com/todos"
        val gson = Gson()
        try {
            val queue = Volley.newRequestQueue(context)
            Log.d("TAG", "getUserReponse: $queue")
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val responseVales: List<UserResponse> = gson.fromJson(
                        response,
                        object : TypeToken<List<UserResponse>>() {}.type
                    )
                    _setUserData.postValue(Resource.Success(responseVales))
                }
            ) { error ->
                _setUserData.postValue(Resource.Error("An unknown error occured: ${error.localizedMessage}"))
            }
            queue.add(stringRequest)
        } catch (e: Exception) {
            _setUserData.postValue(Resource.Error("An unknown error occured."))
        }

        return _setUserData
    }
}








