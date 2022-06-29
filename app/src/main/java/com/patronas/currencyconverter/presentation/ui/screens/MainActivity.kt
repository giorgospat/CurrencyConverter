package com.patronas.currencyconverter.presentation.ui.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.patronas.currencyconverter.base.BaseActivity
import com.patronas.currencyconverter.presentation.ui.screens.home.HomeScreen
import com.patronas.currencyconverter.presentation.ui.screens.home.composables.TopBar
import com.patronas.currencyconverter.presentation.ui.theme.CurrencyConverterTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopBar()
                    }
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyConverterTheme {
    }
}