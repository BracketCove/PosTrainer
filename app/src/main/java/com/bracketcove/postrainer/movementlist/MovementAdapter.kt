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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movement.view.*

class MovementAdapter(var event: MutableLiveData<MovementListEvent> = MutableLiveData(),
                      private val movements: List<MovementListItem>) :
    RecyclerView.Adapter<MovementAdapter.MovementViewHolder>() {
    override fun getItemCount(): Int = movements.size

    internal fun setObserver(observer: Observer<MovementListEvent>) = event.observeForever(observer)

    override fun onBindViewHolder(holder: MovementAdapter.MovementViewHolder, position: Int) {
        movements[position].let { item ->
            holder.movementTitle.text = item.name

            //thumbnail
            Glide.with(holder.itemView)
                .load(item.thumbnail)
                .placeholder(R.drawable.ic_launcher_v2)
                .centerCrop()
                .into(holder.movementThumb)
            //holder.movementThumb.setImageResource(item.thumbnail)


            //Number of targets are finite, but not all will be used for all movements.
            //I could add the ImageViews dynamically, but I've decided to implement an EZ workaround instead
            bindTargets(item, holder)
            showDifficultyIcons(item.difficulty, holder)
            holder.itemView.setOnClickListener {
                event.value = MovementListEvent.OnMovementClick(
                    movements[position].name
                )
            }
        }
    }

    private fun showDifficultyIcons(difficulty: Int, holder:MovementViewHolder) {
        if (difficulty > 0 ) holder.starIconOne.visibility = View.VISIBLE
        if (difficulty > 1) holder.starIconTwo.visibility = View.VISIBLE
        if (difficulty > 2) holder.starIconThree.visibility = View.VISIBLE
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

    //TODO: Performance could possibly take a hit with this many views, probably worth creating one imageview with n number of drawables to represent different difficulties

    class MovementViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        var movementTitle: TextView = root.lblMovementItemName
        var movementThumb: ImageView = root.imvMovementItemThumb
        var targetIconOne: ImageView = root.imvItemIconOne
        var targetIconTwo: ImageView = root.imvItemIconTwo
        var targetIconThree: ImageView = root.imvItemIconThree
        var targetIconFour: ImageView = root.imvItemIconFour
        var targetIconFive: ImageView = root.imvItemIconFive
        var starIconOne: ImageView = root.imvItemStarOne
        var starIconTwo: ImageView = root.imvItemStarTwo
        var starIconThree: ImageView = root.imvItemStarThree
    }
}

