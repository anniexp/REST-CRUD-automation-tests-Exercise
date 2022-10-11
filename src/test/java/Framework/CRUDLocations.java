package Framework;

import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDLocations {
    public static JsonObject createLocationRequestBody(String str1 ){
        JsonObject value = new JsonObject();
        value.put("address",str1);

        return value;
    }
}
