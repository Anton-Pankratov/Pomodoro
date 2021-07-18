package com.rsschool.pomodoro.presentation

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.pomodoro.adapter.TimersAdapter
import com.rsschool.pomodoro.databinding.ActivityPomodoroBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PomodoroActivity : AppCompatActivity() {

    private val viewModel: PomodoroViewModel by viewModel()

    private var _binding: ActivityPomodoroBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setTimers()
    }

    private fun setTimers() {
        viewModel.timersFlow.observe(this) { timers ->
            binding?.timers?.adapter = TimersAdapter().apply {
                this.setTimers(timers)
            }
        }
    }
}