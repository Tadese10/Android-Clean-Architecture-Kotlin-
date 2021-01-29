package com.example.cleanarchitecture.framework.presentation.splash

import android.os.Bundle
import android.view.View
import com.example.cleanarchitecture.R
import com.example.cleanarchitecture.framework.presentation.common.BaseNoteFragment

class SplashFragment: BaseNoteFragment((R.layout.fragment_splash)){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun inject() {
        TODO("prepare dagger")
    }

}