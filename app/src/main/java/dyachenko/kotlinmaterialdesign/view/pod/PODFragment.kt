package dyachenko.kotlinmaterialdesign.view.pod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dyachenko.kotlinmaterialdesign.R
import dyachenko.kotlinmaterialdesign.databinding.BottomSheetLayoutBinding
import dyachenko.kotlinmaterialdesign.databinding.PodFragmentBinding
import dyachenko.kotlinmaterialdesign.util.*
import dyachenko.kotlinmaterialdesign.view.MainActivity
import dyachenko.kotlinmaterialdesign.viewmodel.PODState
import dyachenko.kotlinmaterialdesign.viewmodel.PODViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.util.*

class PODFragment : Fragment(), CoroutineScope by MainScope() {

    private var _binding: PodFragmentBinding? = null
    private val binding get() = _binding!!

    private var _bottomSheetBinding: BottomSheetLayoutBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    private var dayOfPhoto = TODAY_PHOTO

    private var bottomNavigationDrawerFragment =
        BottomNavigationDrawerFragment(object : OnNavigationItemSelected {
            override fun onItemSelected(id: Int) {
                doAction(id)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PodFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetBehavior(view)
        setWikiOnClickListener()
        initChips()
        setBottomAppBar()

        val observer = Observer<PODState> { renderData(it) }
        viewModel.resourceProvider = ResourceProvider(requireContext())
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)

        getData()
    }

    private fun initChips() = with(binding) {
        dayChipGroup.check(R.id.today_chip)

        dayChipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                it.isChecked = true
                var newDayOfPhoto = dayOfPhoto
                when (it.id) {
                    R.id.today_chip -> newDayOfPhoto = TODAY_PHOTO
                    R.id.yesterday_chip -> newDayOfPhoto = YESTERDAY_PHOTO
                    R.id.day_before_yesterday_chip -> newDayOfPhoto = DAY_BEFORE_YESTERDAY_PHOTO
                }
                if (newDayOfPhoto != dayOfPhoto) {
                    dayOfPhoto = newDayOfPhoto
                    getData()
                }
            }
        }
    }

    private fun setWikiOnClickListener() = with(binding) {
        wikiInputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                val text = wikiInputEditText.text.toString()
                data = Uri.parse("$WIKI_BASE_URL$text")
            })
        }
    }

    private fun setBottomSheetBehavior(view: View) {
        val bottomSheet: ConstraintLayout = view.findViewById(R.id.bottom_sheet_container)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        _bottomSheetBinding = BottomSheetLayoutBinding.bind(bottomSheet)
    }

    private fun setBottomAppBar() = with(binding) {
        (activity as MainActivity).setSupportActionBar(bottomAppBar)

        fab.setOnClickListener {
            doAction(fab.id)
        }

        setHasOptionsMenu(true)
    }

    private fun getData() {
        viewModel.getPODFromServer(Date().subDays(dayOfPhoto).format())
    }

    private fun renderData(data: PODState) = with(binding) {
        when (data) {
            is PODState.Success -> {
                val responseData = data.responseData
                val url = responseData.url

                Picasso
                    .get()
                    .load(url)
                    .placeholder(R.drawable.ic_no_photo_vector)
                    .into(podImageView, object : Callback {
                        override fun onSuccess() {
                            podLoadingLayout.hide()
                        }

                        override fun onError(e: Exception?) {
                            podRootView.showSnackBar(getString(R.string.error_server_msg),
                                getString(R.string.reload_msg),
                                { getData() })
                        }
                    })

                bottomSheetBinding.apply {
                    bottomSheetDescriptionHeader.text = responseData.title
                    bottomSheetDescription.text = responseData.explanation
                }
            }
            is PODState.Loading -> {
                podLoadingLayout.show()
            }
            is PODState.Error -> {
                podLoadingLayout.hide()
                podRootView.showSnackBar(data.error.message ?: getString(R.string.error_msg),
                    getString(R.string.reload_msg),
                    { getData() })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (doAction(item.itemId)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun doAction(id: Int): Boolean {
        when (id) {
            R.id.fab -> {
                toastTop(getString(R.string.action_fab))
                return true
            }
            R.id.action_favourite -> {
                toastTop(getString(R.string.action_favourite))
                return true
            }
            R.id.action_about -> {
                toastTop(getString(R.string.action_about))
                return true
            }
            R.id.action_settings -> {
                toastTop(getString(R.string.action_settings))
                return true
            }
            android.R.id.home -> {
                activity?.let {
                    bottomNavigationDrawerFragment.show(it.supportFragmentManager, DRAWER_TAG)
                }
                return true
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomNavigationDrawerFragment.removeListener()
        _bottomSheetBinding = null
        _binding = null
    }

    interface OnNavigationItemSelected {
        fun onItemSelected(id: Int)
    }

    companion object {
        private const val WIKI_BASE_URL = "https://en.wikipedia.org/wiki/"
        private const val TODAY_PHOTO = 0
        private const val YESTERDAY_PHOTO = 1
        private const val DAY_BEFORE_YESTERDAY_PHOTO = 2
        private const val DRAWER_TAG = "DRAWER_TAG"

        fun newInstance() = PODFragment()
    }
}