package com.review.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.review.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DiceRollerApp("Android")
                }
            }
        }
    }
}

@Composable
fun DiceRollerApp(name: String = "") {
    DiceWithButtonAndImage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}

enum class DicePosition {
    Start, Top
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var dice by remember {
         mutableStateOf(1)
    }
    val imageResource = when (dice) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else  -> R.drawable.dice_6
    }

    val screenWidth = (LocalConfiguration.current.screenWidthDp.dp)
    val screenHeight = (LocalConfiguration.current.screenHeightDp.dp)

    var diceState by remember {
        mutableStateOf(DicePosition.Start)
    }
    val animatedOffset: Dp by animateDpAsState(
        targetValue = when(diceState) {
            DicePosition.Start ->  0.dp
            DicePosition.Top -> 100.dp
                                      },
//        animationSpec = tween(500)
        animationSpec = spring(dampingRatio = DampingRatioHighBouncy),
    )


    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(imageResource), contentDescription = "dice"
             , modifier= Modifier.offset(x = 0.dp, y = -animatedOffset)
        )
        Button(onClick = {
            dice = (1..6).random()
            diceState = when (diceState) {
                DicePosition.Start -> DicePosition.Top
                DicePosition.Top -> DicePosition.Start
            }
        }) {
            Text(stringResource(R.string.roll))
        }
    }
}


@Preview
@Composable
fun DiceRollerAppPreview() {
    DiceRollerTheme {
        DiceRollerApp()
    }
}
