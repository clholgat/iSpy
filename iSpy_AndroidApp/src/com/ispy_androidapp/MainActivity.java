package com.ispy_androidapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
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
        
        
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	
    	 Intent myIntent = new Intent(v.getContext(),CreateViewGame.class);
         startActivityForResult(myIntent, 0);
    }
    
}