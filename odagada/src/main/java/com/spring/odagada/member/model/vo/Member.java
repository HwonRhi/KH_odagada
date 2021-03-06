package com.spring.odagada.member.model.vo;

import java.io.Serializable;

public class Member implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int memberNum;
	private String memberId;
	private String memberPw;
	private String memberName;
	private String birth;
	private String phone;
	private String email;
	private String enrollDate;
	private int isAdmin;
	private String gender;
	private String profileImageOri;
	private String profileImageRe;
	private String isPhoneAuth;
	private String isEmailAuth;
	private String mailCode;
	private String phoneCode;
	private String memberStatus;
	private String carMsg;

	public Member() {
		super();
	}

	public Member(int memberNum, String memberId, String memberPw, String memberName, String birth, String phone,
			String email, String enrollDate, int isAdmin, String gender, String profileImageOri, String profileImageRe,
			String isPhoneAuth, String isEmailAuth, String mailCode, String phoneCode) {
		super();
		this.memberNum = memberNum;
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberName = memberName;
		this.birth = birth;
		this.phone = phone;
		this.email = email;
		this.enrollDate = enrollDate;
		this.isAdmin = isAdmin;
		this.gender = gender;
		this.profileImageOri = profileImageOri;
		this.profileImageRe = profileImageRe;
		this.isPhoneAuth = isPhoneAuth;
		this.isEmailAuth = isEmailAuth;
		this.mailCode = mailCode;
		this.phoneCode = phoneCode;
	}
	
	public Member(int memberNum, String memberId, String memberPw, String memberName, String birth, String phone,
			String email, String enrollDate, int isAdmin, String gender, String profileImageOri, String profileImageRe,
			String isPhoneAuth, String isEmailAuth, String mailCode, String phoneCode, String memberStatus,
			String carMsg) {
		super();
		this.memberNum = memberNum;
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberName = memberName;
		this.birth = birth;
		this.phone = phone;
		this.email = email;
		this.enrollDate = enrollDate;
		this.isAdmin = isAdmin;
		this.gender = gender;
		this.profileImageOri = profileImageOri;
		this.profileImageRe = profileImageRe;
		this.isPhoneAuth = isPhoneAuth;
		this.isEmailAuth = isEmailAuth;
		this.mailCode = mailCode;
		this.phoneCode = phoneCode;
		this.memberStatus = memberStatus;
		this.carMsg = carMsg;
	}

	public int getMemberNum() {
		return memberNum;
	}


	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}


	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getMemberPw() {
		return memberPw;
	}


	public void setMemberPw(String memberPw) {
		this.memberPw = memberPw;
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getBirth() {
		return birth;
	}


	public void setBirth(String birth) {
		this.birth = birth;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getEnrollDate() {
		return enrollDate;
	}


	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}


	public int getIsAdmin() {
		return isAdmin;
	}


	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getProfileImageOri() {
		return profileImageOri;
	}


	public void setProfileImageOri(String profileImageOri) {
		this.profileImageOri = profileImageOri;
	}


	public String getProfileImageRe() {
		return profileImageRe;
	}


	public void setProfileImageRe(String profileImageRe) {
		this.profileImageRe = profileImageRe;
	}


	public String getIsPhoneAuth() {
		return isPhoneAuth;
	}


	public void setIsPhoneAuth(String isPhoneAuth) {
		this.isPhoneAuth = isPhoneAuth;
	}


	public String getIsEmailAuth() {
		return isEmailAuth;
	}


	public void setIsEmailAuth(String isEmailAuth) {
		this.isEmailAuth = isEmailAuth;
	}


	public String getMailCode() {
		return mailCode;
	}


	public void setMailCode(String mailCode) {
		this.mailCode = mailCode;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	
	

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getCarMsg() {
		return carMsg;
	}

	public void setCarMsg(String carMsg) {
		this.carMsg = carMsg;
	}

	@Override
	public String toString() {
		return "Member [memberNum=" + memberNum + ", memberId=" + memberId + ", memberPw=" + memberPw + ", memberName="
				+ memberName + ", birth=" + birth + ", phone=" + phone + ", email=" + email + ", enrollDate="
				+ enrollDate + ", isAdmin=" + isAdmin + ", gender=" + gender + ", profileImageOri=" + profileImageOri
				+ ", profileImageRe=" + profileImageRe + ", isPhoneAuth=" + isPhoneAuth + ", isEmailAuth=" + isEmailAuth
				+ ", mailCode=" + mailCode + ", phoneCode=" + phoneCode + ", memberStatus=" + memberStatus + ", carMsg="
				+ carMsg + "]";
	}

	

}
