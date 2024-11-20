package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val students: List<StudentModel>, private val listener: StudentAdapterListener) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  interface StudentAdapterListener {
    fun onEditClick(student: StudentModel)
    fun onDeleteClick(student: StudentModel, position: Int)
  }

  inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)

    init {
      imageEdit.setOnClickListener {
        listener.onEditClick(students[adapterPosition])  // Call onEditClick
      }
      imageRemove.setOnClickListener {
        listener.onDeleteClick(students[adapterPosition], adapterPosition)  // Call onDeleteClick
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]
    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId
  }
}


