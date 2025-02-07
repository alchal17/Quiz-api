package com.example.di

import com.example.daos.QuizDao
import com.example.daos.QuizQuestionDao
import com.example.daos.QuizQuestionOptionDao
import com.example.daos.QuizUserDao
import com.example.files_handlers.BasicFileHandler
import com.example.files_handlers.FileHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf<BasicFileHandler>(::FileHandler)

    singleOf(::QuizUserDao)
    singleOf(::QuizQuestionOptionDao)
    singleOf(::QuizQuestionDao)
    singleOf(::QuizDao)
}