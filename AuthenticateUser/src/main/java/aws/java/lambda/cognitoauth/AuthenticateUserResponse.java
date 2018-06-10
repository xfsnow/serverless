package aws.java.lambda.cognitoauth;

public class AuthenticateUserResponse {
	  protected Integer userId;
	    protected String openIdToken;
	    private String identityId;
	    protected String status;
			
	    public Integer getUserId() { return userId; }
	    public void setUserId(Integer userId) { this.userId = userId; }

	    public String getOpenIdToken() { return openIdToken; }
	   
	    public void setOpenIdToken(String openIdToken) { this.openIdToken = openIdToken; }
			
	    public String getIdentityId() {
			return identityId;
		}
		public void setIdentityId(String identityId) {
			this.identityId = identityId;
		}
	    public String getStatus() {	return status; }
	    public void setStatus(String status) { this.status = status; }		
}
