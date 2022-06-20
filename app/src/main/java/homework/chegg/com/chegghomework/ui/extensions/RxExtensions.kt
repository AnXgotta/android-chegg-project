package homework.chegg.com.chegghomework.ui.extensions

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Drops a crash tracing exception at this [Single]'s call site to improve crash reports, then
 * calls [Single.subscribe].
 */
fun <T> Single<T>.subscribeWithTrace(
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit,
): Disposable {
    return addCrashTrace().subscribe(onSuccess, onError)
}

/**
 * Drops a crash tracing exception at this [Single]'s call site to improve crash reports, then
 * calls [Single.subscribe].
 */
fun <T> Single<T>.subscribeWithTrace(observer: SingleObserver<in T>) {
    return addCrashTrace().subscribe(observer)
}

/**
 * Drops a crash tracing exception at this [Single]'s call site to improve crash reports.
 */
fun <T> Single<T>.addCrashTrace(): Single<T> {
    val crashTraceException = RxCrashTraceException()
    return onErrorResumeNext { e: Throwable ->
        e.stackTrace = getCombinedStackTrace(e, crashTraceException)
        Single.error(e)
    }
}

fun getCombinedStackTrace(throwable: Throwable, crashTraceException: RxCrashTraceException):
    Array<StackTraceElement> {
    return crashTraceException.stackTrace.plus(throwable.stackTrace ?: emptyArray())
}

class RxCrashTraceException : Exception()
