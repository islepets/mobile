package com.example.calcuate

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calcuate.ui.theme.CalcuateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalcuateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalcMain(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun filter_content(input: String) : String{
    return input.filter { it.isDigit() || it == '.' || it == '-'}
}

@Composable
fun CalcMain(modifier: Modifier = Modifier) {
    var op1 by remember { mutableStateOf("") }
    var op2 by remember { mutableStateOf("") }
    var activated by remember { mutableStateOf(1) }
    var operation by remember { mutableStateOf("") }
    val context = LocalContext.current
    var error by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.name_calculate),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0, 255, 0)
        )
        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = op1,
            onValueChange = { newVal -> op1 = filter_content(newVal) },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .onFocusChanged { if (it.isFocused) activated = 1 }
        )

        if (operation.isNotEmpty()){Spacer(
            modifier = Modifier.height(16.dp))
            Text(text = operation, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = op2,
            onValueChange = { newVal -> op2 = filter_content(newVal) },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .onFocusChanged { if (it.isFocused) activated = 2 }
        )

        Spacer(modifier = Modifier.padding(10.dp))

        if(error.isNotEmpty()){
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(1.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            OperationButton("÷", operation, { operation = it })
            OperationButton("×", operation, { operation = it })
            OperationButton("-", operation, { operation = it })
            OperationButton("+", operation, { operation = it })
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            NumberButton("7", activated, { op1 += it }, { op2 += it })
            NumberButton("8", activated, { op1 += it }, { op2 += it })
            NumberButton("9", activated, { op1 += it }, { op2 += it })
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            NumberButton("4", activated, { op1 += it }, { op2 += it })
            NumberButton("5", activated, { op1 += it }, { op2 += it })
            NumberButton("6", activated, { op1 += it }, { op2 += it })
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            NumberButton("1", activated, { op1 += it }, { op2 += it })
            NumberButton("2", activated, { op1 += it }, { op2 += it })
            NumberButton("3", activated, { op1 += it }, { op2 += it })
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            NumberButton("0", activated, { op1 += it }, { op2 += it })
            NumberButton(".", activated, { op1 += it }, { op2 += it })

            Button(
                onClick = {

                    if (op1.isEmpty() || op2.isEmpty() ){
                        error = context.getString(R.string.label_empty)
                        return@Button
                    }

                    val num1 = op1.toDoubleOrNull()
                    val num2 = op2.toDoubleOrNull()

                    if(num1 == null || num2 == null){
                        error = context.getString(R.string.input_correct)
                        return@Button
                    }

                    val result = when (operation) {
                        "+" -> (num1 + num2).toString()
                        "-" -> (num1 - num2).toString()
                        "×" -> (num1 * num2).toString()
                        "÷" -> if (num2 != 0.0) (num1 / num2).toString()
                        else{
                            error = context.getString(R.string.divide_null)
                            return@Button
                        }

                        else ->
                        {
                            error = context.getString(R.string.operation_error)
                            return@Button
                        }
                    }

                    val intent = Intent(context, ResultActivity::class.java).apply {
                        putExtra("RESULT", result)
                        error = ""
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .size(80.dp)
                    .padding(horizontal = 10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text(text = "=", fontSize = 20.sp)
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 100.dp, vertical = 10.dp)
        ){
            Button(
                onClick = {
                    op1 = ""
                    op2 = ""
                    operation = ""
                    error = ""
                },
                modifier = Modifier
                    .size(100.dp)
                    .padding(horizontal = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                )
            ){
                Text(text = "AC", fontSize = 20.sp)
            }
        }

    }
}

@Composable
fun OperationButton(x0: String, x1: String, x2: (String) -> Unit) {
    Button(
        onClick = { x2(x0) },
        modifier = Modifier
            .size(80.dp)
            .padding(horizontal = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White

        )
    ) {
        Text(text = x0, fontSize = 20.sp)
    }
}

@Composable
fun NumberButton(x0: String, x1: Int, x2: (String) -> Unit, x3: (String) -> Unit) {
    Button(
        onClick = {
            when (x1) {
                1 -> x2(x0)
                2 -> x3(x0)
            }
        },
        modifier = Modifier
            .size(80.dp)
            .padding(horizontal = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Green,
            contentColor = Color.White
        )
    ) {
        Text(text = x0, fontSize = 24.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CalcMainPreview() {
    CalcuateTheme {
        CalcMain()
    }
}