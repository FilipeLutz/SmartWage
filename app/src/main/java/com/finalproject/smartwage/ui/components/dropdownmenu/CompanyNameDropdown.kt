package com.finalproject.smartwage.ui.components.dropdownmenu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
 * A dropdown menu for selecting a company name.
 *
 * @param label The label for the dropdown menu.
 * @param selectedItem The currently selected item.
 * @param items The list of items to display in the dropdown menu.
 * @param onItemSelected Callback function to handle item selection.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyNameDropdownMenuField(
    // Parameters
    label: String,
    selectedItem: String,
    items: List<String>,
    onItemSelected: (String) -> Unit
) {
    // State variables
    var expanded by remember { mutableStateOf(false) }
    var filteredItems by remember { mutableStateOf(items) }

    // Show the dropdown menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = it && filteredItems.isNotEmpty()
        }
    ) {
        // Text field for the dropdown menu
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { newValue ->
                // Update the selected item and filter the items
                onItemSelected(newValue)
                filteredItems = items.filter {
                    it.contains(
                        newValue,
                        ignoreCase = true
                    )
                }
                // Update the expanded state based on the filtered items
                expanded = filteredItems.isNotEmpty()
            },
            label = {
                Text(
                    label,
                    fontSize = 17.sp
                )
            },
            textStyle = TextStyle(
                fontSize = 22.sp
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    PrimaryEditable,
                    enabled = items
                        .isNotEmpty()
                ),
            trailingIcon = {
                // Show the dropdown icon if there are more than one item
                if (items.isNotEmpty() && items.size > 1) {
                    // Dropdown icon
                    Icon(
                        Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = DarkBlue,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable(
                                onClick = {
                                    // Show the dropdown menu when clicked
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
                    // Dropdown menu item
                    DropdownMenuItem(
                        text = {
                            Text(
                                item,
                                fontSize = 21.sp
                            )
                        },
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