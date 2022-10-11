package Framework;

import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDTypes {
    public static JsonObject createTypeRequestBody(String str1 ){
        JsonObject value = new JsonObject();
        value.put("name",str1);

        return value;
    }
}
