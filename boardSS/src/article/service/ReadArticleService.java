package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;

public class ReadArticleService {
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();

	public ArticleData getArticle(int articleNum, boolean increaseReadCount) {

		try (Connection conn = ConnectionProvider.getConnection()) {

			Article article = articleDao.selectById(conn, articleNum); // selectById실행 특정기본키로 article검사후 객체리턴
			System.out.println("article selectById 실행");
			if (article == null)
				throw new ArticleNotFoundException(); // 없으면 에러

			ArticleContent content = contentDao.selectById(conn, articleNum); // contentDao실행 특정 content검사후 객체리턴
			System.out.println("content selectById 실행");
			if (content == null)
				throw new ArticleContentNotFoundException(); // 없으면 에러

			if (increaseReadCount)
				articleDao.increaseReadCount(conn, articleNum); // increaseReadCount실행 특정 alrticle검사후 readCount+1
			System.out.println("increaseReadCount 실행 (조회수1업)");
			// ###최종 ArticleData 리턴
			System.out.println("알티클: " + content.getContent());
			return new ArticleData(article, content);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
