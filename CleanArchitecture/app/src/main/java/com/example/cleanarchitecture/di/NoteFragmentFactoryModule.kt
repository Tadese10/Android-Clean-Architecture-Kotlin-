package com.example.cleanarchitecture.di

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.example.cleanarchitecture.business.domain.util.DateUtil
import com.example.cleanarchitecture.framework.presentation.common.NoteFragmentFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@Module
object NoteFragmentFactoryModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideNoteFragmentFactory(
            viewModelFactory: ViewModelProvider.Factory,
            dateUtil: DateUtil
    ): FragmentFactory {
        return NoteFragmentFactory(
                viewModelFactory,
                dateUtil
        )
    }
}
