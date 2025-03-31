package com.finalproject.smartwage.ui.components.dropdownmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryEditable
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

/**
 * A composable function that displays a dropdown menu for selecting a frequency.
 *
 * @param label The label for the dropdown menu.
 * @param selectedItem The currently selected item.
 * @param items The list of items to display in the dropdown menu.
 * @param onItemSelected Callback function to be invoked when an item is selected.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FrequencyDropdownMenuField(
    // Parameters
    label: String,
    selectedItem: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    // State variable to manage the expanded state of the dropdown menu
    var expanded by remember { mutableStateOf(false) }

    // ExposedDropdownMenuBox to create a dropdown menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        // OutlinedTextField to display the selected item and trigger the dropdown menu
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            label = {
                Text(
                    label,
                    fontSize = 17.sp
                )
            },
            textStyle = TextStyle(
                fontSize = 22.sp
            ),
            readOnly = true,
            trailingIcon = {
                Icon(
                    Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = DarkBlue,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            expanded = true
                        }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    PrimaryEditable,
                    enabled = true
                )
                .clickable {
                    expanded = true
                }
        )

        // Dropdown menu items
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .exposedDropdownSize()
        ) {
            // Iterate through the items and create a DropdownMenuItem for each
            items.forEach { item ->
                // DropdownMenuItem to display each item
                DropdownMenuItem(
                    text = {
                        Text(
                            item,
                            style = TextStyle(
                                fontSize = 22.sp
                            )
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                )
            }
        }
    }
}