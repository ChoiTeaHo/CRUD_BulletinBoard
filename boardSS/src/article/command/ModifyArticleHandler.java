package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import member.command.CommandHandler;

public class ModifyArticleHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";

	private ReadArticleService readService = new ReadArticleService();
	private ModifyArticleService modifyService = new ModifyArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}

	private String processForm(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String noVal = req.getParameter("no");
		int no = Integer.parseInt(noVal);

		// 폼에 보여줄 게시글을 구한다. (게시글이존재하지않으면 에러발생시킴)
		try {
			ArticleData articleData = readService.getArticle(no, false);
			User authUser = (User) req.getSession().getAttribute("authUser"); // 세션객체에서 현재 사용자로긴정보
			if (!canModify(authUser, articleData)) { // 세션user객체의 id & ArticleData객체의 writer의 id 비교
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			// 폼에보여줄 사용자객체를 생성하고, request의 modReq속성에 저장한다.
			ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, articleData.getArticle().getTitle(),
					articleData.getContent());

			req.setAttribute("modReq", modReq);

			// 최종리턴
			return FORM_VIEW;
		} catch (ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId = articleData.getArticle().getWriter().getId(); // ArticleDate의 Article객체에서 writer의 id 꺼냄
		return authUser.getId().equals(writerId);
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		User authUser = (User) req.getSession().getAttribute("authUser"); // 게시글수정을 요청한 사용자정보구한다.
		String noVal = req.getParameter("no");
		int no = Integer.parseInt(noVal);

		// 요청파라미터와 현재 사용자정보를 이용해서 ModifyRequest객체를 생성한다.
		ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, req.getParameter("title"),
				req.getParameter("content"));
		req.setAttribute("modReq", modReq); // 생성한 ModifyRequest객체를 request의 modReq속성에 저장한다.

		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		modReq.validate(errors); // modifyRequest의 객체가 유효한지 검사한다.

		if (!errors.isEmpty())
			return FORM_VIEW;
		try {
			modifyService.modify(modReq); // 게시글수정기능을 실행한다.
			return "/WEB-INF/view/modifySuccess.jsp";
		} catch (ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} catch (PermissionDeniedException e) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}

}
