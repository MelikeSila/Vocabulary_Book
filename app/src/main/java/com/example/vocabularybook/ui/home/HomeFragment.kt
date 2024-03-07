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
import android.widget.ImageView
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
            flip(word_text_view)
        }

        // load data

        loadData(root)

        // save data
        val saveBtn = root.findViewById<Button>(R.id.save_button)
        saveBtn.setOnClickListener{
            saveData(root)
        }

        val flipBtn = root.findViewById<Button>(R.id.flip_button)
        flipBtn.setOnClickListener {
            flip(word_text_view)
        }
        var is_saved = false
        val starBtn = root.findViewById<ImageView>(R.id.imageView2)
        starBtn.setOnClickListener {
            //btn_star_big_on
            if (is_saved){
                starBtn.setImageResource(com.example.vocabularybook.R.drawable.ic_star_off)
                is_saved = false
            }else {
                starBtn.setImageResource(R.drawable.ic_star_on)
                is_saved = true
            }

        }

        return root
    }

    private fun flip(textView: TextView){
        val anim_1 = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 0f)
        val anim_2 = ObjectAnimator.ofFloat(textView, "scaleX", 0f, 1f)

        anim_1.interpolator = DecelerateInterpolator()
        anim_2.interpolator = AccelerateInterpolator()

        anim_1.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if ( textView.text == "Front"){
                    textView.text = "Back"
                    anim_2.start()
                }else{
                    textView.text = "Front"
                    anim_2.start()
                }
            }
        })
        anim_1.start()
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