package com.mazaady.task.secondscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mazaady.task.R
import com.mazaady.task.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binds: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binds = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binds.root)


        binds.rvLive.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binds.rvLive.adapter = LiveAdapter(
            listOf(
                R.drawable.ic_live_avatar_1,
                R.drawable.ic_live_avatar_2,
                R.drawable.ic_live_avatar_3,
                R.drawable.ic_live_avatar_1,
                R.drawable.ic_live_avatar_2,
                R.drawable.ic_live_avatar_3
            )
        )

        binds.rvTitle.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binds.rvTitle.adapter = TitlesAdapter(
            listOf("All", "UI/UX", "Illustration", "3D Animation")
        )

        binds.rvBook.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binds.rvBook.adapter = BooksAdapter(2)

    }

}