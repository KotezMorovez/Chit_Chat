package com.example.chit_chat.ui.main

import com.example.chit_chat.data.service.ApiServiceImpl
import com.example.chit_chat.ui.common.BaseFragment
import com.example.chit_chat.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun createViewBinding(): FragmentMainBinding {
        return FragmentMainBinding.inflate(layoutInflater)
    }

    override fun initUi() {
        testAPI()
    }

    override fun observeData() {
    }

    private fun testAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = ApiServiceImpl().testRequest()
            if (result.isSuccess){
                withContext(Dispatchers.Main) {
                    viewBinding.test.text = result.getOrNull().toString()
                }

            } else {
                withContext(Dispatchers.Main) {
                    viewBinding.test.text = result.exceptionOrNull().toString()
                }
            }
        }
    }
}
