package homework.chegg.com.chegghomework.util

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Emits data from the [localDataSource] if it exists while fetching fresh data from the [remoteDataSource].
 * If the [remoteDataSource] returns successfully, we cache the data locally via [saveCallResult]
 * and then emit the fresh data
 */
@JvmName("performGetOperationWithSingles")
fun <T : Any, A : Any> performGetOperation(
    localDataSource: () -> Single<T>,
    remoteDataSource: () -> Single<T>,
    saveCallResult: (remoteData: T) -> Single<A>,
): Observable<T> =
    localDataSource.invoke()
        .concatWith(
            remoteDataSource.invoke()
                .flatMap { remoteData ->
                    saveCallResult.invoke(remoteData)
                        .ignoreElement()
                        .andThen(Single.just(remoteData))
                }
        ).toObservable()

