package com.example.di

import com.example.daos.Finder
import com.example.daos.QuizDao
import com.example.daos.QuizQuestionDao
import com.example.daos.QuizQuestionOptionDao
import com.example.daos.QuizUserDao
import com.example.files_handlers.BasicImageSaver
import com.example.files_handlers.ImageSaver
import org.koin.dsl.module

val appModule = module {
    single { Finder() }
    single { QuizUserDao() }
    single { QuizQuestionOptionDao(get()) }
    single { QuizQuestionDao(get(), get()) }
    single { QuizDao(get(), get()) }
    single<BasicImageSaver> { ImageSaver() }
}