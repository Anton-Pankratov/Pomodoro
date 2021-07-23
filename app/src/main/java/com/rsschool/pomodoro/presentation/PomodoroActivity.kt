package com.rsschool.pomodoro.presentation

import android.os.Bundle
import android.view.MenuItem
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.adapter.OnAddTimerClickListener
import com.rsschool.pomodoro.adapter.TimersAdapter
import com.rsschool.pomodoro.databinding.ActivityPomodoroBinding
import com.rsschool.pomodoro.debug
import org.koin.androidx.viewmodel.ext.android.viewModel

class PomodoroActivity : AppCompatActivity() {

    private val viewModel: PomodoroViewModel by viewModel()

    private var _binding: ActivityPomodoroBinding? = null
    private val binding get() = _binding

    private val timersAdapter by lazy { TimersAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        selectTime()
        setTimers()
    }

    private fun selectTime() {
        binding?.toolbar?.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            val dialog = AlertDialog.Builder(this)

            dialog.show()
            return@OnMenuItemClickListener true
        })
    }

    private fun setTimers() {
        viewModel.timersFlow.observe(this) { timers ->
            binding?.timers?.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = timersAdapter.apply {
                    listenAddTimerButtonClick()
                    setTimers(timers)
                }
            }
        }
    }

    private fun TimersAdapter.listenAddTimerButtonClick() {
        setOnAddTimerClickListener(object : OnAddTimerClickListener {
            override fun onClick() {

            }
        })
    }
}