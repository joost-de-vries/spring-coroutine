# Kotlin Spring CoroutineScope
Run Kotlin coroutines scoped to a Spring framework bean. Provides a Spring `CoroutineScope` that hooks into Spring bean lifecycle.


Add `compile("it.the-source:dispatcher:0.2")`
to your dependencies and use it like this
```kotlin
class MySpringBean(dispatcher: CoroutineDispatcher) : MyAbstractBean(), SpringScope by SpringScope(dispatcher) {
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
```

Coroutines started within the Spring bean will be scoped to the lifecycle of the bean. That is they will be cancelled on Spring [destroy](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lifecycle).   
If you want to append additional elements to the  scope, use `CoroutineScope.plus` operator:
```kotlin
val scope = SpringScope() + CoroutineName("MyActivity")
```
If you want a [SupervisorJob](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-supervisor-job.html) parent
```kotlin
class MySpringBean(dispatcher: CoroutineDispatcher) : SpringScope by SpringScope(dispatcher, SupervisorJob()) {

}
```

If you want to use [Dispatchers.Default](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-default.html) you can leave out the dispatcher argument
```kotlin
class MySpringBean() : SpringScope by SpringScope() {

}
```
