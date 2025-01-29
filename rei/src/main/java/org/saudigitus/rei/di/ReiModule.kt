package org.saudigitus.rei.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.dhis2.commons.network.NetworkUtils
import org.dhis2.commons.resources.ResourceManager
import org.hisp.dhis.android.core.D2
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.data.source.repository.DataManagerImpl
import org.saudigitus.rei.ui.mapper.TEICardMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReiModule {

    @Provides
    @Singleton
    fun providesNetworkUtils(
        @ApplicationContext context: Context
    ): NetworkUtils = NetworkUtils(context)

    @Provides
    @Singleton
    fun providesDataManager(
        d2: D2,
        networkUtils: NetworkUtils,
        resourceManager: ResourceManager,
    ): DataManager = DataManagerImpl(d2, networkUtils, resourceManager)

    @Provides
    @Singleton
    fun providesTEICardMapper(
        @ApplicationContext context: Context,
        resourcesManager: ResourceManager,
    ) = TEICardMapper(context, resourcesManager)
}
