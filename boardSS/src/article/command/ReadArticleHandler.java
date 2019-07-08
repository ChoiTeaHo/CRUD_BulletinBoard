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
		String noVal = req.getParameter("no"); // �Խñ��� Ŭ�������� (�⺻ŰNumber �� �ȴ�.)
		int articleNum = Integer.parseInt(noVal);
		System.out.println("����ض�");

		try {
			System.out.println("����ض�2");

			// ArticleData articleData = new ArticleData(article��ü, cotnent��ü) ��
			ArticleData articleData = readService.getArticle(articleNum, true);

			req.setAttribute("articleData", articleData);
			System.out.println("����ض�3" + articleData.getContent());

			return "/WEB-INF/view/readArticle.jsp";
		} catch (ArticleNotFoundException | ArticleContentNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
}
