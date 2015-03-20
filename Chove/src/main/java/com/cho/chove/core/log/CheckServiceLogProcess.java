package com.cho.chove.core.log;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Admin에서 발생하는 컨트롤러, 즉 서비스를 타겟으로 얼마나 탓는지 시간은 얼마나 걸렷는지를 로그로 쌓아주는 Logging서비스
 * @author 조호영
 * @since 2014.12.26
 * @version 0.1
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *  수정일                   수정자         수정내용
 *  ----------  ----    ---------------------------
 *  2014.12.26  조호영          최초 작성
 *
 * </pre>
 */

/**
 * Check process time in 
 * @author ChoHoyoung
 * @since 2015.03.20
 * @version 0.1
 * @see
 *
 * <pre>
 * << Modification Information >>
 *   
 *  UpdateDate  Updator     Description
 *  ----------  ----------  ---------------------------
 *  2014.03.20  ChoHoyoung  initialize
 *
 * </pre>
 */

@Aspect
public class CheckServiceLogProcess {
	static final Logger log = LoggerFactory.getLogger(CheckServiceLogProcess.class);
	
	@Around("execution(* com.cho.chov.app.*.controller.*.*(..))")
	public Object invoke(final ProceedingJoinPoint pjp) throws Throwable {
		Object[] args = pjp.getArgs();
		Long startTime = System.nanoTime();
		Long startCTime = System.currentTimeMillis();

		Object proceed = pjp.proceed(args);

		Long endTime = System.nanoTime();
		Long endCTime = System.currentTimeMillis();
		Long ime = endTime - startTime;
		
		StringBuilder logBuf = new StringBuilder();
		logBuf.append(startCTime);
		logBuf.append(" ");
		logBuf.append(endCTime);
		logBuf.append(" Process time : ");
		logBuf.append(TimeUnit.SECONDS.convert(ime, TimeUnit.NANOSECONDS));
		logBuf.append(" Seconds ");
		logBuf.append(pjp.getTarget().getClass().getName());
		logBuf.append(" ");
		logBuf.append(pjp.getSignature().getName());
		logBuf.append(" ");
		
		log.info(logBuf.toString());

		return proceed;
	}
}