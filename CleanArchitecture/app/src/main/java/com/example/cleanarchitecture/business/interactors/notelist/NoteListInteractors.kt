package com.example.cleanarchitecture.business.interactors.notelist

import com.example.cleanarchitecture.business.interactors.common.DeleteNote
import com.example.cleanarchitecture.framework.presentation.notelist.state.NoteListViewState

class NoteListInteractors (
    val insertNewNote: InsertNewNote,
    val deleteNote: DeleteNote<NoteListViewState>,
    val searchNotes: SearchNotes,
    val getNumNotes: GetNumNotes,
    val restoreDeletedNote: RestoreDeletedNote,
    val deleteMultipleNotes: DeleteMultipleNotes,
    val insertMultipleNotes: InsertMultipleNotes
)