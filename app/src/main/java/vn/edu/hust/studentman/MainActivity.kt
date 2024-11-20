package vn.edu.hust.studentman

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity(), StudentAdapter.StudentAdapterListener {

  private lateinit var recyclerView: RecyclerView
  private lateinit var studentAdapter: StudentAdapter
  private lateinit var students: MutableList<StudentModel>

  private var removedStudent: StudentModel? = null
  private var removedStudentPosition: Int? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Khởi tạo RecyclerView và Button từ findViewById
    recyclerView = findViewById(R.id.recycler_view_students)
    val btnAddNew: Button = findViewById(R.id.btn_add_new)


    // Khởi tạo danh sách sinh viên mẫu
    students = mutableListOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002"),
      StudentModel("Lê Hoàng Cường", "SV003"),
      StudentModel("Phạm Thị Dung", "SV004"),
      StudentModel("Đỗ Minh Đức", "SV005"),
      StudentModel("Vũ Thị Hoa", "SV006"),
      StudentModel("Hoàng Văn Hải", "SV007"),
      StudentModel("Bùi Thị Hạnh", "SV008"),
      StudentModel("Đinh Văn Hùng", "SV009"),
      StudentModel("Nguyễn Thị Linh", "SV010"),
      StudentModel("Phạm Văn Long", "SV011"),
      StudentModel("Trần Thị Mai", "SV012"),
      StudentModel("Lê Thị Ngọc", "SV013"),
      StudentModel("Vũ Văn Nam", "SV014"),
      StudentModel("Hoàng Thị Phương", "SV015"),
      StudentModel("Đỗ Văn Quân", "SV016"),
      StudentModel("Nguyễn Thị Thu", "SV017"),
      StudentModel("Trần Văn Tài", "SV018"),
      StudentModel("Phạm Thị Tuyết", "SV019"),
      StudentModel("Lê Văn Vũ", "SV020")
    )

    // Khởi tạo adapter cho RecyclerView
    studentAdapter = StudentAdapter(students, this)

    val recyclerView: RecyclerView = findViewById(R.id.recycler_view_students)
    recyclerView.adapter = studentAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)

    // Add new student button
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  // Show Add/Edit Student Dialog
  private fun showAddStudentDialog(student: StudentModel? = null) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
    val studentNameEditText: EditText = dialogView.findViewById(R.id.edit_student_name)
    val studentIdEditText: EditText = dialogView.findViewById(R.id.edit_student_id)

    // If editing, pre-fill the fields
    student?.let {
      studentNameEditText.setText(it.studentName)
      studentIdEditText.setText(it.studentId)
    }

    val alertDialog = AlertDialog.Builder(this)
      .setTitle(if (student == null) "Add Student" else "Edit Student")
      .setView(dialogView)
      .setPositiveButton("Save") { _, _ ->
        val name = studentNameEditText.text.toString()
        val id = studentIdEditText.text.toString()
        if (student == null) {
          students.add(StudentModel(name, id))  // Add new student
        } else {
          student.studentName = name  // Update existing student
          student.studentId = id
        }
        studentAdapter.notifyDataSetChanged()  // Refresh adapter to reflect changes
      }
      .setNegativeButton("Cancel", null)
      .create()

    alertDialog.show()
  }


  private fun showDeleteConfirmationDialog(student: StudentModel, position: Int) {
    val alertDialog = AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete ${student.studentName}?")
      .setPositiveButton("Yes") { _, _ ->
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        // Store removed student and position for undo functionality
        removedStudent = student
        removedStudentPosition = position

        // Show Snackbar with Undo option
        showUndoSnackbar()
      }
      .setNegativeButton("No", null)
      .create()

    alertDialog.show()
  }
  private fun showUndoSnackbar() {
    val view = findViewById<View>(R.id.recycler_view_students)
    Snackbar.make(view, "Student deleted", Snackbar.LENGTH_LONG)
      .setAction("Undo") {
        removedStudent?.let {
          students.add(removedStudentPosition ?: 0, it)
          studentAdapter.notifyItemInserted(removedStudentPosition ?: 0)
        }
      }
      .show()
  }
  override fun onEditClick(student: StudentModel) {
    // Handle Edit button click, show dialog to edit student
    showAddStudentDialog(student)  // Show dialog with student info to edit
  }

  override fun onDeleteClick(student: StudentModel, position: Int) {
    // Handle Delete button click, show confirmation dialog
    AlertDialog.Builder(this)
      .setMessage("Are you sure you want to delete this student?")
      .setPositiveButton("Yes") { _, _ ->
        students.removeAt(position)  // Remove student from the list
        studentAdapter.notifyItemRemoved(position)  // Notify adapter
        Snackbar.make(findViewById(R.id.main), "Student deleted", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            students.add(position, student)  // Undo delete
            studentAdapter.notifyItemInserted(position)
          }
          .show()
      }
      .setNegativeButton("No", null)
      .show()
  }
}
