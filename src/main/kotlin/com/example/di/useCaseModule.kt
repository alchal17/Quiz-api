package com.example.di

import com.example.domain.usecases.image.SaveImageUseCase
import com.example.domain.usecases.quiz.CreateQuizUseCase
import com.example.domain.usecases.quiz.DeleteQuizUseCase
import com.example.domain.usecases.quiz.GetAllQuizzesUseCase
import com.example.domain.usecases.quizUser.CreateQuizUserUseCase
import com.example.domain.usecases.quizUser.DeleteQuizUserUseCase
import com.example.domain.usecases.quizUser.FindQuizUserByUsernameUseCase
import com.example.domain.usecases.quizUser.FindQuzUserByEmailUseCase
import com.example.domain.usecases.quizUser.GetAllQuizUsersUseCase
import com.example.domain.usecases.quizUser.GetQuizUserByIdUseCase
import com.example.domain.usecases.quizUser.UpdateQuizUserUseCase
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

    factoryOf(::SaveImageUseCase)

    factoryOf(::GetAllQuizzesUseCase)
    factoryOf(::CreateQuizUseCase)
    factoryOf(::DeleteQuizUseCase)
}