package cn.aws.cognitowx;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="CognitoWxUser")
public class User {
	private String accessToken;
	private String unionid;
	private Integer userId;
    private String openIdToken;
    private String identityId;
    private String nickname;
    private String headimgurl;
    
    @DynamoDBHashKey(attributeName="unionid")
    public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

    @DynamoDBAttribute(attributeName="nickname")
    public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@DynamoDBAttribute(attributeName="headimgurl ")
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	
    @DynamoDBAttribute(attributeName="userid")
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
    public String getOpenIdToken() { return openIdToken; }
    public void setOpenIdToken(String openIdToken) { this.openIdToken = openIdToken; }
 
    public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
    public User(){ }	
}