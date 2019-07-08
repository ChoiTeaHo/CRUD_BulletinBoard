package article.service;

import java.util.List;

import article.model.Article;

public class ArticlePage {

	private int total; // 즉, total값: 13값
	private int currentPage; // 즉, currentPage값 : 1
	private List<Article> content; // 저장.
	private int totalPages; // 즉, totalPages값: 2
	private int startPage; // 즉, startPage값: 1
	private int endPage; // 즉, endPage값: 2

	public ArticlePage(int total, int currentPage, int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;

		if (total == 0) {
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {

			// total값: 13값
			// currentPage값: 1값
			// size값: 10값

			totalPages = total / size;
			System.out.println("totalPages값: " + totalPages); // 1값 (10개이상있다는것이 증명됨)

			if (total % size > 0) {
				totalPages++;
				System.out.println("totalPages값: " + totalPages); // 2값 (총페이지수 1개증가시킴/ 10개가 딱맞아떨어지는지 검사)
			}

			int modVal = currentPage % 5; // 1 % 5
			System.out.println("modVal값: " + modVal); // 즉 mdVal값: 1

			startPage = currentPage / 5 * 5 + 1;
			System.out.println("currentPage/5값: " + currentPage / 5); // 0값
			System.out.println("startPage값: " + startPage); // 즉, startPage값 1

			if (modVal == 0) // 1==0 (만약 madVal 0이라면 조건실행)
				startPage = startPage - 5; // startPage - 5 하기 ==> 값: -4

			endPage = startPage + 4; // startPage + 4 하기 ==> 값: 5 / 즉, endPage값: 5
			System.out.println("endPage: " + endPage);
			if (endPage > totalPages) // if( 5>2 ) 즉, true
				endPage = totalPages; // 결국 endPage 2저장
			System.out.println("endPage최종값: " + endPage);

			System.out.println("-------------------------------");
			System.out.println("total값: " + total); //
			System.out.println("currentPage " + currentPage);
			System.out.println("content크기: " + content.size());
			System.out.println("totalPages값: " + totalPages);
			System.out.println("startPage값: " + startPage);
			System.out.println("endPage최종값: " + endPage);
			System.out.println("-------------------------------");
		}
	}

	public int getTotal() {
		return total;
	}

	public boolean hasNoArticles() {
		return total == 0;
	}

	public boolean hasArticles() {
		return total > 0;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public List<Article> getContent() {
		return content;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

}
