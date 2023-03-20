package com.danytothemoon.core.data.di

import com.danytothemoon.core.data.repository.MediaRepository
import com.danytothemoon.core.data.repository.MediaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
  @Binds
  fun bindsMediaRepository(
    mediaRepository: MediaRepositoryImpl,
  ): MediaRepository
}