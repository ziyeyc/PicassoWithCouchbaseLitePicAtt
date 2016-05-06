package org.ziyeyc.couchbaselitewithpicasso;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;


/**
 * Created by ziyeyc on 2016/5/5.
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private Manager manager;
    private Database iHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            manager = createManager(getApplication());
            iHello = manager.getDatabase("ihello");
            IdentityDoc.createIdentity(iHello);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Cannot get Database", e);
        }

        Picasso picasso = new Picasso
                .Builder(getApplication())
                .addRequestHandler(new CBLiteAttRequestHandler(iHello))
                .build();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        picasso.load("cbl_att://identity:001/big.jpg").into(imageView);
    }

    public Manager createManager(Context context) {
        try {
            Manager.enableLogging(TAG, Log.VERBOSE);
            Manager.enableLogging(Log.TAG, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_SYNC_ASYNC_TASK, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_SYNC, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_QUERY, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_VIEW, Log.VERBOSE);
            Manager.enableLogging(Log.TAG_DATABASE, Log.VERBOSE);

            return new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            Log.e(TAG, "Cannot create Manager object", e);
            return null;
        }
    }
}

