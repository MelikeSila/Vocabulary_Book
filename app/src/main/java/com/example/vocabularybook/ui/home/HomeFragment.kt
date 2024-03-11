package com.example.vocabularybook.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
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

        var sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE);


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
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        val offIcon = R.drawable.ic_star_off
        val onIcon = R.drawable.ic_star_on
        var currentIcon =  sharedPref?.getInt("selected_icon_resource_id", offIcon);

        val starBtn = root.findViewById<ImageView>(R.id.imageView2)
        if (currentIcon != null) {
            starBtn.setImageResource(currentIcon)
        }
        starBtn.setOnClickListener {
            //btn_star_big_on
            if (currentIcon != offIcon){

                if (sharedPref != null) {
                    favoriteUpdate(root, offIcon, sharedPref)
                }
                starBtn.setImageResource(offIcon)
                currentIcon = offIcon

            }else {

                if (sharedPref != null) {
                    favoriteUpdate(root, onIcon, sharedPref)
                }
                starBtn.setImageResource(onIcon)
                currentIcon = onIcon
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

    private fun favoriteUpdate(root: View, newIcon: Int, sharedPref: SharedPreferences ){
        if (sharedPref != null) {
            with (sharedPref?.edit()) {
                this?.putInt("selected_icon_resource_id", newIcon)
                this?.apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}