package com.app.redcherry.webservice;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Rakshith on 8/6/2015.
 */
class MultipartRequest extends Request<String> {
    private MultipartEntity entity = new MultipartEntity();

    private static final String FILE_PART_NAME = "file";
    private static final String STRING_PART_NAME = "string";

    private final Response.Listener<String> mListener;
    private final File mFilePart;
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private final String  mimeType = "multipart/form-data;boundary=" + boundary;

    public MultipartRequest(int method, String url,Response.Listener<String> resplistener, Response.ErrorListener errorListener,File file) {
        super(method, url, errorListener);
        mListener = resplistener;
        mFilePart = file;
        buildMultipartEntity();
    }


    public MultipartRequest(int method, String url,Response.Listener<String> resplistener, Response.ErrorListener errorListener,MultipartEntity entity ) {
        super(method, url, errorListener);
        mListener = resplistener;
        this.entity = entity;
        mFilePart = null;
    }

    private void buildMultipartEntity() {
        entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart(FILE_PART_NAME, new FileBody(mFilePart));

    }

    @Override
    public String getBodyContentType()
    {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            entity.writeTo(bos);
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        //return Response.success("Uploaded", getCacheEntry());

        String parsed;
        try {
            parsed = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException var4) {
            parsed = new String(networkResponse.data);
        }

        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
