package cn.amazonaws.lambda.cognitowechat;

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


public class CognitoWechat implements RequestHandler<Object, Object> {
	
	public static final String TAG = "CognitoWechatRequestHandler";
	
	// 以下常量替换为你在 AWS 配置的身份池的相应值
    private static final Regions REGION = Regions.CN_NORTH_1;
    private static final String IDENTITY_POOL_ID = "";
    private static final String developerProvider = "cn.aws.cognitowechat";
	
    @Override
    public CognitoWechatResponse handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

        CognitoWechatResponse cwResponse = new CognitoWechatResponse();
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> inputHashMap = (LinkedHashMap<String, String>)input;
        String code = (String) inputHashMap.get("code");
    	// 从输入参数中提取出的 code 值，临时放到响应结果中的OpenIdToken字段
        // 只为测试API Gateway和 Lambda 集成可以走通
//        cwResponse.setOpenIdToken(code);
        CognitoUser user = authenticateUser(code);
        if(user!=null){
        	cwResponse.setIdentityId(user.getIdentityId());
            cwResponse.setUserId(user.getUserId());
            cwResponse.setOpenIdToken(user.getOpenIdToken());
            cwResponse.setStatus("true");
        }
        return cwResponse;
    }
        
    /**
     * 使用用户ID 调取 CognitoIdentity的 token
     * @param userId
     * @return result
     */
    private CognitoUser getCognitoUser(Integer userId){
    	AmazonCognitoIdentity client = AmazonCognitoIdentityClientBuilder.defaultClient();
        GetOpenIdTokenForDeveloperIdentityRequest tokenRequest = new GetOpenIdTokenForDeveloperIdentityRequest();
        tokenRequest.setIdentityPoolId(IDENTITY_POOL_ID);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(developerProvider, userId.toString());

        tokenRequest.setLogins(map);
        tokenRequest.setTokenDuration(new Long(10001));

        GetOpenIdTokenForDeveloperIdentityResult result = client.getOpenIdTokenForDeveloperIdentity(tokenRequest);
        CognitoUser cognitoUser = new CognitoUser();
    	cognitoUser.setUserId(userId);
    	cognitoUser.setOpenIdToken(result.getToken());
    	cognitoUser.setIdentityId(result.getIdentityId());
        System.out.println(TAG+" identityId = "+result.getIdentityId()+"; getToken = "+result.getToken());
        return cognitoUser;
    }
    
    /**
     * 1.	调用微信开放平台接口，使用code参数换取微信平台的access_token。
		2.	调用微信开放平台接口，使用微信平台的access_token获取微信用户的UnionId。
		3.	根据微信用户的UnionId，在DynamoDB表中查询出本系统中的用户userId。
		4.	以本系统中的用户userId调用Cognito接口获取OpenIdTokenForDeveloperIdentity。
     * @param code
     * @return
     */
    private CognitoUser authenticateUser(String code){
    	WechatUser user = new WechatUser();
        WxClient wxClient = new WxClient();
        LinkedHashMap<String, String> ret = wxClient.getWxToken(code);
        String accessToken = (String) ret.get("access_token");
        String openid = (String) ret.get("openid");
        user = wxClient.getWxUser(accessToken, openid);
        String unionid = user.getUnionid();
//        String unionid = "oYkRw0S93pBic-_9aw86gaFO_Whs";
        System.out.println(TAG+" unionid= "+unionid);

        try{
            AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(REGION)
                    .build();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            user = mapper.load(WechatUser.class, unionid);
            if(user!=null){
            	Integer userId = user.getUserId();
                	System.out.println(TAG+" userid= "+userId);
                	CognitoUser cognitoUser = getCognitoUser(userId);
                    return cognitoUser;	
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return null;
    }
}