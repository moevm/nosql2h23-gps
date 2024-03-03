package com.skygrel19.realmsandbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skygrel19.realmsandbox.ui.theme.RealmSandboxTheme
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = RealmConfiguration.create(schema = setOf(Item::class))
        val realm: Realm = Realm.open(config)

        setContent {
            RealmSandboxTheme {
                Column(modifier = Modifier.padding(16.dp)) {
                    CreateItem(realm)
                    Spacer(modifier = Modifier.height(16.dp))
                    ShowItems(realm)
                }
            }
        }
    }
}

@Composable
fun CreateItem(realm: Realm) {
    var text by remember { mutableStateOf("") }

    Column {
        TextField(value = text, onValueChange = { text = it })
        Button(onClick = {
            realm.writeBlocking {
                copyToRealm(Item().apply { summary = text })
            }
        }) {
            Text("Add Item")
        }
    }
}

@Composable
fun ShowItems(realm: Realm) {
    var items by remember { mutableStateOf(listOf<Item>()) }

    Column {
        Button(onClick = {
            items = realm.query(Item::class).find().toList()
        }) {
            Text("Load Items")
        }
        items.forEach { item ->
            Text(item.summary)
        }
    }
}

class Item : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var summary: String = ""
}