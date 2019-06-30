package com.bracketcove.postrainer.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bracketcove.postrainer.R
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * Created by Ryan on 05/03/2017.
 */

class SettingsFragment : Fragment(), SettingsContract.View {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onStart() {
        super.onStart()
        bottomNavSettings.selectedItemId = R.id.settings
        setUpBottomNav()
    }

    override fun startAlarmListActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun setUpBottomNav() {
        bottomNavSettings.setupWithNavController(findNavController())
        bottomNavSettings.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.reminders ->
                    if (findNavController().currentDestination?.id == R.id.settingsFragment) {
                        findNavController().navigate(
                            SettingsFragmentDirections.actionSettingsFragmentToReminderListFragment()
                        )
                    }
                R.id.movements ->
                    if (findNavController().currentDestination?.id == R.id.settingsFragment) {

                        findNavController().navigate(
                            SettingsFragmentDirections.actionSettingsFragmentToMovementListFragment()
                        )
                    }
            }

            true
        }
    }

}
