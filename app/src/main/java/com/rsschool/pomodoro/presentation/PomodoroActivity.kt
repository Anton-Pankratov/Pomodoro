package com.rsschool.pomodoro.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rsschool.domain.entity.ShowTimer
import com.rsschool.pomodoro.R
import com.rsschool.pomodoro.databinding.ActivityPomodoroBinding
import com.rsschool.pomodoro.entities.SelectTimeEntity
import com.rsschool.pomodoro.presentation.adapter.OnButtonsClickListener
import com.rsschool.pomodoro.presentation.adapter.TimersAdapter
import com.rsschool.pomodoro.presentation.timerDialog.OnSelectTimeListener
import com.rsschool.pomodoro.presentation.timerDialog.TimePickerDialogFragment
import com.rsschool.pomodoro.utils.setFormatTime
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PomodoroActivity : AppCompatActivity() {

    private val viewModel: PomodoroViewModel by viewModel()

    private val timePickerDialog: TimePickerDialogFragment by inject()

    private var _binding: ActivityPomodoroBinding? = null
    private val binding get() = _binding

    private var timersAdapter: TimersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setViews()
    }

    private fun setViews() {
        collectTimers()
        setToolbarIcon()
        showTimerPickerByClick()
        listenSelectTimeFromPicker()
    }

    private fun collectTimers() {
        timersAdapter = TimersAdapter()
        binding?.timers?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = timersAdapter

            this@PomodoroActivity.let { context ->
                viewModel.apply {
                    timersFlow.observe(context) { timers ->
                        if (context.lifecycle.currentState == Lifecycle.State.RESUMED)
                            timersAdapter?.apply {
                                listenOnButtonsClicks()
                                setTimers(timers)
                            }
                    }
                }
            }
        }
    }

    private fun setToolbarIcon() {
        binding?.toolbar?.menu?.getItem(0)?.icon =
            ContextCompat.getDrawable(
                this, R.drawable.ic_select_time
            )
    }

    private fun showTimerPickerByClick() {
        binding?.toolbar?.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
            if (!timePickerDialog.isResumed)
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

    private fun TimersAdapter.listenOnButtonsClicks() {
        setOnButtonsClickListener(object : OnButtonsClickListener {
            override fun onControlClick(timer: ShowTimer?) {
                if (timer != null) {
                    viewModel.setControlActionFor(timer)
                }
            }

            override fun onDeleteClick(timer: ShowTimer?) {
                viewModel.deleteTimer(timer)
            }

            override fun onAddListener() {
                viewModel.saveTimer()
            }
        })
    }
}