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
    private static final String dbExtension = "db";

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
        String date = String.valueOf(c.get(Calendar.YEAR)) + "_";
        if(c.get(Calendar.MONTH) < 10)
            date = date.concat("0");
        date = date.concat(String.valueOf(c.get(Calendar.MONTH)) + "_");
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
        intent.setType("application/x-sqlite3");

        parent.startActivityForResult(intent, OPEN_REQUEST_CODE);
    }

    public static boolean isDbExtension(Uri uri){
        String path = uri.getPath();
        if(path.contains("."))
            if (dbExtension.equals(path.split(".")[1]))
                return true;
        return false;
    }


    /*private void deleteFile(Uri uri) {
        Log.d("URI string", uri.toString());
        Log.d("URI is download", String.valueOf(isDownloadsDocument(uri)));

        Log.d("File path from URI", getRealPathFromURI(uri));

        if (isDownloadsDocument(uri)) {
            final String id = DocumentsContract.getDocumentId(uri);
            uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
        }

        Log.d("URI string", uri.toString());
            //parent.getContentResolver().delete(data, null,null);
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA};
            cursor = parent.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    */

    public void copyDataFromExternalFileToLocalDb(Uri externalDbUri){
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

    public void copyLocalDbDataToExternalFile(Uri newFileUri) {
        File db = getDbFile();
        Uri dbUri = Uri.fromFile(db);

        if (db.exists()) {
            try {
                InputStream inStream = parent.getContentResolver().openInputStream(dbUri);
                OutputStream outStream = parent.getContentResolver().openOutputStream(newFileUri);

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

}
