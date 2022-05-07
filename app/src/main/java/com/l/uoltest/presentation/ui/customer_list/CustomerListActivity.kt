package com.l.uoltest.presentation.ui.customer_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.l.uoltest.R
import com.l.uoltest.data.model.Customer
import com.l.uoltest.data.model.Result
import com.l.uoltest.databinding.ActivityCustomerListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerListBinding

    private val viewModel: CustomerListViewModel by viewModels()

    private val adapterCustomer: CustomersAdapter by lazy {
        CustomersAdapter(
            requestManager = Glide.with(this@CustomerListActivity),
            onCustomerClick = ::onCustomerClick
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecycler()
        setObservers()
    }

    private fun setRecycler() {
        binding.recyclerCustomers.run {
            layoutManager = LinearLayoutManager(
                this@CustomerListActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = adapterCustomer
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.customer.collect { result ->
                    when (result) {
                        is Result.Loading -> {}
                        is Result.Success -> {
                            adapterCustomer.submitList(result.data)
                        }
                        is Result.Error -> {
                            Toast.makeText(this@CustomerListActivity, "${result.getError()}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun onCustomerClick(customer: Customer) {

    }
}