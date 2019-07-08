package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import member.command.CommandHandler;

public class ListArticleHandler implements CommandHandler {

	private ListArticleService listService = new ListArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String pageNoVal = req.getParameter("pageNo"); // pageNoVal ������ ��û���� ������ ����
		int pageNo = 1; // �ϴ� pageNo ���� 1�μ���

		if (pageNoVal != null) { // ���� pageNoVal ��û���� ������ PageNo�� ����
			pageNo = Integer.parseInt(pageNoVal);
		}

		ArticlePage articlePage = listService.getArticlePage(pageNo);
		req.setAttribute("articlePage", articlePage); // articlePage�Ӽ��� �������� ����� ArticlePage ��ü����

		return "/WEB-INF/view/listArticle.jsp";
	}

}
