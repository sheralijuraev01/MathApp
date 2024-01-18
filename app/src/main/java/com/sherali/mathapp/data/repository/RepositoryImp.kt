package com.sherali.mathapp.data.repository

import com.sherali.mathapp.data.local.room.dao.AppDao
import com.sherali.mathapp.data.local.room.entity.ScoreEntity
import com.sherali.mathapp.data.local.shared.SharedPref
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val appDao: AppDao,
    private val sharedPref: SharedPref
) : Repository {
    override suspend fun saveName(name: String) = sharedPref.saveName(name)

    override suspend fun getName(): String = sharedPref.getName()
    override suspend fun saveIndex(index: Int) = sharedPref.saveIconIndex(index)

    override suspend fun getIndex(): Int = sharedPref.getIconIndex()
    override suspend fun saveStatus(status: Boolean) = sharedPref.saveLogin(status)

    override suspend fun getStatus(): Boolean = sharedPref.getLogin()
    override suspend fun saveScore(scoreEntity: ScoreEntity) = appDao.saveScore(scoreEntity)

    override suspend fun getAllScore(): List<ScoreEntity> = appDao.getAllScore()
    override suspend fun getScore(key:String): ScoreEntity=appDao.getScore(key)

    override suspend fun getMaxByCategory(category: String): Int = appDao.getMaxByLevelKey(category)
    override suspend fun getMaxScore(): Int = appDao.getMaxScore()

}