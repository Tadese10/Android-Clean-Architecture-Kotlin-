package com.example.cleanarchitecture.di

import com.example.cleanarchitecture.di.AppComponent
import com.example.cleanarchitecture.di.AppModule
import com.example.cleanarchitecture.framework.datasource.cache.NoteDaoServiceTest
import com.example.cleanarchitecture.framework.datasource.data.NoteDataFactory
import com.example.cleanarchitecture.framework.datasource.network.NoteFirestoreServiceTests
import com.example.cleanarchitecture.framework.presentation.TestBaseApplication
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
@Component(
    modules = [
        TestModule::class,
        AppModule::class
    ]
)
interface TestAppComponent: AppComponent {

    @Component.Factory
    interface Factory{

        fun create(@BindsInstance app: TestBaseApplication): TestAppComponent
    }

    fun inject(noteFirestoreServiceTests: NoteFirestoreServiceTests)

    fun inject(noteDaoServiceTest: NoteDaoServiceTest)
}













