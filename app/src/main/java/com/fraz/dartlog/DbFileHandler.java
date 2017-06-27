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

    public static final int WRITE_REQUEST_CODE = 43;

    Activity parent;


    public DbFileHandler(Activity parent){
        this.parent = parent;
    }

    public File getDbFile() {
        File dbDir = new File("/data/data/com.fraz.dartlog/databases");
        File dbFile = new File(dbDir, "DartLog.db");
        return dbFile;
    }

    public void createFile(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // Filter to only show results that can be "opened", such as
        // a file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = String.valueOf(year + "_"  + month + "_" + day );

        // Create a file with the requested MIME type.
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, "db_copy_" + date + ".db");

        parent.startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    public void onFileCreated(Uri fileUri) {
        Log.d("dbFileHandler", "copies db data to path: " + fileUri);
        copyDbDataToNewFile(fileUri);
    }

    private void copyDbDataToNewFile(Uri emptyFileUri) {
        File db = getDbFile();
        Uri dbUri = Uri.fromFile(db);

        if (db.exists()) {
            try {
                InputStream inStream = parent.getContentResolver().openInputStream(dbUri);
                OutputStream outStream = parent.getContentResolver().openOutputStream(emptyFileUri);

                byte[] buffer = new byte[1024]; // Adjust if you want
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
