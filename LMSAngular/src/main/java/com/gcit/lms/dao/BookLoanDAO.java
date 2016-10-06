package com.gcit.lms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;

public class BookLoanDAO extends BaseDAO implements ResultSetExtractor<List<BookLoan>> {
	
@Autowired
BookDAO bookDAO;

@Autowired
BranchDAO branchDAO;

@Autowired
BorrowerDAO borrowerDAO;
	
	private static final String SELECT = "select * from tbl_book_loans";
	private static final String SELECT_ALL_BOOK = "select * from tbl_book join tbl_book_loans on tbl_book.bookId = tbl_book_loans.bookId and tbl_book_loans.branchId = ? and tbl_book_loans.cardNo = ?  and tbl_book_loans.dateIn is null";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_book_loans where title like ?";
	private static final String SELECT_ONE = "select * from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?";
	private static final String SELECT_BY_CARD_NO = "select * from tbl_book_loans where cardNo like ?";
	private static final String SELECT_BY_BOOK = "select * from tbl_book_loans where bookId like ?";
	private static final String SELECT_BY_BRANCH = "select * from tbl_book_loans where branchId like ?";
	private static final String SELECT_BY_DUEDATE = "select * from tbl_book_loans where dueDate = ?";
	private static final String SELECT_OVER_DUEDATE = "select * from tbl_book_loans where Date(tbl_book_loans.dueDate) > now()";
	private static final String SELECT_ALL_BOOK_FOR_BORROWER = "select * from tbl_book_loans where cardNo = ? and dateIn is null";
	private static final String INSERT = "insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate, dateIn) values (?,?,?,?,?,?)";
	private static final String INSERT_FOR_7_DAYS = "insert into tbl_book_loans (bookId, branchId , cardNo, dateOut, dueDate, dateIn) values (?,?,?, now(),DATE_ADD(now(),INTERVAL 7 DAY),null)";
	private static final String DELETE = "delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?";
	private static final String UPDATE = "update tbl_book_loans set bookId = ?, branchId = ?, dateOut = ?, dueDate = ?  dateIn = ? where cardNo = ?";
	private static final String UPDATE_BOOK_COPIES = "update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?";
	private static final String UPDATE_DATEIN = "Update tbl_book_loans set dateIn = now() where bookId =? and branchId=? and cardNo=? ";
	private static final String UPDATE_DUEDATE = "update tbl_book_loans set dueDate = DATE_ADD(dueDate,INTERVAL 7 DAY) where bookId = ? and branchId = ? and cardNo = ? and dateIn is null";
	private static final String GET_COUNT = "select count(*) from tbl_book_loans";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_book_loans where title like ?";
	private static final String UPDATE_NO_OF_COPIES_CHECKOUT = "update tbl_book_copies set noOfCopies = noOfCopies-1 where bookId = ? and branchId = ?";
	private static final String UPDATE_NO_OF_COPIES_CHECKIN = "update tbl_book_copies set noOfCopies = noOfCopies+1 where bookId = ? and branchId = ?";
	private static final String CHECK_BORROWER_CHECKOUT = "select count(*) from tbl_book_loans where cardNo = ? and bookId = ? and branchId = ? and dateIn is null";
	private static final String AMOUNT_OF_COPIES = "select noOfCopies from tbl_book_loan where cardNo = ? and bookId = ? and branchId = ?";
	
	
	public void checkOut(BookLoan bookLoan) throws SQLException {
		template.update(INSERT_FOR_7_DAYS, 
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() });
	}

	public void updateNoOfCopiesForCheckOut(BookLoan bookLoan) throws SQLException {
		template.update(UPDATE_NO_OF_COPIES_CHECKOUT, 
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId()});
	}
	
	public void updateNoOfCopiesForCheckIn(BookLoan bookLoan) throws SQLException {
		template.update(UPDATE_NO_OF_COPIES_CHECKIN, 
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId()});
	}

	
	public void overWrite(BookLoan l) throws SQLException {
		template.update(UPDATE_DUEDATE,
				new Object[] { l.getBookId(), l.getBranchId(), l.getCardNo() });
		System.out.println(l.getBookId() + " " + l.getBranchId() + " "
				+ l.getCardNo());
	}

	public void returnBook(BookLoan bookLoan) throws SQLException {
		template.update(UPDATE_DATEIN,
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() });

	}
	
	public Boolean findAmountOfCopies(BookLoan bookLoan) throws SQLException {
		
		if(template.update(AMOUNT_OF_COPIES,
				new Object[] { bookLoan.getBookId(), bookLoan.getBranchId(), bookLoan.getCardNo() }) > 0) return true;
		else return false;

	}

	public List<BookLoan> readAllBook(BookLoan bookLoan)
			throws SQLException {

		return template
				.query(SELECT_ALL_BOOK,
						new Object[] { bookLoan.getBranchId(), bookLoan.getCardNo() }, this);
	}
	
	public List<BookLoan> readByCardNo(BookLoan bookLoan)
			throws SQLException {

		return template
				.query(SELECT_BY_CARD_NO, new Object[] {bookLoan.getCardNo()}, this);
	}
	
	public List<BookLoan> readAllBooksForABorrower(BookLoan bookLoan)
			throws SQLException {

		return template
				.query(SELECT_ALL_BOOK_FOR_BORROWER,
						new Object[] {bookLoan.getBorrower().getBorrowerCardNo()}, this);
	}
	
	public List<BookLoan> readAllLoans() throws SQLException {
		return template.query(SELECT, new Object[]{}, this);
}

	@Override
	public List<BookLoan> extractData(ResultSet rs) {

		List<BookLoan> bookLoan = new ArrayList<BookLoan>();

		try {
			while (rs.next()) {
				BookLoan loan = new BookLoan();
//				loan.setBookId(rs.getInt("bookId"));
				//loan.setTitle(rs.getString("title"));
				loan.setDueDate(rs.getString("dueDate"));
				loan.setDateOut(rs.getString("dateOut"));

				loan.setBook(bookDAO.readBookById(rs.getInt("bookId")));
				loan.setBranch(branchDAO.readByID(rs.getInt("branchId")));
				loan.setBorrower(borrowerDAO.readByID(rs.getInt("cardNo")));
				
				bookLoan.add(loan);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookLoan;
	}
	
	

	public boolean checkIfBorrowerHasCheckedOutABook(BookLoan bookLoan) {

		if (template.queryForObject(CHECK_BORROWER_CHECKOUT,new Object[]{bookLoan.getCardNo(), bookLoan.getBookId(), bookLoan.getBranchId()}, Integer.class)>0){
			return true;
		}else{
			return  false;
		}
		
	}

}
