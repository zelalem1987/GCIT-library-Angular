package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Branch;

public class BranchDAO extends BaseDAO implements ResultSetExtractor<List<Branch>>{

	private static final String SELECT_BY_NAME = "SELECT * FROM tbl_library_branch WHERE branchName = ?";
	private static final String SELECT_ALL = "SELECT * FROM tbl_library_branch";
	private static final String SELECT = "SELECT * FROM tbl_library_branch WHERE branchId = ?";
	private static final String SELECT_WITH_BOOKID = "select * from tbl_library_branch where branchId IN (select branchId from tbl_book_copies where bookId = ?)";
	private static final String SELECT_WITH_BORROWER_CARD_NO = "Select distinct tbl_library_branch.branchId, tbl_library_branch.branchName, tbl_library_branch.branchAddress from tbl_library_branch join tbl_book_loans where tbl_library_branch.branchId = tbl_book_loans.branchId and tbl_book_loans.cardNo = ? and dateIn is null ";
	private static final String DELETE = "DELETE FROM tbl_library_branch WHERE branchId = ? ";
	private static final String UPDATE = "UPDATE tbl_library_branch SET branchName = ?, branchAddress = ? WHERE branchId = ?";
	private static final String DELETE_BRANCH_FROM_BOOK_COPIES = "delete from tbl_book_copies where branchId = ?";
	private static final String INSERT = "INSERT INTO tbl_library_branch (branchName, branchAddress) VALUES (?, ?)";
	private static final String INSERT_TO_BOOK_COPIES = "insert tbl_book_copies (bookId, branchId,noOfCopies) values (?,?,?)";
	private static final String GET_COUNT = "select count(*) from tbl_library_branch";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_library_branch where branchName like ?";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_library_branch where branchName like ?";
	
	public void addBranch(Branch branch) throws SQLException {
		template.update(INSERT, new Object[] {branch.getBranchName(), branch.getBranchAddress()});
	}
	
	public Integer addBranchWithID(Branch branch) {
		final String branchName = branch.getBranchName();
		final String branchAddress = branch.getBranchAddress();

		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(INSERT, new String[] { "branchId" });
				pstmt.setString(1, branchName);
				pstmt.setString(2, branchAddress);
				return pstmt;
			}
		}, keyHolder);
		
		return keyHolder.getKey().intValue();
	}
	
	public void updatebranch(Branch branch) throws SQLException {

		template.update(UPDATE,	new Object[] { branch.getBranchName(),
				branch.getBranchAddress(), branch.getBranchId() });
		template.update(DELETE_BRANCH_FROM_BOOK_COPIES,	new Object[] { 
				branch.getBranchId() });
		
		if (branch.getBooks() != null) {			
			for (Book book : branch.getBooks()) {
				template.update(INSERT_TO_BOOK_COPIES, new Object[] { 
						book.getBookId(),branch.getBranchId(), 1 });
			}
		}
	}

	
	public void deleteBranch(Branch branch) throws SQLException {		
		template.update(DELETE, new Object[] {branch.getBranchId()});
	}
	
	public Branch readByID(Integer branchId) throws SQLException {
		List<Branch> branch = template.query(SELECT,
				new Object[] { branchId }, this);
		if (branch != null) {
			return branch.get(0);
		}
		return null;
	}
	
	public List<Branch> readallBranchesByName(Integer pageNo, Integer pageSize, String searchString) throws SQLException {
		
		setPageNo(pageNo);
		setPageSize(pageSize);
		
		if(searchString != null && !"".equals(searchString)){
			
			searchString = "%" + searchString + "%";
			
			return template.query(SELECT_ALL_SEARCH, new Object[]{searchString}, this);
		}
		else{
		
			return template.query(SELECT_ALL, new Object[] {}, this);			
		}
	}
	
	public List<Branch> readallBranches() throws SQLException {		
			return template.query(SELECT_ALL, new Object[] {}, this);	
	}
	
	public List<Branch> readBookBranchs(int bookId) {
		return template.query(SELECT_WITH_BOOKID, new Object[] { bookId }, this);
	}
	
	public List<Branch> readAllBranches(int borrowerCardNo) {
		return template.query(SELECT_WITH_BORROWER_CARD_NO, new Object[] { borrowerCardNo }, this);
	}
	
	@Override
	public List<Branch> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Branch> branches = new ArrayList<Branch>();
		
			while (rs.next()) {
				Branch branch = new Branch();
				branch.setBranchId(rs.getInt("branchId"));
				branch.setBranchName(rs.getString("branchName"));
				branch.setBranchAddress(rs.getString("branchAddress"));
				branches.add(branch);
			}
		return branches;
	}

	
	public Branch readbyName(String branchName) throws SQLException {
		List<Branch> branches = template.query(SELECT_BY_NAME, new Object[] {branchName}, this); 
		if(branches != null){
			return branches.get(0);
		}
		return null;
	}

	
	public Integer getCount() throws SQLException {
		
		return template.queryForObject(GET_COUNT, Integer.class);
	}

	
	public Integer getCountByName(String searchString) throws SQLException {
		
		if(searchString != null && !"".equals(searchString)){
			
			searchString = "%" + searchString + "%";
			
			return template.queryForObject(GET_COUNT_BY_NAME, new Object[]{searchString}, Integer.class);
		}
		else {
			return template.queryForObject(GET_COUNT, Integer.class);
		}
		
	}

}
