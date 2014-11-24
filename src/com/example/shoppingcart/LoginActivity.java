package com.example.shoppingcart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.shoppingcart.storage.DBHelper;

public class LoginActivity extends ActionBarActivity 
{
    private Button loginButton;
	private EditText userIdEditText;
	private EditText passwordEditText;

	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().hide();
        final DBHelper dbHelper = new DBHelper(this);
        
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        
        loginButton = (Button)findViewById(R.id.buttonLogin);
        userIdEditText = (EditText)findViewById(R.id.emailIdEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				if(userIdEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
					alertDialog.setTitle("Login Failed");
					alertDialog.setMessage("UserName/Password cannot be blank.");
					alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg1) 
						{
							dialog.cancel();
						}
					});
					alertDialog.show();
				}
				else
				{
				String result = dbHelper.validateUser(userIdEditText.getText().toString(), passwordEditText.getText().toString());
				if(result.equalsIgnoreCase("User exists"))
				{
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra("Open", "");
					startActivity(intent);
					LoginActivity.this.finish();
				}
				else
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
					alertDialog.setTitle("Login Failed");
					alertDialog.setMessage("User does not  exists please register first.");
					alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int arg1) 
						{
							dialog.cancel();
						}
					});
					alertDialog.show();
				}
			}
			}
		});
        TextView registerTextView = (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();	
			}
		});
        
    }	
	
	private void buildAlertMessageNoGps() 
	{
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
               .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                   }
               });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}