package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import member.command.CommandHandler;

public class WriteArticleHandler implements CommandHandler {
	private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";
	private WriteArticleService writeService = new WriteArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res); // 처음에 get방식호출로 시작

		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);

		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);

		User user = (User) req.getSession().getAttribute("authUser");
		System.out.println("실행2");
		System.out.println("세션객체 유저아이디: " + user.getId());
		System.out.println("세션객체 유저이름: " + user.getName());

		WriteRequest writeReq = createWriteRequest(user, req);
		System.out.println("실행3");
		System.out.println(
				writeReq.getWriter().getName() + ":" + writeReq.getWriter().getId() + ":" + writeReq.getTitle() + ":");

		writeReq.validate(errors);
		System.out.println("실행4");

		// 에러가 하나라도 존재시 이동 다시 게시물쓰기로 이동
		if (!errors.isEmpty())
			return FORM_VIEW;

		int newArticleNo = writeService.write(writeReq); // 최근 게시물에대한 (saveArticle객체) 의 최근추가Number를 받음
		System.out.println("실행5");

		req.setAttribute("newArticleNo", newArticleNo); // newArticleNO 속성에 최근 추가 Number를 저장
		System.out.println("실행6");
		System.out.println("saveArticle 객체 반환값: " + newArticleNo);

		return "/WEB-INF/view/newArticleSuccess.jsp";
	}

	private WriteRequest createWriteRequest(User user, HttpServletRequest req) {
		return new WriteRequest(new Writer(user.getId(), user.getName()), req.getParameter("title"),
				req.getParameter("content"));
	}

}
