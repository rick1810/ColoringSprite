package com.dutch_computer_technology.ColoringSprite.sprite.extension;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;

public class OrderingExtension implements Extension {
	
	@Override
	public int getKey() {
		return 3;
	};
	
	private int[] order;
	
	public OrderingExtension() {
		
		this.order = new int[0];
		
	};
	
	public OrderingExtension(int[] order) {
		
		this.order = order;
		
	};
	
	public OrderingExtension(byte[] bytes, int version) throws ExtensionParseException {
		
		Offset offset = new Offset();
		this.order = new int[0];
		
		for (int i = 0; i < bytes.length; i+=2) {
			
			int key = 0;
			try {
				key = ByteManager.getInt(bytes, offset, 2);
			} catch (ByteException ignore) {
				throw new ExtensionParseException("Size of Key exceeds Size of Extension");
			};
			
			add(key);
			
		};
		
	};
	
	@Override
	public byte[] toByte(int version) {
		
		byte[] bytes = new byte[0];
		for (int key : this.order) {
			try {
				bytes = ByteManager.append(bytes, ByteManager.intToByte(key, 2));
			} catch (ByteException ignore) {};
		};
		return bytes;
		
	};
	
	public int[] getOrder() {
		return this.order;
	};
	
	public void add(int key) {
		if (key < 0) return;
		if (key > 65535) return; //Max number of keys posible in 2bytes
		rem(key);
		int len = this.order.length;
		int[] order = new int[len+1];
		System.arraycopy(this.order, 0, order, 0, len);
		order[len] = key;
		this.order = order;
	};
		
	public void rem(int key) {
		for (int i = 0; i < this.order.length; i++) {
			int mKey = this.order[i];
			if (mKey == key) {
				int len = this.order.length;
				if (len == 1) {
					this.order = new int[0];
					break;
				};
				int[] order = new int[len-1];
				System.arraycopy(this.order, 0, order, 0, i);
				System.arraycopy(this.order, i+1, order, i, len-i-1);
				this.order = order;
				break;
			};
		};
	};
	
};