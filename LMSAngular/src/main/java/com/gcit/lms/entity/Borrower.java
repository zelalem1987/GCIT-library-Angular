package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

public class Borrower implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2595807773483394036L;
	
	private int borrowerCardNo;
	private String borrowerName;
	private String borrowerAddress;
	private String borrowerPhone;
	
	List<Book> book;
	List<Branch>branch;
	
	public Borrower(){
		super();
	}
	
	

	public List<Book> getBook() {
		return book;
	}

	public void setBook(List<Book> book) {
		this.book = book;
	}

	public List<Branch> getBranch() {
		return branch;
	}

	public void setBranch(List<Branch> branch) {
		this.branch = branch;
	}

	public int getBorrowerCardNo() {
		return borrowerCardNo;
	}
	
	public void setBorrowerCardNo(int cardNo) {
		this.borrowerCardNo = cardNo;
	}
	
	public String getBorrowerName() {
		return borrowerName;
	}
	
	public void setBorrowerName(String name) {
		this.borrowerName = name;
	}
	
	public String getBorrowerAddress() {
		return borrowerAddress;
	}
	
	public void setBorrowerAddress(String address) {
		this.borrowerAddress = address;
	}
	
	public String getBorrowerPhone() {
		return borrowerPhone;
	}
	
	public void setBorrowerPhone(String phone) {
		this.borrowerPhone = phone;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((book == null) ? 0 : book.hashCode());
		result = prime * result + ((borrowerAddress == null) ? 0 : borrowerAddress.hashCode());
		result = prime * result + borrowerCardNo;
		result = prime * result + ((borrowerName == null) ? 0 : borrowerName.hashCode());
		result = prime * result + ((borrowerPhone == null) ? 0 : borrowerPhone.hashCode());
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Borrower))
			return false;
		Borrower other = (Borrower) obj;
		if (book == null) {
			if (other.book != null)
				return false;
		} else if (!book.equals(other.book))
			return false;
		if (borrowerAddress == null) {
			if (other.borrowerAddress != null)
				return false;
		} else if (!borrowerAddress.equals(other.borrowerAddress))
			return false;
		if (borrowerCardNo != other.borrowerCardNo)
			return false;
		if (borrowerName == null) {
			if (other.borrowerName != null)
				return false;
		} else if (!borrowerName.equals(other.borrowerName))
			return false;
		if (borrowerPhone == null) {
			if (other.borrowerPhone != null)
				return false;
		} else if (!borrowerPhone.equals(other.borrowerPhone))
			return false;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Borrower [borrowerCardNo=" + borrowerCardNo + ", borrowerName=" + borrowerName + ", borrowerAddress="
				+ borrowerAddress + ", borrowerPhone=" + borrowerPhone + ", book=" + book + ", branch=" + branch + "]";
	}
	
	
}
