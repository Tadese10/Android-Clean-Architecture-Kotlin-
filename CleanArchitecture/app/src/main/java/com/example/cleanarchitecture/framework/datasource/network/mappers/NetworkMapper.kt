package com.example.cleanarchitecture.framework.datasource.network.mappers

import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.business.domain.util.DateUtil
import com.example.cleanarchitecture.business.domain.util.EntityMapper
import com.example.cleanarchitecture.framework.datasource.network.model.NoteNetworkEntity
import javax.inject.Inject


/**
 * Maps Note to NoteNetworkEntity or NoteNetworkEntity to Note.
 */
class NetworkMapper
@Inject
constructor(
    private val dateUtil: DateUtil
): EntityMapper<NoteNetworkEntity, Note>
{

    fun entityListToNoteList(entities: List<NoteNetworkEntity>): List<Note>{
        val list: ArrayList<Note> = ArrayList()
        for(entity in entities){
            list.add(mapFromEntity(entity))
        }
        return list
    }

    fun noteListToEntityList(notes: List<Note>): List<NoteNetworkEntity>{
        val entities: ArrayList<NoteNetworkEntity> = ArrayList()
        for(note in notes){
            entities.add(mapToEntity(note))
        }
        return entities
    }

    override fun mapFromEntity(entity: NoteNetworkEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            body = entity.body,
            updatedAt = dateUtil.convertFirebaseTimestampToStringDate(entity.updated_at),
            createdAt = dateUtil.convertFirebaseTimestampToStringDate(entity.created_at)
        )
    }

    override fun mapToEntity(domainModel: Note): NoteNetworkEntity {
        return NoteNetworkEntity(
            id = domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
            updated_at = dateUtil.convertStringDateToFirebaseTimeStamp(domainModel.updatedAt),
            created_at = dateUtil.convertStringDateToFirebaseTimeStamp(domainModel.createdAt)
        )
    }


}