package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.DeleteService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import member.command.CommandHandler;

public class DeleteArticleHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/deleteSuccess.jsp";

	private ReadArticleService readService = new ReadArticleService();
	private DeleteService deleteService = new DeleteService();

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

		String noVal = req.getParameter("no"); // �Խñۻ��������� ���ÿ� no�Ķ���� ����
		int no = Integer.parseInt(noVal);

		// ���� ������ �Խñ��� ���Ѵ�. (�Խñ����������������� �����߻���Ŵ)
		try {
			ArticleData articleData = readService.getArticle(no, false);
			User authUser = (User) req.getSession().getAttribute("authUser"); // ���ǰ�ü���� ���� ����ڷα�����
			if (!canModify(authUser, articleData)) { // ����user��ü�� id & ArticleData��ü�� writer�� id ��
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			// ���������� ����ڰ�ü�� �����ϰ�, request�� modReq�Ӽ��� �����Ѵ�.
			ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, articleData.getArticle().getTitle(),
					articleData.getContent());

			req.setAttribute("modReq", modReq);

			Map<String, Boolean> errors = new HashMap<>();
			req.setAttribute("errors", errors);
			modReq.validate(errors); // modifyRequest�� ��ü�� ��ȿ���� �˻��Ѵ�.

			// ����ó��
			if (!errors.isEmpty())
				return FORM_VIEW;

			// �Խñۻ�����ɼ���
			try {
				deleteService.delete(modReq); // �Խñۻ����� �����Ѵ�.
				return "/WEB-INF/view/deleteSuccess.jsp";
			} catch (ArticleNotFoundException e) {
				res.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			} catch (PermissionDeniedException e) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

		} catch (ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId = articleData.getArticle().getWriter().getId(); // ArticleDate�� Article��ü���� writer�� id ����
		return authUser.getId().equals(writerId);
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {

		return null;
	}

}
