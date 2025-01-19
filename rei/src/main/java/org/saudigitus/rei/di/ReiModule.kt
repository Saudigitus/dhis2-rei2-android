package org.saudigitus.rei.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.dhis2.commons.resources.ResourceManager
import org.hisp.dhis.android.core.D2
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.data.source.repository.DataManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReiModule {

    @Provides
    @Singleton
    fun providesDataManager(
        d2: D2,
        resourceManager: ResourceManager,
    ): DataManager = DataManagerImpl(d2, resourceManager)
}
