package com.example.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.shoppingcart.storage.DBHelper;

public class RegistrationActivity extends Activity 
{
	private EditText userIdEditText,passwordEditText,confirmPasswordEditText;
	private Button registerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		getActionBar().hide();
		userIdEditText = (EditText)findViewById(R.id.userIdEditText);
		passwordEditText = (EditText)findViewById(R.id.passwordEditText);
		confirmPasswordEditText = (EditText)findViewById(R.id.confirmPasswordEditText);
		registerButton = (Button)findViewById(R.id.buttonRegister);
		registerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				if(passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
				{
					DBHelper dbHelper = new DBHelper(RegistrationActivity.this);
					dbHelper.addUserToTable(userIdEditText.getText().toString(), passwordEditText.getText().toString());
					Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
					intent.putExtra("Open", "");
					startActivity(intent);
					RegistrationActivity.this.finish();
				}
			}
		});
		
	}

}
