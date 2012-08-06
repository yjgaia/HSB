package kr.swmaestro.hsb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/** 
 * @author 이한솔
 */
public class ArgsUtil {
	/**
	 * 아규먼트의 목록을 받아서 그 중에 HttpServletRequest 를 찾아 리턴한다.
	 * @param args 아규먼트 배열
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequestByArgs(Object[] args) {

		// 아규먼트 갯수만큼 루프 돌림
		if( args != null ) {
			for( int i = 0 ; i < args.length ; i++ ) {
				if( args[i] instanceof HttpServletRequest ) {
					return (HttpServletRequest) args[i];
				}
			}
		}
		return null;
	}

	/**
	 * 아규먼트의 목록을 받아서 그 중에 HttpServletResponse 를 찾아 리턴한다.
	 * @param args 아규먼트 배열
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponseByArgs(Object[] args) {

		// 아규먼트 갯수만큼 루프 돌림
		if( args != null ) {
			for( int i = 0 ; i < args.length ; i++ ) {
				if( args[i] instanceof HttpServletResponse ) {
					return (HttpServletResponse) args[i];
				}
			}
		}
		return null;
	}


	/**
	 * 아규먼트의 목록을 받아서 Class 배열로 리턴한다.
	 */
	public static Class<?>[] getClassArray(Object[] args) {

		// 아규먼트 갯수만큼 루프 돌림
		if( args != null ) {
			Class<?>[] clazz = new Class<?>[args.length];
			for( int i = 0 ; i < args.length ; i++ ) {
				clazz[i] = args[i].getClass();
			}
			return clazz;
		}
		return null;
	}
}
