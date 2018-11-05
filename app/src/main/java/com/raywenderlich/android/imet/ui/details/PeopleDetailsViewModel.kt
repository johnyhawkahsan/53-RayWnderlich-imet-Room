package com.raywenderlich.android.imet.ui.details

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.raywenderlich.android.imet.IMetApp
import com.raywenderlich.android.imet.data.model.People

class PeopleDetailsViewModel(application: Application) : AndroidViewModel(application){
    private val peopleRepository = getApplication<IMetApp>().getPeopleRepository()
    private val peopleId = MutableLiveData<Int>()

    // Maps people id to people details
    fun getPeopleDetails(id: Int): LiveData<People>{
        peopleId.value = id
        val peopleDetails = Transformations.switchMap<Int, People>(peopleId) { id ->
            peopleRepository.findPeople(id)
        }
        return peopleDetails
    }

    // Delete people based on id - Note: We do not need any return type like LiveData
    fun deletePeople(id: Int){
        peopleRepository.deletePeople(id)
    }

    fun deletePeople(people: People){
        peopleRepository.deletePeopleObject(people)
    }
}