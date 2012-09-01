import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PerformanceTestTool {

	private final static String BASE_ADRESS = "http://localhost:8080/HSB";
	private final static String USERNAME = "test1";
	private final static String PASSWORD = "test";
	private final static boolean P = false;
	private final static int COUNT = 100;

	// 인증
	public static String auth() throws Exception {
		URL url = new URL(BASE_ADRESS + "/user/auth");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/534.52.7 (KHTML, like Gecko) Version/5.1.2 Safari/534.52.7");
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("Cache-Control", "max-age=0");
		connection.setRequestMethod("POST");

		String charset = "UTF-8";
		String query = String.format("username=%s&password=%s", URLEncoder.encode(USERNAME, charset), URLEncoder.encode(PASSWORD, charset));
		
		connection.getOutputStream().write(query.getBytes(charset));

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			if (line.indexOf("\"generatedSecureKey\":") != -1) {
				return line.substring(line.indexOf("\"generatedSecureKey\":") + 22, line.indexOf("\"generatedSecureKey\":") + 62);
			}
		}
		return null;
	}

	// 글 작성
	public static void write(String secureKey) throws Exception {
		URL url = new URL(BASE_ADRESS + "/" + USERNAME);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/534.52.7 (KHTML, like Gecko) Version/5.1.2 Safari/534.52.7");
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("Cache-Control", "max-age=0");
		connection.setRequestMethod("POST");

		String charset = "UTF-8";
		String content = "테스트 중입니다.";
		String query = String.format("secureKey=%s&content=%s", URLEncoder.encode(secureKey, charset), URLEncoder.encode(content, charset));
		
		connection.getOutputStream().write(query.getBytes(charset));

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			if (P) {
				System.out.println(line);
			}
		}
	}

	// 글 목록 보기
	public static void list(String secureKey) throws Exception {
		URL url = new URL(BASE_ADRESS + "/" + USERNAME);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/534.52.7 (KHTML, like Gecko) Version/5.1.2 Safari/534.52.7");
		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		connection.setRequestProperty("Cache-Control", "max-age=0");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String line;
		while ((line = br.readLine()) != null) {
			if (P) {
				System.out.println(line);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String secureKey = auth();
		
		long start = System.currentTimeMillis();
		for (int i = 0 ; i < COUNT ; i++) {
			write(secureKey);
		}
		long end = System.currentTimeMillis();
		System.out.println("입력에 " + (end - start) / 1000.0 + "초 걸림");
		
		start = System.currentTimeMillis();
		for (int i = 0 ; i < COUNT ; i++) {
			list(secureKey);
		}
		end = System.currentTimeMillis();
		System.out.println("출력에 " + (end - start) / 1000.0 + "초 걸림");
		
	}

}
