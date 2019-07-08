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

	@Override // config�� web.xml�� ���ǵ� ������ �ǹ��Ѵ�.
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding"); // xml�� encoding �� �� utf-8 ȣ��
		if (encoding == null) { // xml�� ����url ������ /* �̹Ƿ� ��� ���͸��ȴ�.
			encoding = "UTF-8"; // �ᱹ JSP���� ��ûĳ���� ���ڵ��� �������� �ʾƵ� �ȴ�.
		}
	}

	@Override
	public void destroy() {
	}

}
