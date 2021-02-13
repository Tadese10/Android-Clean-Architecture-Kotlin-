package com.example.cleanarchitecture.framework.datasource.network

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.cleanarchitecture.business.domain.model.Note
import com.example.cleanarchitecture.business.domain.model.NoteFactory
import com.example.cleanarchitecture.di.TestAppComponent
import com.example.cleanarchitecture.framework.BaseTest
import com.example.cleanarchitecture.framework.datasource.data.NoteDataFactory
import com.example.cleanarchitecture.framework.datasource.network.abstraction.NoteFireStoreService
import com.example.cleanarchitecture.framework.datasource.network.implementation.NoteFireStoreServiceImpl
import com.example.cleanarchitecture.framework.datasource.network.mappers.NetworkMapper
import com.example.cleanarchitecture.util.printLogD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random.Default.nextInt

@ExperimentalCoroutinesApi
@FlowPreview
@RunWith(AndroidJUnit4ClassRunner::class)
class NoteFirestoreServiceTests : BaseTest(){

    // system in test
    private lateinit var noteFirestoreService: NoteFireStoreService


        @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var noteDataFactory: NoteDataFactory

    @Inject
    lateinit var noteFactory: NoteFactory

    @Inject
    lateinit var networkMapper: NetworkMapper

    init {
        injectTest()
        signIn()
        insertTestData()
    }


    fun insertTestData() {
        val entityList = networkMapper.noteListToEntityList(
            noteDataFactory.produceListOfNotes()
        )
        for(entity in entityList){
            firestore
                .collection(NoteFireStoreServiceImpl.NOTES_COLLECTION)
                .document(NoteFireStoreServiceImpl.USER_ID)
                .collection(NoteFireStoreServiceImpl.NOTES_COLLECTION)
                .document(entity.id)
                .set(entity)
        }
    }

    private fun signIn() = runBlocking{
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            EMAIL,
            PASSWORD
        ).await()
    }

    @Before
    fun before(){
        noteFirestoreService = NoteFireStoreServiceImpl(
            firebaseAuth = FirebaseAuth.getInstance(),
            firestore = firestore,
            networkMapper = networkMapper
        )
    }

    @Test
    fun insertSingleNote_CBS() = runBlocking{
        val note = noteFactory.createSingleNote(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )

        noteFirestoreService.insertOrUpdateNote(note)

        val searchResult = noteFirestoreService.searchNote(note)

        assertEquals(note, searchResult)
    }

    @Test
    fun queryAllNotes() = runBlocking {
        val notes = noteFirestoreService.getAllNotes()
        printLogD("FireStoreServiceTests", "notes: ${notes.size}")
        assertTrue(notes.isNotEmpty())
    }


    @Test
    fun updateSingleNote_CBS() = runBlocking{

        val searchResults = noteFirestoreService.getAllNotes()

        // choose a random note from list to update
        val randomNote = searchResults.get(nextInt(0,searchResults.size-1) + 1)
        val UPDATED_TITLE = UUID.randomUUID().toString()
        val UPDATED_BODY = UUID.randomUUID().toString()
        var updatedNote = noteFactory.createSingleNote(
            id = randomNote.id,
            title = UPDATED_TITLE,
            body = UPDATED_BODY,
            createdAt = randomNote.createdAt
        )

        // make the update
        noteFirestoreService.insertOrUpdateNote(updatedNote)

        // query the note after update
        updatedNote = noteFirestoreService.searchNote(updatedNote)!!

        assertEquals(UPDATED_TITLE, updatedNote.title)
        assertEquals(UPDATED_BODY, updatedNote.body)
    }

//    @Test
//    fun insertNoteList_CBS() = runBlocking {
//        val list = noteDataFactory.produceListOfNotes()
//
//        noteFirestoreService.insertOrUpdateNotes(list)
//
//        val searchResults = noteFirestoreService.getAllNotes()
//
//        assertTrue(searchResults.containsAll(list))
//    }

    @Test
    fun deleteSingleNote_CBS() = runBlocking {
        val noteList = noteFirestoreService.getAllNotes()

        // choose one at random to delete
        val noteToDelete = noteList.get(kotlin.random.Random.nextInt(0, noteList.size - 1) + 1)

        noteFirestoreService.deleteNote(noteToDelete.id)

        // confirm it no longer exists in firestore
        val searchResults = noteFirestoreService.getAllNotes()
        var result = searchResults.contains(noteToDelete)
        assertFalse(result)
    }

    @Test
    fun insertIntoDeletesNode_CBS() = runBlocking {
        val noteList = noteFirestoreService.getAllNotes()

        // choose one at random to insert into "deletes" node
        val noteToDelete = noteList.get(kotlin.random.Random.nextInt(0, noteList.size - 1) + 1)

        noteFirestoreService.insertDeletedNote(noteToDelete)

        // confirm it is now in the "deletes" node
        val searchResults = noteFirestoreService.getDeletedNotes()

        assertTrue(searchResults.contains(noteToDelete))
    }

    @Test
    fun insertListIntoDeletesNode_CBS() = runBlocking {
        val noteList = ArrayList(noteFirestoreService.getAllNotes())

        // choose some random notes to add to "deletes" node
        val notesToDelete: ArrayList<Note> = ArrayList()

        // 1st
        var noteToDelete = noteList.get(kotlin.random.Random.nextInt(0, noteList.size - 1) + 1)
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // 2nd
        noteToDelete = noteList.get(kotlin.random.Random.nextInt(0, noteList.size - 1) + 1)
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // 3rd
        noteToDelete = noteList.get(kotlin.random.Random.nextInt(0, noteList.size - 1) + 1)
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // 4th
        noteToDelete = noteList.get(kotlin.random.Random.nextInt(0, noteList.size - 1) + 1)
        noteList.remove(noteToDelete)
        notesToDelete.add(noteToDelete)

        // insert into "deletes" node
        noteFirestoreService
            .insertDeletedNotes(notesToDelete)

        // confirm the notes are in "deletes" node
        val searchResults = noteFirestoreService.getDeletedNotes()

        assertTrue(searchResults.containsAll(notesToDelete))
    }

    @Test
    fun deleteDeletedNote_CBS() = runBlocking {
        val note = noteFactory.createSingleNote(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()
        )

        // insert into "deletes" node
        noteFirestoreService.insertDeletedNote(note)

        // confirm note is in "deletes" node
        var searchResults = noteFirestoreService.getDeletedNotes()

        assertTrue(searchResults.contains(note))

        // delete from "deletes" node
        noteFirestoreService.deleteDeletedNote(note)

        // confirm note is deleted from "deletes" node
        searchResults = noteFirestoreService.getDeletedNotes()

        assertFalse(searchResults.contains(note))
    }

    companion object{
        const val EMAIL = "tadese.teejay@gmail.com"
        const val PASSWORD = "Qwerty10,,"
    }

    override fun injectTest() {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

}
