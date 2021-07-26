package com.rsschool.pomodoro.presentation.timerDialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.rsschool.pomodoro.databinding.DialogTimePickerBinding
import com.rsschool.pomodoro.entities.SelectTimeEntity
import com.rsschool.pomodoro.utils.TimeUnits
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimePickerDialogFragment : DialogFragment() {

    private val viewModel: TimerPickerDialogViewModel by viewModel()

    private var _binding: DialogTimePickerBinding? = null
    private val binding get() = _binding

    private var onSelectTimeListener: OnSelectTimeListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogTimePickerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPickersRanges()
        setStartPickersValues()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onDismiss(dialog: DialogInterface) {
        binding?.apply {
            SelectTimeEntity(
                hoursPicker.value,
                minutesPicker.value,
                secondsPicker.value,
            ).apply {
                keepSelectTime()
                passSelectTime()
            }
        }
        super.onDismiss(dialog)
    }

    fun setOnSelectTimeListener(listener: OnSelectTimeListener) {
        onSelectTimeListener = listener
    }

    private fun setPickersRanges() {
        val pickers = arrayOf(binding?.hoursPicker, binding?.minutesPicker, binding?.secondsPicker)
        val units = arrayOf(TimeUnits.HOUR, TimeUnits.MINUTE, TimeUnits.SECOND)

        pickers.forEachIndexed { index, numberPicker ->
            numberPicker?.apply {
                setFormatter { value ->
                    String.format("%02d", value)
                }
                units[index].let { unit ->
                    minValue = unit.min
                    maxValue = unit.max
                    value = unit.default
                }
            }
        }
    }

    private fun setStartPickersValues() {
        binding?.apply {
            viewModel.selectTime.apply {
                hoursPicker.value = hours
                minutesPicker.value = minutes
                secondsPicker.value = seconds
            }
        }
    }

    private fun SelectTimeEntity.keepSelectTime() {
        viewModel.keepSelectTime(this)
    }

    private fun SelectTimeEntity.passSelectTime() {
        onSelectTimeListener?.setTime(this)
    }

}