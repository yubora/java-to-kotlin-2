package com.group.libraryapp.dto.book.request;

@Deprecated
public class JavaBookRequest {

  private String name;

  public JavaBookRequest(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
