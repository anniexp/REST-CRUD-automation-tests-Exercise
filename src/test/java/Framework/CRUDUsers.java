package Framework;

import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDUsers {
    public static JsonObject createUserRequestBody(String str1, String str2, String str3 ){
        JsonObject value = new JsonObject();
        value.put("email",str1);
        value.put("fullName",str2);
        value.put("password", str3);

        return value;
    }
}
