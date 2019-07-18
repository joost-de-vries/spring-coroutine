package springcoroutine

/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.disposeOnCancellation
import org.springframework.scheduling.TaskScheduler
import java.util.Date
import java.util.concurrent.ScheduledFuture
import kotlin.coroutines.CoroutineContext

/** Allows running of coroutines on the spring taskscheduler */
// from https://github.com/konrad-kaminski/spring-kotlin-coroutine/blob/master/spring-kotlin-coroutine/src/main/kotlin/org/springframework/kotlin/coroutine/context/resolver/TaskSchedulerCoroutineContextResolver.kt
@ExperimentalCoroutinesApi
fun TaskScheduler.asCoroutineDispatcher(): CoroutineContext =
    TaskSchedulerDispatcher(this)

@UseExperimental(InternalCoroutinesApi::class)
@ExperimentalCoroutinesApi
class TaskSchedulerDispatcher(private val scheduler: TaskScheduler) : CoroutineDispatcher(), Delay {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        scheduler.schedule(block, Date())
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val disposable = scheduler.schedule({
            with(continuation) { resumeUndispatched(Unit) }
        }, Date(System.currentTimeMillis() + timeMillis)).asDisposableHandle()

        continuation.disposeOnCancellation(disposable)
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable): DisposableHandle =
        scheduler.schedule(block, Date(System.currentTimeMillis() + timeMillis)).asDisposableHandle()

    override fun toString(): String = scheduler.toString()
}

private fun <V> ScheduledFuture<V>.asDisposableHandle(): DisposableHandle = object : DisposableHandle {
    override fun dispose() {
        this@asDisposableHandle.cancel(true)
    }
}
