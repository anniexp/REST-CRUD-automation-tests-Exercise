package Framework;

import com.github.cliftonlabs.json_simple.JsonObject;

public class CRUDItems {
    public static JsonObject createItemRequestBody(String str1,String str2,String str3,
                                                   String str4,String str5,String str6,String str7 ){
        JsonObject value = new JsonObject();
        value.put("assigneeEmail",str1);
        value.put("inventoryNumber",str2);
        value.put("location",str3);
        value.put("model",str4);
        value.put("ownerEmail",str5);
        value.put("serialNumber",str6);
        value.put("type",str7);

        return value;
    }
}
