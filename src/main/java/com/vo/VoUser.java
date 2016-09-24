package com.vo;

public class VoUser {
private String userid;
private boolean isOnline=true;
public VoUser(String userid){
	this.userid=userid;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public boolean isOnline() {
	return isOnline;
}
public void setIsOnline(boolean state) {
	this.isOnline = state;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((userid == null) ? 0 : userid.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	VoUser other = (VoUser) obj;
	if (userid == null) {
		if (other.userid != null)
			return false;
	} else if (!userid.equals(other.userid))
		return false;
	return true;
}
@Override
public String toString() {
	if(isOnline==true)
	return userid+"(在线)";
	else
	return userid+"(离线)";
}

}
