package com.example.microclimates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class LoadingScreenFragment : Fragment() {
    companion object {
        fun newInstance(loadingText: String?): LoadingScreenFragment {
            val args = Bundle().apply {
                putString("loadingText", loadingText)
            }

            val fragment = LoadingScreenFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loadingText = arguments?.getString("loadingText")
        val view = inflater.inflate(R.layout.loading_screen, container, false)

        if (loadingText != null) {
            view.findViewById<TextView>(R.id.loading_screen_message)?.text = loadingText
        }

        return view
    }

    fun setMessage(newText: String): Unit {
        view?.findViewById<TextView>(R.id.loading_screen_message)?.text = newText
    }

    fun setFailed(failureMessage: String): Unit {
        setMessage(failureMessage)
        view?.findViewById<ProgressBar>(R.id.loading_screen_spinner)?.visibility = View.GONE
    }

    fun hide(fm: FragmentManager): LoadingScreenFragment {
        fm.beginTransaction().remove(this).commit()
        return this
    }

    fun show(fm: FragmentManager, containerResourceId: Int): LoadingScreenFragment {
        val tag = "LoadingScreenFragment"
        if (fm.findFragmentByTag(tag) == null) {
            fm.beginTransaction().add(containerResourceId, this, tag).commit()
        }
        return this
    }
}