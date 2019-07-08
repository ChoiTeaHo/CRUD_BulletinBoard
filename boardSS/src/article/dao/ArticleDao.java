package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.JdbcUtil;

public class ArticleDao {

	public Article insert(Connection conn, Article article) throws SQLException {
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("insert into article "
					+ "(writer_id, writer_name, title, regdate, moddate, read_cnt) " + "values(?,?,?,?,?,0)");

			pstmt.setString(1, article.getWriter().getId()); // user id리턴
			pstmt.setString(2, article.getWriter().getName()); // user pw리턴
			pstmt.setString(3, article.getTitle()); // title(제목) 리턴
			pstmt.setTimestamp(4, toTimestamp(article.getRegDate())); // date정보리턴
			pstmt.setTimestamp(5, toTimestamp(article.getModifiedDate())); // date정보리턴

			int insertedCount = pstmt.executeUpdate(); // insert하여 삽입함 (그리고 몇줄리턴했는지 개수저장)

			if (insertedCount > 0) { // 존재한다면 최근 등록한 기본키 article_no를 int로 추가하여 Article객체 리턴
				stmt = conn.createStatement();
				rs = stmt.executeQuery("select last_insert_id() from article"); // 테이블에 추가된 최근 레코드의 기본키구함
				if (rs.next()) { // 즉 , insert로 추가된 article_no칼럼값으로 리턴
					Integer newNum = rs.getInt(1); // 쿼리문 1번째 필드를 Integer형태로 받는다. //즉 기본키 자동증가를받는다.
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegDate(),
							article.getModifiedDate(), 0);
				}
			}
			return null;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
			JdbcUtil.close(pstmt);
		}
	}

	// (2)게시글목록조회기능
	public int selectCount(Connection conn) throws SQLException {

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT COUNT(*) FROM article");
			if (rs.next())
				return rs.getInt(1);

			return 0;
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}

	// (2-1)저장한범위의 게시글을 읽는 기능
	public List<Article> select(Connection conn, int startRow, int size) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			pstmt = conn.prepareStatement("SELECT * FROM article ORDER BY article_no DESC LIMIT ?, ?");
			pstmt.setInt(1, startRow); // 0부터
			pstmt.setInt(2, size); // 10까지
			rs = pstmt.executeQuery();

			List<Article> result = new ArrayList<>();
			while (rs.next()) {
				result.add(convertArticle(rs));
			}

			return result;
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
	}

	private Article convertArticle(ResultSet rs) throws SQLException {

		Article article = new Article(rs.getInt("article_no"), // 기본키
				new Writer(rs.getString("writer_id"), // userID
						rs.getString("writer_name")), // userName
				rs.getString("title"), // title
				new Date(rs.getTimestamp("regdate").getTime()), // Date관련
				new Date(rs.getTimestamp("moddate").getTime()), // Date관련
				rs.getInt("read_cnt")); // read_cnt

		return article;
	}

	// 3. 특정번호에 해당하는 게시글데이터 읽기
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			pstmt = conn.prepareStatement("SELECT * FROM article WHERE article_no=?");
			pstmt.setInt(1, no);
			rs = pstmt.executeQuery();

			Article article = null;
			if (rs.next()) {
				article = convertArticle(rs);
			}

			return article;
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
	}

	// 3-1 특정게시글의 조회수 카운트하기
	public void increaseReadCount(Connection conn, int no) throws SQLException {
		try (PreparedStatement pstmt = conn
				.prepareStatement("UPDATE article SET read_cnt = read_cnt + 1 WHERE article_no =?")) {
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}

	// 4. 게시물 수정기능 ...moddate 칼럼의 값을 현재시간으로 설정해서 최근수정기록
	public int update(Connection conn, int no, String title) throws SQLException {
		try (PreparedStatement pstmt = conn
				.prepareStatement("UPDATE article SET title=?, moddate=NOW() WHERE article_no=?")) {
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}

	// 5. 게시물삭제기능...
	public void delete(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement("DELETE FROM article where article_no=?");
			pstmt.setInt(1, no);
			pstmt.executeUpdate();

		} catch (Exception e) {
		}
	}

	// 서브기능
	private Timestamp toTimestamp(Date date) {
		return new Timestamp(date.getTime());
	}

}
