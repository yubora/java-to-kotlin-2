package com.group.libraryapp.dto.book.request;

import org.jetbrains.annotations.NotNull;

@Deprecated
public class JavaBookReturnRequest {

  private String userName;
  private String bookName;

  public JavaBookReturnRequest(String userName, String bookName) {
    this.userName = userName;
    this.bookName = bookName;
  }

  @NotNull
  public String getUserName() {
    return userName;
  }

  @NotNull
  public String getBookName() {
    return bookName;
  }

}
