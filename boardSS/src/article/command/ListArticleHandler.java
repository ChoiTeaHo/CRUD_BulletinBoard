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

		String pageNoVal = req.getParameter("pageNo"); // pageNoVal 변수에 요청값을 가져와 저장
		int pageNo = 1; // 일단 pageNo 값을 1로설정

		if (pageNoVal != null) { // 만약 pageNoVal 요청값이 없으면 PageNo에 저장
			pageNo = Integer.parseInt(pageNoVal);
		}

		ArticlePage articlePage = listService.getArticlePage(pageNo);
		req.setAttribute("articlePage", articlePage); // articlePage속성에 페이지를 계산한 ArticlePage 객체대입

		return "/WEB-INF/view/listArticle.jsp";
	}

}
