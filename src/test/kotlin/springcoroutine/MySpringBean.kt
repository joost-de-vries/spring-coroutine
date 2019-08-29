package springcoroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.springframework.beans.factory.DisposableBean

class MySpringBean(dispatcher: CoroutineDispatcher) : SpringCoroutineScope by SpringCoroutineScope(dispatcher) {
    init {
        launch {
            while (true) {
                delay(100.hours)
            }
        }.invokeOnCompletion {
            println("my spring bean is being destroyed")
        }
    }
}