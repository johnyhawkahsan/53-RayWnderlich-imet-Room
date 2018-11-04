package com.raywenderlich.android.imet.ui.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.raywenderlich.android.imet.IMetApp
import com.raywenderlich.android.imet.data.model.People

class PeoplesListViewModel(application: Application) : AndroidViewModel(application) {

    private val peopleRepository = getApplication<IMetApp>().getPeopleRepository()
    private val peopleList = MediatorLiveData<List<People>>()

    init {
        getAllPeople()
    }

    // 1
    fun getPeopleList(): LiveData<List<People>> {
        return peopleList
    }

    // 2
    fun getAllPeople() {
        peopleList.addSource(peopleRepository.getAllPeople()) { peoples ->
            peopleList.postValue(peoples)
        }
    }

    // 3
    fun searchPeople(name: String){
        //Performs the search using peopleRepository.findPeople(name) and sets the resulting LiveData as a source of peopleList.
        peopleList.addSource(peopleRepository.findPeople(name)) { peoples ->
            peopleList.postValue(peoples) //Posts the value of the resulting LiveData to the observer of peopleList. As a result, your people list will show with the name of searched people (if found) instead of showing all People.
        }
    }

}