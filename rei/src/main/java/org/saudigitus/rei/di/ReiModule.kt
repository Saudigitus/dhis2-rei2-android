package org.saudigitus.rei.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.dhis2.commons.di.dagger.PerActivity
import org.dhis2.commons.locationprovider.LocationProvider
import org.dhis2.commons.locationprovider.LocationProviderImpl
import org.dhis2.commons.network.NetworkUtils
import org.dhis2.commons.resources.ResourceManager
import org.dhis2.form.ui.provider.FormResultDialogProvider
import org.dhis2.form.ui.provider.FormResultDialogResourcesProvider
import org.hisp.dhis.android.core.D2
import org.saudigitus.rei.data.source.DataManager
import org.saudigitus.rei.data.source.EnrollmentRepository
import org.saudigitus.rei.data.source.repository.DataManagerImpl
import org.saudigitus.rei.data.source.repository.EnrollmentRepositoryImpl
import org.saudigitus.rei.ui.mapper.TEICardMapper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReiModule {

    @Provides
    @Singleton
    fun providesNetworkUtils(
        @ApplicationContext context: Context,
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
    fun providesEnrollmentRepository(
        d2: D2,
    ): EnrollmentRepository = EnrollmentRepositoryImpl(d2)

    @Provides
    @Singleton
    fun providesTEICardMapper(
        @ApplicationContext context: Context,
        resourcesManager: ResourceManager,
    ) = TEICardMapper(context, resourcesManager)

    @Provides
    @Singleton
    fun provideResultDialogProvider(
        resourceManager: ResourceManager,
    ): FormResultDialogProvider {
        return FormResultDialogProvider(
            FormResultDialogResourcesProvider(resourceManager),
        )
    }

    @Provides
    @Singleton
    fun locationProvider(@ApplicationContext context: Context): LocationProvider {
        return LocationProviderImpl(context)
    }
}
