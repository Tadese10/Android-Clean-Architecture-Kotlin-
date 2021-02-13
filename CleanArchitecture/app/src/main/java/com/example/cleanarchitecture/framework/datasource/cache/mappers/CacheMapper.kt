package com.example.cleanarchitecture.framework.datasource.cache.mappers

import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.business.domain.util.DateUtil
import com.example.cleanarchitecture.business.domain.util.EntityMapper
import com.example.cleanarchitecture.framework.datasource.cache.model.NoteCacheEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheMapper
@Inject
constructor():
    EntityMapper<NoteCacheEntity, Note>{

    fun entityListToNoteList(entities: List<NoteCacheEntity>): List<Note>{
        val list : ArrayList<Note> = ArrayList()
        for(entity in entities)
            list.add(mapFromEntity(entity))

        return list
    }

    fun noteListToEntityList(notes: List<Note>): List<NoteCacheEntity>{
        val list : ArrayList<NoteCacheEntity> = ArrayList()
        for(note in notes)
            list.add(mapToEntity(note))

        return list
    }

    override fun mapFromEntity(entity: NoteCacheEntity): Note {
       return Note(
           id =  entity.id,
           title = entity.title,
           body = entity.body,
           createdAt = entity.created_at,
           updatedAt = entity.updated_at
       )
    }

    override fun mapToEntity(domainModel: Note): NoteCacheEntity {
        return NoteCacheEntity(
            id =  domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
            created_at = domainModel.createdAt,
            updated_at = domainModel.updatedAt
        )
    }

}