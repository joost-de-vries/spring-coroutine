Archived. Use 
```kotlin
Executor.asCorasCoroutineDispatcher()
``` 
on `TaskExecutor` instead.


# Run Kotlin coroutines on Spring TaskScheduler

Makes it possible to run Kotlin coroutines in existing Spring Boot applications.

All credit goes to [Konrad Kaminski](https://github.com/konrad-kaminski). 
This exists because the implementation is currently [private](https://github.com/konrad-kaminski/spring-kotlin-coroutine/blob/master/spring-kotlin-coroutine/src/main/kotlin/org/springframework/kotlin/coroutine/context/resolver/TaskSchedulerCoroutineContextResolver.kt).

