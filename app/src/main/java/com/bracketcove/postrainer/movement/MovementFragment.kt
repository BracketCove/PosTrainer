package com.bracketcove.postrainer.movement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.R
import com.bracketcove.postrainer.getTargetsByResource
import com.bracketcove.postrainer.movement.buildlogic.MovementInjector
import com.bracketcove.postrainer.showToast
import kotlinx.android.synthetic.main.fragment_movement.*
import kotlinx.android.synthetic.main.subview_movement.*


class MovementFragment : Fragment(), MovementContract.View {

    var logic: BaseLogic<MovementEvent>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movement, container, false)

    }

    override fun onStart() {
        super.onStart()
        logic = MovementInjector.provideLogic(
            requireActivity().application,
            this
        )

        logic?.handleEvent(
            MovementEvent.OnStart(
                MovementFragmentArgs.fromBundle(arguments!!).movementId
            )
        )
    }

    override fun setTootlbarTitle(title: String) {
        ctlMovement.title = title
    }

    override fun setParallaxImage(resourceList: List<String>) {
        imvMovement.setImageResource(
            resources.getIdentifier(
                resourceList[0],
                "drawable",
                activity?.packageName
            )
        )
    }

    override fun setTargets(targets: List<String>) {
        val targetIds = getTargetsByResource(targets)

        if (targetIds.size > 0) imvIconOne.setImageResource(targetIds[0])
        if (targetIds.size > 1) imvIconOne.setImageResource(targetIds[1])
        if (targetIds.size > 2) imvIconOne.setImageResource(targetIds[2])
        if (targetIds.size > 3) imvIconOne.setImageResource(targetIds[3])
        if (targetIds.size > 4) imvIconOne.setImageResource(targetIds[4])
    }

    override fun setFrequency(frequency: String) {
        lblFrequency.text = frequency
    }

    //Default is the drawable for repetition based exercises
    override fun setIsTimed(isTimed: Boolean) {
        if (isTimed) imvRepsOrTime.setImageResource(R.drawable.ic_alarm_black_48dp)
    }

    override fun setTimeOrRepetitions(value: String) {
        lblRepsOrTime.text = value
    }

    override fun setDifficulty(difficulty: String) {
        lblDifficulty.text = difficulty
    }

    override fun setDescription(description: String) {
        lblDescriptionContent.text = description
    }

    override fun setInstructions(instructions: String) {
        lblInstructionContent.text = instructions
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    override fun startMovementListView() {
        findNavController().navigate(
            MovementFragmentDirections
                .actionMovementFragmentToMovementListFragment()
        )
    }


}