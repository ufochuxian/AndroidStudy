import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eric.androidstudy.databinding.ItemNormalBinding
import com.eric.androidstudy.databinding.ItemShimmerBinding

class ShimmerAdapter(
    private var items: List<String>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val isLoading: Boolean
        get() = items == null

    companion object {
        private const val TAG = "ShimmerAdapter"
        private const val VIEW_TYPE_SHIMMER = 0
        private const val VIEW_TYPE_DATA = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoading) VIEW_TYPE_SHIMMER else VIEW_TYPE_DATA
    }

    override fun getItemCount(): Int {
        val count = if (isLoading) 10 else items!!.size
        Log.d(TAG, "getItemCount: $count")
        return count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d(TAG, "onCreateViewHolder: viewType=$viewType")
        return if (viewType == VIEW_TYPE_SHIMMER) {
            val binding = ItemShimmerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            Log.d(TAG, "创建 shimmer 占位 ViewHolder")
            LoadingViewHolder(binding)
        } else {
            val binding = ItemNormalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            Log.d(TAG, "创建正常数据 ViewHolder")
            DataViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: position=$position")
        if (holder is LoadingViewHolder) {
            Log.d(TAG, "绑定 shimmer 占位 item at position $position")
            holder.binding.shimmerContainer.startShimmer()
        } else if (holder is DataViewHolder) {
            val itemText = items!![position]
            Log.d(TAG, "绑定正常 item at position $position: $itemText")
            holder.binding.text.text = itemText
        }
    }

    fun submitData(newItems: List<String>) {
        Log.d(TAG, "收到真实数据，item 数量：${newItems.size}")
        this.items = newItems
        notifyDataSetChanged()
    }


    class LoadingViewHolder(val binding: ItemShimmerBinding) : RecyclerView.ViewHolder(binding.root)
    class DataViewHolder(val binding: ItemNormalBinding) : RecyclerView.ViewHolder(binding.root)
}
