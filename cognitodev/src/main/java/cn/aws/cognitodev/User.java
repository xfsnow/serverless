package cn.aws.cognitodev;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="CognitoDev")
public class User {
	
    private String userName;
    private Integer userId;
    private String passwordHash;
    private String openIdToken;
    private String identityId;
	
    @DynamoDBHashKey(attributeName="username")
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
	
    @DynamoDBAttribute(attributeName="userid")
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
	
    @DynamoDBAttribute(attributeName="passwordhash")
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
	
    @DynamoDBAttribute(attributeName="openidtoken")
    public String getOpenIdToken() { return openIdToken; }
    public void setOpenIdToken(String openIdToken) { this.openIdToken = openIdToken; }
    
    public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	
    public User(String userName, Integer userId, String passwordHash, String openIdToken, String identityId) {
        this.userName = userName;
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.openIdToken = openIdToken;
        this.identityId = identityId;
    }
	
    public User(){ }	
}