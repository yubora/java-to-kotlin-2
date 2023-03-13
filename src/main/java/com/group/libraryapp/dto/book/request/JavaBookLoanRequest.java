package com.group.libraryapp.dto.book.request;

@Deprecated
public class JavaBookLoanRequest {

  private String userName;
  private String bookName;

  public JavaBookLoanRequest(String userName, String bookName) {
    this.userName = userName;
    this.bookName = bookName;
  }

  public String getUserName() {
    return userName;
  }

  public String getBookName() {
    return bookName;
  }

}
