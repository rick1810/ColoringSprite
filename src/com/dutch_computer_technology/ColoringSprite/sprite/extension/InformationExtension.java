package com.dutch_computer_technology.ColoringSprite.sprite.extension;

import com.dutch_computer_technology.ColoringSprite.bytes.ByteManager;
import com.dutch_computer_technology.ColoringSprite.bytes.Offset;
import com.dutch_computer_technology.ColoringSprite.exception.ByteException;
import com.dutch_computer_technology.ColoringSprite.exception.ExtensionParseException;

public class InformationExtension implements Extension {
	
	@Override
	public int getKey() {
		return 2;
	};
	
	private String name; //60
	private String author; //70
	private long date; //8
	private String description; //100
	private String category; //30
	private String[] tags; //20 x 10
	
	public InformationExtension() {
		
		this.name = "";
		this.author = "";
		this.date = System.currentTimeMillis();
		this.description = "";
		this.tags = new String[0];
		this.category = "";
		
	};
	
	private String cleanStr(byte[] bytes) {
		
		String str = "";
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] == '\0') break;
			str += (char) bytes[i];
		};
		return str.stripTrailing();
		
	};
	
	public InformationExtension(byte[] bytes, int version) throws ExtensionParseException {
		
		Offset offset = new Offset();
		
		try {
			int nameLength = ByteManager.getInt(bytes, offset, 1);
			byte[] nameBytes = ByteManager.getByte(bytes, offset, nameLength);
			this.name = cleanStr(nameBytes);
		} catch(ByteException ignore) {
			throw new ExtensionParseException("Could not get Name");
		};
		
		try {
			int authorLength = ByteManager.getInt(bytes, offset, 1);
			byte[] authorBytes = ByteManager.getByte(bytes, offset, authorLength);
			this.author = cleanStr(authorBytes);
		} catch(ByteException ignore) {
			throw new ExtensionParseException("Could not get Author");
		};
		
		try {
			int dateLength = ByteManager.getInt(bytes, offset, 1);
			if (dateLength < 1 || dateLength > 8) {
				this.date = 0;
			} else {
				this.date = ByteManager.getLong(bytes, offset, dateLength);
			};
		} catch(ByteException ignore) {
			throw new ExtensionParseException("Could not get Date");
		};
		
		try {
			int descriptionLength = ByteManager.getInt(bytes, offset, 1);
			byte[] descriptionBytes = ByteManager.getByte(bytes, offset, descriptionLength);
			this.description = cleanStr(descriptionBytes);
		} catch(ByteException e) {
			e.printStackTrace();
			throw new ExtensionParseException("Could not get Description");
		};
		
		try {
			int categoryLength = ByteManager.getInt(bytes, offset, 1);
			byte[] categoryBytes = ByteManager.getByte(bytes, offset, categoryLength);
			this.category = cleanStr(categoryBytes);
		} catch(ByteException ignore) {
			throw new ExtensionParseException("Could not get Category");
		};
		
		this.tags = new String[0];
		for (int t = 0; t < 10; t++) {
			
			if (offset.value >= bytes.length) break;
			byte[] tagBytes;
			try {
				int tagLength = ByteManager.getInt(bytes, offset, 1);
				tagBytes = ByteManager.getByte(bytes, offset, tagLength);
			} catch (ByteException ignore) {
				throw new ExtensionParseException("Could not get Tag " + t);
			};
			addTag(cleanStr(tagBytes));
			
		};
		
 	};
	
	@Override
	public byte[] toByte(int version) {
		
		byte[] bytes = new byte[0];
		
		byte[] nameBytes = this.name.getBytes();
		byte nameLength = ByteManager.intToByte(nameBytes.length);
		bytes = ByteManager.append(bytes, nameLength);
		bytes = ByteManager.append(bytes, nameBytes);
		
		byte[] authorBytes = this.author.getBytes();
		byte authorLength = ByteManager.intToByte(authorBytes.length);
		bytes = ByteManager.append(bytes, authorLength);
		bytes = ByteManager.append(bytes, authorBytes);
		
		byte[] dateBytes = new byte[0];
		try {
			dateBytes = ByteManager.longToByte(this.date, 8);
		} catch (ByteException ignore) {}; //Only called if longToByte size is <1 or >8
		dateBytes = ByteManager.trim(dateBytes);
		byte dateLength = ByteManager.intToByte(dateBytes.length);
		bytes = ByteManager.append(bytes, dateLength);
		bytes = ByteManager.append(bytes, dateBytes);
		
		byte[] descriptionBytes = this.description.getBytes();
		byte descriptionLength = ByteManager.intToByte(descriptionBytes.length);
		bytes = ByteManager.append(bytes, descriptionLength);
		bytes = ByteManager.append(bytes, descriptionBytes);
		
		byte[] categoryBytes = this.category.getBytes();
		byte categoryLength = ByteManager.intToByte(categoryBytes.length);
		bytes = ByteManager.append(bytes, categoryLength);
		bytes = ByteManager.append(bytes, categoryBytes);
		
		int t = 0;
		for (String tag : this.tags) {
			
			byte[] tagBytes = tag.getBytes();
			byte tagLength = ByteManager.intToByte(tagBytes.length);
			bytes = ByteManager.append(bytes, tagLength);
			bytes = ByteManager.append(bytes, tagBytes);
			t++;
			if (t >= 10) break;
			
		};
		
		return bytes;
		
	};
	
	public String getName() {
		return this.name;
	};
	public void setName(String name) {
		if (name.length() > 60) name = name.substring(0, 60);
		this.name = name;
	};
	
	public String getAuthor() {
		return this.author;
	};
	public void setAuthor(String author) {
		if (author.length() > 70) author = author.substring(0, 70);
		this.author = author;
	};
	
	public long getDate() {
		return this.date;
	};
	public void setDate(long date) {
		this.date = date;
	};
	
	public String getDescription() {
		return this.description;
	};
	public void setDescription(String description) {
		if (description.length() > 100) description = description.substring(0, 100);
		this.description = description;
	};
	
	public String getCategory() {
		return this.category;
	};
	public void setCategory(String category) {
		if (category.length() > 30) category = category.substring(0, 30);
		this.category = category;
	};
	
	public int tags() {
		return this.tags.length;
	};
	
	public String[] getTags() {
		return this.tags;
	};
	
	public void addTag(String str) {
		if (str == null) return;
		if (str.length() > 20) str = str.substring(0, 20);
		if (hasTag(str)) return;
		int len = this.tags.length;
		if (len >= 10) return;
		String[] tags = new String[len+1];
		System.arraycopy(this.tags, 0, tags, 0, len);
		tags[len] = str;
		this.tags = tags;
	};
	
	public boolean hasTag(String str) {
		
		for (String tag : this.tags) {
			if (tag.equals(str)) return true;
		};
		return false;
		
	};
	
	public void clearTags() {
		
		this.tags = new String[0];
		
	};
	
	public void remTag(String str) {
		for (int i = 0; i < this.tags.length; i++) {
			String tag = this.tags[i];
			if (tag.equals(str)) {
				int len = this.tags.length;
				if (len == 1) {
					this.tags = new String[0];
					break;
				};
				String[] tags = new String[len-1];
				System.arraycopy(this.tags, 0, tags, 0, i);
				System.arraycopy(this.tags, i+1, tags, i, len-i-1);
				this.tags = tags;
				break;
			};
		};
	};
	
};