package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun clean() {
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("Book 저장 정상")
    fun saveBookTest() {
        // given
        val request = BookRequest("book1")

        // when
        bookService.saveBook(request)

        // then
        val saved = bookRepository.findAll()
        assertThat(saved).hasSize(1)
        assertThat(saved[0].name).isEqualTo("book1")
    }

    @Test
    @DisplayName("Book 대출 정상")
    fun loanBookTest() {
        // given
        bookRepository.save(Book("book1"))
        val savedUser = userRepository.save(User("yubora", null))
        val request = BookLoanRequest("yubora", "book1")

        // when
        bookService.loanBook(request)

        //then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo("book1")
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].isReturn).isFalse

    }

    @Test
    @DisplayName("이미 대출된 책일 경우 예외 처리 정상")
    fun loanBookExceptionTest() {
        // given
        bookRepository.save(Book("book1"))

        val savedUser = userRepository.save(User("yubora", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "book1", false))

        val newRequest = BookLoanRequest("yugreen", "book1")

        // when & then
        val message = assertThrows<IllegalArgumentException> {
            bookService.loanBook(newRequest)
        }.message
        assertThat(message).isEqualTo("이미 대출되어 있는 책입니다.")
    }

    @Test
    @DisplayName("Book 반납 정상")
    fun returnBookTest() {
        // given
        val savedUser = userRepository.save(User("yubora", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "book1", false))
        val request = BookReturnRequest("yubora", "book1")

        // when
        bookService.returnBook(request)

        // then
//        val returnHistory = userLoanHistoryRepository.findByBookNameAndIsReturn("book1", true)
//        assertThat(returnHistory).isNotNull
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo("book1")
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].isReturn).isTrue
    }

}