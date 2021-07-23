package com.rsschool.pomodoro.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.presentation.adapter.OnAddTimerClickListener
import com.rsschool.pomodoro.presentation.adapter.TimersAdapter
import com.rsschool.pomodoro.databinding.ActivityPomodoroBinding
import com.rsschool.pomodoro.entities.SelectTimeEntity
import com.rsschool.pomodoro.presentation.adapter.TimersDiffUtilsCallback
import com.rsschool.pomodoro.presentation.timerDialog.OnSelectTimeListener
import com.rsschool.pomodoro.presentation.timerDialog.TimePickerDialogFragment
import com.rsschool.pomodoro.setFormatTime
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PomodoroActivity : AppCompatActivity() {

    private val viewModel: PomodoroViewModel by viewModel()
    private val timePickerDialog: TimePickerDialogFragment by inject()

    private var _binding: ActivityPomodoroBinding? = null
    private val binding get() = _binding

    private val timersAdapter by lazy { TimersAdapter() }
    private val diffUtilCallback: TimersDiffUtilsCallback? = null
    private val diffResult: DiffUtil.DiffResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setViews()
    }

    private fun setViews() {
        setToolbarIcon()
        showTimerPickerByClick()
        listenSelectTimeFromPicker()
        setTimers()
    }

    private fun setToolbarIcon() {
        binding?.toolbar?.menu?.getItem(0)?.icon =
            ContextCompat.getDrawable(
                this, R.drawable.ic_select_time
            )
    }

    private fun showTimerPickerByClick() {
        binding?.toolbar?.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            timePickerDialog.show(supportFragmentManager, "picker")
            return@OnMenuItemClickListener true
        })
    }

    private fun listenSelectTimeFromPicker() {
        timePickerDialog.setOnSelectTimeListener(object : OnSelectTimeListener {
            override fun setTime(time: SelectTimeEntity) {
                binding?.toolbar?.menu?.getItem(0)?.apply {
                    icon = null
                    time.let {
                        title = it.setFormatTime()
                    }
                }
            }
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
                //viewModel.saveTimer(ShowTimer())
            }
        })
    }
}