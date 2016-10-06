package com.gcit.lms;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

/**
 * Handles requests for the application home page.
 */
@RestController
public class HomeController {	
	
	@Autowired
	AuthorDAO authorDao;
	
	@Autowired
	PublisherDAO publisherDao;
	
	@Autowired
	GenreDAO genreDao;
	
	@Autowired
	BranchDAO branchDao;
	
	@Autowired
	BorrowerDAO borrowerDao;
	
	@Autowired
	BookDAO bookDao;
	
	@Autowired
	BookLoanDAO bookLoanDao;
	
	@Autowired
	BookCopiesDAO bookCopiesDao;
	
	
	/*
	 * *******View methods*******
	 */	
	
	@RequestMapping(value = "/viewAuthors", method = RequestMethod.GET, produces = "application/json")
	public List<Author> getAuthors() throws SQLException {
		return authorDao.readAllAuthors();
	}
	
	@RequestMapping(value = "/viewBranches", method = RequestMethod.GET, produces = "application/json")
	public List<Branch> getBranches() throws SQLException {
		return branchDao.readallBranches();
	}
	
	@RequestMapping(value = "/viewGenres", method = RequestMethod.GET, produces = "application/json")
	public List<Genre> getGenres() throws SQLException {
		return genreDao.readallGenres();
	}
	
	@RequestMapping(value = "/viewBorrower", method = RequestMethod.GET, produces = "application/json")
	public List<Borrower> getBorrower() throws SQLException {
		return borrowerDao.readallBorrower();
	}
	
	@RequestMapping(value = "/overWriteList", method = RequestMethod.GET, produces = "application/json")
	public List<BookLoan> getBorrowerAndDueDate() throws SQLException {
		return bookLoanDao.readAllLoans();
	}
	
	@RequestMapping(value = "/viewPublishers", method = RequestMethod.GET, produces = "application/json")
	public List<Publisher> getPublishers() throws SQLException {
		return publisherDao.readAllPublishers();
	}
	
