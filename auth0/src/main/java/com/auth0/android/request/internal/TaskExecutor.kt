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

/**
 * A task executor that can divide tasks into logical groups.
 *
 *
 * It holds a collection a executors for each group of task.
 *
 *
 * @hide
 */
public abstract class TaskExecutor {
    /**
     * Executes the given task in the disk IO thread pool.
     *
     * @param runnable The runnable to run in the disk IO thread pool.
     */
    public abstract fun backgroundThread(runnable: Runnable)

    /**
     * Posts the given task to the main thread.
     *
     * @param runnable The runnable to run on the main thread.
     */
    public abstract fun mainThread(runnable: Runnable)

    /**
     * Executes the given task on the main thread.
     *
     *
     * If the current thread is a main thread, immediately runs the given runnable.
     *
     * @param runnable The runnable to run on the main thread.
     */
    public fun executeOnMainThread(runnable: Runnable) {
        if (isMainThread) {
            runnable.run()
        } else {
            mainThread(runnable)
        }
    }

    /**
     * Returns true if the current thread is the main thread, false otherwise.
     *
     * @return true if we are on the main thread, false otherwise.
     */
    public abstract val isMainThread: Boolean
}