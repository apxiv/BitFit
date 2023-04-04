package com.google.bitfit

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import java.util.*

class Notification : Fragment() {

    private lateinit var submitButton: Button
    private lateinit var titleET: EditText
    private lateinit var messageET: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var datePicker: DatePicker


    companion object {
        const val notificationID = 1
        const val channelID = "channel1"
        const val titleExtra = "titleExtra"
        const val messageExtra = "messageExtra"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        submitButton = view.findViewById(R.id.submitButton)
        titleET = view.findViewById(R.id.titleET)
        messageET = view.findViewById(R.id.messageET)
        timePicker = view.findViewById(R.id.timePicker)
        datePicker = view.findViewById(R.id.datePicker)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        submitButton.setOnClickListener {
            titleET.setText("")
            messageET.setText("")
            scheduleNotification()
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(requireActivity(), NotificationBroadcastReceiver::class.java)
        val title = titleET.text.toString()
        val message = messageET.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()

        if (time <= System.currentTimeMillis()) {
            showAlert(time, title, message)
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        showAlert(time, title, message)
    }


    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireActivity())
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireActivity())

        AlertDialog.Builder(requireActivity()).setTitle("Reminder is scheduled").setMessage(
            "Title: $title, Message: $message at ${dateFormat.format(date)} ${
                timeFormat.format(date)
            }"
        ).setPositiveButton("Okay") { _, _ -> }.show()
    }

    private fun getTime(): Long {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel"
            val desc = "A Description of the Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = desc
            val notificationManager =
                requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
