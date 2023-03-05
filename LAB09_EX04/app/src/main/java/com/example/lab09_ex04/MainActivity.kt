package com.example.lab09_ex04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import com.example.lab09_ex04.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        var binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.activityMain)

        var isStopped = true
        var isPaused = false

        val channel = Channel<Int>()
        val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
        var sum = 0L

        backgroundScope.launch {

            for (i in 1..2_000_000_000) {
                if (!isPaused && !isStopped) {
                    sum += 1
                }
                channel.send(sum.toInt())
            }
        }


        var mainScope = GlobalScope.launch(Dispatchers.Main) {
            channel.consumeEach {
                binding.time.text = toTime(it)
            }
        }

        binding.start.setOnClickListener {
            if ( isStopped ) {
                isStopped = false
            }
        }

        binding.stop.setOnClickListener {
            if ( !isStopped ) {
                isStopped = true
                isPaused = false
                sum = 0L
            }
        }

        binding.resume.setOnClickListener {
            if ( isPaused ) {
                isPaused = false
            }
        }

        binding.pause.setOnClickListener {
            if ( !isStopped && !isPaused ) {
                isPaused = true
            }
        }
    }

    fun toTime(inputInt : Int) : String {
        var time = inputInt/110

        var underDot : Int = 0
        var underDotStr : String = ""

        var seconds : Int = 0
        var secondsStr : String = ""

        var minutes : Int = 0
        var minutesStr : String = ""

        underDot = time % 100
        time /= 100

        if ( underDot < 10) {
            underDotStr = "0" + underDot.toString()
        }
        else {
            underDotStr = underDot.toString()
        }

        seconds = time % 60
        time /= 60

        if ( seconds < 10) {
            secondsStr = "0" + seconds.toString()
        }
        else {
            secondsStr = seconds.toString()
        }

        minutes = time % 100

        if ( minutes < 10) {
            minutesStr = "0" + minutes.toString()
        }
        else {
            minutesStr = minutes.toString()
        }

        return "%s:%s:%s".format(minutesStr, secondsStr, underDotStr)
    }

}