package com.example.lab2

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

data class Task(val description: String, var isCompleted: Boolean = false)

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
        Text(
            text = "Task Manager",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
        )

        TaskInputField(
            taskText = taskText,
            onTaskTextChange = { taskText = it },
            onAddTask = {
                if (taskText.isNotBlank()) {
                    taskList.add(Task(taskText))
                    taskText = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TaskList(
            taskList = taskList,
            onTaskCheckChanged = { task, isChecked ->
                val index = taskList.indexOf(task)
                if (index != -1) {
                    taskList[index] = task.copy(isCompleted = isChecked)
                }
            },
            onDeleteTask = { taskList.remove(it) }
        )
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)),
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Add Task", color = Color.White)
        }
    }
}

@Composable
fun TaskList(
    taskList: List<Task>,
    onTaskCheckChanged: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    LazyColumn {
        items(taskList) { task ->
            TaskItem(task, onTaskCheckChanged, onDeleteTask)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun TaskItem(
    task: Task,
    onTaskCheckChanged: (Task, Boolean) -> Unit,
    onDeleteTask: (Task) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked -> onTaskCheckChanged(task, isChecked) }
        )

        Text(
            text = task.description,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
            color = if (task.isCompleted) Color.Gray else Color.Black
        )

        IconButton(onClick = { onDeleteTask(task) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = Color.Red
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
