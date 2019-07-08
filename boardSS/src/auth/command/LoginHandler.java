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
		String password = trim(req.getParameter("password")); // 폼에전송한 ID Password 파라미터를 구한다.

		Map<String, Boolean> errors = new HashMap<>(); // 에러정보를 담을 Map객체를 생성하고 error속성에 저장.
		req.setAttribute("errors", errors);

		if (id == null || id.isEmpty()) { // id값이 없으면 맵객체에 에러추가
			errors.put("id", Boolean.TRUE);
		}
		if (password == null || password.isEmpty()) { // password값이 없으면 맵객체에 추가
			errors.put("password", Boolean.TRUE);
		}

		if (!errors.isEmpty()) { // 에러가 존재하면 폼뷰를 리턴한다.
			return FROM_VIEW;
		}

		try {
			// loginService클래스의 login으로 인증을수행한다.
			User user = loginService.login(id, password);// 로그인에 성공하면 User객체를 리턴한다
			req.getSession().setAttribute("authUser", user); // User객체를 세션에 authUser 속성에 저장한다.
			res.sendRedirect(req.getContextPath() + "/index.jsp"); // inddex.jsp로 리다이렉트한다.
			return null;
		} catch (LoginFailException e) { // 로그인에 실패해서 LoginFaileException이 발생하면 해당에러를 추가한다.
			errors.put("idOrPwNotMatch", Boolean.TRUE); // 그리고 폼뷰를 리턴한다.
			return FROM_VIEW;
		}

	}

	private String trim(String str) {
		return str == null ? null : str.trim();
	}

}
