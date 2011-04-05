package com.tax.droidpropa;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CallLogAdapter extends CursorAdapter {

    private final LayoutInflater mInflater;

    public CallLogAdapter(Context context, Cursor cursor) {
      super(context, cursor, true);

      mInflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

      final String nume;
    	
      TextView nome = (TextView) view.findViewById(R.id.nome);
      nome.setText(cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)));

      TextView numero = (TextView) view.findViewById(R.id.numero);
      numero.setText(cursor.getString(cursor.getColumnIndex(Calls.NUMBER)));
            
      // che cosa solno le variabili final
      nume = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
      
      ImageView icon = (ImageView) view.findViewById(R.id.tipo);
      int type = cursor.getInt(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
      switch (type) {
      	case android.provider.CallLog.Calls.MISSED_TYPE: 
      		icon.setImageResource(android.R.drawable.sym_call_missed);
      		break;
      	case android.provider.CallLog.Calls.INCOMING_TYPE: 
      		icon.setImageResource(android.R.drawable.sym_call_incoming);
      		break;
      	case android.provider.CallLog.Calls.OUTGOING_TYPE: 
      		icon.setImageResource(android.R.drawable.sym_call_outgoing);
      		break;
      }
      icon.setOnClickListener(new OnClickListener(){  
			
			public void onClick(View v) {
				
				//Log.v("TaxLog", v.toString());
				String phoneId =  nume;
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + phoneId));
			    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    v.getContext().startActivity(intent);

			}
		});
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

      final View view = mInflater.inflate(R.layout.row_calllog, parent, false);
      return view;

    }
}
