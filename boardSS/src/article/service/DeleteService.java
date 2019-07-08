package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class DeleteService {
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();

	public void delete(ModifyRequest modReq) {
		Connection conn = null;

		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);

			// modReq객체안에 특정데이터에 대한 기본키가 존재하는지검사
			Article article = articleDao.selectById(conn, modReq.getArticleNumber());
			if (article == null)
				throw new ArticleNotFoundException();

			if (!canModify(modReq.getUserId(), article))
				throw new PermissionDeniedException();

			// 삭제기능수행
			articleDao.delete(conn, modReq.getArticleNumber());
			contentDao.delete(conn, modReq.getArticleNumber());

			conn.commit();

		} catch (SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch (PermissionDeniedException e) {
			JdbcUtil.rollback(conn);
			throw e;
		} finally {
			JdbcUtil.close(conn);
		}
	}

	private boolean canModify(String userId, Article article) {
		return article.getWriter().getId().equals(userId);
	}
}
