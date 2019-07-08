package util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {

	private String encoding;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		req.setCharacterEncoding(encoding);
		chain.doFilter(req, res);
	}

	@Override // config는 web.xml에 정의된 내용을 의미한다.
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding"); // xml의 encoding 의 값 utf-8 호출
		if (encoding == null) { // xml의 적용url 패턴은 /* 이므로 모두 필터링된다.
			encoding = "UTF-8"; // 결국 JSP마다 요청캐릭터 인코딩을 설정하지 않아도 된다.
		}
	}

	@Override
	public void destroy() {
	}

}
