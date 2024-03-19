package com.skygrel19.realmsandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skygrel19.realmsandbox.exceptions.WrongFormatException
import com.skygrel19.realmsandbox.models.DistanceRecord
import com.skygrel19.realmsandbox.models.LocationModel
import com.skygrel19.realmsandbox.models.RecordModel
import com.skygrel19.realmsandbox.models.TrainingSessionModel
import com.skygrel19.realmsandbox.models.UserModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.schema.RealmClass
import io.realm.kotlin.types.RealmObject
import kotlin.reflect.KClass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = RealmConfiguration.create(schema = setOf(UserModel::class, DistanceRecord::class,
            LocationModel::class, RecordModel::class, TrainingSessionModel::class, UserModel::class))
        val realm: Realm = Realm.open(config)


        setContent {
            App(realm)
        }
    }
}

@Composable
fun <T: RealmObject> DisplayModelData(realm: Realm, modelClass: IRealmModel<T>, generation: Int) {
    val clazz = modelClass.as_class()

    val results = realm.query(clazz).find()


    if (results.isEmpty()) {
        Text("No data found for ${modelClass.name()}")
        return
    }
    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.onSurface).heightIn(max = 400.dp)) {
        items(results.size) { index ->
            val model = results[index]
            TextField(value = model.toString(), onValueChange = {}, readOnly = true)
            // really nice line to separate items
            Box(modifier = Modifier.size(400.dp, 1.dp).border(1.dp, MaterialTheme.colorScheme.onSurface))
        }
    }
}


@Composable
fun App(realm: Realm) {
    var selectedModelClass by remember { mutableStateOf(UserModel() as IRealmModel<*>) }
    val selectedModelClassName = selectedModelClass.name()

    var text by remember { mutableStateOf("") }

    var generation by remember { mutableIntStateOf(0) }

    // exception dialog stuff
    var dialogVisible by remember { mutableStateOf(false) }
    var exceptionMessage by remember { mutableStateOf("") }

    if (dialogVisible) {
        AlertDialog(
            onDismissRequest = { dialogVisible = false },
            title = { Text("An error occurred") },
            text = { Text(exceptionMessage) },
            confirmButton = {
                Button(onClick = { dialogVisible = false }) {
                    Text("OK")
                }
            }
        )
    }

    Column (Modifier.padding(10.dp)) {
        Text("App db prototype", Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.headlineLarge)
        Row {
            Text("Selected Model: $selectedModelClassName", Modifier.align(Alignment.CenterVertically))
            Box(modifier = Modifier.weight(1f))
            DropdownMenuExample(selectedModelClass, onModelClassSelection = { selectedModelClass = it })
        }

        // Table
        DisplayModelData(realm, selectedModelClass, generation)

        Box(modifier = Modifier.weight(1f))

        // Insertion form
        Text(text = "Insert New Entry", Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.headlineMedium)
        Text("Enter comma separated values for the model", Modifier.align(Alignment.CenterHorizontally))
        Text("Hint: ${selectedModelClass.hint()}", Modifier.align(Alignment.CenterHorizontally))
        TextField(value = text, onValueChange = { text = it }, label = { Text("Enter comma separated values") },
            modifier = Modifier.padding(10.dp).align(Alignment.CenterHorizontally).width(400.dp))
        Button(onClick = {
            realm.writeBlocking {
                try {
                    copyToRealm(selectedModelClass.create_from_text(text))
                } catch (e: Exception) {
                    // Set the exception message and show the dialog
                    exceptionMessage = if (e is WrongFormatException) {
                        "Wrong format. Hint: ${selectedModelClass.hint()}"
                    } else {
                        e.toString()
                    }
                    dialogVisible = true
                }
            }
            generation++
        }, Modifier.align(Alignment.CenterHorizontally)) {
            Text("Add New Entry")
        }
    }
}


@Composable
fun <T: RealmObject> DropdownMenuExample(selectedModelClass: IRealmModel<T>, onModelClassSelection: (IRealmModel<T>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }


    val modelClasses = listOf(UserModel(), DistanceRecord(), LocationModel(), RecordModel(), TrainingSessionModel()) as List<IRealmModel<T>>

    Column {
        Button(onClick = { expanded = true }) {
            Text(selectedModelClass.name())
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            modelClasses.forEach { relation ->
                DropdownMenuItem(onClick = {
                    onModelClassSelection(relation)
                    expanded = false
                }, text = {
                    Text(relation.name())
                })
            }
        }
    }
}