package cn.amazonaws.lambda.cognitowechat;

public class CognitoWechatResponse {
	private String identityId;
	private Integer userId = null;
	private String openIdToken = null;
	private String status = "false";
    
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}		
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getOpenIdToken() { return openIdToken; }
    public void setOpenIdToken(String openIdToken) { this.openIdToken = openIdToken; }
		
    public String getStatus() {	return status; }
    public void setStatus(String status) { this.status = status; }	
}
