package springcoroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.springframework.beans.factory.DisposableBean

/**
 * Creates the [CoroutineScope] for Spring beans.
 * Coroutines launched within this Spring bean will be scope to the lifecycle of the spring bean
 *
 * Example of use:
 * ```
*class MySpringBean(dispatcher: CoroutineDispatcher) : MyAbstractBean(), SpringCoroutineScope by SpringCoroutineScope(dispatcher) {

*}
 * ```
 *
 * The resulting scope has [ob] and [Dispatchers.Default] context elements.
 * If you want to append additional elements to the  scope, use [CoroutineScope.plus] operator:
 * `val scope = SpringScope() + CoroutineName("MyActivity")`.
 */
abstract class AbstractSpringScope(dispatcher: CoroutineDispatcher = Dispatchers.Default, job: Job = Job()) :
    CoroutineScope by CoroutineScope(dispatcher + job), DisposableBean {
    val job: Job
        get() = coroutineContext[Job]!!

    override fun destroy() {
        job.cancel()
    }
}
interface SpringScope : CoroutineScope, DisposableBean {
    val job: Job
}

@Suppress("FunctionName")
fun SpringScope(dispatcher: CoroutineDispatcher = Dispatchers.Default, job: Job = Job()): SpringScope = object :
    SpringScope, CoroutineScope by CoroutineScope(dispatcher + job), DisposableBean {
    override val job: Job
        get() = coroutineContext[Job]!!

    override fun destroy() {
        job.cancel()
    }
}
