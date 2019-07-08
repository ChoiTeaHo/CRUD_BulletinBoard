package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class WriteArticleService {
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();

	// write() 메소드 //user객체의 id,pw 그리고 req의 title, content 내용을담고있는 WriteRequest리턴
	public Integer write(WriteRequest req) {

		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			Article article = toArticle(req); // 이부분에서 WriteRequest객체의 내용을 Article로 리턴(date정보를추가함)
			Article savedArticle = articleDao.insert(conn, article); // DB 삽입(그리고 최근추가한 Article객체를 받음)
			if (savedArticle == null) {
				throw new RuntimeException("Fail to insert article");
			}

			// 위에서리턴한 getNumber최근추가 받음, 입력한 content받음 ( ArticleContent객체는 Num필드와
			// content필드밖에없다. 즉, 이 부분은 Content 게시글의대한 내용을들을 DB에 삽입한다.)
			ArticleContent content = new ArticleContent(savedArticle.getNumber(), req.getContent());
			ArticleContent savedContent = contentDao.insert(conn, content);
			if (savedContent == null) {
				throw new RuntimeException("Fail to insert article_content");
			}

			conn.commit();

			// savedArticle.setNumber(0); //자동증가값에대한 처리 필요할듯
			return savedArticle.getNumber();

		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private Article toArticle(WriteRequest req) {
		Date now = new Date();
		return new Article(null, req.getWriter(), req.getTitle(), now, now, 0);
	}

}