package com.example.di

import com.example.daos.QuizDao
import com.example.daos.QuizQuestionDao
import com.example.daos.QuizQuestionOptionDao
import com.example.daos.QuizUserDao
import com.example.files_handlers.BasicFileDeleter
import com.example.files_handlers.BasicImageSaver
import com.example.files_handlers.FileDeleter
import com.example.files_handlers.ImageSaver
import org.koin.dsl.module

val appModule = module {
    single<BasicImageSaver> { ImageSaver() }
    single<BasicFileDeleter> { FileDeleter() }
    single { QuizUserDao() }
    single { QuizQuestionOptionDao() }
    single { QuizQuestionDao(quizQuestionOptionDao = get()) }
    single { QuizDao(quizQuestionDao = get(), quizUserDao = get(), fileDeleter = get()) }
}