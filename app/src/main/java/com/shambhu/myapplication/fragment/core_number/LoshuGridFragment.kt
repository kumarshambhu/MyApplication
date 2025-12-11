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

    private fun createMissingNumberAccordionItems(numberCounts: IntArray, missingNumberData: MissingNumberData) {
        val missingNumberItems = mutableListOf<LoshuGridPlaneAccordionItem>()
        val missingNumberMap = missingNumberData.missingNumbers.associateBy { it.number }

        for (i in 1..9) {
            if (numberCounts[i] == 0) {
                missingNumberMap[i]?.let { missingNumber ->
                    var content = "<ul>"
                    missingNumber.impacts.forEach { impact ->
                        content += "<li>$impact</li>"
                    }
                    content += "</ul>"
                    missingNumberItems.add(
                        LoshuGridPlaneAccordionItem(
                            "Missing Number: $i",
                            "",
                            content = convertToHtml(content),
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

    private fun createRepeatingNumberAccordionItems(numberCounts: IntArray, repetitiveNumberData: RepetitiveNumberData) {
        val repeatingNumberItems = mutableListOf<LoshuGridPlaneAccordionItem>()
        val repetitiveNumberMap = repetitiveNumberData.repetitiveNumbers.associateBy { it.number }

        for (i in 1..9) {
            val count = numberCounts[i]
            if (count > 1) {
                repetitiveNumberMap[i]?.let { repetitiveNumber ->
                    val foundOccurrence = repetitiveNumber.occurrences.find { it.count.matches(count) }

                    foundOccurrence?.let { occurrence ->
                        var content = "<ul>"
                        occurrence.effects.forEach { effect ->
                            content += "<li>$effect</li>"
                        }
                        content += "</ul>"
                        repeatingNumberItems.add(
                            LoshuGridPlaneAccordionItem(
                                "Repeating Number: $i (x$count)",
                                "",
                                content = convertToHtml(content),
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

    private fun getPresentNumbers(planeNumbers: List<Int>, missingNumbers: List<Int>): String {
        return "Present Number: " + planeNumbers.filter { missingNumbers.contains(it) }
            .joinToString(", ")
    }

    private fun createLoshuPlaneItemForRecyclerView(loshuPlanes: LoshuGridPlanes) {
        val loshuPlaneItems = mutableListOf<LoshuGridPlaneAccordionItem>()
        val planeMeanings = CommonUtils.readAssetFile(requireContext(), "plane.json")
        val jsonObject = JSONObject(planeMeanings)
        val jsonArray = jsonObject.getJSONArray("planes")

        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Mental Plane(4, 9, 2)",
                getPresentNumbers(listOf(4, 9, 2), loshuPlanes.mentalPlane),
                content = getPlaneMessage(
                    "mental_plane",
                    loshuPlanes.mentalPlane, jsonArray
                ),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.mental_plane_background,
                headerColor =  R.color.mental_plane_header,
                isExpanded = false
            )
        )

        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Emotional Plane(3, 5, 7)",
                getPresentNumbers(listOf(3, 5, 7), loshuPlanes.emotionalPlane),
                content = getPlaneMessage("heart_plane", loshuPlanes.emotionalPlane, jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.emotional_plane_background,
                headerColor = R.color.emotional_plane_header,
                isExpanded = false
            )
        )

        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Practical Plane(8, 1, 6)",
                getPresentNumbers(listOf(8, 1, 6), loshuPlanes.practicalPlane),
                content = getPlaneMessage("practical_plane", loshuPlanes.practicalPlane, jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.practical_plane_background,
                headerColor = R.color.practical_plane_header,
                isExpanded = false
            )
        )
        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Thought Plane(4, 3, 8)",
                getPresentNumbers(listOf(4, 3, 8), loshuPlanes.thoughtPlane),
                content = getPlaneMessage("vision_plane", loshuPlanes.thoughtPlane,jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.thought_plane_background,
                headerColor = R.color.thought_plane_header,
                isExpanded = false
            )
        )
        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Will Plane(9, 5, 1)",
                getPresentNumbers(listOf(9, 5, 1), loshuPlanes.willPlane),
                content = getPlaneMessage("will_plane", loshuPlanes.willPlane, jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.will_plane_background,
                headerColor = R.color.will_plane_header,
                isExpanded = false
            )
        )
        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Action Plane(2, 7, 6)",
                getPresentNumbers(listOf(2, 7, 6), loshuPlanes.actionPlane),
                content = getPlaneMessage("action_plane", loshuPlanes.actionPlane, jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.action_plane_background,
                headerColor = R.color.action_plane_header,
                isExpanded = false
            )
        )
        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Silver Success Plane(4, 5, 6)",
                getPresentNumbers(listOf(4, 5, 6), loshuPlanes.silverSuccessPlane),
                content = getPlaneMessage("silver_success_plane", loshuPlanes.silverSuccessPlane, jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.silver_success_plane_background,
                headerColor = R.color.silver_success_plane_header,
                isExpanded = false
            )
        )

        loshuPlaneItems.add(
            LoshuGridPlaneAccordionItem(
                "Golden Success Plane(2, 5, 8)",
                getPresentNumbers(listOf(2, 5, 8), loshuPlanes.goldenSuccessPlane),
                content = getPlaneMessage("golden_success_plane", loshuPlanes.goldenSuccessPlane, jsonArray),
                imageSource = "ic_moon",
                backgroundColor = R.drawable.golden_success_plane_background,
                headerColor = R.color.golden_success_plane_header,
                isExpanded = false
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

    private fun setupRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView, gridItems: MutableList<LoshuGridPlaneAccordionItem>) {
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

        return when (item.itemId) {
            R.id.action_toggle_planes -> {
                if (!planeVisibility) binding.planeRecyclerView.visibility = View.VISIBLE
                true
            }
            R.id.action_toggle_missing_number -> {
                if (!missingVisibility) binding.missingNumberRecyclerView.visibility = View.VISIBLE
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