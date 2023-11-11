package com.yj.hydro_yan

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yj.hydro_yan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        this.binding.btnCalculate.setOnClickListener(this)
        this.binding.btnReset.setOnClickListener(this)

        binding.llResults.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_calculate -> {
                this.calculator()
            }
            R.id.btn_reset -> {
                this.resetAll()
            }
        }
    }

    private fun calculator(){

        var output = ""

//        Get data from UI
        val morningUsageInput = binding.etMorningUsage.text.toString()
        val eveningUsageInput = binding.etEveningUsage.text.toString()
        val isRenewable = binding.swIsRenewable.isChecked
        val morningUsage = morningUsageInput.toDoubleOrNull()
        val eveningUsage = eveningUsageInput.toDoubleOrNull()

//        Error handling for input fields
        if (morningUsageInput.isEmpty() || morningUsage == null || morningUsage < 0){
            binding.etMorningUsage.error = "Invalid input! Please enter a positive number."
            return
        } else if (eveningUsageInput.isEmpty() || eveningUsage == null ||eveningUsage < 0){
            binding.etEveningUsage.error = "Invalid input! Please enter a positive number."
            return
        }

//        Calculate results
        val morningUsageCost =  morningUsage * 0.132
        val eveningUsageCost = eveningUsage * 0.094
        val totalUsageCharge = morningUsageCost + eveningUsageCost
        val rebate = if (isRenewable) (totalUsageCharge * 0.09) else 0.0
        val subtotal = totalUsageCharge - rebate
        val tax = subtotal * 0.13
        val total = subtotal + tax

//        Print results
        output = buildString {
            append("Morning usage charge: $${formatDouble(morningUsageCost)}\n")
            append("Evening usage charge: $${formatDouble(eveningUsageCost)}\n")
            append("Total usage charge: $${formatDouble(totalUsageCharge)}\n")
            append("Environmental rebate: -$${formatDouble(rebate)}\n")
            append("--------------------------------\n")
            append("Subtotal: $${formatDouble(subtotal)}\n")
            append("Tax: $${formatDouble(tax)}\n")
        }
        binding.apply {
            tvChargeBreakdown.text = output
            tvYouMustPay.text = "YOU MUST PAY: $${formatDouble(total)}"
            llResults.visibility = View.VISIBLE
        }
    }

    private fun resetAll(){
        binding.apply {
            tvChargeBreakdown.text = ""
            etMorningUsage.text.clear()
            etEveningUsage.text.clear()
            swIsRenewable.isChecked = false
            llResults.visibility = View.GONE
        }
    }

    private fun formatDouble(value: Double): Double {
        return String.format("%.2f", value).toDouble()
    }
}