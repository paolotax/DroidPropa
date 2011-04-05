package com.tax.droidpropa;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class AppuntiLista extends ListActivity {
	
	public final static String ID_EXTRA="com.tax.droidpropa._ID";
	
	public static final int MENU_ELIMINA = 1;
	public static final int MENU_DAFARE  = 2;
	public static final int MENU_FATTO   = 3;
	public static final int MENU_SOSPESO = 4;
	
	Cursor model=null;
	AppuntiAdapter adapter=null;
	AppuntiHelper helper=null;
	SharedPreferences prefs=null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		helper=new AppuntiHelper(this);
		prefs=PreferenceManager.getDefaultSharedPreferences(this);
		

		initList();
		prefs.registerOnSharedPreferenceChangeListener(prefListener);
		registerForContextMenu(getListView());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		helper.close();
	}
	
	@Override
	public void onListItemClick(ListView list, View view,
															int position, long id) {
		
		Intent i=new Intent(AppuntiLista.this, DetailAppunto.class);

		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
	}
	
	private void initList() {
		if (model!=null) {
			stopManagingCursor(model);
			model.close();
		}
		
		String where=null;

		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			where="(scuola LIKE \"%"+getIntent().getStringExtra(SearchManager.QUERY)+"%\")" +
					" OR (destinatario LIKE \"%"+getIntent().getStringExtra(SearchManager.QUERY)+"%\")";
		}
		
		model=helper.getAll(where, prefs.getString("sort_order", "scuola"));
		startManagingCursor(model);
		
		adapter=new AppuntiAdapter(model);
		setListAdapter(adapter);
	}
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener=
		 new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences sharedPrefs,
																						String key) {
				if (key.equals("sort_order")) {
					initList();
				}
			}
		};
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);

		return(super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId()==R.id.nuovo) {
			startActivity(new Intent(AppuntiLista.this, DetailAppunto.class));
			
			return(true);
		}
		else if (item.getItemId()==R.id.opzioni) {
			//startActivity(new Intent(this, EditPreferences.class));
			Intent i = new Intent(AppuntiLista.this, CallLogLista.class);
			
			startActivity(i);
			return(true);
		}
		else if (item.getItemId()==R.id.cerca) {
			
			//onSearchRequested();
			AppuntiExport ae = new AppuntiExport(this, helper.getReadableDatabase());
			ae.exportData();
			
			return(true);
		}

		return(super.onOptionsItemSelected(item));
	}
	
	class AppuntiAdapter extends CursorAdapter {
		
		AppuntiAdapter(Cursor c) {
			super(AppuntiLista.this, c);
		}
		
		@Override
		public void bindView(View row, Context ctxt,
												 Cursor c) {
			AppuntiHolder holder=(AppuntiHolder)row.getTag();
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c,
												 ViewGroup parent) {
			LayoutInflater inflater=getLayoutInflater();
			View row=inflater.inflate(R.layout.row, parent, false);
			AppuntiHolder holder=new AppuntiHolder(row);
			
			row.setTag(holder);
			
			return(row);
		}
	}
	
	static class AppuntiHolder {
		private TextView scuola=null;
		private TextView destinatario=null;
		private TextView note=null;
		private TextView scadenza=null;
		private ImageView icon=null;
		private View row=null;
		
		AppuntiHolder(View row) {
			this.row=row;
			
			scuola=(TextView)row.findViewById(R.id.scuola);
			destinatario=(TextView)row.findViewById(R.id.destinatario);
			note=(TextView)row.findViewById(R.id.note);
			scadenza=(TextView)row.findViewById(R.id.scadenza);
			icon=(ImageView)row.findViewById(R.id.icon);
		}
		
		void populateFrom(Cursor c, AppuntiHelper helper) {
			
			scuola.setText(helper.getScuola(c));
			destinatario.setText(helper.getDestinatario(c));
			note.setText(helper.getNote(c));
			scadenza.setText(helper.getScadenza(c));
			//icon.setImageResource(R.drawable.ball_red);
			
			if (helper.getStato(c) != null) {
				
				if (helper.getStato(c).equals("X")) {
					icon.setImageResource(android.R.drawable.ic_menu_agenda);
				}
				else if (helper.getStato(c).equals("P")) {
					icon.setImageResource(R.drawable.ball_yellow);
				}
				else if (helper.getStato(c).equals("")) {
					icon.setImageResource(R.drawable.ic_null);
				}
			}
			else {
				icon.setImageResource(R.drawable.ic_null);
			}
				
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
																		ContextMenu.ContextMenuInfo menuInfo) {
		populateMenu(menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		return(applyMenuChoice(item) ||
	
				super.onContextItemSelected(item));
	}
	
	private void populateMenu(Menu menu) {
		menu.add(Menu.NONE,  MENU_ELIMINA, Menu.NONE, "elimina");
		menu.add(Menu.FIRST, MENU_DAFARE,  Menu.NONE, "da fare");
		menu.add(Menu.FIRST, MENU_FATTO,   Menu.NONE, "fatto");
		menu.add(Menu.FIRST, MENU_SOSPESO, Menu.NONE, "sospeso");
	}
	
	private boolean applyMenuChoice(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		final String bookId = String.valueOf(info.id);
		
		switch (item.getItemId()) {
			case MENU_ELIMINA:
				
				new AlertDialog.Builder(this)
					.setTitle("Elimina appunto")
					.setMessage("Confermi eliminazione?")
					.setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dlg, int sumthin) {
							
							helper.delete(bookId);
							model.requery();
						}
					})
					.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dlg, int id) {
							//  Action for 'NO' Button
							dlg.cancel();
						}
					})
					.show();

				return(true);
		
			case MENU_DAFARE:

				helper.updateStato(bookId, "");
				model.requery();
				return(true);
		
			case MENU_FATTO:

				helper.updateStato(bookId, "X");
				model.requery();
				return(true);
		
			case MENU_SOSPESO:

				helper.updateStato(bookId, "P");
				model.requery();
				return(true);
		
		}
		
		return(false);
	}
	
	
}