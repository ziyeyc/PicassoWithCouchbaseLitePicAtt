package org.ziyeyc.couchbaselitewithpicasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.UnsavedRevision;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by ziyeyc on 2016/5/5.
 */
public class IdentityDoc {
    private static final String DOC_TYPE = "identity";


    /**
     * identity
     *
     * @param database
     * @return
     * @throws com.couchbase.lite.CouchbaseLiteException
     */
    public static Document createIdentity(Database database)
            throws CouchbaseLiteException {

        if (database.getExistingDocument(DOC_TYPE + ":" + "001") != null) {
            return database.getExistingDocument(DOC_TYPE + ":" + "001");
        }

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("type", DOC_TYPE);

        // create the document with the given documentID
        Document document = database.getDocument(DOC_TYPE + ":" + "001");
        UnsavedRevision unsrevision = document.createRevision();
        unsrevision.setProperties(properties);

        try {
            // here should replace a picture in your test phone
            String picPath = "/storage/emulated/0/DCIM/Camera/IMG_20150816_094759.jpg";
            Bitmap bitmap = getBitmapFromPath(picPath);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            unsrevision.setAttachment("big.jpg", "image/jpg", in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            unsrevision.save();
        } catch (CouchbaseLiteException e) {
            database.deleteLocalDocument(document.getId());
            document = null;
        } finally {
            return document;
        }
    }

    public static Bitmap getBitmapFromPath(String path) throws FileNotFoundException {
        InputStream is = new FileInputStream(path);
        return BitmapFactory.decodeStream(is);
    }
}
