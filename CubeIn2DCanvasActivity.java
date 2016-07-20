package com.codentronix.android;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
 
public class CubeIn2DCanvasActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
 
        setContentView(R.layout.main);
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
 
    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if( id == R.id.about ) {
            builder.setMessage("Developed by tamil ")
                .setPositiveButton("OK", null);
        }
        return builder.create();
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == R.id.about ) {
            showDialog(R.id.about);
        }
        return true;
    }
}