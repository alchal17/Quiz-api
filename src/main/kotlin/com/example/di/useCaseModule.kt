package com.example.di

import com.example.domain.usecases.quizUser.CreateQuizUserUseCase
import com.example.domain.usecases.quizUser.GetAllQuizUsersUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::CreateQuizUserUseCase)
    factoryOf(::GetAllQuizUsersUseCase)
}