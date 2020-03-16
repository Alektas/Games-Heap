package alektas.gamesheap.common

/**
 * Disposable event that returns containing value only once.
 * After the first requesting returns 'null'.
 *
 * @param <T> entity that this event contain
 */
class DisposableContainer<T>(v: T?) {
    private var isHandled = false
    val value: T? = v
        /**
         * Disposable request for the entity that is in the Event.
         *
         * @return entity at the first request or null after.
         */
        get() {
            if (isHandled) return null
            isHandled = true
            return field
        }
}