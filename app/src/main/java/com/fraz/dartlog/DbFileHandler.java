package com.fraz.dartlog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by CarlR on 17/06/2017.
 */

public class DbFileHandler {

    /* These numbers are randomly selected */
    public static final int WRITE_REQUEST_CODE = 43;
    public static final int OPEN_REQUEST_CODE = 44;

    Activity parent;


    public DbFileHandler(Activity parent){
        this.parent = parent;
    }

    public File getDbFile() {
        File dbDir = new File("/data/data/com.fraz.dartlog/databases");
        File dbFile = new File(dbDir, "DartLog.db");

        return dbFile;
    }

    private String getDateAsString() {
        Calendar c = Calendar.getInstance();
        String date = String.valueOf(c.get(Calendar.YEAR));
        if(c.get(Calendar.MONTH) < 10)
            date.concat("0");
        date.concat(String.valueOf(c.get(Calendar.MONTH)));
        if(c.get(Calendar.DAY_OF_MONTH) < 10)
            date.concat("0");
        date.concat(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        return date;
    }

    public void createCopyOfLocalDb() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/x-sqlite3");
        intent.putExtra(Intent.EXTRA_TITLE, "db_copy_" + getDateAsString() + ".db");

        parent.startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    public void selectExternalDbFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/x-sqlite3");

        parent.startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public void onFileCreated(Uri fileUri) {
        copyDbDataToNewFile(fileUri);
    }

    public void onFileSelected(Uri fileUri){
        copyDataFromFileToDb(fileUri);
    }

    private void copyDataFromFileToDb(Uri externalDbUri){
        File appDb = getDbFile();
        Uri appDbUri = Uri.fromFile(appDb);

        if (appDb.exists()) {
            try {
                InputStream inStream = parent.getContentResolver().openInputStream(externalDbUri);
                OutputStream outStream = parent.getContentResolver().openOutputStream(appDbUri);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();
                outStream.close();
            } catch (Exception e) {
            }
        }
    }

    private void copyDbDataToNewFile(Uri emptyFileUri) {
        File db = getDbFile();
        Uri dbUri = Uri.fromFile(db);

        if (db.exists()) {
            try {
                InputStream inStream = parent.getContentResolver().openInputStream(dbUri);
                OutputStream outStream = parent.getContentResolver().openOutputStream(emptyFileUri);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1)
                {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();
                outStream.close();
            } catch (Exception e) {
                Log.d("DbFileHandler Error", "Shit happened!: " + e.getMessage());
            }
        }
    }
}
