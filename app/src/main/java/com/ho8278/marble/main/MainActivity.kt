package com.ho8278.marble.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ho8278.marble.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}