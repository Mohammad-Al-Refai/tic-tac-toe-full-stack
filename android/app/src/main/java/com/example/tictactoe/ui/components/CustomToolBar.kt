package com.example.tictactoe.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomToolBar(clientName: String) {
    TopAppBar(title = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row {
                Text(text = "Tic", color = MaterialTheme.colorScheme.primary, style = Typography.titleLarge)
                Text(text = "Tac", color = MaterialTheme.colorScheme.secondary, style = Typography.titleLarge)
                Text(text = "Toe", color = MaterialTheme.colorScheme.tertiary, style = Typography.titleLarge)
            }
            Text(
                fontSize = 13.sp,
                text = clientName,
                modifier = Modifier.padding(end = 10.dp),
            )
        }
    })
}
