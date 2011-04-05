package com.tax.droidpropa;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;



public class CallLogLista extends ListActivity {
	
	public final static String NOME = "com.tax.droidpropa.NOME";
	public final static String TELEFONO = "com.tax.droidpropa.TELEFONO";
	
	
	private ListAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_calllog);
		
		Cursor c = managedQuery(
		           android.provider.CallLog.Calls.CONTENT_URI,
		           null, 
		           null, //"TYPE = 1", incoming
		           null,
		           android.provider.CallLog.Calls.DATE + " DESC");    

		startManagingCursor(c);
		 
		adapter = new CallLogAdapter(this, c);
		setListAdapter(adapter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	
		//helper.close();
	}
	
	@Override
	public void onListItemClick(ListView list, View view,
			int position, long id) {

		Log.v("TaxLog", view.getClass().toString());
		newAppuntoFromCall(position);
		
	}
	
	public void makeCall(int position) {
		
		//		CHIAMATA TELEFONICA
		Cursor cursor = (Cursor) adapter.getItem(position);
        String phoneId = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
		try {
			   Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + phoneId));
			   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   startActivity(intent);
			} catch (Exception e) {
			   Log.e("DroidPropa", "errore nella chiamata", e);
		}
	}

	public void newAppuntoFromCall(int position) {
		
		Cursor cursor = (Cursor) adapter.getItem(position);
		String numero  = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
		String nome    = cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME));
		
		
		Intent intent = new Intent(CallLogLista.this, DetailAppunto.class);
		intent.putExtra(TELEFONO, numero);
		intent.putExtra(NOME, nome);
		
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(intent);
	}
}
	
	
	
	
