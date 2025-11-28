package com.shambhu.myapplication.fragment.core_number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.shambhu.myapplication.databinding.FragmentLoshuGridBinding
import com.shambhu.myapplication.model.LoshuGridPlanes
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants
import com.shambhu.myapplication.utils.NumerologyCalculationUtils
import kotlin.text.iterator

class LoshuGridFragment : Fragment() {

    private var _binding: FragmentLoshuGridBinding? = null
    private val binding get() = _binding!!

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
       /* arguments?.getString(Constants.Companion.ARG_FULL_NAME)?.let { fullName ->
            val mulankNumber = NumerologyCalculationUtils.calculateBirthdayNumber(fullName)
            val destinyNumber = NumerologyCalculationUtils.calculateExpression(fullName)

            binding.tvPersonalityNumber.text = mulankNumber.toString()
            binding.tvDestinyNumber.text = destinyNumber.toString()
        }*/
        arguments?.getString(Constants.Companion.ARG_DOB)?.let { dob ->
            var digits = dob.filter { it.isDigit() }

            // Calculate numerology numbers
            val birthDate = CommonUtils.parseDate(dob)
            val birthDay = birthDate.dayOfMonth
            val birthMonth = birthDate.monthValue
            val birthYear = birthDate.year

            val mulankNumber = NumerologyCalculationUtils.calculateBirthdayNumber(birthDay)
            val bhagyankNumber = NumerologyCalculationUtils.calculateLifePath(birthDay, birthMonth, birthYear)
            val kuaNumber = NumerologyCalculationUtils.calculateKuaNumber(birthYear, true)

            binding.loshuGridDobValue.text = dob
            binding.loshuGridMulankValue.text = mulankNumber.toString()
            binding.loshuGridBhagyankValue.text = bhagyankNumber.toString()
            binding.loshuGridKuaNumberValue.text = kuaNumber.toString()

            println("DOB: $digits")
            digits = digits+ CommonUtils.reduceNumber(birthDay) +   CommonUtils.reduceNumberIgnoreMasterNumber(bhagyankNumber) + kuaNumber
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
                val jsonString = requireContext().assets.open("loshu_planes_meaning.json").bufferedReader().use { it.readText() }
                val meaningsJson = org.json.JSONObject(jsonString)
                updatePlanesUI(loshuPlanes, meaningsJson)

                val effectsJsonString = requireContext().assets.open("loshu_missing_number_effects.json").bufferedReader().use { it.readText() }
                val effectsJson = org.json.JSONObject(effectsJsonString)
                displayMissingNumberEffects(loshuPlanes, effectsJson)
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error, e.g., show a toast or log
            }
        }


    }

    private fun displayMissingNumberEffects(loshuPlanes: LoshuGridPlanes, effectsJson: org.json.JSONObject) {
        val allMissingNumbers = listOf(
            loshuPlanes.mentalPlane,
            loshuPlanes.emotionalPlane,
            loshuPlanes.practicalPlane,
            loshuPlanes.thoughtPlane,
            loshuPlanes.willPlane,
            loshuPlanes.actionPlane,
            loshuPlanes.silverSuccessPlane,
            loshuPlanes.goldenSuccessPlane
        ).flatten().distinct().sorted()

        if (allMissingNumbers.isNotEmpty()) {
            val effectsStringBuilder = StringBuilder("Effects of Missing Numbers:\n")
            for (number in allMissingNumbers) {
                effectsJson.optString(number.toString())?.let { effect ->
                    effectsStringBuilder.append("\n- $effect")
                }
            }
            binding.loshuPlaneLayout.tvMissingNumberEffects.text = effectsStringBuilder.toString()
            binding.loshuPlaneLayout.tvMissingNumberEffects.visibility = View.VISIBLE
        } else {
            binding.loshuPlaneLayout.tvMissingNumberEffects.visibility = View.GONE
        }
    }

    private fun updatePlanesUI(loshuPlanes: LoshuGridPlanes, meanings: org.json.JSONObject) {
        updatePlaneText(binding.loshuPlaneLayout.tvMentalPlane, meanings.getJSONObject("mental_plane"), loshuPlanes.mentalPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvEmotionalPlane, meanings.getJSONObject("emotional_plane"), loshuPlanes.emotionalPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvPracticalPlane, meanings.getJSONObject("practical_plane"), loshuPlanes.practicalPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvThoughtPlane, meanings.getJSONObject("thought_plane"), loshuPlanes.thoughtPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvWillPlane, meanings.getJSONObject("will_plane"), loshuPlanes.willPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvActionPlane, meanings.getJSONObject("action_plane"), loshuPlanes.actionPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvSilverSuccessPlane, meanings.getJSONObject("silver_success_plane"), loshuPlanes.silverSuccessPlane)
        updatePlaneText(binding.loshuPlaneLayout.tvGoldenSuccessPlane, meanings.getJSONObject("golden_success_plane"), loshuPlanes.goldenSuccessPlane)
    }

    private fun updatePlaneText(textView: TextView, planeMeanings: org.json.JSONObject, missingNumbers: List<Int>) {
        val message = if (missingNumbers.isEmpty()) {
            planeMeanings.getString("complete")
        } else {
            val incompleteMessage = planeMeanings.getString("incomplete")
            "$incompleteMessage Missing: ${missingNumbers.joinToString(", ")}"
        }
        textView.text = message
        textView.visibility = View.VISIBLE
    }

    private fun updateCell(textView: TextView, number: Int, count: Int) {
        if (count > 0) {
            textView.text = number.toString().repeat(count)
        } else {
            textView.text = ""
        }
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