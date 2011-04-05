package com.tax.droidpropa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppuntiHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME="droidpropa.db";
	private static final int SCHEMA_VERSION=1;
	
	public AppuntiHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE appunti (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
										  "scuola TEXT, " +
										  "provincia TEXT, " +
										  "citta TEXT, " +
										  
										  "destinatario TEXT, " +
										  "note TEXT, " +
										  "telefono TEXT, " +
										  
										  "stato TEXT, " +
										  "scadenza TEXT, " +
										  "classe TEXT, " +
										  
										  "esportato TEXT" +
										  ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// no-op, since will not be called until 2nd schema
		// version exists
	}

	public Cursor getAll(String where, String orderBy) {
		StringBuilder buf=new StringBuilder("SELECT _id, " +
											"scuola, provincia, citta, " +
											"destinatario, note, telefono, " +
											"stato, scadenza, classe, " +
											"esportato " +
											"FROM appunti");
									
		if (where!=null) {
			buf.append(" WHERE ");
			buf.append(where);
		}
		
		if (orderBy!=null) {
			buf.append(" ORDER BY ");
			buf.append(orderBy);
		}
		
		return(getReadableDatabase().rawQuery(buf.toString(), null));
	}
	
	public Cursor getById(String id) {
		String[] args={id};

		return(getReadableDatabase()
						.rawQuery("SELECT _id, " +
								  "scuola, provincia, citta, " +
								  "destinatario, note, telefono, " +
								  "stato, scadenza, classe, " +
								  "esportato " +
								  "FROM appunti WHERE _ID=?",
								  args));
	}
	
	public void insert(String scuola, String provincia, String citta, 
					   String destinatario, String note, String telefono, 
					   String stato, String scadenza, String classe, 
					   String esportato) {
	
		ContentValues cv=new ContentValues();
					
		cv.put("scuola", scuola);
		cv.put("provincia", provincia);
		cv.put("citta", citta);
		
		cv.put("destinatario", destinatario);
		cv.put("note", note);
		cv.put("telefono", telefono);
		
		cv.put("stato", stato);
		cv.put("scadenza", scadenza);
		cv.put("classe", classe);
		cv.put("esportato", esportato);
		
		
		
		//getWritableDatabase().insert("appunti", "name", cv);
		getWritableDatabase().insert("appunti", "scuola", cv);
	
	}
	
	
	public void update(String id, 
					   String scuola, String provincia, String citta, 
					   String destinatario, String note, String telefono, 
					   String stato, String scadenza, String classe, 
					   String esportato) {
		
		ContentValues cv=new ContentValues();
		String[] args={id};
		
		cv.put("scuola", scuola);
		cv.put("provincia", provincia);
		cv.put("citta", citta);
		
		cv.put("destinatario", destinatario);
		cv.put("note", note);
		cv.put("telefono", telefono);
		
		cv.put("stato", stato);
		cv.put("scadenza", scadenza);
		cv.put("classe", classe);
		cv.put("esportato", esportato);
		
		getWritableDatabase().update("appunti", cv, "_ID=?", args);
	}
	
	
	public void updateStato( String id, String stato ) {

		ContentValues cv=new ContentValues();
		String[] args={id};
		cv.put("stato", stato);

		getWritableDatabase().update("appunti", cv, "_ID=?", args);
	}
	
	public void delete( String id ) {

		String[] args={id};
		getWritableDatabase().delete("appunti", "_ID=?", args);

	}
	
	
	public String getScuola(Cursor c) {
		return(c.getString(1));
	}
	
	public String getProvincia(Cursor c) {
		return(c.getString(2));
	}
	
	public String getCitta(Cursor c) {
		return(c.getString(3));
	}
	
	public String getDestinatario(Cursor c) {
		return(c.getString(4));
	}
	
	public String getNote(Cursor c) {
		return(c.getString(5));
	}
	
	public String getTelefono(Cursor c) {
		return(c.getString(6));
	}
	
	public String getStato(Cursor c) {
		return(c.getString(7));
	}
	
	public String getScadenza(Cursor c) {
		return(c.getString(8));
	}
	
	public String getClasse(Cursor c) {
		return(c.getString(9));
	}
	
	public String getEsportato(Cursor c) {
		return(c.getString(10));
	}
}


