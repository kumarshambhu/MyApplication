package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shambhu.myapplication.R
import com.shambhu.myapplication.adapter.LoshuGridPlaneRecyclerViewAdapter
import com.shambhu.myapplication.databinding.FragmentLoshuGridBinding
import com.shambhu.myapplication.model.LoshuGridPlaneAccordionItem
import com.shambhu.myapplication.model.LoshuGridPlanes
import com.shambhu.myapplication.model.Plane
import com.shambhu.myapplication.model.Section
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import com.shambhu.myapplication.utils.NumerologyCalculationUtils.convertToHtml
import org.json.JSONArray
import org.json.JSONObject

class LoshuGridFragment : Fragment() {

    private var _binding: FragmentLoshuGridBinding? = null
    private val binding get() = _binding!!

    private lateinit var gridPlaneRecyclerViewAdapter: LoshuGridPlaneRecyclerViewAdapter

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

            try {
                createLoshuPlaneItemForRecyclerView(loshuPlanes)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error, e.g., show a toast or log
            }
        }
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
                headerColor = R.drawable.mental_plane_background,
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
                headerColor = R.drawable.emotional_plane_background,
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
                headerColor = R.drawable.practical_plane_background,
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
                headerColor = R.drawable.thought_plane_background,
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
                headerColor = R.drawable.will_plane_background,
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
                headerColor = R.drawable.action_plane_background,
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
                headerColor = R.drawable.silver_success_plane_background,
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
                headerColor = R.drawable.golden_success_plane_background,
                isExpanded = false
            )
        )
        println(loshuPlaneItems)
        setupCoreNumberRecyclerView(loshuPlaneItems)
    }

    private fun updateCell(textView: TextView, number: Int, count: Int) {
        if (count > 0) {
            textView.text = number.toString().repeat(count)
        } else {
            textView.text = ""
        }
    }

    private fun LoshuGridFragment.setupCoreNumberRecyclerView(gridItems: MutableList<LoshuGridPlaneAccordionItem>) {
        var expandedPosition = -1
        gridPlaneRecyclerViewAdapter =
            LoshuGridPlaneRecyclerViewAdapter(gridItems, this.requireContext()) { position ->
                val previousExpandedPosition = expandedPosition
                if (expandedPosition == position) {
                    // Clicked on the already expanded item, so collapse it
                    gridItems[position].isExpanded = false
                    gridPlaneRecyclerViewAdapter.notifyItemChanged(position)
                    expandedPosition = -1
                } else {
                    // A new item is clicked
                    if (previousExpandedPosition != -1) {
                        // Collapse the previously expanded item
                        gridItems[previousExpandedPosition].isExpanded = false
                        gridPlaneRecyclerViewAdapter.notifyItemChanged(previousExpandedPosition)
                    }
                    // Expand the new item
                    gridItems[position].isExpanded = true
                    gridPlaneRecyclerViewAdapter.notifyItemChanged(position)
                    expandedPosition = position
                }
            }
        binding.planeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.planeRecyclerView.adapter = gridPlaneRecyclerViewAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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