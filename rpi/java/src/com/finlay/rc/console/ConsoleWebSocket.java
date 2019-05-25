package com.finlay.rc.console;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.Framedata.Opcode;

public class ConsoleWebSocket implements WebSocket {

	@Override
	public void close(int code, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close(int code) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeConnection(int code, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(String text) throws NotYetConnectedException {
		System.out.println(text);
	}

	@Override
	public void send(ByteBuffer bytes) throws IllegalArgumentException, NotYetConnectedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(byte[] bytes) throws IllegalArgumentException, NotYetConnectedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFrame(Framedata framedata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFrame(Collection<Framedata> frames) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPing() throws NotYetConnectedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFragmentedFrame(Opcode op, ByteBuffer buffer, boolean fin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasBufferedData() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InetSocketAddress getRemoteSocketAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InetSocketAddress getLocalSocketAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnecting() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosing() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFlushAndClose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Draft getDraft() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public READYSTATE getReadyState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResourceDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void setAttachment(T attachment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getAttachment() {
		// TODO Auto-generated method stub
		return null;
	}

}
