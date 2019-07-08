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

			Article article = articleDao.selectById(conn, articleNum); // selectById���� Ư���⺻Ű�� article�˻��� ��ü����
			System.out.println("article selectById ����");
			if (article == null)
				throw new ArticleNotFoundException(); // ������ ����

			ArticleContent content = contentDao.selectById(conn, articleNum); // contentDao���� Ư�� content�˻��� ��ü����
			System.out.println("content selectById ����");
			if (content == null)
				throw new ArticleContentNotFoundException(); // ������ ����

			if (increaseReadCount)
				articleDao.increaseReadCount(conn, articleNum); // increaseReadCount���� Ư�� alrticle�˻��� readCount+1
			System.out.println("increaseReadCount ���� (��ȸ��1��)");
			// ###���� ArticleData ����
			System.out.println("��ƼŬ: " + content.getContent());
			return new ArticleData(article, content);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
