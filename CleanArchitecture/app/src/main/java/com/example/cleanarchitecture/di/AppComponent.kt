package com.example.cleanarchitecture.di

import com.example.cleanarchitecture.framework.presentation.BaseApplication
import com.example.cleanarchitecture.framework.presentation.MainActivity
import com.example.cleanarchitecture.framework.presentation.notedetail.NoteDetailFragment
import com.example.cleanarchitecture.framework.presentation.notelist.NoteListFragment
import com.example.cleanarchitecture.framework.presentation.splash.SplashFragment
import dagger.Binds
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
        AppModule::class,
        ProductionModule::class,
        NoteViewModelModule::class,
        NoteFragmentFactoryModule::class
    ]
)
interface  AppComponent{


    @Component.Factory//You can also use @Component.Builder
    interface Factory{
        fun create(@BindsInstance app: BaseApplication): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(noteDetailFragment: NoteDetailFragment)

    fun inject(noteListFragment: NoteListFragment)

    fun inject(splashFragment: SplashFragment)

}