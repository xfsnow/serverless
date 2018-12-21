package cn.amazonaws.lambda.cognitowechat;

import java.util.LinkedHashMap;
import org.json.JSONObject;
import cn.amazonaws.lambda.cognitowechat.util.HttpConnection;

public class WxClient {
	public static final String TAG = "WXC";

	// 以下常量替换为你的应用从微信开放平台申请到的应用 ID 和密钥
	public static final String WX_APP_ID = "";
	public static final String WX_APP_SECRET = "";
	public static final String WX_AUTH_SCOPE = "snsapi_userinfo";
	public static final String WX_AUTH_STATE = "wechat_sdk_demo_test";
	public static final String WX_GRANT_TYPE = "authorization_code";

	public LinkedHashMap<String, String> getWxToken(String code) {
		LinkedHashMap<String, String> ret = new LinkedHashMap<String, String>() ;
		try {
			String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WX_APP_ID
				+ "&secret=" + WX_APP_SECRET
				+ "&code=" + code
				+ "&grant_type="+WX_GRANT_TYPE;
			System.out.println(TAG+" accessTokenUrl="+accessTokenUrl);
			String response = HttpConnection.get(accessTokenUrl);
			JSONObject jsonToken = new JSONObject(response);
			ret.put("access_token", jsonToken.getString("access_token"));
			ret.put("openid", jsonToken.getString("openid"));
			System.out.println(TAG+" access_token="+ret.get("access_token")+", openid= "+ ret.get("openid"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public WechatUser getWxUser(String accessToken, String openid) {
		WechatUser user = new WechatUser();
		try {
			String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
			System.out.println(TAG+" userUrl="+userUrl);
			String responseDataUser = HttpConnection.get(userUrl);
			JSONObject jsonUser = new JSONObject(responseDataUser);
//			String nickname = jsonUser.getString("nickname");
//			String headimgurl = jsonUser.getString("headimgurl");
			String unionid = jsonUser.getString("unionid");
			System.out.println(TAG+" unionid ="+unionid);
			user.setUnionid(unionid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
