package com.sugaryple.testsample

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import io.mockk.clearStaticMockk
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.spekframework.spek2.dsl.Root

fun mockLiveDataTaskExecutor() {
    ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) {
            runnable.run()
        }

        override fun isMainThread(): Boolean {
            return true
        }

        override fun postToMainThread(runnable: Runnable) {
            runnable.run()
        }
    })
}

@ExperimentalCoroutinesApi
fun Root.mockCoroutineDispatcher(): TestCoroutineScope {

    val testDispatcher = TestCoroutineDispatcher()
    val testScope = TestCoroutineScope(testDispatcher)

    beforeEachTest {
        Dispatchers.setMain(testDispatcher)
    }
    afterEachTest {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }
    return testScope
}

fun Root.mockDispatcherIO(coroutineDispatcher: CoroutineDispatcher) {
    beforeEachTest {
        mockkStatic(Dispatchers::class).apply {
            every {
                Dispatchers.IO
            } returns (coroutineDispatcher)
        }
    }
    afterEachTest {
        clearStaticMockk(Dispatchers::class)
    }
}
