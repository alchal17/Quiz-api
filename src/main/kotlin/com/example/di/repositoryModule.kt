package com.example.di

import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.data.repositories.quizUserRepository.QuizUserRepositoryHelperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::QuizUserRepositoryHelperImpl) bind QuizUserRepository::class
}