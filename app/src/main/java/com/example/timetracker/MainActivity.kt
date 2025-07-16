package com.example.timetracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var currentProject: String? = null
    private var startTime: Long = 0L
    private lateinit var logFile: File
    private lateinit var logView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logFile = File(filesDir, "timetracker_log.csv")
        logView = findViewById(R.id.logView)

        findViewById<Button>(R.id.buttonA).setOnClickListener { handleButton("A") }
        findViewById<Button>(R.id.buttonB).setOnClickListener { handleButton("B") }
        findViewById<Button>(R.id.buttonC).setOnClickListener { handleButton("C") }

        displayLog()
    }

    private fun handleButton(project: String) {
        if (currentProject == project) {
            stopTimer(project)
        } else {
            currentProject?.let { stopTimer(it) }
            startTimer(project)
        }
    }

    private fun startTimer(project: String) {
        currentProject = project
        startTime = System.currentTimeMillis()
    }

    private fun stopTimer(project: String) {
        val end = System.currentTimeMillis()
        if (currentProject == project) {
            currentProject = null
        }
        val line = "$project,$startTime,$end\n"
        logFile.appendText(line)
        displayLog()
    }

    private fun displayLog() {
        if (!logFile.exists()) {
            logView.text = ""
            return
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val logs = buildString {
            logFile.forEachLine { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    val project = parts[0]
                    val start = sdf.format(Date(parts[1].toLong()))
                    val end = sdf.format(Date(parts[2].toLong()))
                    append("$project: $start - $end\n")
                }
            }
        }
        logView.text = logs
    }
}
