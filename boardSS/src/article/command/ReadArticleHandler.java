package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadArticleService;
import member.command.CommandHandler;

public class ReadArticleHandler implements CommandHandler {

	private ReadArticleService readService = new ReadArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String noVal = req.getParameter("no"); // 게시글을 클릭했을때 (기본키Number 가 된다.)
		int articleNum = Integer.parseInt(noVal);
		System.out.println("출력해라");

		try {
			System.out.println("출력해라2");

			// ArticleData articleData = new ArticleData(article객체, cotnent객체) 들어감
			ArticleData articleData = readService.getArticle(articleNum, true);

			req.setAttribute("articleData", articleData);
			System.out.println("출력해라3" + articleData.getContent());

			return "/WEB-INF/view/readArticle.jsp";
		} catch (ArticleNotFoundException | ArticleContentNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
}
