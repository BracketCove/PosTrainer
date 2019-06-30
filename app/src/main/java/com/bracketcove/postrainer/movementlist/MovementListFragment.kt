package com.bracketcove.postrainer.movementlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bracketcove.postrainer.*
import com.bracketcove.postrainer.movementlist.buildlogic.MovementListInjector
import com.wiseassblog.domain.domainmodel.Movement
import kotlinx.android.synthetic.main.fragment_movement_list.*


class MovementListFragment : Fragment(), MovementListContract.View {

    var logic: BaseLogic<MovementListEvent>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movement_list, container, false)
    }

    override fun onStart() {
        super.onStart()

        logic = MovementListInjector.provideLogic(
            requireActivity().application,
            this
        )

        setUpBottomNav()
        logic?.handleEvent(MovementListEvent.OnStart)
    }

    private fun setUpBottomNav() {
        bottomNavMovements.setupWithNavController(findNavController())
        bottomNavMovements.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.reminders -> logic?.handleEvent(MovementListEvent.OnRemindersClick)
                R.id.settings -> logic?.handleEvent(MovementListEvent.OnSettingsClick)
            }

            true
        }

        bottomNavMovements.selectedItemId = R.id.movements
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //prevent memory leaks
        logic?.handleEvent(MovementListEvent.OnStop)
        recMovementList.adapter = null
    }

    override fun setMovementListData(movementListData: List<Movement>) {
        val movementAdapter = MovementAdapter(
            movements = movementListData.toMovementItemList()
        )

        //forward event to logic class
        movementAdapter.event.observe(this,
            Observer {
                logic?.handleEvent(it)
            }
        )

        recMovementList.adapter = movementAdapter

        proMovementList.visibility = View.INVISIBLE
        recMovementList.visibility = View.VISIBLE
    }

    override fun startMovementView(movementId: String) {
        findNavController().navigate(
            MovementListFragmentDirections
                .actionMovementListFragmentToMovementFragment(movementId)
        )
    }

    //Why the if clause? I was getting crashes from fast successive calls to this function
    override fun startSettingsView() {
        if (findNavController().currentDestination?.id == R.id.movementListFragment) {
            findNavController().navigate(
                MovementListFragmentDirections
                    .actionMovementListFragmentToSettingsFragment()
            )
        }
    }

    override fun showMessage(msg: String) {
        showToast(msg)
    }

    override fun startRemindersView() {
        if (findNavController().currentDestination?.id == R.id.movementListFragment) {
            findNavController().navigate(
                MovementListFragmentDirections
                    .actionMovementListFragmentToReminderListFragment()
            )
        }

    }

    /**
     * Since grabbing resources (Int) from predefined names is easiest done in the Fragment, I've chosen to do it in the
     * fragment.
     */
    fun List<Movement>.toMovementItemList(): List<MovementListItem> {
        val newList = mutableListOf<MovementListItem>()
        this.forEach { movement ->
            newList.add(
                MovementListItem(
                    movement.name,
                    getTargetsByResource(movement.targets),
                    getThumbnailByResourceName(movement.imageResourceNames[0]),
                    movement.difficulty.difficultyToInt()
                )
            )
        }

        return newList
    }

    private fun String.difficultyToInt(): Int {
        return when (this) {
            "Easy" -> 1
            "Medium" -> 2
            "Advanced" -> 3
            else -> 2
        }
    }

}
