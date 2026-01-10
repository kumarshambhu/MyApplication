package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.LoshuGridPlaneRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentLoshuGridBinding
import com.shambhu.myapplication.model.LoshuGridPlaneAccordionItem
import com.shambhu.myapplication.model.LoshuGridPlanes
import com.shambhu.myapplication.model.MissingNumberData
import com.shambhu.myapplication.model.Plane
import com.shambhu.myapplication.model.RepetitiveNumberData
import com.shambhu.myapplication.model.Section
import com.shambhu.myapplication.repository.LoshuGridRepository
import com.shambhu.myapplication.repository.impl.LoshuGridRepositoryImpl
import com.shambhu.myapplication.service.impl.LoshuGridServiceImpl
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils.convertToHtml
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class LoshuGridFragment : Fragment() {

    private var _binding: FragmentLoshuGridBinding? = null
    private val binding get() = _binding!!
    private val loshuGridRepository: LoshuGridRepository by lazy {
        LoshuGridRepositoryImpl(LoshuGridServiceImpl(requireContext()))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoshuGridBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.getString(Constants.Companion.ARG_DOB)?.let { dob ->
            var digits = dob.filter { it.isDigit() }

            // Calculate numerology numbers
            val birthDate = CommonUtils.parseDate(dob)
            val birthDay = birthDate.dayOfMonth
            val birthMonth = birthDate.monthValue
            val birthYear = birthDate.year

            val mulankNumber = NumerologyCalculationUtils.calculateBirthdayNumber(birthDay)
            val bhagyankNumber =
                NumerologyCalculationUtils.calculateLifePath(birthDay, birthMonth, birthYear)
            val kuaNumber = NumerologyCalculationUtils.calculateKuaNumber(birthYear, true)

            binding.loshuGridDobValue.text = dob
            binding.loshuGridMulankValue.text = mulankNumber.toString()
            binding.loshuGridBhagyankValue.text = bhagyankNumber.toString()
            binding.loshuGridKuaNumberValue.text = kuaNumber.toString()

            println("DOB: $digits")
            digits =
                digits + CommonUtils.reduceNumber(birthDay) + CommonUtils.reduceNumberIgnoreMasterNumber(
                    bhagyankNumber
                ) + kuaNumber
            println("DOB: $digits")
            val numberCounts = IntArray(10)
            for (digitChar in digits) {
                val digit = digitChar.toString().toInt()
                numberCounts[digit]++
            }


            updateCell(binding.cell1, 1, numberCounts[1])
            updateCell(binding.cell2, 2, numberCounts[2])
            updateCell(binding.cell3, 3, numberCounts[3])
            updateCell(binding.cell4, 4, numberCounts[4])
            updateCell(binding.cell5, 5, numberCounts[5])
            updateCell(binding.cell6, 6, numberCounts[6])
            updateCell(binding.cell7, 7, numberCounts[7])
            updateCell(binding.cell8, 8, numberCounts[8])
            updateCell(binding.cell9, 9, numberCounts[9])

            val loshuPlanes = NumerologyCalculationUtils.calculateLoshuGridPlanes(numberCounts)
            createLoshuPlaneItemForRecyclerView(loshuPlanes)

            lifecycleScope.launch {
                loshuGridRepository.getMissingNumberData()
                    .zip(loshuGridRepository.getRepetitiveNumberData()) { missingData, repetitiveData ->
                        Pair(missingData, repetitiveData)
                    }
                    .collect { (missingData, repetitiveData) ->
                        createMissingNumberAccordionItems(numberCounts, missingData)
                        createRepeatingNumberAccordionItems(numberCounts, repetitiveData)
                    }
            }
            binding.planeRecyclerView.visibility = View.VISIBLE
            binding.missingNumberRecyclerView.visibility = View.GONE
            binding.repeatNumberRecyclerView.visibility = View.GONE
        }
    }

    private fun createMissingNumberAccordionItems(
        numberCounts: IntArray,
        missingNumberData: MissingNumberData
    ) {
        val missingNumberItems = mutableListOf<LoshuGridPlaneAccordionItem>()
        val missingNumberMap = missingNumberData.missingNumbers.associateBy { it.number }

        for (i in 1..9) {
            if (numberCounts[i] == 0) {
                missingNumberMap[i]?.let { missingNumber ->
                    var content = "<ul>"
                    val traits = mutableListOf<String>()
                    missingNumber.impacts.forEach { impact ->
                        content += "<li>$impact</li>"
                        traits.add(impact)
                    }
                    var remedies = mutableListOf<String>()
                    missingNumber.remedies.forEach { impact ->
                        remedies.add(impact)
                    }
                    content += "</ul>"
                    missingNumberItems.add(
                        LoshuGridPlaneAccordionItem(
                            "Missing Number: $i",
                            "",
                            content = "",//convertToHtml(content),
                            traitsHeading = "Impacts",
                            traits = traits,
                            remedies = remedies,
                            imageSource = "",
                            backgroundColor = R.drawable.missing_number_background,
                            headerColor = 0,
                            isExpanded = false
                        )
                    )
                }
            }
        }
        setupRecyclerView(binding.missingNumberRecyclerView, missingNumberItems)
    }

    private fun createRepeatingNumberAccordionItems(
        numberCounts: IntArray,
        repetitiveNumberData: RepetitiveNumberData
    ) {
        val repeatingNumberItems = mutableListOf<LoshuGridPlaneAccordionItem>()
        val repetitiveNumberMap = repetitiveNumberData.repetitiveNumbers.associateBy { it.number }

        for (i in 1..9) {
            val count = numberCounts[i]
            if (count > 1) {
                repetitiveNumberMap[i]?.let { repetitiveNumber ->
                    val foundOccurrence =
                        repetitiveNumber.occurrences.find { it.count.matches(count) }

                    foundOccurrence?.let { occurrence ->
                        var content = "<ul>"
                        var traits = mutableListOf<String>()
                        occurrence.effects.forEach { effect ->
                            content += "<li>$effect</li>"
                            traits.add(effect)
                        }
                        content += "</ul>"
                        repeatingNumberItems.add(
                            LoshuGridPlaneAccordionItem(
                                "Repeating Number: $i (x$count)",
                                "",
                                content = "",//convertToHtml(content),
                                traitsHeading = "Effects",
                                traits = traits,
                                remedies = emptyList(),
                                imageSource = "",
                                backgroundColor = R.drawable.repeating_number_background,
                                headerColor = 0,
                                isExpanded = false
                            )
                        )
                    }
                }
            }
        }
        setupRecyclerView(binding.repeatNumberRecyclerView, repeatingNumberItems)
    }

    fun searchPlane(name: String, title: String, planesJsonArray: JSONArray): Section? {
        val gson = Gson()
        val planeListType = object : TypeToken<List<Plane>>() {}.type
        val planes: List<Plane> = gson.fromJson(planesJsonArray.toString(), planeListType)

        return planes
            .firstOrNull { it.name.equals(name, ignoreCase = true) }
            ?.sections
            ?.firstOrNull { it.title.equals(title, ignoreCase = true) }
    }

    private fun getPlaneMessage(
        planeName: String,
        availableNumbers: List<Int>,
        planesJsonArray: JSONArray
    ): String {
        val title = availableNumbers.joinToString(", ")
        val section = searchPlane(planeName, title, planesJsonArray)

        if (section != null) {
            var traits = "<ul>"
            section.traits.iterator().forEach {
                traits += ("<li>$it</li>")
            }
            traits += ("</ul")

            return convertToHtml(traits)
        } else {
            return ""
        }
    }

    private fun getPlaneTraits(
        planeName: String,
        availableNumbers: List<Int>,
        planesJsonArray: JSONArray
    ): List<String> {
        val title = availableNumbers.joinToString(", ")
        val section = searchPlane(planeName, title, planesJsonArray)
        var traits = mutableListOf<String>()
        section?.traits?.iterator()?.forEach {
            traits.add(it)
        }
        return traits
    }

    private fun getPresentNumbers(planeNumbers: List<Int>, missingNumbers: List<Int>): String {
        return "Present Number: " + planeNumbers.filter { missingNumbers.contains(it) }
            .joinToString(", ")
    }

    private fun createLoshuGridPlaneAccordionItem(
        header: String,
        planeName: String,
        planeNumbers: List<Int>,
        presentNumbers: List<Int>,
        planesJsonArray: JSONArray,
        backgroundColor: Int,
        headerColor: Int
    ): LoshuGridPlaneAccordionItem {
        return LoshuGridPlaneAccordionItem(
            header = header,
            presentNumber = getPresentNumbers(planeNumbers, presentNumbers),
            content = "",//getPlaneMessage(planeName, presentNumbers, planesJsonArray),
            traitsHeading = "Traits",
            traits = getPlaneTraits(planeName, presentNumbers, planesJsonArray),
            remedies = emptyList(),
            imageSource = "ic_moon",
            backgroundColor = backgroundColor,
            headerColor = headerColor,
            isExpanded = false
        )
    }

    private fun createLoshuPlaneItemForRecyclerView(loshuPlanes: LoshuGridPlanes) {
        val loshuPlaneItems = mutableListOf<LoshuGridPlaneAccordionItem>()
        val planeMeanings = CommonUtils.readAssetFile(requireContext(), "plane.json")
        val jsonObject = JSONObject(planeMeanings)
        val jsonArray = jsonObject.getJSONArray("planes")

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Mental Plane(4, 9, 2)", "mental_plane", listOf(4, 9, 2),
                loshuPlanes.mentalPlane, jsonArray,
                R.drawable.mental_plane_background, R.color.mental_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Emotional Plane(3, 5, 7)", "heart_plane", listOf(3, 5, 7),
                loshuPlanes.emotionalPlane, jsonArray,
                R.drawable.emotional_plane_background, R.color.emotional_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Practical Plane(8, 1, 6)", "practical_plane", listOf(8, 1, 6),
                loshuPlanes.practicalPlane, jsonArray,
                R.drawable.practical_plane_background, R.color.practical_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Thought Plane(4, 3, 8)", "vision_plane", listOf(4, 3, 8),
                loshuPlanes.thoughtPlane, jsonArray,
                R.drawable.thought_plane_background, R.color.thought_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Will Plane(9, 5, 1)", "will_plane", listOf(9, 5, 1),
                loshuPlanes.willPlane, jsonArray,
                R.drawable.will_plane_background, R.color.will_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Action Plane(2, 7, 6)", "action_plane", listOf(2, 7, 6),
                loshuPlanes.actionPlane, jsonArray,
                R.drawable.action_plane_background, R.color.action_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Silver Success Plane(4, 5, 6)", "silver_success_plane", listOf(4, 5, 6),
                loshuPlanes.silverSuccessPlane, jsonArray,
                R.drawable.silver_success_plane_background, R.color.silver_success_plane_header
            )
        )

        loshuPlaneItems.add(
            createLoshuGridPlaneAccordionItem(
                "Golden Success Plane(2, 5, 8)", "golden_success_plane", listOf(2, 5, 8),
                loshuPlanes.goldenSuccessPlane, jsonArray,
                R.drawable.golden_success_plane_background, R.color.golden_success_plane_header
            )
        )
        println(loshuPlaneItems)
        setupRecyclerView(binding.planeRecyclerView, loshuPlaneItems)
    }



    private fun updateCell(textView: TextView, number: Int, count: Int) {
        if (count > 0) {
            textView.text = number.toString().repeat(count)
        } else {
            textView.text = ""
        }
    }

    private fun setupRecyclerView(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        gridItems: MutableList<LoshuGridPlaneAccordionItem>
    ) {
        var expandedPosition = -1
        val adapter =
            LoshuGridPlaneRecyclerViewAdapter(gridItems, this.requireContext()) { position ->
                val previousExpandedPosition = expandedPosition
                if (expandedPosition == position) {
                    // Clicked on the already expanded item, so collapse it
                    gridItems[position].isExpanded = false
                    recyclerView.adapter?.notifyItemChanged(position)
                    expandedPosition = -1
                } else {
                    // A new item is clicked
                    if (previousExpandedPosition != -1) {
                        // Collapse the previously expanded item
                        gridItems[previousExpandedPosition].isExpanded = false
                        recyclerView.adapter?.notifyItemChanged(previousExpandedPosition)
                    }
                    // Expand the new item
                    gridItems[position].isExpanded = true
                    recyclerView.adapter?.notifyItemChanged(position)
                    expandedPosition = position
                }
            }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.loshu_grid_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val planeVisibility = binding.planeRecyclerView.visibility == View.VISIBLE
        val missingVisibility = binding.missingNumberRecyclerView.visibility == View.VISIBLE
        val repeatVisibility = binding.repeatNumberRecyclerView.visibility == View.VISIBLE

        binding.planeRecyclerView.visibility = View.GONE
        binding.missingNumberRecyclerView.visibility = View.GONE
        binding.repeatNumberRecyclerView.visibility = View.GONE
        binding.loshuGridElement.visibility = View.GONE
        binding.loshuGrid.visibility = View.VISIBLE
        return when (item.itemId) {
            R.id.action_toggle_planes -> {
                if (!planeVisibility) binding.planeRecyclerView.visibility = View.VISIBLE
                true
            }

            R.id.action_toggle_missing_number -> {
                if (!missingVisibility) binding.missingNumberRecyclerView.visibility = View.VISIBLE
                binding.loshuGridElement.visibility = View.VISIBLE
                binding.loshuGrid.visibility = View.GONE
                true
            }

            R.id.action_toggle_repeat_number -> {
                if (!repeatVisibility) binding.repeatNumberRecyclerView.visibility = View.VISIBLE
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance(dob: String, fullName: String): LoshuGridFragment {
            val fragment = LoshuGridFragment()
            val args = Bundle()
            args.putString(Constants.Companion.ARG_DOB, dob)
            args.putString(Constants.Companion.ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}