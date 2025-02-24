package com.utsman.universitylist.di

import com.utsman.universitylist.domain.api.GetRecentSearchUseCase
import com.utsman.universitylist.domain.api.GetUniversityUseCase
import com.utsman.universitylist.domain.api.PutRecentSearchUseCase
import com.utsman.universitylist.domain.impl.GetRecentSearchUseCaseImpl
import com.utsman.universitylist.domain.impl.GetUniversityUseCaseImpl
import com.utsman.universitylist.domain.impl.PutRecentSearchUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dagger Hilt module for binding use case implementations to their interfaces.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    /**
     * Binds [GetUniversityUseCaseImpl] to [GetUniversityUseCase] interface.
     *
     * @param impl The [GetUniversityUseCaseImpl] instance.
     * @return The [GetUniversityUseCase] interface.
     */
    @Binds
    abstract fun bindGetUniversityUseCase(impl: GetUniversityUseCaseImpl): GetUniversityUseCase

    /**
     * Binds [GetRecentSearchUseCaseImpl] to [GetRecentSearchUseCase] interface.
     *
     * @param impl The [GetRecentSearchUseCaseImpl] instance.
     * @return The [GetRecentSearchUseCase] interface.
     */
    @Binds
    abstract fun bindGetRecentSearchUseCase(impl: GetRecentSearchUseCaseImpl): GetRecentSearchUseCase

    /**
     * Binds [PutRecentSearchUseCaseImpl] to [PutRecentSearchUseCase] interface.
     *
     * @param impl The [PutRecentSearchUseCaseImpl] instance.
     * @return The [PutRecentSearchUseCase] interface.
     */
    @Binds
    abstract fun bindPutRecentSearchUseCase(impl: PutRecentSearchUseCaseImpl): PutRecentSearchUseCase
}