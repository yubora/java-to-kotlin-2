package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
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
        println("# CLEAN #")
        bookRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("Book 저장 정상")
    fun saveBookTest() {
        // given
        val request = BookRequest("book1", BookType.COMPUTER)

        // when
        bookService.saveBook(request)

        // then
        val saved = bookRepository.findAll()
        assertThat(saved).hasSize(1)
        assertThat(saved[0].name).isEqualTo("book1")
        assertThat(saved[0].type).isEqualTo(BookType.COMPUTER)
    }

    @Test
    @DisplayName("Book 대출 정상")
    fun loanBookTest() {
        // given
        bookRepository.save(Book.fixture("book1"))
        val savedUser = userRepository.save(User("yubora", null))
        val request = BookLoanRequest("yubora", "book1")

        // when
        bookService.loanBook(request)

        //then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].bookName).isEqualTo("book1")
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.LOANED)

    }

    @Test
    @DisplayName("이미 대출된 책일 경우 예외 처리 정상")
    fun loanBookExceptionTest() {
        // given
        bookRepository.save(Book.fixture("book1"))

        val savedUser = userRepository.save(User("yubora", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "book1"))

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
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "book1"))
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
        assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @Test
    @DisplayName("전체 도서 대출 현황")
    fun countLoanedBookTest() {
        // given
        val savedUser = userRepository.save(User("yubora", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "book1"))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "book2", UserLoanStatus.RETURNED))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "book3", UserLoanStatus.RETURNED))

        // when
        val result = bookService.countLoanedBook()

        // then
        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("분야별 보유 도서 수량")
    fun getBookStatistics() {
        // given
        bookRepository.saveAll(listOf(
            Book.fixture("book1", BookType.COMPUTER),
            Book.fixture("book2", BookType.ECONOMY),
            Book.fixture("book3", BookType.ECONOMY),
        ))

        // when
        val results = bookService.getBookStatistics()

        // then
        assertThat(results).hasSize(2)
        assertCount(results, BookType.COMPUTER, 1L)
        assertCount(results, BookType.ECONOMY, 2L)
    }

    private fun assertCount(result: List<BookStatResponse>, type: BookType, expectedCount: Long) {
        assertThat(result.first { it.type == type }.count).isEqualTo(expectedCount)
    }

}