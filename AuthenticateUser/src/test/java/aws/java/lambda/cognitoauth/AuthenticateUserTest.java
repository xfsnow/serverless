package aws.java.lambda.cognitoauth;

//import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class AuthenticateUserTest {

    private static Object input;

    @BeforeClass
    public static void createInput()  {
        // TODO: set up your sample input object here.
    	HashMap<String, String> input = new LinkedHashMap<String, String>();
        input.put("userName", "Dhruv");
        input.put("passwordHash","8743b52063cd84097a65d1633f5c74f5");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");
        createInput();

        return ctx;
    }

    @Test
    public void testAuthenticateUser() {
        AuthenticateUser handler = new AuthenticateUser();
        Context ctx = createContext();

        AuthenticateUserResponse output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        Assert.assertEquals("Hello from Lambda!", output);
    }
    
    @Test	
    public void testhandleRequest() {
        AuthenticateUser handler = new AuthenticateUser();
        Context ctx = createContext();
        
        AuthenticateUserResponse output = (AuthenticateUserResponse)handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        System.out.println(output.getStatus());
        if (output.getStatus().equalsIgnoreCase("true")) {
            System.out.println("AuthenticateUser JUnit Test Passed");
        }else{
        	Assert.fail("AuthenticateUser JUnit Test Failed");
        }
    }
}
