package com.golive;

import android.util.Log;
import org.webrtc.*;

public class PeerConnectionObserver implements PeerConnection.Observer {

    private String tag;

    public PeerConnectionObserver(String tag) {
        this.tag = "Mithun " + tag;
    }

    private void log(String s) {
        Log.d(tag, s);
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        log("onSignalingChange " + signalingState);
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        log("onIceConnectionChange " + iceConnectionState);
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        log("onIceConnectionReceivingChange " + b);
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        log("onIceGatheringChange " + iceGatheringState);
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        log("onIceCandidate " + iceCandidate);
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        log("onIceCandidatesRemoved " + iceCandidates);
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        log("onAddStream " + mediaStream);
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        log("onRemoveStream " + mediaStream);
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        log("onDataChannel " + dataChannel);
    }

    @Override
    public void onRenegotiationNeeded() {
        log("onRenegotiationNeeded ");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        log("onAddTrack " + mediaStreams);
    }
}
