package kr.swmaestro.hsb.auth;

import java.lang.annotation.Annotation;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.swmaestro.hsb.util.ArgsUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.annotation.Secured;

import com.couchbase.client.CouchbaseClient;

@Aspect
public class UserAuthAOP {
	
	@Resource(name="couchbaseClient")
	CouchbaseClient client;
	
	@Around("execution(* kr.swmaestro.hsb.controller.*.*.*(..))")
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
		

		// 어노테이션을 가져옴
		Annotation[] annotations = joinPoint.getSignature().getDeclaringType().getAnnotations();
		
		for (Annotation annotation : annotations) {
			// Secured 어노테이션의 값을 가져와 인증 시 권한 비교
			if (annotation.annotationType() == Secured.class) {
				Secured secured = (Secured) annotation;
				System.out.println(secured.value());
			}
		}
		
		return joinPoint.proceed();
	}
}
