package com.bracketcove.postrainer.movement

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bracketcove.postrainer.BaseLogic
import com.bracketcove.postrainer.R
import com.bracketcove.postrainer.getTargetsByResource
import com.bracketcove.postrainer.movement.buildlogic.MovementInjector
import com.bracketcove.postrainer.showToast
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_movement.*
import kotlinx.android.synthetic.main.subview_movement.*


class MovementFragment : Fragment(), MovementContract.View {

    var logic: BaseLogic<MovementEvent>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return inflater.inflate(R.layout.fragment_movement, container, false)
    }

    override fun onDestroyView() {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        super.onDestroyView()
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

        imvMovement.setOnClickListener {
            logic?.handleEvent(MovementEvent.OnImageClick)
        }

        ablMovement.addOnOffsetChangedListener(
            object : AppBarLayout.OnOffsetChangedListener {
                //Note: because 'reasons', appbar offset ranges from [0, -1428], 0 being expanded state
                override fun onOffsetChanged(appBar: AppBarLayout?, offset: Int) {
                    if (offset == 0) {
                        imvMovement.isEnabled = true
                    } else if (imvMovement.isEnabled) {
                        //if offset is not 0, and the image is enabled, disable it
                        imvMovement.isEnabled = false
                    }
                }

            }
        )
    }

    override fun setTootlbarTitle(title: String) {
        tlbMovement.text = title
    }

    override fun setParallaxImage(resource: String) {

        val animFadeOut = AnimationUtils.loadAnimation(
            context,
            R.anim.fade_out
        )

        imvMovement.startAnimation(animFadeOut)


        Glide.with(imvMovement)
            .load(
                resources.getIdentifier(
                    resource,
                    "drawable",
                    activity?.packageName
                )
            )
            .into(imvMovement)


        val animSlideInRight = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_in_right
        )

        imvMovement.startAnimation(animSlideInRight)

    }

    override fun hideProgressBar() {
        proMovement.visibility = View.INVISIBLE
    }

    override fun setTargets(targets: List<String>) {
        val targetIds = getTargetsByResource(targets)

        if (targetIds.size > 0) imvIconOne.setImageResource(targetIds[0])
        if (targetIds.size > 1) imvIconTwo.setImageResource(targetIds[1])
        if (targetIds.size > 2) imvIconThree.setImageResource(targetIds[2])
        if (targetIds.size > 3) imvIconFour.setImageResource(targetIds[3])
        if (targetIds.size > 4) imvIconFive.setImageResource(targetIds[4])
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