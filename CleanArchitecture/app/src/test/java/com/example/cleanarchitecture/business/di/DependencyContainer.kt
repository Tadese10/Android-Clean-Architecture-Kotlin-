package com.example.cleanarchitecture.business.di

import com.example.cleanarchitecture.business.data.NoteDataFactory
import com.example.cleanarchitecture.business.data.cache.FakeNoteCacheDataSourceImpl
import com.example.cleanarchitecture.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanarchitecture.business.data.network.FakeNoteNetworkDataSourceImpl
import com.example.cleanarchitecture.business.data.network.abstraction.NoteNetworkDataSource
import com.example.cleanarchitecture.business.domain.util.DateUtil
import com.example.cleanarchitecture.business.domain.model.NoteFactory
import com.example.cleanarchitecture.util.isUnitTest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


//Manual dependencies cox we haven't set up dagger for production yet
class DependencyContainer {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val dateUtil =
        DateUtil(dateFormat)
    lateinit var noteNetworkDataSource: NoteNetworkDataSource
    lateinit var noteCacheDataSource: NoteCacheDataSource
    lateinit var noteFactory: NoteFactory
    lateinit var noteDataFactory: NoteDataFactory

    init {
        isUnitTest = true // for Logger.kt
    }

    fun build() {

        this.javaClass.classLoader?.let{classLoader ->
            noteDataFactory = NoteDataFactory(classLoader)
        }
        noteFactory = NoteFactory(dateUtil)
        noteNetworkDataSource = FakeNoteNetworkDataSourceImpl(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            deletedNotesData = HashMap(),
            dateUtil = dateUtil
        )
        noteCacheDataSource = FakeNoteCacheDataSourceImpl(
            notesData = noteDataFactory.produceHashMapOfNotes(
                noteDataFactory.produceListOfNotes()
            ),
            dateUtil = dateUtil
        )
    }


}