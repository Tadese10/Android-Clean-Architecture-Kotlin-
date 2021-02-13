package com.example.cleanarchitecture.framework.presentation

import com.example.cleanarchitecture.di.DaggerTestAppComponent
import com.example.cleanarchitecture.framework.presentation.BaseApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class TestBaseApplication : BaseApplication(){

    override fun initAppComponent() {
        appComponent = DaggerTestAppComponent
            .factory()
            .create(this)
    }

}