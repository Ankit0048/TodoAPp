package `in`.day1.todoapp

import `in`.day1.todoapp.databinding.ItemRowBinding
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val items :ArrayList<TaskEntity>,
                  private val updateListener:(id:Int)->Unit,
                  private val deleteListener:(id:Int)->Unit

): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    class ViewHolder(binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvTask = binding.tvTask
        val tvTime = binding.tvTime
        val ivEdit = binding.editTask
        val ivDelete = binding.deleteTask
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRowBinding.inflate(LayoutInflater.from(parent.context),
        parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvTime.text = item.time
        holder.tvTask.text = item.task

        if(position %2 == 0) {
            holder.llMain.setBackgroundColor(Color.parseColor("#C1C1C1"))
        }
        else {
            holder.llMain.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}