package com.example.makelayout

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.widget.Toast

var opened = -1
var near_win=16

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(applicationContext)

        layout.orientation = LinearLayout.VERTICAL

        val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.weight = 1.toFloat() // единичный вес

        // TODO: 3) реализовать переворот карт с "рубашки" на лицевую сторону и обратно
        val colorListener = View.OnClickListener() {
            GlobalScope.launch (Dispatchers.Main)
                { setImageWithDelay(it as ImageView) }

        }


        val catViews = ArrayList<ImageView>()

        val pictures= arrayOf(R.drawable.borsch, R.drawable.pushnoy,R.drawable.kopatich,R.drawable.mirror)
        var mas_of_pictures=arrayOf(1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4)
        mas_of_pictures.shuffle()
        for (i in 0..15) {
            catViews.add( // вызываем конструктор для создания нового ImageView
                    ImageView(applicationContext).apply {
                        setImageResource(R.drawable.another_cat)
                        layoutParams = params
                        setOnClickListener(colorListener)
                        when(mas_of_pictures[i])
                        {
                            1 -> setTag("Borsch")
                            2 -> setTag("Pushnoy")
                            3 -> setTag("Mirror")
                            4 -> setTag("Kopatich")
                        }
                        id=i
                    })
        }
        val rows = Array(4, { LinearLayout(applicationContext)})
        var count = 0
        for (view in catViews) {
            val row: Int = count / 4
            rows[row].addView(view)
            count ++
        }
        for (row in rows) {
            layout.addView(row)
        }

        setContentView(layout)
    }

    suspend fun setImageWithDelay(v: ImageView) {
        delay(1000)
        if(v.tag=="Borsch")
        {
            v.setImageResource(R.drawable.borsch)
        }
        else if(v.tag=="Pushnoy")
        {
            v.setImageResource(R.drawable.pushnoy)
        }
        else if(v.tag=="Mirror")
        {
            v.setImageResource(R.drawable.mirror)
        }
        else
            v.setImageResource(R.drawable.kopatich)
        if(opened==-1)
            opened=v.id
        else
        {
            delay(1000)
            val previous=findViewById(opened) as ImageView
            if(previous.tag==v.tag && previous.id!=v.id)
            {
                v.visibility = View.INVISIBLE
                v.isClickable = false
                previous.visibility = View.INVISIBLE
                previous.isClickable = false
                near_win-=2
                opened=-1
                if(near_win==0)
                {
                    val text = "ВЫ победили!"
                    val duration = Toast.LENGTH_SHORT

                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
            }
            else
            {
                v.setImageResource(R.drawable.another_cat)
                previous.setImageResource(R.drawable.another_cat)
                opened=-1
            }
        }
    }

    suspend fun openCards() {

    }
}