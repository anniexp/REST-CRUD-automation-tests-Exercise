package Framework;

import com.github.cliftonlabs.json_simple.JsonObject;

public class Login {
    public static JsonObject createLoginRequestBody(String email, String password ){
        JsonObject value = new JsonObject();
        value.put("email",email);
        value.put("password",password);

        return value;
    }
}
