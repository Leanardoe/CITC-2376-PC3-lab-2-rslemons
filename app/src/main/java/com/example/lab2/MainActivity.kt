package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var taskText by remember { mutableStateOf("") }
    val taskList = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TaskInputField(
            taskText = taskText,
            onTaskTextChange = { taskText = it },
            onAddTask = {
                if (taskText.isNotBlank()) {
                    taskList.add(Task(taskText))
                    taskText = "" // Clear input field
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskList(taskList = taskList, onDeleteTask = { taskList.remove(it) })
    }
}

@Composable
fun TaskInputField(taskText: String, onTaskTextChange: (String) -> Unit, onAddTask: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = taskText,
            onValueChange = onTaskTextChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            placeholder = { Text("Enter a task...") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onAddTask() })
        )

        Button(
            onClick = onAddTask,
            modifier = Modifier
                .background(Color(0xFF6200EA), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text("Add Task", color = Color.White)
        }
    }
}

@Composable
fun TaskList(taskList: List<Task>, onDeleteTask: (Task) -> Unit) {
    LazyColumn {
        items(taskList) { task ->
            TaskItem(task, onDeleteTask)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TaskItem(task: Task, onDeleteTask: (Task) -> Unit) {
    var isChecked by remember { mutableStateOf(task.isCompleted) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                task.isCompleted = it
            }
        )

        Text(
            text = task.description,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None,
            color = if (isChecked) Color.Gray else Color.Black
        )

        IconButton(onClick = { onDeleteTask(task) }) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_delete),
                contentDescription = "Delete Task",
                tint = Color.Red
            )
        }
    }
}

data class Task(val description: String, var isCompleted: Boolean = false)

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
