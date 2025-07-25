package egovframework.com.cmm.interceptor;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQL 로그 인터셉터
 * @author 양민우
 * @since 2025.05.07
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.05.07  양민우          최초 생성
 *  </pre>
 */
@Intercepts({
	@Signature(
		type= Executor.class,
		method = "query",
		args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
	),
	@Signature(
		type= Executor.class,
		method = "update",
		args = {MappedStatement.class, Object.class}
	)
})
public class SqlLogInterceptor implements Interceptor {
	// 고정된 로거 이름 하나만 선언
	private static final Logger LOGGER = LoggerFactory.getLogger("SqlLogger");
	private static final Pattern PARAM = Pattern.compile("\\?");

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
		Object paramObject = invocation.getArgs()[1];

		BoundSql bound = ms.getBoundSql(paramObject);
		String sql = bound.getSql(); // 멀티라인 그대로 유지

		// 파라미터 바인딩
		List<ParameterMapping> pmaps = bound.getParameterMappings();
		for (ParameterMapping pm : pmaps) {
			String prop = pm.getProperty();
			Object val;

			if (bound.hasAdditionalParameter(prop)) {
				// MyBatis 내부 추가 파라미터
				val = bound.getAdditionalParameter(prop);
			} else if (paramObject instanceof Map) {
				// 여러 @Param 이거나 Map일 때
				val = ((Map<?, ?>) paramObject).get(prop);
			} else {
				// 단일 기본 타입(String, Integer, …) 이면
				val = paramObject;
			}

			String s = format(val);
			// 순차적으로 ? 하나씩 치환
			sql = PARAM.matcher(sql).replaceFirst(Matcher.quoteReplacement(s));
		}

		// 실행
		long start = System.currentTimeMillis();
		Object result = invocation.proceed();
		long elapsed = System.currentTimeMillis() - start;

		// SLF4J 로그: 첫 줄에 Mapper ID + 실행시간, 두 번째 줄부터 SQL
		LOGGER.debug("{} | Time: {}ms\n        {}", ms.getId(), elapsed, sql);

		return result;
	}

	private String format(Object o) {
		if (o == null)
			return "NULL";
		if (o instanceof String)
			return "'" + o + "'";
		if (o instanceof Date) {
			String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(o);
			return "'" + ts + "'";
		}
		return o.toString();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(java.util.Properties props) {
		// no-op
	}
}