	@RequestMapping(value="/viewBooks", method={RequestMethod.GET, RequestMethod.POST}, produces="application/json")
	public List<Book> getBooks(){
		try {
			return bookDao.readAllBooks();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/*
	 * *******Add methods*******
	 */
	@RequestMapping(value = "/addAuthor", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes = "application/json")
	public String addAuthor(@RequestBody Author author){
		try {
			authorDao.addAuthor(author);
			return "Author added Sucessfully.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Author add failed!";
		}
	}
	
	@RequestMapping(value = "/addBranch", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes = "application/json")
	public String addBranch(@RequestBody Branch branch){
		try {
			branchDao.addBranch(branch);
			return "Branch added Sucessfully.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Branch add failed!";
		}
	}
	
	@RequestMapping(value = "/addGenre", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes = "application/json")
	public String addGenre(@RequestBody Genre genre){
		try {
			genreDao.addGenre(genre);
			return "Genre added Sucessfully.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Genre add failed!";
		}
	}
	
	@RequestMapping(value = "/addBorrower", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes = "application/json")
	public String addBorrower(@RequestBody Borrower borrower){
		try {
			borrowerDao.addBorrower(borrower);
			return "Borrower added Sucessfully.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Borrower add failed!";
		}
	}
	
	@RequestMapping(value = "/addPublisher", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes = "application/json")
	public String addPublisher(@RequestBody Publisher publisher){
		try {
			publisherDao.addPublisher(publisher);
			return "Publisher added Sucessfully.";
		} catch (Exception e) {
			e.printStackTrace();
			return "Publisher add failed!";
		}
	}
	
	@RequestMapping(value="/addBook", method={RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public String addBook(@RequestBody Book book){
		try {
			bookDao.addBook(book);
//			return book;
			return "Book Added Sucessfully.";
		} catch (Exception e) {
			e.printStackTrace();
//			return null;
			return "Book Add Failed.";
		}
	}
	
	/*
	 * *******View by Id methods*******
	 */
	
	@RequestMapping(value = "/viewAuthorByID/{authorId}", method = RequestMethod.GET, produces = "application/json")
	public Author viewAuthorByID(@PathVariable Integer authorId) throws SQLException {
		return authorDao.readAuthorByID(authorId);
	}	
	
	@RequestMapping(value = "/viewBranchByID/{branchId}", method = RequestMethod.GET, produces = "application/json")
	public Branch viewBranchByID(@PathVariable Integer branchId) throws SQLException {
		return branchDao.readByID(branchId);
	}	
	
	@RequestMapping(value = "/viewGenreByID/{genre_id}", method = RequestMethod.GET, produces = "application/json")
	public Genre viewGenreByID(@PathVariable Integer genreId) throws SQLException {
		return genreDao.readById(genreId);
	}	
	
	@RequestMapping(value = "/viewBorrowerByID/{borrowerCardNo}", method = RequestMethod.GET, produces = "application/json")
	public Borrower viewBorrowerByID(@PathVariable Integer borrowerCardNo) throws SQLException {
		return borrowerDao.readBorrowerByID(borrowerCardNo);
	}	
	
	@RequestMapping(value = "/viewPublisherByID/{publisherId}", method = RequestMethod.GET, produces = "application/json")
	public Publisher viewPublisherByID(@PathVariable Integer publisherId) throws SQLException {
		return publisherDao.readPublisherById(publisherId);
	}	
	
	@RequestMapping(value = "/viewBookByID/{bookId}", method = RequestMethod.GET, produces = "application/json")
	public Book viewBookByID(@PathVariable Integer bookId) throws SQLException {
		return bookDao.readBookById(bookId);
	}
	/*
	 * *******Edit methods*******
	 */
	
	@RequestMapping(value = "/updateAuthor", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = "application/json")
	public Author updateAuthor(@RequestBody Author author) {

		try {
			authorDao.updateAuthor(author);
			return author;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateBranch", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = "application/json")
	public Branch updateBranch(@RequestBody Branch branch) {

		try {
			branchDao.updatebranch(branch);
			return branch;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateGenre", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = "application/json")
	public Genre updateGenre(@RequestBody Genre genre) {

		try {
			genreDao.updateGenre(genre);
			return genre;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateBorrower", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = "application/json")
	public Borrower updateBorrower(@RequestBody Borrower borrower) {

		try {
			borrowerDao.updateBorrower(borrower);
			return borrower;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updatePublisher", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = "application/json")
	public Publisher updatePublisher(@RequestBody Publisher publisher) {

		try {
			publisherDao.updatePublisher(publisher);
			return publisher;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateBook", method = { RequestMethod.GET,
			RequestMethod.POST }, consumes = "application/json")
	public Book updateBook(@RequestBody Book book) {

		try {
			bookDao.updateBook(book);
			return book;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	
	/*
	 * *******Delete methods*******
	 */
	
	@RequestMapping(value = "/deleteAuthor", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public Author deleteAuthor(@RequestBody Author author) {
		try {
			authorDao.deleteAuthor(author);
			return author;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteBranch", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public Branch deleteBranch(@RequestBody Branch branch) {
		try {
			branchDao.deleteBranch(branch);
			return branch;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteGenre", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public Genre deleteGenre(@RequestBody Genre genre) {
		try {
			genreDao.deleteGenre(genre);
			return genre;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteBorrower", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public Borrower deleteBorrower(@RequestBody Borrower borrower) {
		try {
			borrowerDao.deleteBorrower(borrower);
			return borrower;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/deletePublisher", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public Publisher deletePublisher(@RequestBody Publisher publisher) {
		try {
			publisherDao.deletePublisher(publisher);
			return publisher;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/deleteBook", method = {RequestMethod.GET, 
			RequestMethod.POST}, consumes="application/json")
	public Book deleteBook(@RequestBody Book book) {
		try {
			bookDao.deleteBook(book);
			return book;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/viewbranch", method = RequestMethod.GET, produces = "application/json")
	public List<Branch> viewBranch()
			throws SQLException {

		List<Branch> branch = branchDao.readallBranches();

//		for (Branch b : branch) {
//			b.setBooks(bookDao.readBranchBook(b.getBranchId()));
//		}
		return branch;
	}
	
}
