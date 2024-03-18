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

        // load data

        loadData(root)

        // save data
        val saveBtn = root.findViewById<Button>(R.id.save_button)
        saveBtn.setOnClickListener{
            saveData(root)
        }

        /// Flip the card
        val word_text_view = root.findViewById<TextView>(R.id.word_text_view)
        word_text_view.setOnClickListener{
            var currentText = setNewText(word_text_view.text)
            flip(word_text_view, currentText)
        }

        // Flip button
        val flipBtn = root.findViewById<Button>(R.id.flip_button)
        flipBtn.setOnClickListener {
            var currentText = ""
            if ( word_text_view.text == "Front"){
                currentText = "Back"
            }else if ( word_text_view.text == "Back"){
                currentText = "Front"
            }else if ( word_text_view.text == "New Word"){
                currentText = "Another New Word"
            }else{
                currentText = "New Word"
            }
            flip(word_text_view, currentText)
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        val offIcon = R.drawable.ic_star_off
        val onIcon = R.drawable.ic_star_on
        var currentIcon =  sharedPref?.getInt("selected_icon_resource_id", offIcon);

        // Star button
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

        // Next button
        val nextBtn = root.findViewById<Button>(R.id.button2)
        nextBtn.setOnClickListener {
            var currentText = ""
            if ( word_text_view.text == "Front" || word_text_view.text == "Back"){
                currentText = "New Word"
            }else{
                currentText = "Front"
            }
            flip(word_text_view, currentText)
        }


        return root
    }

    private fun setNewText(controlText: CharSequence):String{
        var currentText = ""
        if ( controlText == "Front"){
            currentText = "Back"
        }else if ( controlText == "Back"){
            currentText = "Front"
        }else if ( controlText == "New Word"){
            currentText = "Another New Word"
        }else{
            currentText = "New Word"
        }
        return currentText
    }

    private fun flip(textView: TextView, current_text:String){
        val anim_1 = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 0f)
        val anim_2 = ObjectAnimator.ofFloat(textView, "scaleX", 0f, 1f)

        anim_1.interpolator = DecelerateInterpolator()
        anim_2.interpolator = AccelerateInterpolator()

        anim_1.addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                textView.text = current_text
                anim_2.start()
            }
        })
        anim_1.start()
    }

    private fun loadData(root: View) {
        val sharedPreferences = activity?.getSharedPreferences("EnglishSharedPreferences", Context.MODE_PRIVATE)
        val savedString = sharedPreferences?.getString("STRING_KEY", null)
        val lastWord = sharedPreferences?.getString("LAST_WORD", null)

        root.findViewById<TextView>(R.id.textView).text = savedString
        root.findViewById<TextView>(R.id.word_text_view).text = lastWord

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

    private fun saveLastWord(root: View){
        val lastWord = root.findViewById<TextView>(R.id.word_text_view).text.toString()

        val sharedPreferences = activity?.getSharedPreferences("EnglishSharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            putString("LAST_WORD", lastWord)
            //putBoolean("BOOLEAN_KEY", )
        }?.apply()

        Toast.makeText(this.context, "Last word saved!", Toast.LENGTH_SHORT).show()
    }

    private fun favoriteUpdate(root: View, newIcon: Int, sharedPref: SharedPreferences ){
        if (sharedPref != null) {
            with (sharedPref?.edit()) {
                this?.putInt("selected_icon_resource_id", newIcon)
                this?.apply()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveLastWord(binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}