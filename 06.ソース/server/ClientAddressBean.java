package server;
/**
 * クライアントのアドレスを保存するBean
 * @author kanayama
 *
 */
public class ClientAddressBean {
	private String FirstAddress;
	private String SecondAddress;


	public String getFirstAddress() {
		return FirstAddress;
	}
	public void setFirstAddress(String firstAddress) {
		FirstAddress = firstAddress;
	}
	public String getSecondAddress() {
		return SecondAddress;
	}
	public void setSecondAddress(String secondAddress) {
		SecondAddress = secondAddress;
	}
}
