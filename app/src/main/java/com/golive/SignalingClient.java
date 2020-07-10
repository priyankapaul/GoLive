package com.golive;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SignalingClient {
    private static final String TAG = SignalingClient.class.getSimpleName();

    private static SignalingClient instance;
    private Socket socket;
    private Callback callback;

    private SignalingClient() {
    }

    public static SignalingClient get() {
        if (instance == null) {
            synchronized (SignalingClient.class) {
                if (instance == null) {
                    instance = new SignalingClient();
                }
            }
        }
        return instance;
    }

    public void init(String room, Callback callback) {
        this.callback = callback;
        try {
//            socket = IO.socket("http://192.168.1.110:3000/");
            socket = IO.socket("http://192.168.1.101:3000");
            socket.connect();

            socket.emit("create or join", room);

            socket.on("created", args -> {
                Log.e(TAG, "room created:" + socket.id());
                callback.onCreateRoom();
            });
            socket.on("full", args -> {
                Log.e(TAG, "room full");
            });
            socket.on("join", args -> {
                Log.e(TAG, "peer joined " + Arrays.toString(args));
                callback.onPeerJoined(String.valueOf(args[1]));
            });
            socket.on("joined", args -> {
                Log.e(TAG, "self joined:" + socket.id());
                callback.onSelfJoined();
            });
            socket.on("log", args -> {
                Log.e(TAG, "log call " + Arrays.toString(args));
            });

            socket.on("bye", args -> {
                Log.e(TAG, "bye " + args[0]);
                callback.onPeerLeave((String) args[0]);
            });
            socket.on("message", args -> {
                Log.e(TAG, "message " + Arrays.toString(args));
                Object arg = args[0];
                if (arg instanceof String) {

                } else if (arg instanceof JSONObject) {
                    JSONObject data = (JSONObject) arg;
                    String type = data.optString("type");
                    if ("offer".equals(type)) {
                        callback.onOfferReceived(data);
                    } else if ("answer".equals(type)) {
                        callback.onAnswerReceived(data);
                    } else if ("candidate".equals(type)) {
                        callback.onIceCandidateReceived(data);
                    }
                }
            });
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        if (socket != null) {
            socket.emit("bye", socket.id());
            socket.disconnect();
            socket.close();
            instance = null;
        }
    }

    public void sendIceCandidate(IceCandidate iceCandidate, String to) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("type", "candidate");
            jo.put("label", iceCandidate.sdpMLineIndex);
            jo.put("id", iceCandidate.sdpMid);
            jo.put("candidate", iceCandidate.sdp);
            jo.put("from", socket.id());
            jo.put("to", to);

            socket.emit("message", jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendSessionDescription(SessionDescription sdp, String to) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("type", sdp.type.canonicalForm());
            jo.put("sdp", sdp.description);
            jo.put("from", socket.id());
            jo.put("to", to);
            socket.emit("message", jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface Callback {
        void onCreateRoom();

        void onPeerJoined(String socketId);

        void onSelfJoined();

        void onPeerLeave(String msg);

        void onOfferReceived(JSONObject data);

        void onAnswerReceived(JSONObject data);

        void onIceCandidateReceived(JSONObject data);
    }

}
