package com.group.libraryapp.dto.user.request;

@Deprecated
public class JavaUserUpdateRequest {

  private long id;
  private String name;

  public JavaUserUpdateRequest(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
