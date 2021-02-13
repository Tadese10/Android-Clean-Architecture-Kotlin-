package com.example.cleanarchitecture.business.data.cache.abstraction

import com.example.cleanarchitecture.business.domain.model.Note

interface NoteCacheDataSource{

    suspend fun insertNote(note : Note):Long

    suspend fun deleteNote(primary: String) : Int

    suspend fun deleteNotes(notes: List<Note>) : Int

    suspend fun updateNote(primary: String, newTitle:   String, newBody: String, timeStamp: String?): Int

    suspend fun searchNotes(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note>

    suspend fun searchNoteById(primaryKey: String): Note?

    suspend fun getNumNotes(): Int

    suspend fun insertNotes(notes: List<Note>): LongArray

    suspend fun getAllNotes(): List<Note>
}