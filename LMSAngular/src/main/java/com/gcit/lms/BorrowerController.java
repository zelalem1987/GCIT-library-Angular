package com.gcit.lms;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;

class Message{
	boolean success;
	public Message(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	String message;
}

@RestController
public class BorrowerController {

	@Autowired
	BorrowerDAO borrowerDAO;

	@Autowired
	BookLoanDAO bookLoanDAO;

	@Autowired
	BookCopiesDAO bookCopiesDAO;

	@Autowired
	BranchDAO branchDAO;
	
	@Autowired
	BookDAO bookDAO;

	@RequestMapping(value = "/checkId", method = RequestMethod.POST, 
			consumes = "application/json", produces = "application/json")
	public String LogIn(@RequestBody Borrower borrower) {

		boolean logIn = false;
		try {
			logIn = borrowerDAO.CheckID(borrower.getBorrowerCardNo());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (logIn == true) {
			
			return "true";
		} else {
			return "false";
		}
		

	}

	@RequestMapping(value = "/checkeOut", method = RequestMethod.POST, 
			consumes = "application/json",produces= "application/json")
	public Message checkBook(@RequestBody BookLoan bookLoan) {

		try {
			
			if(bookLoanDAO.checkIfBorrowerHasCheckedOutABook(bookLoan)){
				return new Message(false,"You have already checked out this book");
			}else{
				bookLoanDAO.checkOut(bookLoan);
				bookLoanDAO.updateNoOfCopiesForCheckOut(bookLoan);
				return new Message(true,"Book checked out successfully");
			}
			
		} catch (SQLIntegrityConstraintViolationException ex) {
			ex.printStackTrace();
			return new Message(true,"You have already checked out this book");

		}catch (SQLException e) {
			e.printStackTrace();
			return new Message(true,"Operation failed !!!" + e.getMessage());
		}
	}

	@RequestMapping(value = "/return", method = RequestMethod.POST, 
			consumes = "application/json")
	public void returnBook(@RequestBody BookLoan bookLoan) {

		try {
			bookLoanDAO.returnBook(bookLoan);
			bookLoanDAO.updateNoOfCopiesForCheckIn(bookLoan);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}



	@RequestMapping(value = "/viewBranchBook/{branchId}", method = RequestMethod.GET, 
			produces = "application/json")
	public List<Book> viewBookBranch(@PathVariable Integer branchId) throws SQLException {
		return bookDAO.readBranchBook(branchId);
	}


//	@RequestMapping(value = "/viewBookBranchForABorrower/{branchId}/{cardNumber}", method = RequestMethod.GET, 
//			produces = "application/json")
//	public List<Book> viewBookBranchForABorrower(@PathVariable Integer branchId,@PathVariable Integer cardNumber) throws SQLException {
//		return bookDAO.readBranchBook(branchId);
//	}
	
	@RequestMapping(value = "/checkInBranch/{borrowerCardNo}", method = RequestMethod.GET, 
			produces = "application/json")
	public List<BookLoan> viewBranch(@PathVariable int borrowerCardNo) throws SQLException {
		Borrower borrower = new Borrower();
		borrower.setBorrowerCardNo(borrowerCardNo);
		BookLoan bloan=new BookLoan();
		bloan.setBorrower(borrower);
		return bookLoanDAO.readAllBooksForABorrower(bloan);

	}
	
	@RequestMapping(value = "/checkInBook", method = RequestMethod.POST, produces = "application/json")
	public List<BookLoan> viewBook(@RequestBody BookLoan bookLoan) throws SQLException {
		return bookLoanDAO.readAllBook(bookLoan);
	}


}
