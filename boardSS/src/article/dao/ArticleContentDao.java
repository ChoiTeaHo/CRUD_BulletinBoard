package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.JdbcUtil;

public class ArticleContentDao {

	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("insert into article_content " + "(article_no, content) values (?, ?)");

			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());

			int insertedCount = pstmt.executeUpdate();
			if (insertedCount > 0) {
				System.out.println("실행");
				return content;
			} else {
				return null;
			}
		} finally {
			JdbcUtil.close(pstmt);
		}
	}

	// 3. 조회관련 메소드
	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement("SELECT * FROM article_content WHERE article_no = ?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();

			ArticleContent content = null;
			if (rs.next()) {
				content = new ArticleContent(rs.getInt("article_no"), rs.getString("content"));
				System.out.println("데이터 조회: " + content.getContent());
			}

			return content;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}

	// 4. 게시물수정기능메소드 (내용인 content를 수정)
	public int update(Connection conn, int no, String content) throws SQLException {
		try (PreparedStatement pstmt = conn
				.prepareStatement("UPDATE article_content SET content=? WHERE article_no=?")) {
			pstmt.setString(1, content);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}

	public void delete(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("DELETE FROM article_content where article_no=?");
			pstmt.setInt(1, no);
			pstmt.executeUpdate();

		} catch (Exception e) {
		}
	}
}
