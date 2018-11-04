package com.raywenderlich.android.imet.data.db

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.nfc.Tag
import android.os.AsyncTask
import com.raywenderlich.android.imet.data.model.People
import com.raywenderlich.android.imet.data.net.PeopleInfoProvider
import java.util.logging.Logger

@Database(entities = [People::class], version = 1)
abstract class PeopleDatabase : RoomDatabase(){

    abstract fun peopleDao(): PeopleDao // create an object of peopleDao interface

    companion object {
        private val lock = Any()
        private const val DB_NAME = "People.db"
        private var INSTANCE: PeopleDatabase? = null
        val LOG = Logger.getLogger(PeopleDatabase::class.java.name);


        fun getInstance(application: Application) : PeopleDatabase{
            synchronized(lock){
                if (PeopleDatabase.INSTANCE == null){
                    PeopleDatabase.INSTANCE = Room.databaseBuilder(application, PeopleDatabase::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .addCallback(object : RoomDatabase.Callback(){
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    PeopleDatabase.INSTANCE?.let{
                                        PeopleDatabase.prePopulate(it, PeopleInfoProvider.peopleList)
                                        LOG.warning("prePopulate database with items")
                                    }
                                }
                            })
                            .build()
                }
                return INSTANCE!!
            }
        }

        // This function adds People from a provided people list and inserts them into the PeopleDatabase asynchronously.
        fun prePopulate(database: PeopleDatabase, peopleList: List<People>){
            for (people in peopleList){
                AsyncTask.execute { database.peopleDao().insert(people) }
            }
        }

    }
}