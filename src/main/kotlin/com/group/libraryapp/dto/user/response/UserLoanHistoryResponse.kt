package com.group.libraryapp.dto.user.response

data class UserLoanHistoryResponse (
    val name: String, // user name
    val books: List<BookHistoryResponse>,
)

data class BookHistoryResponse (
    val name: String, // book name
    val isReturn: Boolean,
)