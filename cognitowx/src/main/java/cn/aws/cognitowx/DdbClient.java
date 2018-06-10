package cn.aws.cognitowx;

public class DdbClient {

    public static final String TAG = "ddb";

    // 以下常量替换为你的应用从微信开放平台申请到的应用 ID 和密钥
    public static final String WX_APP_ID = "wx8d52df84e93e18b3";
    public static final String WX_APP_SECRET = "7b71bc92b349e5c8096c7a752ff520e4";

    public static final String WX_AUTH_SCOPE = "snsapi_userinfo";
    public static final String WX_AUTH_STATE = "wechat_sdk_demo_test";

    public static final String WX_GRANT_TYPE = "authorization_code";
}
