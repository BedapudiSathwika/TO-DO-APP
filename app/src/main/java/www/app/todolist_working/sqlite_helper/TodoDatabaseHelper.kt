package www.app.todolist_working.sqlite_helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import www.app.todolist_working.data_class.TodoItem

class TodoDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "todo.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_TODO_LISTS = "todo_lists"
        const val COLUMN_LIST_ID = "id"
        const val COLUMN_LIST_NAME = "name"

        const val TABLE_TODO_ITEMS = "todo_items"
        const val COLUMN_ITEM_ID = "item_id"
        const val COLUMN_ITEM_NAME = "item_name"
        const val COLUMN_ITEM_DUE_DATE = "due_date"
        const val COLUMN_ITEM_COMPLETED = "completed"
        const val COLUMN_LIST_ID_FK = "list_id_fk"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createListsTable = """
            CREATE TABLE $TABLE_TODO_LISTS (
                $COLUMN_LIST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LIST_NAME TEXT NOT NULL UNIQUE
            )
        """.trimIndent()

        val createItemsTable = """
            CREATE TABLE $TABLE_TODO_ITEMS (
                $COLUMN_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ITEM_NAME TEXT NOT NULL,
                $COLUMN_ITEM_DUE_DATE TEXT,
                $COLUMN_ITEM_COMPLETED INTEGER DEFAULT 0,
                $COLUMN_LIST_ID_FK INTEGER,
                FOREIGN KEY($COLUMN_LIST_ID_FK) REFERENCES $TABLE_TODO_LISTS($COLUMN_LIST_ID)
            )
        """.trimIndent()

        db.execSQL(createListsTable)
        db.execSQL(createItemsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO_ITEMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO_LISTS")
        onCreate(db)
    }


    // Method to insert a new todo item
    fun insertTodoItem(itemName: String, dueDate: String, listIdFk: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ITEM_NAME, itemName)
            put(COLUMN_ITEM_DUE_DATE, dueDate) // If your table supports due date
            put(COLUMN_LIST_ID_FK, listIdFk) // Add this line to insert the foreign key
        }
        val result = db.insert(TABLE_TODO_ITEMS, null, contentValues)
        db.close()
        return result // Returns the row ID of the newly inserted row, or -1 if an error occurred
    }



    // Method to get all todo lists
    @SuppressLint("Range")
    fun getAllTodoItems(): List<TodoItem> {
        val itemList = mutableListOf<TodoItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM todo_items", null)

        if (cursor.moveToFirst()) {
            do {
                val itemId = cursor.getInt(cursor.getColumnIndex("item_id")) // Correct column name
                val name = cursor.getString(cursor.getColumnIndex("item_name")) // Update this line
                val dueDate = cursor.getString(cursor.getColumnIndex("due_date")) // Correct column name
                val completed = cursor.getInt(cursor.getColumnIndex("completed")) // Correct column name
                val listIdFk = cursor.getInt(cursor.getColumnIndex("list_id_fk")) // Correct column name

                val todoItem = TodoItem(itemId, name, dueDate, completed, listIdFk)
                itemList.add(todoItem)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return itemList
    }

    //Update checkbox
    fun updateCheckboxTodoItem(todoItem: TodoItem) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("completed", todoItem.completed)

        // Update the todo item in the database
        db.update("todo_items", contentValues, "item_id = ?", arrayOf(todoItem.itemId.toString()))
        db.close()
    }

    //Delete To Do Item
    fun deleteTodoItem(itemId: Int): Boolean {
        val db = this.writableDatabase

        // Delete the item from the database where item_id matches
        val rowsAffected = db.delete("todo_items", "item_id = ?", arrayOf(itemId.toString()))

        db.close() // Close the database connection

        // Return true if the delete was successful (rowsAffected > 0), otherwise false
        return rowsAffected > 0
    }

    //Edit name and due date of item
    fun editTodoItem(itemId: Int, newName: String, newDueDate: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("item_name", newName) // Update item_name
            put("due_date", newDueDate) // Update due_date
        }

        // Update the item in the database where item_id matches
        val rowsAffected = db.update("todo_items", contentValues, "item_id = ?", arrayOf(itemId.toString()))

        db.close() // Close the database connection

        // Return true if the update was successful (rowsAffected > 0), otherwise false
        return rowsAffected > 0
    }


