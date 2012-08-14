import redis.clients.jedis.Jedis;


public class foooooo {
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("hanul.co");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
	}

}
