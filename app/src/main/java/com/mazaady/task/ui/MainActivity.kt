package com.mazaady.task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mazaady.task.databinding.ActivityMainBinding
import com.mazaady.task.ui.category.CategoryActivity
import com.mazaady.task.ui.second.SecondActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binds: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binds.root)

        binds.btnFirstScreen.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        binds.btnSecondScreen.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}