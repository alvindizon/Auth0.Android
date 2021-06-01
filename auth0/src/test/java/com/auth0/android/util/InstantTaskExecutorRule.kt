package com.auth0.android.util

import com.auth0.android.request.internal.ArchTaskExecutor
import com.auth0.android.request.internal.TaskExecutor
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit Test Rule that swaps the background executor used by the Architecture Components with a
 * different one which executes each task synchronously.
 *
 *
 * You can use this rule for your host side tests that use Architecture Components.
 */
public class InstantTaskExecutorRule : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        ArchTaskExecutor.instance.setDelegate(object : TaskExecutor() {
            override fun backgroundThread(runnable: Runnable) {
                runnable.run()
            }

            override fun mainThread(runnable: Runnable) {
                runnable.run()
            }

            override val isMainThread: Boolean
                get() = true
        })
    }

    override fun finished(description: Description) {
        super.finished(description)
        ArchTaskExecutor.instance.setDelegate(null)
    }
}