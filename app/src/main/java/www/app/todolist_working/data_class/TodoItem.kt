package www.app.todolist_working.data_class

data class TodoItem(
    val itemId: Int,
    val name: String,
    val dueDate: String,
    var completed: Int,
    val listIdFk: Int
)

