package com.fraz.dartlog;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

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

    private File getDbFile() {
        File dbDir = new File("/data/data/com.fraz.dartlog/databases");
        File dbFile = new File(dbDir, "DartLog.db");

        return dbFile;
    }

    private String getDateAsString() {
        Calendar c = Calendar.getInstance();
        String date = String.valueOf(c.get(Calendar.YEAR)) + "_";
        if(c.get(Calendar.MONTH) < 10)
            date = date.concat("0");
        date = date.concat(String.valueOf(c.get(Calendar.MONTH) + 1) + "_");
        if(c.get(Calendar.DAY_OF_MONTH) < 10)
            date = date.concat("0");
        date = date.concat(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

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
        intent.setDataAndType(Uri.fromParts("content", "*.db", null), "*/*");

        parent.startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public static boolean isDbExtension(Uri uri, Activity parent) {
        String fileName = "";
        Cursor returnCursor = parent.getContentResolver().query(uri, null, null, null, null);

        try {
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            fileName = returnCursor.getString(nameIndex);
        } catch (Exception e) {
            Log.d(TAG, "error: ", e);
            return false;
        } finally {
            returnCursor.close();
        }

        if (fileName.contains(".db"))
            return true;
        return false;
    }

    private InputStream getInputStream(Uri uri) throws FileNotFoundException {
        return parent.getContentResolver().openInputStream(uri);
    }

    private OutputStream getOutputStream(Uri uri) throws FileNotFoundException {
        return parent.getContentResolver().openOutputStream(uri);
    }

    public void copyDataFromExternalFileToLocalDb(Uri externalDbUri){
        File appDb = getDbFile();
        Uri appDbUri = Uri.fromFile(appDb);

        if (appDb.exists()) {
            try {
                InputStream inStream = getInputStream(externalDbUri);
                OutputStream outStream = getOutputStream(appDbUri);

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

    public void copyLocalDbDataToExternalFile(Uri newFileUri) {
        File db = getDbFile();
        Uri dbUri = Uri.fromFile(db);

        if (db.exists()) {
            try {
                InputStream inStream = getInputStream(dbUri);
                OutputStream outStream = getOutputStream(newFileUri);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1)
                {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();
                outStream.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean verifyImportedDb(Uri data, Activity parent) {
        return isDbExtension(data, parent);
    }
}
