package server;

import java.io.Serializable;
/**
 * 指し手情報を格納するBeanクラス
 * @author kanayama
 *
 */
public class LogicInfoBean implements Serializable {
private String logicName;
private String writer;
private String version;
private String address;


public String getLogicName() {
	return logicName;
}
public void setLogicName(String logicName) {
	this.logicName = logicName;
}
public String getWriter() {
	return writer;
}
public void setWriter(String writer) {
	this.writer = writer;
}
public String getVersion() {
	return version;
}
public void setVersion(String version) {
	this.version = version;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}

}
