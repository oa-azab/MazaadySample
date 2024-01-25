package com.mazaady.task.ui.result

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mazaady.task.R
import com.mazaady.task.databinding.ActivityResultBinding
import com.mazaady.task.model.AppResult

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binds = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binds.root)

        val result = intent.getParcelableArrayListExtra<AppResult>("result") ?: arrayListOf()
        binds.rvResult.layoutManager = LinearLayoutManager(this)
        binds.rvResult.adapter = ResultAdapter(result.toList())
    }

    companion object {
        fun start(context: Context, array: ArrayList<AppResult>) {
            val intent = Intent(context, ResultActivity::class.java)
            intent.putExtra("result", array)
            context.startActivity(intent)
        }
    }
}