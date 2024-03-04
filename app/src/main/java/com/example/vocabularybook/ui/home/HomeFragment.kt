package com.example.vocabularybook.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vocabularybook.R
import com.example.vocabularybook.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /// Flip the card
        val word_text_view = root.findViewById<TextView>(R.id.word_text_view)
        word_text_view.setOnClickListener{
            val anim_1 = ObjectAnimator.ofFloat(word_text_view, "scaleX", 1f, 0f)
            val anim_2 = ObjectAnimator.ofFloat(word_text_view, "scaleX", 0f, 1f)

            anim_1.interpolator = DecelerateInterpolator()
            anim_2.interpolator = AccelerateInterpolator()

            anim_1.addListener(object: AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    if ( word_text_view.text == "Front"){
                        word_text_view.text = "Back"
                        anim_2.start()
                    }else{
                        word_text_view.text = "Front"
                        anim_2.start()
                    }
                }
            })
            anim_1.start()
        }

        // load data

        loadData(root)

        // save data
        val save_btn = root.findViewById<Button>(R.id.save_button)
        save_btn.setOnClickListener{
            saveData(root)
        }

        return root
    }

    private fun loadData(root: View) {
        val sharedPreferences = activity?.getSharedPreferences("EnglishSharedPreferences", Context.MODE_PRIVATE)
        val savedString = sharedPreferences?.getString("STRING_KEY", null)

        root.findViewById<TextView>(R.id.textView).text = savedString

    }

    private fun saveData(root: View){
        val insertedText = root.findViewById<EditText>(R.id.textView2).text.toString()
        root.findViewById<TextView>(R.id.textView).text = insertedText

        val sharedPreferences = activity?.getSharedPreferences("EnglishSharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            putString("STRING_KEY", insertedText)
            //putBoolean("BOOLEAN_KEY", )
        }?.apply()

        Toast.makeText(this.context, "Data saved!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}