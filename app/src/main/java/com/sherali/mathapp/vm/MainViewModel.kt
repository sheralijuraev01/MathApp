package com.sherali.mathapp.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sherali.mathapp.data.local.room.entity.ScoreEntity
import com.sherali.mathapp.data.model.UserData
import com.sherali.mathapp.data.repository.Repository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    fun getUserData(): MutableLiveData<UserData?> {
        val userData: MutableLiveData<UserData?> = MutableLiveData(null)
        viewModelScope.launch {
            val name = repository.getName()
            val index = repository.getIndex()
            userData.postValue(UserData(name, index))

        }

        return userData
    }


    fun getStatus(): MutableLiveData<Boolean?> {
        val status: MutableLiveData<Boolean?> = MutableLiveData(null)
        viewModelScope.launch {
            status.postValue(repository.getStatus())
        }

        return status

    }


    fun getAllScore(): MutableLiveData<List<ScoreEntity>?> {
        val result: MutableLiveData<List<ScoreEntity>?> = MutableLiveData(null)
        viewModelScope.launch {
            result.postValue(repository.getAllScore())
        }

        return result
    }

    fun getMaxCategory(category: String): MutableLiveData<Int?> {
        val maxScore: MutableLiveData<Int?> = MutableLiveData(null)
        viewModelScope.launch {
            maxScore.postValue(repository.getMaxByCategory(category))
        }

        return maxScore
    }

    fun saveScore(scoreEntity: ScoreEntity) {
        viewModelScope.launch {
            repository.saveScore(scoreEntity)
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            repository.saveName(name)
        }
    }

    fun saveIndex(index: Int) {
        viewModelScope.launch {
            repository.saveIndex(index)
        }
    }

    fun saveStatus(status: Boolean) {
        viewModelScope.launch {
            repository.saveStatus(status)
        }
    }


}