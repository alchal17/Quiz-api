package com.example.di

import com.example.data.repositories.filesHandlers.FileHandlerRepository
import com.example.data.repositories.filesHandlers.FileHandlerRepositoryImpl
import com.example.data.repositories.quizRpository.QuizRepository
import com.example.data.repositories.quizRpository.QuizRepositoryImpl
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.data.repositories.quizUserRepository.QuizUserRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::QuizUserRepositoryImpl) bind QuizUserRepository::class
    factoryOf(::QuizRepositoryImpl) bind QuizRepository::class
    factoryOf(::FileHandlerRepositoryImpl) bind FileHandlerRepository::class
}