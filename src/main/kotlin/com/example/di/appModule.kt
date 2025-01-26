package com.example.di

import com.example.daos.QuizDao
import com.example.daos.QuizQuestionDao
import com.example.daos.QuizQuestionOptionDao
import com.example.daos.QuizUserDao
import com.example.files_handlers.BasicFileHandler
import com.example.files_handlers.FileHandler
import org.koin.dsl.module

val appModule = module {
    single<BasicFileHandler> { FileHandler() }

    single { QuizUserDao() }
    single { QuizQuestionOptionDao() }
    single { QuizQuestionDao(quizQuestionOptionDao = get(), fileHandler = get()) }
    single { QuizDao(quizQuestionDao = get(), quizUserDao = get(), fileHandler = get()) }
}