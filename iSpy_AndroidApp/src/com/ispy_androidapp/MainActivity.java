package com.ispy_androidapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends ListActivity {
	private String TAG = "MainActivity";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list);
       
        
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccountsByType("com.google");
        
        int num_accounts = accounts.length;
        
        String listAccounts[] = new String[num_accounts];
        
        for(int i = 0 ; i < num_accounts ; i++)
        {
        	listAccounts[i] = accounts[i].name;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
      		  R.layout.account_text, listAccounts);
        
        
        this.setListAdapter(arrayAdapter);
        
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(this);
        if (regId.equals("")) {
          GCMRegistrar.register(this, "carsonholgate@gmail.com");
        } else {
          Log.v(TAG, "Already registered");
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }
    
}