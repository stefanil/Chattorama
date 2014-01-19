/**
 * 
 */
package de.saxsys.jfx.chattorama;


/**
 * @author stefan.illgen
 * 
 */
public enum Emoticons {

	CRY("0x1f625"), 	// 😥
	LAUGH("0x1F603"), 	// 😃
	SMILE("0x1F60B"), 	// 😋 / 263A
	EGYPT("0xF3000"), 	// 󳀀
	RHINO("0x130EF"), 	// 𓃯
	BULL("0xF3891"), 	// 󳢑
	EAGLE("0xF3CA8"); 	// 󳲨

	private String emoticon;

	private Emoticons(String hexString) {
		this.emoticon = new String(Character.toChars(Integer.parseInt(
				hexString.substring(2), 16)));
	}

	public String toString() {
		return emoticon;
	}

}
