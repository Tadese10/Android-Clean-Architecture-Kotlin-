package com.example.cleanarchitecture.framework.datasource.cache.implementation

import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.business.domain.util.DateUtil
import com.example.cleanarchitecture.framework.datasource.cache.abstraction.NoteDaoService
import com.example.cleanarchitecture.framework.datasource.cache.database.NoteDao
import com.example.cleanarchitecture.framework.datasource.cache.database.returnOrderedQuery
import com.example.cleanarchitecture.framework.datasource.cache.mappers.CacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDaoServiceImpl
@Inject
constructor(
    private val noteDao: NoteDao,
    private val noteMapper: CacheMapper,
    private val dateUtil: DateUtil
): NoteDaoService{
    override suspend fun insertNote(note: Note): Long {
        return noteDao.insertNote(noteMapper.mapToEntity(note))
    }

    override suspend fun deleteNote(primary: String): Int {
       return noteDao.deleteNote(primary)
    }

    override suspend fun deleteNotes(notes: List<Note>): Int {
       return noteDao.deleteNotes(noteMapper.noteListToEntityList(notes).map { it.id })
    }

    override suspend fun updateNote(primary: String, newTitle: String, newBody: String, timeStamp: String?): Int {
      return noteDao.updateNote(primary,newTitle, newBody, timeStamp ?: dateUtil.getCurrentTimeStamp())
    }

    override suspend fun searchNotes(): List<Note> {
        return noteMapper.entityListToNoteList(noteDao.searchNotes())
    }

    override suspend fun searchNotesOrderByDateDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
       return noteDao.searchNotesOrderByDateDESC(query,page,pageSize)?.let { noteMapper.entityListToNoteList(it) }
    }

    override suspend fun searchNotesOrderByDateASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteDao.searchNotesOrderByDateASC(query,page,pageSize)?.let { noteMapper.entityListToNoteList(it) }
    }

    override suspend fun searchNotesOrderByTitleDESC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteDao.searchNotesOrderByTitleDESC(query,page,pageSize)?.let { noteMapper.entityListToNoteList(it) }
    }

    override suspend fun searchNotesOrderByTitleASC(
        query: String,
        page: Int,
        pageSize: Int
    ): List<Note> {
        return noteDao.searchNotesOrderByTitleASC(query,page,pageSize)?.let { noteMapper.entityListToNoteList(it) }
    }

    override suspend fun searchNoteById(primaryKey: String): Note? {
        return noteDao.searchNoteById(primaryKey)?.let { noteMapper.mapFromEntity(it) }
    }

    override suspend fun getNumNotes(): Int {
        return noteDao.getNumNotes()
    }

    override suspend fun insertNotes(notes: List<Note>): LongArray {
        return noteDao.insertNotes(noteMapper.noteListToEntityList(notes))
    }

    override suspend fun returnOrderedQuery(
        query: String,
        filterAndOrder: String,
        page: Int
    ): List<Note> {
        return noteDao.returnOrderedQuery(query,filterAndOrder,page)?.let { noteMapper.entityListToNoteList(it) }
    }

    override suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()?.let{
            noteMapper.entityListToNoteList(it)
        }
    }

}