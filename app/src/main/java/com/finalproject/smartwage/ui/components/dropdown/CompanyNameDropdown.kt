package com.finalproject.smartwage.ui.components.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finalproject.smartwage.ui.theme.DarkBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyNameDropdownMenuField(
    label: String,
    selectedItem: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && items.isNotEmpty() }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { onItemSelected(it) },
            label = { Text(label, fontSize = 17.sp) },
            textStyle = TextStyle(fontSize = 22.sp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    MenuAnchorType.PrimaryEditable,
                    enabled = items.isNotEmpty()
                ),
            trailingIcon = {
                if (items.isNotEmpty()) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = DarkBlue,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                onClick = {
                                    if (items.isNotEmpty()) {
                                        expanded = true
                                    }
                                }
                            )
                    )
                }
            },
        )

        // Only show the dropdown if there are items
        if (items.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontSize = 18.sp) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}