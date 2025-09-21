import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.walmartassessment.data.model.CountryItem
import com.android.walmartassessment.databinding.ItemCountryBinding

class CountryAdapter :
    ListAdapter<CountryItem, CountryAdapter.CountryViewHolder>(CountryDiffCallback()) {

    class CountryViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = getItem(position)
        holder.binding.apply {
            tvNameRegion.text = "${country.name}, ${country.region}"
            tvCode.text = country.code
            tvCapital.text = country.capital
        }
    }

    class CountryDiffCallback : DiffUtil.ItemCallback<CountryItem>() {
        override fun areItemsTheSame(oldItem: CountryItem, newItem: CountryItem): Boolean {
            // Use a unique field — country code is unique
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: CountryItem, newItem: CountryItem): Boolean {
            // Check all fields
            return oldItem == newItem
        }
    }
}
