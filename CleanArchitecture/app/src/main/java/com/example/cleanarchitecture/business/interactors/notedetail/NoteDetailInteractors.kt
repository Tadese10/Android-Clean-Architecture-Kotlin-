package com.example.cleanarchitecture.business.interactors.notedetail

import com.example.cleanarchitecture.business.interactors.common.DeleteNote
import com.example.cleanarchitecture.framework.presentation.notedetail.state.NoteDetailViewState

class NoteDetailInteractors (
    val deleteNote: DeleteNote<NoteDetailViewState>,
    val updateNote: UpdateNote
)