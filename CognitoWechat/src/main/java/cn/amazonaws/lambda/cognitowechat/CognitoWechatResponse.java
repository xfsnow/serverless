package cn.amazonaws.lambda.cognitowechat;

public class CognitoWechatResponse {
	protected Integer userId = null;
    protected String openIdToken = null;
    protected String status = "false";
		
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getOpenIdToken() { return openIdToken; }
    public void setOpenIdToken(String openIdToken) { this.openIdToken = openIdToken; }
		
    public String getStatus() {	return status; }
    public void setStatus(String status) { this.status = status; }	
}
