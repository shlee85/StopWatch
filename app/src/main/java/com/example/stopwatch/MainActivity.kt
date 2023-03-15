package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.NonCancellable.start
import org.w3c.dom.Text
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var isRunning = false

    private lateinit var btn_start: Button
    private lateinit var btn_refresh: Button
    private lateinit var tv_millsecond: TextView
    private lateinit var tv_second: TextView
    private lateinit var tv_minute: TextView

    var timer: Timer? = null
    var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start = findViewById(R.id.btn_start)
        btn_refresh = findViewById(R.id.btn_refresh)
        tv_millsecond = findViewById(R.id.tv_millisecond)
        tv_second = findViewById(R.id.tv_second)
        tv_minute = findViewById(R.id.tv_minute)

        //click listener 등록
        btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_start -> {
                Log.d(TAG, "Btn Start")
                if(isRunning) {
                    pause()
                } else {
                    start()
                }
            }
            R.id.btn_refresh -> {
                Log.d(TAG, "Btn refresh")
                refresh()
            }
        }
    }

    private fun start() {
        Log.d(TAG, "start!!")

        //textView 변경
        btn_start.text = "일시정지"
        btn_start.setBackgroundColor(getColor(R.color.red))
        isRunning = true

        //스톱워치 시작 로직, timer()는 10미리단위로 백그라운드쓰레드에서 동작 된다.
        timer = timer(period = 10) {
            time++ //10밀리초 단위 타이머

            //시간 계산
            val milli_second = time % 100
            val second = (time % 6000) / 100
            val minute = time / 6000

            //밀리초
            runOnUiThread {
                if(isRunning) {
                    tv_millsecond.text =
                        if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                    tv_second.text = if (second < 10) ":0${second}" else ":${second}"
                    tv_minute.text = "$minute"
                }
            }
        }
    }

    private fun pause() {
        Log.d(TAG, "pause!!")

        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))
        isRunning = false

        timer?.cancel()
    }

    private fun refresh() {
        Log.d(TAG, "refresh!!")

        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))
        isRunning = false

        timer = null
        time = 0

        tv_millsecond.text = ".00"
        tv_second.text = ":00"
        tv_minute.text = "00"
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}