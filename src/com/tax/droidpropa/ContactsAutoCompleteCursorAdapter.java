package com.tax.droidpropa;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;








public class ContactsAutoCompleteCursorAdapter extends CursorAdapter implements Filterable {

    private TextView mName, mNumber;

    public ContactsAutoCompleteCursorAdapter (Context context, Cursor c) {
        super(context, c);
        mContent = context.getContentResolver();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       
    	final LinearLayout ret = new LinearLayout(context);
        final LayoutInflater inflater = LayoutInflater.from(context);
        
    	
//        View row=inflater.inflate(R.layout.autocomplete_scuola_item, parent, false);
//        TextView mName = (TextView)row.findViewById(R.id.textscuola);
//        int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
//	    String name = cursor.getString(nameIdx);
//	    mName.setText(name);
//	    return row;
//        
        mName = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        ret.setOrientation(LinearLayout.VERTICAL);
        LinearLayout horizontal = new LinearLayout(context);
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
        String name = cursor.getString(nameIdx);
        mName.setText(name);
        mName.setPadding(10, 10, 10, 10);
//        mName.setTextSize(16);

//        ImageView icon = new ImageView(context);
//        Drawable image_icon  = null;
//        image_icon = context.getResources().getDrawable(R.drawable.icon);
//        icon.setImageDrawable(image_icon);
//        icon.setPadding(0, -2, 2, 6);

        // an example of how you can arrange your layouts programmatically
        // place the number and icon next to each other
        //horizontal.addView(mNumber, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        //horizontal.addView(icon, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ret.addView(mName, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ret.addView(horizontal, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return ret;
        
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        
    	int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
        String name = cursor.getString(nameIdx);
        
        
        
//        TextView nNome = (TextView)view.findViewById(R.id.textscuola);
//        if (nNome != null) {
//        	nNome.setText(name);
//        	Log.v("bindView", name);
//        }
        /**
         * Always add an icon, even if it is null. Keep the layout children;
         * indices consistent.
         */
       Drawable image_icon  = null;
       //        if (carrier != CarrierInfo.NOT_CACHED && carrier != CarrierInfo.ERROR && carrier != CarrierInfo.NETWORK_ERROR) {
            image_icon = context.getResources().getDrawable(R.drawable.icon);
       //}

        // notice views have already been inflated and layout has already been set so all you need to do is set the data
        ((TextView) ((LinearLayout) view).getChildAt(0)).setText(name);
        LinearLayout horizontal = (LinearLayout) ((LinearLayout) view).getChildAt(1);
        //((TextView) horizontal.getChildAt(0)).setText(number);
        //((ImageView) horizontal.getChildAt(1)).setImageDrawable(image_icon);
    }

//    @Override
    public String convertToString(Cursor cursor) {
        // this method dictates what is shown when the user clicks each entry in your autocomplete list
        // in my case i want the number data to be shown
        int numCol = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
        String number = cursor.getString(numCol);
        return number;
    	 
    }

    public static final String[] PEOPLE_PROJECTION = new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
    
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        // this is how you query for suggestions
        // notice it is just a StringBuilder building the WHERE clause of a cursor which is the used to query for results
        if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }

        StringBuilder buffer = null;
        String[] args = null;
        
        if (constraint != null) {
            buffer = new StringBuilder();
            //buffer.append(People.NAME + " IS NOT NULL AND " + People.NUMBER_KEY + " IS NOT NULL AND ");
            buffer.append(ContactsContract.Contacts.DISPLAY_NAME + " IS NOT NULL AND ");
            buffer.append("UPPER(");
            buffer.append(ContactsContract.Contacts.DISPLAY_NAME);
            buffer.append(") GLOB ?");
            //buffer.append(") LIKE \"%?%\"");
            args = new String[] { "*" + constraint.toString().toUpperCase() + "*" };
        }
        
        Log.e("buffer", buffer.toString());
        Log.e("constraint", constraint.toString());
        
        
        return mContent.query(ContactsContract.Contacts.CONTENT_URI, PEOPLE_PROJECTION, buffer == null ? null : buffer
                .toString(), args, ContactsContract.Contacts.DISPLAY_NAME);
    }

    private ContentResolver mContent;

}
