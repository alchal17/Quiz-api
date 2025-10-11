package com.example.di

import com.example.domain.usecases.quiz.*
import com.example.domain.usecases.quizUser.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::CreateQuizUserUseCase)
    factoryOf(::GetAllQuizUsersUseCase)
    factoryOf(::GetQuizUserByIdUseCase)
    factoryOf(::DeleteQuizUserUseCase)
    factoryOf(::UpdateQuizUserUseCase)
    factoryOf(::FindQuizUserByUsernameUseCase)
    factoryOf(::FindQuzUserByEmailUseCase)


    factoryOf(::GetAllQuizzesUseCase)
    factoryOf(::CreateQuizUseCase)
    factoryOf(::DeleteQuizUseCase)
    factoryOf(::GetQuizByIdUseCase)
    factoryOf(::UpdateQuizUseCase)
    factoryOf(::GetQuizWithBase64ImageUseCase)
    factoryOf(::GetQuizzesByUserIdUseCase)
}