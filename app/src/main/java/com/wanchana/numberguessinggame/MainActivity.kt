package com.wanchana.numberguessinggame

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.wanchana.numberguessinggame.ui.theme.NumberGuessingGameTheme
import kotlin.random.Random.Default.nextInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NumberGuessingGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val randomized = nextInt(0,1000)
                    NumberGuessingGame(this,randomized)
                }
            }
        }
    }
}



@Composable
fun NumberGuessingGame(context : Context,initRandom : Int) {
    val guessValue = remember { mutableStateOf("") }
    val valueEntered = remember { mutableStateOf(false) }
    val statusValue = remember { mutableStateOf("") }
    val realAnswer = remember { mutableStateOf(initRandom) }
    val attempt = remember { mutableStateOf(0) }
    val score = remember { mutableStateOf(0) }


    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Number Guessing Game",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(40.dp)
                .fillMaxWidth()
        )

        StatusInfo(curStatus = valueEntered.value, statusValue.value)


        if ((valueEntered.value) and (statusValue.value != "won")){
        Text(
            text = "Attempted : "+ attempt.value,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        }else if (statusValue.value == "won"){
            Text(
                text = "Guess more for more points !",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        else{
            Text(
                text = "",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        val focusManager = LocalFocusManager.current
        TextField(
            value = guessValue.value,
            onValueChange = {
                guessValue.value = it } ,
            label = { Text("Enter your guess") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier
                .padding(50.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ResetButton(onClick = {
                valueEntered.value = false
                attempt.value = 0;
                score.value = 0;
                statusValue.value = ""
                guessValue.value = ""
                realAnswer.value = nextInt(0,1000)

            })

            ConfirmButton(onClick = {
                if (!guessValue.value.isDigitsOnly()){

                    Toast.makeText(context,"Please provide number between 0-1000!", Toast.LENGTH_LONG).show()

                }else {

                    if (!valueEntered.value) {
                        valueEntered.value = true
                    }
                    if ((guessValue.value.toInt() > 1000) or (guessValue.value.toInt() < 0)) {
                        statusValue.value = "invalid"
                    } else {

                        statusValue.value = checkAnswer(realAnswer.value, guessValue.value.toInt())
                        attempt.value++

                    }
                    if (statusValue.value == "won") {
                        Toast.makeText(context,"The answer is "+realAnswer.value + " in " +attempt.value+" attempts", Toast.LENGTH_LONG).show()
                        score.value++
                        attempt.value = 0
                        realAnswer.value = nextInt(0,1000)

                    }
                }
                guessValue.value=""

            })
        }

        if (valueEntered.value) {
            Text(
                text = "Points: "+score.value,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            )
        }else{
            Text(
                text = "",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(50.dp)
            )
        }
    }
}

@Composable
fun ConfirmButton(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ) {
        Text(text = "Confirm", fontSize = 18.sp)
    }
}

@Composable
fun ResetButton(onClick: () -> Unit) {
    Button(
        onClick = onClick
    ) {
        Text(text = "Reset", fontSize = 18.sp)
    }
}



fun checkAnswer(realAnswer:Int,answer:Int): String {

    return if(realAnswer == answer){
        "won"
    }else if(answer < realAnswer){
        "lower"
    }else{
        "higher"
    }
}

@Composable
fun StatusInfo(curStatus : Boolean,statusValue : String) {
    var status: String = "Guess number between 0 and 1000!";

    status = if (curStatus) {
        if((statusValue == "won") ) {
            "Congratulations, you WON!"
        }else if (statusValue == "invalid"){
            "Please enter number between 0 and 1000!"
        }
        else {
            if (statusValue == "lower") {
                "Wrong, your guess is too LOW!";
            } else if(statusValue == "higher"){
                "Wrong, your guess is too HIGH!";
            }else {
                "$statusValue"
            }
        }
    }else{
        "Guess number between 0 and 1000!";
    }

    Text(
        text = status,
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    )
}