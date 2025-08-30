package com.example.di

import com.example.dao.QuizDao
import com.example.dao.QuizQuestionDao
import com.example.dao.QuizQuestionOptionDao
import com.example.dao.QuizUserDao
import com.example.files_handlers.FileHandler
import com.example.files_handlers.FileHandlerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf<FileHandler>(::FileHandlerImpl)

    singleOf(::QuizUserDao)
    singleOf(::QuizQuestionOptionDao)
    singleOf(::QuizQuestionDao)
    singleOf(::QuizDao)
}