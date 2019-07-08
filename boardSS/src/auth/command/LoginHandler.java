package auth.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.LoginFailException;
import auth.service.LonginService;
import auth.service.User;

public class LoginHandler implements member.command.CommandHandler {

	private static final String FROM_VIEW = "/WEB-INF/view/loginForm.jsp";
	private LonginService loginService = new LonginService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		}

		return null;
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FROM_VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String id = trim(req.getParameter("id"));
		String password = trim(req.getParameter("password")); // ���������� ID Password �Ķ���͸� ���Ѵ�.

		Map<String, Boolean> errors = new HashMap<>(); // ���������� ���� Map��ü�� �����ϰ� error�Ӽ��� ����.
		req.setAttribute("errors", errors);

		if (id == null || id.isEmpty()) { // id���� ������ �ʰ�ü�� �����߰�
			errors.put("id", Boolean.TRUE);
		}
		if (password == null || password.isEmpty()) { // password���� ������ �ʰ�ü�� �߰�
			errors.put("password", Boolean.TRUE);
		}

		if (!errors.isEmpty()) { // ������ �����ϸ� ���並 �����Ѵ�.
			return FROM_VIEW;
		}

		try {
			// loginServiceŬ������ login���� �����������Ѵ�.
			User user = loginService.login(id, password);// �α��ο� �����ϸ� User��ü�� �����Ѵ�
			req.getSession().setAttribute("authUser", user); // User��ü�� ���ǿ� authUser �Ӽ��� �����Ѵ�.
			res.sendRedirect(req.getContextPath() + "/index.jsp"); // inddex.jsp�� �����̷�Ʈ�Ѵ�.
			return null;
		} catch (LoginFailException e) { // �α��ο� �����ؼ� LoginFaileException�� �߻��ϸ� �ش翡���� �߰��Ѵ�.
			errors.put("idOrPwNotMatch", Boolean.TRUE); // �׸��� ���並 �����Ѵ�.
			return FROM_VIEW;
		}

	}

	private String trim(String str) {
		return str == null ? null : str.trim();
	}

}