//    @SuppressLint("Range")
//    fun getAllTodoItems(): List<TodoItem> {
//        val itemList = mutableListOf<TodoItem>()
//        val db = this.readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM todo_items", null)
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                Log.d("Database", "Item ID Index: ${cursor.getColumnIndex("item_id")}")
//                Log.d("Database", "Name Index: ${cursor.getColumnIndex("name")}")
//                Log.d("Database", "Due Date Index: ${cursor.getColumnIndex("due_date")}")
//                Log.d("Database", "Completed Index: ${cursor.getColumnIndex("completed")}")
//                Log.d("Database", "List ID FK Index: ${cursor.getColumnIndex("list_id_fk")}")
//
//
//                val itemId = cursor.getInt(cursor.getColumnIndex("item_id"))
//                val name = cursor.getString(cursor.getColumnIndex("name"))
//                val dueDate = cursor.getString(cursor.getColumnIndex("due_date"))
//                val completed = cursor.getInt(cursor.getColumnIndex("completed"))
//                val listIdFk = cursor.getInt(cursor.getColumnIndex("list_id_fk"))
//
//                val todoItem = TodoItem(itemId, name, dueDate, completed, listIdFk)
//                itemList.add(todoItem)
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        return itemList
//    }


    @SuppressLint("Range")
    fun logTableSchema() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("PRAGMA table_info(todo_items)", null)

        if (cursor.moveToFirst()) {
            do {
                val columnName = cursor.getString(cursor.getColumnIndex("name"))
                Log.d("DatabaseSchema", "Column: $columnName")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }



    // Method to log all data from both tables
    @SuppressLint("Range")
    fun logAllData() {
        val db = this.readableDatabase

        try {
            // Log all data from todo_lists
            val todoListsCursor = db.rawQuery("SELECT * FROM $TABLE_TODO_LISTS", null)
            if (todoListsCursor.moveToFirst()) {
                do {
                    val id = todoListsCursor.getInt(todoListsCursor.getColumnIndex(COLUMN_LIST_ID))
                    val name = todoListsCursor.getString(todoListsCursor.getColumnIndex(COLUMN_LIST_NAME))
                    Log.d("Database", "Todo List - ID: $id, Name: $name")
                } while (todoListsCursor.moveToNext())
            }
            todoListsCursor.close()

            // Log all data from todo_items
            val todoItemsCursor = db.rawQuery("SELECT * FROM $TABLE_TODO_ITEMS", null)
            if (todoItemsCursor.moveToFirst()) {
                do {
                    val itemId = todoItemsCursor.getInt(todoItemsCursor.getColumnIndex(COLUMN_ITEM_ID))
                    val itemName = todoItemsCursor.getString(todoItemsCursor.getColumnIndex(COLUMN_ITEM_NAME))
                    val dueDate = todoItemsCursor.getString(todoItemsCursor.getColumnIndex(COLUMN_ITEM_DUE_DATE))
                    val completed = todoItemsCursor.getInt(todoItemsCursor.getColumnIndex(COLUMN_ITEM_COMPLETED))
                    val listIdFk = todoItemsCursor.getInt(todoItemsCursor.getColumnIndex(COLUMN_LIST_ID_FK))
                    Log.d("Database", "Todo Item - Item ID: $itemId, Name: $itemName, Due Date: $dueDate, Completed: $completed, List ID FK: $listIdFk")
                } while (todoItemsCursor.moveToNext())
            }
            todoItemsCursor.close()
        } finally {
            db.close() // Ensure the database is closed in case of any exceptions
        }
    }

    fun getTodoCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM todo_items", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun getCompletedTodoCount(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM todo_items WHERE completed = 1", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

}
