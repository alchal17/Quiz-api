package com.example.domain.usecases.quizUser

import com.example.data.models.toQuizUserEntity
import com.example.data.repositories.quizUserRepository.QuizUserRepository
import com.example.domain.entities.QuizUserEntity
import com.example.domain.entities.toQuizUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class UpdateQuizUserUseCase(private val quizUserRepository: QuizUserRepository) {
    suspend operator fun invoke(quizUserEntity: QuizUserEntity): Result<QuizUserEntity> {
        val quizUser = quizUserEntity.toQuizUser()

        val quizUserId = quizUserEntity.id ?: return Result.failure(Exception("No id has been provided."))

        quizUserRepository.getById(quizUserId)
            ?: return Result.failure(Exception("User with id $quizUserId does not exist."))

        return coroutineScope {
            val emailCheckDeferred = async(Dispatchers.IO) {
                quizUserRepository.findAnotherUserByEmail(
                    id = quizUserId,
                    email = quizUserEntity.email
                )
            }

            val usernameCheckDeferred = async(Dispatchers.IO) {
                quizUserRepository.findAnotherUserByUsername(
                    id = quizUserId,
                    username = quizUserEntity.username
                )
            }

            val emailCheck = emailCheckDeferred.await()
            val usernameCheck = usernameCheckDeferred.await()

            when {
                emailCheck != null -> {
                    Result.failure(Exception("A user with email ${quizUserEntity.email} already exists."))
                }

                usernameCheck != null -> {
                    Result.failure(Exception("A user with username ${quizUserEntity.username} already exists."))
                }

                else -> {
                    val updateResult = quizUserRepository.update(quizUser)
                        ?: return@coroutineScope Result.failure(Exception("Unknown error occurred while updating $quizUserId user."))

                    Result.success(updateResult.toQuizUserEntity())
                }
            }
        }
    }
}