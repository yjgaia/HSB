package kr.swmaestro.hsb.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AuthAop {
	
	@Around("@annotation(kr.swmaestro.hsb.auth.Auth)")
	public void setAroundUserAuth(ProceedingJoinPoint joinPoint) throws Throwable {
		
		AuthFilter.setNeedAuth(true);
		
		joinPoint.proceed();
	}
}
