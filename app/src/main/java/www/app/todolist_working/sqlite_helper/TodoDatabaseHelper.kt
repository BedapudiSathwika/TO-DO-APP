package www.app.todolist_working.sqlite_helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
}