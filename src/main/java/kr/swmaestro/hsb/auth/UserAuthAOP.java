package kr.swmaestro.hsb.auth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.swmaestro.hsb.util.ArgsUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.couchbase.client.CouchbaseClient;

@Aspect
public class UserAuthAOP {
	
	@Resource(name="couchbaseClient")
	CouchbaseClient client;
	
	@Around("args(HttpServletRequest)&&execution(* kr.swmaestro.hsb.controller.*.*.*(..))")
	public Object setAroundUserAuth(ProceedingJoinPoint joinPoint) throws Throwable {
		
		System.out.println("---------@Around--------");
		String declaringType=joinPoint.getSignature().getDeclaringType().toString();
		HttpServletRequest request=ArgsUtil.getRequestByArgs(joinPoint.getArgs());
		if(request!=null){
			HttpSession session=request.getSession();
			System.out.println("sessionId?:"+session.getId());
			System.out.println("isSessionInCB?"+client.get("sessionId:"+session.getId()));
		}else{
			System.out.println("no request arg");
		}
		
		return joinPoint.proceed();
	}
}
