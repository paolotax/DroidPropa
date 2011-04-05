package com.tax.droidpropa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

public class DetailAppunto extends Activity {
	
	AppuntiHelper appHelper=null;
	String appuntoId=null;
	
	AutoCompleteTextView editScuola;
	EditText editDestinatario;
	EditText editTelefono;
	EditText editNote;

	String[] arrScadenze = { "Giorno/Tempo specifici", "Oggi", "Domani", 
							 "Dopodomani", "Prossima settimana", "Nessun Termine" };
	ArrayList<String> listScadenze;
	Spinner spinScadenza;
	String mScadenza;
	ArrayAdapter<String> adapter;
	SimpleDateFormat fmtData =  new SimpleDateFormat("E, d MMM");
	Calendar data = Calendar.getInstance();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.detail_appunto);
        appHelper=new AppuntiHelper(this);
        
        editDestinatario=(EditText)findViewById(R.id.destinatario);
		editNote=(EditText)findViewById(R.id.note);
		editTelefono=(EditText)findViewById(R.id.telefono);
		
		
		// и proprio necessario questo cursore che cми gia nel filter???
        Cursor contactsCursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, "DISPLAY_NAME");
        ContactsAutoCompleteCursorAdapter contactsAdapter = new ContactsAutoCompleteCursorAdapter(this, contactsCursor);
        
        editScuola=(AutoCompleteTextView)findViewById(R.id.scuola);
        editScuola.setOnItemClickListener(new OnItemClickListener() {
			
        	public void onItemClick(AdapterView<?> list, View view, int position,
					long id) {
				
				Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
								                         null, 
											      		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
											      		 new String[]{String.valueOf(id)}, null);
	
				if (pCur.getCount() > 0) {
					pCur.moveToFirst(); 
						
					final String tel = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String oldTel = editTelefono.getText().toString();
					Log.v("OldTel", oldTel);
					
					if (oldTel != null || oldTel != "" || oldTel != tel) {
						
						Log.v("Alert", oldTel + " " + tel);
						
						AlertDialog.Builder builder = new AlertDialog.Builder(DetailAppunto.this);
						builder.setMessage("Sostituisco il numero \n" + oldTel + " con \n" + tel + " ?")
							   .setCancelable(false)
							   .setPositiveButton("Si", new OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										editTelefono.setText(tel);
									}
							   })
							   .setNegativeButton("No", new OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
							   })
							   .show();
						
					} else { 
						editTelefono.setText(tel); 
					}
					
				} else {
					editTelefono.setText("");
				}
			
				pCur.close();
			}
        });
        editScuola.setAdapter(contactsAdapter);
        editScuola.setHint("inserisci Contatto");
        
        
        
        
		listScadenze = new ArrayList<String>();
		for (String s : arrScadenze) {
			listScadenze.add(s);		    
		};

		spinScadenza = (Spinner)findViewById(R.id.spinner);
		
		adapter = new ArrayAdapter<String>(DetailAppunto.this, android.R.layout.simple_spinner_item, listScadenze);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinScadenza.setAdapter(adapter);
		spinScadenza.setSelection(adapter.getPosition("Nessun Termine"));
		
		Button salva = (Button)findViewById(R.id.salva);
		salva.setOnClickListener(onSave);
		
		Button annulla = (Button)findViewById(R.id.annnulla);
		annulla.setOnClickListener(onClose);
		
		appuntoId=getIntent().getStringExtra(AppuntiLista.ID_EXTRA);
		
		if (appuntoId!=null) {
			load();
		}
		else {
			
			String tNome = getIntent().getStringExtra(CallLogLista.NOME);
			if (tNome != null) {
				if (tNome.startsWith("E ") || tNome.startsWith("D ") || tNome.startsWith("IC ") || tNome.startsWith("C ")) {
					editScuola.setText(getIntent().getStringExtra(CallLogLista.NOME));
				}
				else {
					editDestinatario.setText(getIntent().getStringExtra(CallLogLista.NOME));
				}
			}
			editTelefono.setText(getIntent().getStringExtra(CallLogLista.TELEFONO));
		}
		
		spinScadenza.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				String s = spinScadenza.getSelectedItem().toString();
				if (s == "Giorno/Tempo specifici") {
					new DatePickerDialog(DetailAppunto.this,
										d,
										data.get(Calendar.YEAR),
										data.get(Calendar.MONTH),
										data.get(Calendar.DAY_OF_MONTH)).show();
				}
			}
		
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//updateLabel();
	}
    
	private String convertiScadenza(String s) {
		
		if (s == "Oggi") {
			
			String oggi = fmtData.format(data.getTime());
			return oggi;
			
		} else if (s == "Domani") {
			
			Calendar cTomorrow = Calendar.getInstance();
			cTomorrow.add(Calendar.DAY_OF_MONTH, 1);
			String domani = fmtData.format(cTomorrow.getTime());
			return domani;
			
		} else if (s == "Dopodomani") {
			
			Calendar cDopoTomorrow = Calendar.getInstance();
			cDopoTomorrow.add(Calendar.DAY_OF_MONTH, 2);
			String dd = fmtData.format(cDopoTomorrow.getTime());
			return dd;
			
		} else if (s == "Prossima settimana") {

			Calendar c = Calendar.getInstance();
			Integer i = c.get(Calendar.DAY_OF_MONTH);
			Integer d = c.get(Calendar.DAY_OF_WEEK);
			c.set(Calendar.DAY_OF_MONTH, i + 9 - d);
			String nw = fmtData.format(c.getTime());
			return nw;

		} else if (s == "Nessun Termine" || s == "Giorno/Tempo specifici") {
			return null;
		} else return s;
	}
	
    @Override
	public void onDestroy() {
		super.onDestroy();
	
		appHelper.close();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putString("scuola",       editScuola.getText().toString());
		state.putString("destinatario", editDestinatario.getText().toString());
		state.putString("telefono",     editTelefono.getText().toString());
		state.putString("note",         editNote.getText().toString());
		state.putString("scadenza",     spinScadenza.getSelectedItem().toString());
	}	
		
	@Override
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		editScuola.setText(state.getString("scuola"));
		editDestinatario.setText(state.getString("destinatario"));
		editTelefono.setText(state.getString("telefono"));
		editNote.setText(state.getString("note"));
		//scadenza.setText(spinner.getSelectedItem()("telefono"));
	}
	
	private void load() {
		Cursor c=appHelper.getById(appuntoId);

		c.moveToFirst();		
		editScuola.setText(appHelper.getScuola(c));
		editDestinatario.setText(appHelper.getDestinatario(c));
		editTelefono.setText(appHelper.getTelefono(c));
		editNote.setText(appHelper.getNote(c));
		if (appHelper.getScadenza(c) != null &&  appHelper.getScadenza(c) != "")  {
			insertScadenza(appHelper.getScadenza(c));
		}
		
		
//		if (helper.getType(c).equals("sit_down")) {
//			types.check(R.id.sit_down);
//		}
//		else if (helper.getType(c).equals("take_out")) {
//			types.check(R.id.take_out);
//		}
//		else {
//			types.check(R.id.delivery);
//		}
		c.close();
	}
	
	private View.OnClickListener onSave=new View.OnClickListener() {
		
		public void onClick(View v) {
			
			if (appuntoId==null) {
				appHelper.insert(editScuola.getText().toString(),
							  null,
							  null,
							  editDestinatario.getText().toString(), 
							  editNote.getText().toString(),
							  editTelefono.getText().toString(),
							  null,
							  convertiScadenza(spinScadenza.getSelectedItem().toString()),
							  null,
							  null);
			}
			else {
				appHelper.update(appuntoId, editScuola.getText().toString(),
						  null,
						  null,
						  editDestinatario.getText().toString(), 
						  editNote.getText().toString(),
						  editTelefono.getText().toString(),
						  null,
						  convertiScadenza(spinScadenza.getSelectedItem().toString()),
						  null,
						  null);
			}
			
			finish();
		}
	};
	
	private View.OnClickListener onClose=new View.OnClickListener() {
		
		public void onClick(View v) {
			
			finish();
		}
	};


	
	
	
	
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
			data.set(Calendar.YEAR, year);
			data.set(Calendar.MONTH, monthOfYear);
			data.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			new TimePickerDialog(DetailAppunto.this,
					t,
					data.get(Calendar.HOUR_OF_DAY),
					data.get(Calendar.MINUTE),
					true).show();
//			updateLabel();
//			insertData();
		}
	};
	
	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			data.set(Calendar.HOUR_OF_DAY, hourOfDay);
			data.set(Calendar.MINUTE, minute);
			
			String scadenza = fmtData.format(data.getTime());
			insertScadenza(scadenza);
		}
	};
	
	private void insertScadenza(String scadenza) {
		
		int p = adapter.getPosition("Giorno/Tempo specifici");
		if (p==1) {
			adapter.remove(adapter.getItem(0));
		}
		adapter.insert(scadenza, 0);
		adapter.notifyDataSetChanged();
		spinScadenza.setSelection(0);
	}	

	private void updateScadenza() {
		//dateAndTimeLabel.setText(fmtDateAndTime.format(dateAndTime.getTime()));
	}
	
}