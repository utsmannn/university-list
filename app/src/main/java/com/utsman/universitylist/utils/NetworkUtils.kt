package com.utsman.universitylist.utils

import retrofit2.Response
import java.net.UnknownHostException

/**
 * Extension function to convert [Response<T>] to [Result<T>].
 *
 * @receiver [Response<T>] dari Retrofit.
 * @return [Result<T>] yang berisi data jika sukses atau exception jika gagal.
 */
fun <T> Response<T>.toResult(): Result<T> {
    return try {
        if (this.isSuccessful) {
            val body = this.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Throwable("Response body is null"))
            }
        } else {
            Result.failure(Throwable("Response failed with code ${this.code()} and message ${this.message()}"))
        }
    } catch (e: UnknownHostException) {
        Result.failure(Throwable("Response failed with code ${this.code()} and message ${this.message()}"))
    } catch (e: Throwable) {
        Result.failure(Throwable("Response failed with code ${this.code()} and message ${this.message()}"))
    } catch (e: Exception) {
        Result.failure(Throwable("Response failed with code ${this.code()} and message ${this.message()}"))
    }
}