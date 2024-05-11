package com.example.todolist.ui.theme

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

data class ToDoItems(var name: String, var time: String)

@Composable
fun ToDoListAPP() {
    var showDialog by remember { mutableStateOf(false) }
    var Name by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>>(0 to 0) }
    var displayTime by remember { mutableStateOf("00:00") }
    var ToDoListItems by remember {
        mutableStateOf(listOf<ToDoItems>())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showDialog = true }) {
            Text(text = "Type ToDo List")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(ToDoListItems){ item ->
                ToDOListItem(item)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(onClick = {
                            if (Name.isNotEmpty()){
                                val newItem = ToDoItems(
                                    name = Name,
                                    time = displayTime
                                )
                                ToDoListItems = ToDoListItems + newItem
                                Name = ""
                                showDialog = false
                            }
                        }) {
                            Text(text = "Confirm")
                        }
                        Button(onClick = { showDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
                },
                title = { Text(text = "Add your Todo List") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = Name,
                            onValueChange = { Name = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        ClockPickerDemo(
                            selectedTime = selectedTime,
                            displayTime = displayTime,
                            onTimeSet = { hour, minute ->
                                selectedTime = hour to minute
                                displayTime = "$hour:${minute.toString().padStart(2, '0')}"
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun ClockPickerDemo(
    selectedTime: Pair<Int, Int>,
    displayTime: String,
    onTimeSet: (hourOfDay: Int, minute: Int) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = displayTime,
            onValueChange = {},
            label = { Text("Selected Time") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                showTimePickerDialog(context) { hourOfDay, minute ->
                    onTimeSet(hourOfDay, minute)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Select Time")
        }
    }
}

fun showTimePickerDialog(context: Context, onTimeSet: (hourOfDay: Int, minute: Int) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            onTimeSet(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true
    )
    timePickerDialog.show()
}

@Composable
fun ToDOListItem(
    items: ToDoItems
){
    Row(
        modifier = Modifier.run{
            fillMaxWidth()
                .padding(8.dp)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color(0xFF018786),
                    )
                )
        }
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Title: ${items.name}", color = Color.Green)
            Text(text = "Time: ${items.time}", color = Color.Red)
        }
    }
}
