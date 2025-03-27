package com.finalproject.smartwage.ui.components.dropdownmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
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
    var filteredItems by remember { mutableStateOf(items) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && filteredItems.isNotEmpty() }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { newValue ->
                onItemSelected(newValue)
                filteredItems = items.filter { it.contains(newValue, ignoreCase = true) }
                expanded = filteredItems.isNotEmpty()
            },
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
                if (items.isNotEmpty() && items.size > 1) {
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

        // Show the dropdown if there are more than one item
        if (filteredItems.isNotEmpty() && filteredItems.size > 1) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                // Show only the first 3 items
                filteredItems.take(3).forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, fontSize = 21.sp) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                    // Divider between each item when more than one item
                    if (filteredItems.size > 1 && item != filteredItems.last()) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                    }
                }
            }
        }
    }
}