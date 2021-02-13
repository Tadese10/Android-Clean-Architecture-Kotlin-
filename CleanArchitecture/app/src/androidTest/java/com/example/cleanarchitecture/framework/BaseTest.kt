package com.example.cleanarchitecture.framework

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cleanarchitecture.framework.presentation.TestBaseApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class BaseTest {

    // dependencies
    val application : TestBaseApplication = ApplicationProvider
        .getApplicationContext<Context>() as TestBaseApplication

    abstract fun injectTest()
}