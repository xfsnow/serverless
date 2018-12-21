package cn.amazonaws.lambda.cognitowechat;

/**
 * Cognito 返回的用户对象
 *
 */
public class CognitoUser {
	private String openIdToken;
	private String identityId;
	private Integer userId;
	public String getOpenIdToken() {
		return openIdToken;
	}
	public void setOpenIdToken(String openIdToken) {
		this.openIdToken = openIdToken;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
