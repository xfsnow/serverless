package cn.aws.cognitodev;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClientBuilder;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityRequest;
import com.amazonaws.services.cognitoidentity.model.GetOpenIdTokenForDeveloperIdentityResult;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AuthenticateUser implements RequestHandler<Object, Object> {
	
    private static final String IDENTITY_POOL_ID = "cn-north-1:e200e98d-1c35-48c8-8062-533962175372";
    private static final Regions REGION = Regions.CN_NORTH_1;
    private static final String developerProvider = "cn.aws.cognitodev";

    @Override
    public AuthenticateUserResponse handleRequest(Object input, Context context) {
        AuthenticateUserResponse authenticateUserResponse = new AuthenticateUserResponse();
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> inputHashMap = (LinkedHashMap<String, String>)input;
        User user = authenticateUser(inputHashMap);
        
        if(user!=null){
            authenticateUserResponse.setUserId(user.getUserId());
            authenticateUserResponse.setStatus("true");
            authenticateUserResponse.setOpenIdToken(user.getOpenIdToken());
            authenticateUserResponse.setIdentityId(user.getIdentityId());
        }else{
            authenticateUserResponse.setUserId(null);
            authenticateUserResponse.setStatus("false");
            authenticateUserResponse.setOpenIdToken(null);
            authenticateUserResponse.setIdentityId(null);
        }
            
        return authenticateUserResponse;
    }

    public User authenticateUser(LinkedHashMap<String, String> input){
        User user=null;
        	
        String userName = (String) input.get("userName");
        String passwordHash = (String) input.get("passwordHash");
        	
        try{
//            AmazonDynamoDBClient client = new AmazonDynamoDBClient();
//            client.setRegion(Region.getRegion(REGION));
            final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(REGION)
                    .build();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
    	    	
            user = mapper.load(User.class, userName);
    	    	
            if(user!=null){
            	System.out.println("authenticateUser: user.getUserId()="+ user.getUserId()
            	+ " user.getPasswordHash()="+user.getPasswordHash()
            	+ " passwordHash="+passwordHash);
                if(user.getPasswordHash().equals(passwordHash)){
                	
                	GetOpenIdTokenForDeveloperIdentityResult result = getOpenIdToken(user.getUserId()); 
                    user.setOpenIdToken(result.getToken());
                    user.setIdentityId(result.getIdentityId());
                    return user;
                }
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return null;
    }
    
    /**
     * 使用用户ID 调取 CognitoIdentity的 token 
     * @param userId
     * @return token
     */
    private GetOpenIdTokenForDeveloperIdentityResult getOpenIdToken(Integer userId){
//    	AmazonCognitoIdentityClient client = new AmazonCognitoIdentityClient();
    	AmazonCognitoIdentity client = AmazonCognitoIdentityClientBuilder.defaultClient();
    	GetOpenIdTokenForDeveloperIdentityRequest tokenRequest = new GetOpenIdTokenForDeveloperIdentityRequest();
        tokenRequest.setIdentityPoolId(IDENTITY_POOL_ID);
//        System.out.println(IDENTITY_POOL_ID + " "+ tokenRequest.getIdentityPoolId());	
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(developerProvider, userId.toString());
        	
        tokenRequest.setLogins(map);
        tokenRequest.setTokenDuration(new Long(10001));
        	
        GetOpenIdTokenForDeveloperIdentityResult result = client.getOpenIdTokenForDeveloperIdentity(tokenRequest);
//        System.out.println(result);
        return result;	
//        String token = result.getToken();
//        	
//        return token;
    }
}

