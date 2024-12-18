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

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindGetUniversityUseCase(impl: GetUniversityUseCaseImpl): GetUniversityUseCase

    @Binds
    abstract fun bindGetRecentSearchUseCase(impl: GetRecentSearchUseCaseImpl): GetRecentSearchUseCase

    @Binds
    abstract fun bindPutRecentSearchUseCase(impl: PutRecentSearchUseCaseImpl): PutRecentSearchUseCase
}