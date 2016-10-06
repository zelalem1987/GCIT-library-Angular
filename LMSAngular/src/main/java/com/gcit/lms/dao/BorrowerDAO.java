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

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO implements ResultSetExtractor<List<Borrower>>{

	private static final String SELECT_ID_BY_NAME = "SELECT * FROM tbl_borrower WHERE borrowerName = ?";
	private static final String SELECT_ALL = "SELECT * FROM tbl_borrower";
	private static final String SELECT = "SELECT * FROM tbl_borrower WHERE borrowerCardNo = ?";
	private static final String DELETE = "DELETE FROM tbl_borrower WHERE borrowerCardNo = ? ";
	private static final String UPDATE = "UPDATE tbl_borrower SET borrowerName = ?, borrowerAddress = ?, borrowerPhone = ? WHERE borrowerCardNo = ?";
	private static final String INSERT = "INSERT INTO tbl_borrower (borrowerName, borrowerAddress, borrowerPhone) VALUES (?, ?, ?)";
	private static final String INSERT_BY_ID = "insert into tbl_borrower (name) values (?) ";
	private static final String SELECT_ALL_SEARCH = "select * from tbl_borrower where borrowerName like ?";
	private static final String GET_COUNT = "select count(*) from tbl_borrower";
	private static final String GET_COUNT_BY_NAME = "select count(*) from tbl_borrower where borrowerName like ?";
	



	public void addBorrower(Borrower be) throws SQLException {		
		template.update(INSERT, 
				new Object[] {be.getBorrowerName(), be.getBorrowerAddress(), be.getBorrowerPhone()});
	}
	
	public Integer addBorrowerWithID(Borrower borrower) {
		final String borrowerName = borrower.getBorrowerName();

		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(INSERT_BY_ID, new String[] { "cardNo" });
				ps.setString(1, borrowerName);
				return ps;
			}
		}, keyHolder);
		Integer cardNo = keyHolder.getKey().intValue();
		return cardNo;

	}


	public void updateBorrower(Borrower be) throws SQLException {		
		template.update(UPDATE,	new Object[] {be.getBorrowerName(), 
				be.getBorrowerAddress(), be.getBorrowerPhone(), be.getBorrowerCardNo()});
	}


	public void deleteBorrower(Borrower be) throws SQLException {
		template.update(DELETE, new Object[] {be.getBorrowerCardNo()});
	}
	
	public Borrower readByID(Integer borrowerCardNo) throws SQLException {
		List<Borrower> borrower = template.query(SELECT, new Object[] { borrowerCardNo }, this);
		if (borrower != null) {
			return borrower.get(0);
		}
		return null;
	}

	public List<Borrower> readall(Integer pageNo, Integer pageSize, String searchString) throws SQLException {
		
		setPageNo(pageNo);
		setPageSize(pageSize);
		
		if(searchString != null && !"".equals(searchString)){
			
			searchString = "%" + searchString + "%";
			
			return template.query(SELECT_ALL_SEARCH, new Object[]{searchString}, this);
		}	else{
				return template.query(SELECT_ALL, new Object[]{}, this);
		}
	}
	
	public List<Borrower> readallBorrower() throws SQLException {
			return template.query(SELECT_ALL, new Object[]{}, this);
	}
	
	public Borrower readBorrowerByID(Integer borrowerCardNo) throws SQLException {
		List<Borrower> borrowers = template.query(SELECT, new Object[] { borrowerCardNo }, this);
		if (borrowers != null) {
			return borrowers.get(0);
		}
		return null;
	}

	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException, DataAccessException {
		List<Borrower> list = new ArrayList<Borrower>();
		
		while(rs.next()){
			Borrower borrower = new Borrower();
			borrower.setBorrowerCardNo(rs.getInt("borrowerCardNo"));
			borrower.setBorrowerName(rs.getString("borrowerName"));
			borrower.setBorrowerAddress(rs.getString("borrowerAddress"));
			borrower.setBorrowerPhone(rs.getString("borrowerPhone"));
			list.add(borrower);
		}
		return list;
	}

	public Borrower readbyName(String entityName) throws SQLException {
		List<Borrower> borrowers = template.query(SELECT_ID_BY_NAME, new Object[]{entityName}, this);
		if(borrowers != null){
			return borrowers.get(0);
		}
		return null;
	}
	
	public boolean CheckID(Integer borrowerCardNo) throws SQLException {
		List<Borrower> borrower = template.query(SELECT, new Object[] { borrowerCardNo }, this);
		if (!borrower.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}


	public Integer getCount() throws SQLException {
		return template.queryForObject(GET_COUNT, Integer.class);
	}


	public Integer getCountByName(String searchString) throws SQLException {
		
		if(searchString != null && !"".equals(searchString)){

			searchString = "%" + searchString + "%";

			return template.queryForObject(GET_COUNT_BY_NAME, new Object[]{searchString}, Integer.class);
		}	
		else{
			return template.queryForObject(GET_COUNT, Integer.class);
		}
	}
}
