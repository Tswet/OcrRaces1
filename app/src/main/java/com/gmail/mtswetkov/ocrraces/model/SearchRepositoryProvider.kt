package com.gmail.mtswetkov.ocrraces.model


object SearchRepositoryProvider {
    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(apiService = OcrApi.create())
    }
}