package com.example.di

import com.example.presentation.controllers.QuizController
import com.example.presentation.controllers.QuizUserController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val controllersModule = module {
    singleOf(::QuizUserController)
    singleOf(::QuizController)
}