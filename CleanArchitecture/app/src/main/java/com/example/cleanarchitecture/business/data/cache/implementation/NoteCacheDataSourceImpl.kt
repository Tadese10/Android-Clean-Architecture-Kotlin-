package com.example.cleanarchitecture.business.data.cache.implementation

import com.example.cleanarchitecture.business.data.cache.abstraction.NoteCacheDataSource
import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.framework.datasource.cache.abstraction.NoteDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteCacheDataSourceImpl
@Inject
constructor(
    private val noteDaoService : NoteDaoService
) : NoteCacheDataSource
{

    override suspend fun insertNote(note: Note) = noteDaoService.insertNote(note)

    override suspend fun deleteNote(primary: String) = noteDaoService.deleteNote(primary)

    override suspend fun deleteNotes(notes: List<Note>) = noteDaoService.deleteNotes(notes)

    override suspend fun updateNote(primary: String, newTitle: String, newBody: String, timeStamp: String?) = noteDaoService.updateNote(primary,newTitle,newBody, timeStamp)

    override suspend fun searchNotes(query: String, filterAndOrder: String, page: Int) = noteDaoService.returnOrderedQuery(
        query, filterAndOrder, page
    )

    override suspend fun searchNoteById(primaryKey: String) = noteDaoService.searchNoteById(primaryKey)

    override suspend fun getNumNotes(): Int = noteDaoService.getNumNotes()

    override suspend fun insertNotes(notes: List<Note>): LongArray  = noteDaoService.insertNotes(notes)
    override suspend fun getAllNotes(): List<Note> = noteDaoService.getAllNotes()

}