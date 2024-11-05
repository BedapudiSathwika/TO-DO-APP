package www.app.todolist_working.data_class

data class TodoList(
    val listId: Int,
    val name: String,
    var nearestDueDate: String? = null
)
