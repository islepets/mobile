package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListMain(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun CheckboxListItem(
    item: CheckBoxItem,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isCheck,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = item.label,
            fontSize = 16.sp,
            color = if (item.isCheck) Color.Gray else Color.Black,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ListMain(modifier: Modifier = Modifier){

    val config = LocalConfiguration.current
    val isLand = config.screenWidthDp > config.screenHeightDp

    val checkBoxItems = rememberSaveable {
        mutableStateListOf<CheckBoxItem>()
    }

    val newItemText = rememberSaveable { mutableStateOf("") }

    val onAddItem = {
        if (newItemText.value.isNotBlank()){
            checkBoxItems.add(CheckBoxItem(newItemText.value, false))
            newItemText.value = ""
        }
    }

    val onTongleAllClick: () -> Unit = {
        checkBoxItems.replaceAll{ it.copy(isCheck = true)}
    }

    val fullReset: () -> Unit = {
        checkBoxItems.replaceAll{ it.copy(isCheck = false)}
    }

    val onDelete: () -> Unit = {
        checkBoxItems.removeAll{it.isCheck}
    }

    if(isLand){
        Land(
            checkBoxItems = checkBoxItems,
            newItemText = newItemText.value,
            newItemTextChange = { newItemText.value = it },
            onAddItem = onAddItem,
            onItemCheckChange = { index, isChecked ->
                checkBoxItems[index] = checkBoxItems[index].copy(isCheck = isChecked)
            },
            onTongleAllClick = onTongleAllClick,
            onDelete = onDelete,
            onFullFalse = fullReset
        )
    }
    else {
        Portain(
            checkBoxItems = checkBoxItems,
            newItemText = newItemText.value,
            newItemTextChange = { newItemText.value = it },
            onAddItem = onAddItem,
            onItemCheckChange = { index, isChecked ->
                checkBoxItems[index] = checkBoxItems[index].copy(isCheck = isChecked)
            },
            onTongleAllClick = onTongleAllClick,
            onDelete = onDelete,
            onFullFalse = fullReset
        )
    }
}

@Composable
fun Land(
    checkBoxItems : List<CheckBoxItem>,
    newItemText: String,
    newItemTextChange: (String) -> Unit,
    onAddItem: () -> Unit,
    onItemCheckChange: (Int, Boolean) -> Unit,
    onTongleAllClick: () -> Unit,
    onDelete: () -> Unit,
    onFullFalse: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.EN_addElem),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0, 0, 255),
            modifier = Modifier.padding(top = 50.dp, start = 20.dp)
        )
        Row {
            TextField(
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp)
                    .fillMaxWidth(0.7f),
                value = newItemText,
                onValueChange = newItemTextChange,

                )
            Button(
                modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                onClick = onAddItem,
                enabled = newItemText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )
            ) {
                Text(text = stringResource(R.string.EN_add))
            }
        }
        Row() {
            Spacer(modifier = Modifier.height(16.dp))
            if (checkBoxItems.isEmpty()) {
                Text(
                    text = stringResource(R.string.EN_empty),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0, 0, 255),
                    modifier = Modifier.fillMaxWidth(0.7f),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(0.7f)
                ) {
                    items(checkBoxItems) { item ->
                        CheckboxListItem(
                            item = item,
                            onCheckedChange = { isChecked ->
                                val index = checkBoxItems.indexOf(item)
                                if (index != -1) {
                                    onItemCheckChange(index, isChecked)
                                }
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.weight(0.3f)
            ) {
                Button(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp)
                        .fillMaxWidth(0.6f),
                    onClick = onTongleAllClick,
                    enabled = checkBoxItems.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = stringResource(R.string.EN_allEl))
                }

                Button(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp)
                        .fillMaxWidth(0.6f),
                    onClick = onFullFalse,
                    enabled = checkBoxItems.any { it.isCheck },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = stringResource(R.string.EN_reset))
                }

                Button(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp)
                        .fillMaxWidth(0.6f),
                    onClick = onDelete,
                    enabled = checkBoxItems.any { it.isCheck },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.Black
                    )
                ) {
                    Text(text = stringResource(R.string.EN_del))
                }

            }
        }
    }
}

@Composable
fun Portain(
    checkBoxItems : List<CheckBoxItem>,
    newItemText: String,
    newItemTextChange: (String) -> Unit,
    onAddItem: () -> Unit,
    onItemCheckChange: (Int, Boolean) -> Unit,
    onTongleAllClick: () -> Unit,
    onDelete: () -> Unit,
    onFullFalse: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.addElem),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0, 0, 255),
            modifier = Modifier.padding(top = 50.dp, start = 20.dp)
        )
        Row{
            TextField(
                modifier = Modifier
                    .padding(top = 10.dp, start = 20.dp)
                    .fillMaxWidth(0.7f),
                value = newItemText,
                onValueChange = newItemTextChange,

            )
            Button(
                modifier = Modifier.padding(top = 10.dp, start = 5.dp),
                onClick = onAddItem,
                enabled = newItemText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )
            ){
                Text(text = stringResource(R.string.add))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (checkBoxItems.isEmpty()){
            Text(
                text = stringResource(R.string.empty),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0, 0, 255),
            )
        }
        else{
            LazyColumn {
                items(checkBoxItems){item ->
                    CheckboxListItem(
                        item = item,
                        onCheckedChange = {isChecked ->
                            val index = checkBoxItems.indexOf(item)
                            if (index != -1){
                                onItemCheckChange(index, isChecked)
                            }
                        }
                    )
                }
            }
        }

        Column(){
            Button(
                modifier = Modifier
                    .padding(top = 10.dp, start = 5.dp)
                    .fillMaxWidth(0.8f),
                onClick = onTongleAllClick,
                enabled = checkBoxItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )
            ){
                Text(text = stringResource(R.string.allEl))
            }

            Button(
                modifier = Modifier
                    .padding(top = 10.dp, start = 5.dp)
                    .fillMaxWidth(0.8f),
                onClick = onFullFalse,
                enabled = checkBoxItems.any{it.isCheck},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )
            ){
                Text(text = stringResource(R.string.reset))
            }

            Button(
                modifier = Modifier
                    .padding(top = 10.dp, start = 5.dp)
                    .fillMaxWidth(0.8f),
                onClick = onDelete,
                enabled = checkBoxItems.any{it.isCheck},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )
            ){
                Text(text = stringResource(R.string.del))
            }

        }



    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab2Theme {
        ListMain()
    }
}

data class CheckBoxItem(
    val label: String,
    val isCheck: Boolean
)