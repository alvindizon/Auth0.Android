/*
 * Copyright (C) 2017 The Android Open Source Project
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
package com.auth0.android.request.internal

import java.util.concurrent.Executor

/**
 * A static class that serves as a central point to execute common tasks.
 *
 *
 *
 * @hide This API is not final.
 */
public class ArchTaskExecutor private constructor() : TaskExecutor() {
    private var mDelegate: TaskExecutor
    private val mDefaultTaskExecutor: TaskExecutor

    /**
     * Sets a delegate to handle task execution requests.
     *
     *
     * If you have a common executor, you can set it as the delegate and App Toolkit components will
     * use your executors. You may also want to use this for your tests.
     *
     *
     * Calling this method with `null` sets it to the default TaskExecutor.
     *
     * @param taskExecutor The task executor to handle task requests.
     */
    public fun setDelegate(taskExecutor: TaskExecutor?) {
        mDelegate = taskExecutor ?: mDefaultTaskExecutor
    }

    override fun backgroundThread(runnable: Runnable) {
        mDelegate.backgroundThread(runnable)
    }

    override fun mainThread(runnable: Runnable) {
        mDelegate.mainThread(runnable)
    }


    override val isMainThread: Boolean
        get() = mDelegate.isMainThread

    public companion object {
        @Volatile
        private var sInstance: ArchTaskExecutor? = null
        public val mainThreadExecutor: Executor = Executor { command -> instance.mainThread(command) }
        public val iOThreadExecutor: Executor = Executor { command -> instance.backgroundThread(command) }

        /**
         * Returns an instance of the task executor.
         *
         * @return The singleton ArchTaskExecutor.
         */
        @JvmStatic
        public val instance: ArchTaskExecutor
            get() {
                if (sInstance != null) {
                    return sInstance!!
                }
                synchronized(ArchTaskExecutor::class.java) {
                    if (sInstance == null) {
                        sInstance = ArchTaskExecutor()
                    }
                }
                return sInstance!!
            }
    }

    init {
        mDefaultTaskExecutor = DefaultTaskExecutor()
        mDelegate = mDefaultTaskExecutor
    }
}