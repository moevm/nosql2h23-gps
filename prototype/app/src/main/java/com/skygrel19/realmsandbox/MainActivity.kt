package com.skygrel19.realmsandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
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
fun <T: RealmObject> DisplayModelData(realm: Realm, modelClass: IRealmModel<T>) {
    val clazz = modelClass.as_class()

    //use reflection to get the model class
    val results = realm.query(clazz).find()


    LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(results.size) { index ->
            val model = results[index]
            Text(model.toString())
        }
    }
}


@Composable
fun App(realm: Realm) {
    var selectedModelClass by remember { mutableStateOf(UserModel() as IRealmModel<*>) }
    val selectedModelClassName = selectedModelClass.name()
    val selectedModelClassKClass = selectedModelClass.as_class()

    var text by remember { mutableStateOf("") }


    Column {
        Text("Realm Sandbox")
        Text("Selected Model: $selectedModelClassName")
        DropdownMenuExample(realm, selectedModelClass, onModelClassSelection = { selectedModelClass = it })

        DisplayModelData(realm, selectedModelClass)

        TextField(value = text, onValueChange = { text = it })
        Button(onClick = {
            realm.writeBlocking {
                copyToRealm(selectedModelClass.create_from_text(text))
            }
        }) {
            Text("Add New Entry")
        }
    }
}

@Composable
fun <T: RealmObject> DropdownMenuExample(realm: Realm, selectedModelClass: IRealmModel<T>, onModelClassSelection: (IRealmModel<T>) -> Unit) {
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