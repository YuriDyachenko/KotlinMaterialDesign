package dyachenko.kotlinmaterialdesign.view.pod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import dyachenko.kotlinmaterialdesign.R

class BottomNavigationDrawerFragment(
    private var onNavigationItemSelected: PODFragment.OnNavigationItemSelected?
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_navigation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            dismiss()
            onNavigationItemSelected?.onItemSelected(menuItem.itemId)
            true
        }
    }

    fun removeListener() {
        onNavigationItemSelected = null
    }
}
