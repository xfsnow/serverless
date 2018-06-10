package cn.aws.cognitowx;

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

public class CognitoWx implements RequestHandler<Object, Object> {

	public static final String TAG = "RH";

	// 以下常量替换为你的应用从微信开放平台申请到的应用 ID 和密钥
    public static final String WX_APP_ID = "";
    public static final String WX_APP_SECRET = "";

	// 以下常量替换为你在 AWS 配置的身份池的相应值
    private static final Regions REGION = Regions.CN_NORTH_1;
    private static final String IDENTITY_POOL_ID = "cn-north-1:";
    private static final String developerProvider = "cn.aws.cognitowx";

    @Override
    public CognitoWxResponse handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

        CognitoWxResponse cwsResponse = new CognitoWxResponse();
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> inputHashMap = (LinkedHashMap<String, String>)input;
        User user = authenticateUser(inputHashMap);

        if(user!=null){
            cwsResponse.setUserId(user.getUserId());
            cwsResponse.setStatus("true");
            cwsResponse.setOpenIdToken(user.getOpenIdToken());
            cwsResponse.setIdentityId(user.getIdentityId());
        }
        return cwsResponse;
    }

    private User authenticateUser(LinkedHashMap<String, String> input){
        User user=new User();
        String code = (String) input.get("code");
        WxClient wxClient = new WxClient();
        LinkedHashMap<String, String> ret = wxClient.getWxToken(code);
        String accessToken = (String) ret.get("access_token");
        String openid = (String) ret.get("openid");

//        String accessToken = "10_GtHMiRbP56RgVmhpYe3hRE87tORAdLcBdZhW7KT84wmQJGqmaZ54I0dTUMZOoarjGnStGX3zlpeTo-QW0GvjAG5QXCW55hXDSTy67GD-6io";
//        String openid = "oev1k0stQYV3JWRIDW8-tslvM9vo";

        user = wxClient.getWxUser(accessToken, openid);
        String unionid = user.getUnionid();
        System.out.println(TAG+" unionid= "+unionid);

        try{
            final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard()
                    .withRegion(REGION)
                    .build();
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            user = mapper.load(User.class, unionid);
            if(user!=null){
            	Integer userId = user.getUserId();
                	System.out.println(TAG+" userid= "+userId);
                	GetOpenIdTokenForDeveloperIdentityResult result = getOpenIdToken(userId);
                    user.setOpenIdToken(result.getToken());
                    user.setIdentityId(result.getIdentityId());
                    System.out.println(TAG+" identityId= "+user.getIdentityId());
                    return user;
            }
        }catch(Exception e){
            System.out.println(e.toString());
        }
        return user;
    }

    /**
     * 使用用户ID 调取 CognitoIdentity的 token
     * @param userId
     * @return token
     */
    private GetOpenIdTokenForDeveloperIdentityResult getOpenIdToken(Integer userId){
    	AmazonCognitoIdentity client = AmazonCognitoIdentityClientBuilder.defaultClient();
        GetOpenIdTokenForDeveloperIdentityRequest tokenRequest = new GetOpenIdTokenForDeveloperIdentityRequest();
        tokenRequest.setIdentityPoolId(IDENTITY_POOL_ID);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(developerProvider, userId.toString());

        tokenRequest.setLogins(map);
        tokenRequest.setTokenDuration(new Long(10001));

        GetOpenIdTokenForDeveloperIdentityResult result = client.getOpenIdTokenForDeveloperIdentity(tokenRequest);
        return result;
    }
}
