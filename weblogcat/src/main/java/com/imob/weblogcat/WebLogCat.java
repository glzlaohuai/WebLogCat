package com.imob.weblogcat;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.WebSocket;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class WebLogCat {

    private static final String TAG = "IMOB-WebLogCat";
    private static final int HTTP_POPRT = 8088;
    private static final int WEB_SOCKET_PORT = 8089;
    private static WebSocket currentWebSocket;

    private static boolean isWIFIConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        String typeName = null;

        if (networkInfo != null) {
            typeName = networkInfo.getTypeName();
        } else {
            typeName = "null";
        }
        return typeName.trim().equalsIgnoreCase("wifi");
    }


    private static String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public static void init(Context context) {
        if (isWIFIConnected(context)) {
            AsyncHttpServer server = new AsyncHttpServer();

            HttpServerRequestCallbckImpl callback = new HttpServerRequestCallbckImpl(context);
            server.get("[\\d\\D]*", callback);
            server.listen(AsyncServer.getDefault(), HTTP_POPRT);
            AsyncHttpServer webSocketServer = new AsyncHttpServer();
            webSocketServer.websocket("/log", new AsyncHttpServer.WebSocketRequestCallback() {
                @Override
                public void onConnected(WebSocket webSocket, AsyncHttpServerRequest request) {
                    closePreviousSocket();
                    currentWebSocket = webSocket;
                    log(generateDeviceMsg(context));
                }
            });
            webSocketServer.listen(WEB_SOCKET_PORT);
            Log.i(TAG, "open: http://" + getIP() + ":8088 to view logs");
        } else {
            Log.e(TAG, "please connect wifi to use web logcat");
        }
    }

    private static JSONArray msgToJsonArray(String msg) {
        JSONArray jsonArray = new JSONArray();
        try {
            BufferedReader reader = new BufferedReader(new StringReader(msg));
            String line;

            while ((line = reader.readLine()) != null) {
                jsonArray.put(line);
            }
        } catch (Exception e) {
        }

        return jsonArray;
    }

    private static JSONObject generateLogMsg(String tag, String msg, int logLevel, Throwable throwable) {

        JSONObject dataObject = new JSONObject();

        try {
            dataObject.put("tag", tag);
            dataObject.put("msg", msgToJsonArray(msg));
            dataObject.put("level", logLevel);

            if (throwable != null) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                throwable.printStackTrace(printWriter);
                dataObject.put("warns", msgToJsonArray(stringWriter.toString()));
            }
        } catch (JSONException e) {
        }

        return generateToClientJsonObject("log", dataObject);
    }

    private static JSONObject generateDeviceMsg(Context context) {
        String model = Build.MODEL;
        String manufacture = Build.MANUFACTURER;
        String osVersion = Build.VERSION.RELEASE;
        String pkgName = "unknown packageName";
        try {
            pkgName = context.getPackageName();
        } catch (Exception e) {
        }

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("device", model + " - " + manufacture + " - " + osVersion + " - < " + pkgName + " >");
        } catch (JSONException e) {
        }

        return generateToClientJsonObject("device", dataObject);
    }

    private static JSONObject generateToClientJsonObject(String type, JSONObject dataJsonObject) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", type);
            jsonObject.put("data", dataJsonObject);
            jsonObject.put("time", System.currentTimeMillis());
        } catch (JSONException e) {
        }
        return jsonObject;
    }

    public static void log(String tag, String msg, int logLevel, Throwable throwable) {
        log(generateLogMsg(tag, msg, logLevel, throwable));
    }

    private static void log(JSONObject jsonObject) {
        if (currentWebSocket != null && currentWebSocket.isOpen() && jsonObject != null) {
            currentWebSocket.send(jsonObject.toString());
        }
    }

    private static void closePreviousSocket() {
        if (currentWebSocket != null) {
            currentWebSocket.close();
        }
    }

    private static class HttpServerRequestCallbckImpl implements HttpServerRequestCallback {

        private Context context;
        private AssetManager assetManager;

        public HttpServerRequestCallbckImpl(Context context) {
            this.context = context;
            assetManager = context.getAssets();
        }

        @Override
        public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
            String path = request.getPath();
            InputStream inputStream = null;

            if (path.equals("/")) {
                try {
                    inputStream = assetManager.open("imob_weblogcat.html");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    inputStream = assetManager.open(path.substring(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    response.sendStream(inputStream, inputStream.available());
                } catch (Exception e) {
                    response.code(404).end();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            } else {
                response.code(404).end();
            }
        }
    }

}
