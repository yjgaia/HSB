import redis.clients.jedis.Jedis;

public class foooooo {
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("hanul.co");
		//jedis.set("foo", "bar");
		//System.out.println(jedis.mget(new String[]{"foo", "ttt"}));
		System.out.println(jedis.smembers("L"));
	}

}
