package com.tax.droidpropa;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

public class Main extends Activity implements TextWatcher {
	
	AutoCompleteTextView edit;
	
	ArrayList<String> scadenze;
	Spinner spinner;
	ArrayAdapter<String> adapter;
	
	
	String[] scadAr = { "Giorno/Tempo specifici", "Oggi", "Domani", "Dopodomani", "Prossima settimana", "Nessun Termine" };
	
	
	String[] scuole ={
					"IC ALFONSINE", "E MATTEOTTI ALF", "E RODARI ALF", "IC BAGNACAVALLO", "E BAGNACAVALLO",
					"E VILLANOVA BAGNA", "IC BRISIGHELLA", "E BRISIGHELLA", "E FOGNANO", "E MARZENO",
					"IC CASTELB", "E BASSI CASTELB", "E GINNASI CASTELB", "E SOLAROLO", "D CERVIA",
					"E SPALLICCI", "E PINARELLA", "E MONTALETTO", "E TAGLIATA", "E PASCOLI CERVIA",
					"IC CONSELICE", "E CONSELICE", "E LAVEZZOLA", "IC COTIGNOLA", "E COTIGNOLA", "E BARBIANO",
					"D FAENZA 4", "E TOLOSANO", "E GULLI", "E S.UMILTA'", "D FAENZA 5",
					"E MARTIRI CEFALONIA", "E GRANAROLO FAE", "E PIRAZZINI", "IC CARCHIDIO", "E CARCHIDIO",
					"E REDA", "IC EUROPA", "E DON MILANI FAE", "IC FUSIGNANO", "E FUSIGNANO",
					"IC BARACCA", "E CODAZZI", "E S.GIUSEPPE LUGO", "E M.AUSILIATRICE LUGO", "IC GHERARDI",
					"E GARIBALDI LUGO", "E VOLTANA", "E S.BERNARDINO", "E SACRO CUORE LUGO",
					
					"IC MARINA", "E MARINA", "E PORTO CORSINI", "E PUNTA MARINA", "IC MASSA LOMBARDA",
					"E MASSA LOMBARDA", "E FRUGES", "E BAGNARA", "E SANT'AGATA", "IC MEZZANO",
					"E MEZZANO", "E PIANGIPANE", "E S.ALBERTO", "E SAVARNA", "E CASAL BORSETTI",
					"D MILANO MARITTIMA", "E MILANO MARITTIMA", "E MARTIRI FANTINI", "E CASTIGLIONE CE", "E PISIGNANO",
					"D RA 2", "E MORDANI", "E RICCI", "E TAVELLI",
					"E S.VINCENZO RA"
					
					};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
        edit=(AutoCompleteTextView)findViewById(R.id.scuola);
	    edit.addTextChangedListener(this);
		edit.setAdapter(new ArrayAdapter<String>(this,
													android.R.layout.simple_dropdown_item_1line,
													scuole));
		
		
		scadenze = new ArrayList<String>();
		for (String s : scadAr) {
			scadenze.add(s);		
		};

		spinner = (Spinner)findViewById(R.id.spinner);
		
		adapter = new ArrayAdapter<String>(Main.this, android.R.layout.simple_spinner_item, scadenze);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition("Nessun Termine"));
		
		
    }

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
}