package com.wiseassblog.domain.usecase

/**
 *
 * From Darel Bitsy's clean-arch repo here:
 *
 * https://github.com/bitsydarel/clean-arch/blob/master/core/src/commonMain/kotlin/com/bitsydarel/cleanarch/core/usecases/UseCase.kt
 * @param <out>
 */
abstract class UseCaseWithParams<in Params, out R> {
    /**
     * Build the use case to be executed.
     *
     * @param params required to build this use case.
     *
     * @return result [R] of the use case.
     */
   suspend protected abstract fun buildUseCase(params: Params) : R

    /**
     * Execute the use case.
     *
     * Called by client of the use case.
     *
     * @return [R] result of executing this use case
     */
    suspend fun execute(params: Params): R = buildUseCase(params)
}

