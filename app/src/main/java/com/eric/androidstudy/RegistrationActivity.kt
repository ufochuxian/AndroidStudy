package com.eric.androidstudy

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.eric.androidstudy.viewmodel.RegistrationViewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        nameEditText = findViewById(R.id.editTextName)
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)

        // 初始化 ViewModel
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]

        // 观察 ViewModel 数据变化并更新 UI
        viewModel.name.observe(this) { name ->
            nameEditText.setText(name)
        }

        viewModel.email.observe(this) { email ->
            emailEditText.setText(email)
        }

        viewModel.password.observe(this) { password ->
            passwordEditText.setText(password)
        }

        // 更新 ViewModel 数据
        nameEditText.addTextChangedListener { text ->
            viewModel.name.value = text.toString()
        }

        emailEditText.addTextChangedListener { text ->
            viewModel.email.value = text.toString()
        }

        passwordEditText.addTextChangedListener { text ->
            viewModel.password.value = text.toString()
        }
    }
}
