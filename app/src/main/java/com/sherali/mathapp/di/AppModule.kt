package com.sherali.mathapp.di

import android.content.Context
import androidx.room.Room
import com.sherali.mathapp.data.local.room.appdatabase.AppDatabase
import com.sherali.mathapp.data.local.room.dao.AppDao
import com.sherali.mathapp.data.local.shared.SharedImp
import com.sherali.mathapp.data.local.shared.SharedPref
import com.sherali.mathapp.data.repository.Repository
import com.sherali.mathapp.data.repository.RepositoryImp
import com.sherali.mathapp.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigrationOnDowngrade().build()
    }


    @Provides
    @Singleton
    fun provideAppDao(appDatabase: AppDatabase): AppDao = appDatabase.getAppDao()

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPref = SharedImp(context)

    @Provides
    @Singleton
    fun provideRepositoryImp(appDao: AppDao, sharedPref: SharedPref): Repository =
        RepositoryImp(appDao = appDao, sharedPref = sharedPref)
}