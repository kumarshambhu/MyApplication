package com.shambhu.myapplication.fragment.others


// NumerologyCalculatorFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.shambhu.myapplication.adapter.CommonAdapterUtil
import com.shambhu.myapplication.databinding.FragmentMaturityBinding
import com.shambhu.myapplication.model.CoreNumberAccordionItem
import com.shambhu.myapplication.model.MaturityNumbersResponse
import com.shambhu.myapplication.utils.CommonUtils
import com.shambhu.myapplication.utils.Constants.Companion.ARG_DOB
import com.shambhu.myapplication.utils.Constants.Companion.ARG_FULL_NAME
import com.shambhu.myapplication.utils.NumerologyCalculationUtils

class MaturityFragment : Fragment() {
    private var _binding: FragmentMaturityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMaturityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val dob = it.getString(ARG_DOB)
            val fullName = it.getString(ARG_FULL_NAME)
            calculateNumerology(dob.toString(), fullName.toString())
        }
    }

    private fun calculateNumerology(dob: String, name: String) {
        try {
            // Calculate numbers
            val (day, month, year) = CommonUtils.parseDateTriple(dob)
            val lifePath = NumerologyCalculationUtils.calculateLifePath(day, month, year)
            val destiny = NumerologyCalculationUtils.calculateExpression(name)
            val maturity = NumerologyCalculationUtils.calculateMaturityNumber(lifePath, destiny)

            // Display basic results
            binding.tvLifePath.text = "Life Path Number: $lifePath"
            binding.tvDestiny.text = "Destiny Number: $destiny"
            binding.maturityHeader.text = "Maturity Number: $maturity"

            // Get and display maturity data from JSON
            val data = Gson().fromJson(
                CommonUtils.readAssetFile(requireContext(), "maturity.json"),
                MaturityNumbersResponse::class.java
            )
            val data1 = data.maturity_numbers
            val data2 = data1.get(maturity.toString())

            data2?.let {
                binding.tvOverview.text = it.overview

                if (!it.positive_traits.isNullOrEmpty()) {
                    CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                        requireContext(),
                        binding.positiveTraitsRecyclerView, it.positive_traits
                    )
                } else {
                    binding.positiveTraitsRecyclerView.visibility = View.GONE
                    binding.tvPositiveTraits.visibility = View.GONE
                }


                if (!it.challenges.isNullOrEmpty()) {
                    CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                        requireContext(),
                        binding.challengesRecyclerView, it.challenges
                    )
                } else {
                    binding.challengesRecyclerView.visibility = View.GONE
                    binding.tvChallenges.visibility = View.GONE
                }


                // Show additional info based on what's available
                val additionalInfoList = mutableListOf<String>();

                if (!it.karmic_notes.isNullOrEmpty()) additionalInfoList.add("Karmic Notes: ${it.karmic_notes}")
                if (!it.life_purpose.isNullOrEmpty()) additionalInfoList.add("Life Purpose: ${it.life_purpose}")
                if (!it.life_outcome.isNullOrEmpty()) additionalInfoList.add("Life Outcome: ${it.life_outcome}")

                if (additionalInfoList.isNotEmpty()) {
                    CommonAdapterUtil.setupNumberBulletRecyclerViewAdapter(
                        requireContext(),
                        binding.additionalInfoRecyclerView, it.challenges
                    )
                } else {
                    binding.additionalInfoRecyclerView.visibility = View.GONE
                    binding.tvAdditionalInfo.visibility = View.GONE
                }

            }

            // Show results
            binding.resultsContainer.visibility = View.VISIBLE

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "Error in calculation: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newInstance(dob: String, fullName: String): MaturityFragment {
            val fragment = MaturityFragment()
            val args = Bundle()
            args.putString(ARG_DOB, dob)
            args.putString(ARG_FULL_NAME, fullName)
            fragment.arguments = args
            return fragment
        }
    }
}