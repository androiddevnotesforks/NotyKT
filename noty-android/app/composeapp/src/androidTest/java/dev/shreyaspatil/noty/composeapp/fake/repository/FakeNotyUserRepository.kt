/*
 * Copyright 2020 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.shreyaspatil.noty.composeapp.fake.repository

import dev.shreyaspatil.noty.core.model.AuthCredential
import dev.shreyaspatil.noty.core.repository.Either
import dev.shreyaspatil.noty.core.repository.NotyUserRepository
import javax.inject.Inject

data class UserCredentials(val username: String, val password: String, val token: String)

/**
 * Fake implementation for user repository
 *
 * This stored credentials in memory
 */
class FakeNotyUserRepository @Inject constructor() : NotyUserRepository {
    private val users = mutableListOf<UserCredentials>()

    init {
        // Seed one user
        users.add(
            UserCredentials(
                username = "johndoe",
                password = "johndoe1234",
                token = "johndoejohndoe"
            )
        )
    }

    override suspend fun addUser(username: String, password: String): Either<AuthCredential> {
        if (users.any { it.username == username }) return Either.error("User already exist")
        val credential = AuthCredential("$username-$password")
        users.add(
            UserCredentials(
                username = username,
                password = password,
                token = credential.token
            )
        )
        return Either.success(credential)
    }

    override suspend fun getUserByUsernameAndPassword(
        username: String,
        password: String
    ): Either<AuthCredential> {
        return users.find { it.username == username && it.password == password }.let {
            if (it != null) {
                Either.success(AuthCredential(it.token))
            } else {
                Either.error("User not exist")
            }
        }
    }
}
