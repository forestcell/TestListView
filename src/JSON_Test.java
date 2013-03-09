import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import junit.framework.TestCase;


public class JSON_Test extends TestCase {

	public void testStringObject(){
		JSONObject item = new JSONObject();	
		item.put("postid", 1);
		item.put("thumbnail", 0);
		String itemstr = item.toString();
		JSONArray jarr = new JSONArray();
		jarr.add(item);
		
		System.out.println(jarr.toString());
		
		JSONObject item2 = JSONObject.parseObject(itemstr);		
		String itemstr2 = item2.toJSONString();
		System.out.println(itemstr);
		System.out.println(itemstr2);
		
		JSONArray jarr2 = JSONArray.parseArray(jarr.toString());
		System.out.println(jarr2.toString());
		
		String strjson = "[{\"avatar\":\"0\",\"avatardateline\":\"0\",\"message\":\"ÆÚ´ýiosµÄapp~\",\"postdate\":\"2013-02-09\",\"postid\":\"1141830\",\"posttime\":\"19:03:23\",\"thumbnail\":0,\"userid\":319488,\"username\":\"trackies\"}]";
		JSONArray jarr3 = JSONArray.parseArray(strjson);
		System.out.println(jarr3.toString());
		assertEquals(itemstr, itemstr2);
		
	}	
	
    public void testAdd() {
        double result= 0.1+0.4;
        assertTrue(1 == 1);
     }
	
}
