import redis.clients.jedis.Jedis;

public class foooooo {
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("hanul.co");
		
		String[] ids = new String[10000];
		
		for (int i = 0 ; i < 10000 ; i++) {
			String id = Integer.toString(i);
			jedis.set(id, "test");
			ids[i] = id;
		}
		//jedis.set("foo", "bar");
		System.out.println(jedis.mget(ids));
		//System.out.println(jedis.smembers("L"));
	}

}
