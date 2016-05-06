package org.ziyeyc.couchbaselitewithpicasso;

import com.couchbase.lite.Attachment;
import com.couchbase.lite.Database;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by ziyeyc on 2016/5/5.
 */
public class CBLiteAttRequestHandler extends RequestHandler {
    private static final String CBL_ATT_SCHEME = "cbl_att";
    private static Database database;


    public CBLiteAttRequestHandler() {
        super();
    }

    public CBLiteAttRequestHandler(Database database) {
        super();
        this.database = database;
    }

    @Override
    public boolean canHandleRequest(Request data) {
        return CBL_ATT_SCHEME.equals(data.uri.getScheme());
    }

    // uri: cbl_att://docid/big.jpg
    @Override
    public Result load(Request request, int networkPolicy) throws IOException {
        String docidattname = request.uri.toString();
        String docid = getDocId(docidattname);
        String attname = getAttName(docidattname);

        try {
            Attachment image = database.getDocument(docid).getCurrentRevision().getAttachment(attname);
            if (null != image) {
                InputStream stream = image.getContent();
                return new Result(stream, Picasso.LoadedFrom.DISK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getDocId(String docidattname) {
        String pairstr = docidattname.substring("cbl_att://".length(), docidattname.length());
        String[] idlist = pairstr.split("/");
        String docid = idlist[0];

        return docid;
    }

    private String getAttName(String docidattname) {
        String pairstr = docidattname.substring("cbl_att://".length(), docidattname.length());
        String[] idlist = pairstr.split("/");
        String attname = idlist[1];

        return attname;
    }
}
