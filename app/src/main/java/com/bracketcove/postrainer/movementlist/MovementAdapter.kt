package com.bracketcove.postrainer.movementlist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bracketcove.postrainer.R
import kotlinx.android.synthetic.main.item_movement.view.*

class MovementAdapter(var event: MutableLiveData<MovementListEvent> = MutableLiveData(),
                      private val movements: List<MovementListItem>) :
    RecyclerView.Adapter<MovementAdapter.MovementViewHolder>() {
    override fun getItemCount(): Int = movements.size

    internal fun setObserver(observer: Observer<MovementListEvent>) = event.observeForever(observer)

    override fun onBindViewHolder(holder: MovementAdapter.MovementViewHolder, position: Int) {
        movements[position].let { item ->
            holder.movementTitle.text = item.name
            holder.movementThumb.setImageResource(item.thumbnail)
            //Number of targets are finite, but not all will be used for all movements.
            //I could add the ImageViews dynamically, but I've decided to implement an EZ workaround instead
            bindTargets(item, holder)
            holder.itemView.setOnClickListener {
                event.value = MovementListEvent.OnMovementClick(
                    movements[position].name
                )
            }
        }
    }

    //Since
    private fun bindTargets(item: MovementListItem, holder: MovementViewHolder) {
        if (item.targets.size > 0 ) holder.targetIconOne.setImageResource(item.targets[0])
        if (item.targets.size > 1) holder.targetIconTwo.setImageResource(item.targets[1])
        if (item.targets.size > 2) holder.targetIconThree.setImageResource(item.targets[2])
        if (item.targets.size > 3) holder.targetIconFour.setImageResource(item.targets[3])
        if (item.targets.size > 4) holder.targetIconFive.setImageResource(item.targets[4])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MovementViewHolder(
            inflater.inflate(R.layout.item_movement, parent, false)
        )
    }

    class MovementViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        var movementTitle: TextView = root.lblMovementItemName
        var movementThumb: ImageView = root.imvMovementItemThumb
        var targetIconOne: ImageView = root.imvItemIconOne
        var targetIconTwo: ImageView = root.imvItemIconTwo
        var targetIconThree: ImageView = root.imvItemIconThree
        var targetIconFour: ImageView = root.imvItemIconFour
        var targetIconFive: ImageView = root.imvItemIconFive
    }
}

