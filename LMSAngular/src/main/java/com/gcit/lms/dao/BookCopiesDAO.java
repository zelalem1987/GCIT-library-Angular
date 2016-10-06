package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.Borrower;

public class BookCopiesDAO extends BaseDAO implements ResultSetExtractor<List<BookCopies>>{
	
	private static final String SELECT_ALL = "select * from tbl_book_copies";
	private static final String SELECT_DISTINCT = "select distinct * from tbl_book join tbl_book_copies on tbl_book_copies.bookId = tbl_book.bookId and tbl_book_copies.noOfCopies >'0' and tbl_book_copies.branchId = ? and tbl_book.bookId not in (select bookId from tbl_book_loans where branchId = ? and cardNo =?)";
	private static final String SELECT_COPIES_BY_BOOKID = "select tbl_book_copies.noOfCopies, tbl_book.title, tbl_book.bookId from tbl_book_copies, tbl_book where tbl_book_copies.branchId = ? and tbl_book_copies.bookId = tbl_book.bookId";
	private static final String SELECT_ONE = "select * from tbl_book_copies where bookId = ? and branchId = ?";
	private static final String SELECT_COPIES_AND_BOOKS = "select tbl_book_copies.noOfCopies, tbl_book.title, "
			+ "tbl_book.bookId from tbl_book_copies, tbl_book where tbl_book_copies.branchId = ? "
			+ "and tbl_book_copies.bookId = tbl_book.bookId";
	private static final String INSERT = "insert into tbl_book_copies (bookId, branchId, noOfCopies) values (?,?,?)";
	private static final String INSERT_BY_ID = "insert into tbl_book_copies noOfCopies values (?,?,?)";
	private static final String DELETE = "delete from tbl_book_copies where bookId = ? and branchId = ?";
	private static final String UPDATE = "update tbl_book_copies set noOfCopies = ? where bookId = ? branchId = ?";
	private static final String TABLE_NAME = "tbl_book_copies";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_book_copies where bookId = ?";
	private static final String GET_COUNT = "select count(*) from tbl_book_copies";
	private static final String GET_COUNT_BY_BRANCH = "select count(*) from tbl_book_copies where title like branchId = ?";
	private static final String GET_COUNT_BY_BOOK = "select count(*) from tbl_book_copies where title like bookId = ?";	
	private static final String SELECT_BOOK_WITH_BRANCH = "SELECT * FROM tbl_book b join tbl_book_copies bc on b.bookId = bc.bookId join tbl_library_branch br on bc.branchId = br.branchId where br.branchId = ?";
	

	public void addBookCopeies(BookCopies bookCopies) throws SQLException {

		template.update(INSERT,	new Object[] { bookCopies.getBookId(), bookCopies.getBranchId(),
						bookCopies.getNoOfCopies() });
	}

	public void addBookCopeies2(BookCopies bookCopies) throws SQLException {

		template.update(UPDATE, new Object[] { bookCopies.getNoOfCopies(), bookCopies.getBookId(),
						bookCopies.getBranchId() });
	}

	public List<BookCopies> readAllTitle(int branchID, int cardNo)
			throws SQLException {

		return template
				.query(SELECT_DISTINCT,	new Object[] { branchID, branchID, cardNo }, this);
	}

	public List<BookCopies> getNCopies(int branchId) throws SQLException {

		return template
				.query(SELECT_COPIES_BY_BOOKID,	new Object[] { branchId }, this);
	}

	@Override
	public List<BookCopies> extractData(ResultSet rs) {

		List<BookCopies> bookCopies = new ArrayList<BookCopies>();

		try {
			while (rs.next()) {
				BookCopies b = new BookCopies();
				b.setTitle(rs.getString("title"));
				b.setBookId(rs.getInt("bookId"));
				b.setNoOfCopies(rs.getInt("noOfcopies"));
				b.setBranchId(rs.getInt("branchId"));
				bookCopies.add(b);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookCopies;
	}
	
	public List<BookCopies> readBranchBook(int branchId) {
		return template.query(SELECT_BOOK_WITH_BRANCH, new Object[] { branchId }, this);
	}

}
