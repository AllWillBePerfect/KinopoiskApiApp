package com.example.kinopoiskapiapp.core.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseFragment<T : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> T) :
    Fragment() {

    private var _binding: T? = null
    val binding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun setupAppbar(toolBar: MaterialToolbar, title: String) {
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        val toolbar = getToolbar()
        toolbar?.title = title
    }

    fun setupNullAppbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(null)
    }

    fun getToolbar(): ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
}