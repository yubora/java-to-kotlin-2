package com.group.libraryapp.service.user

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val bookRepository: BookRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    // 각각의 테스트들은 Spring Context를 공유하기 때문에
    // 정상적인 테스트를 위해서는 각 메서드 종료 후 공유 자원인 DB를 정리해줘야 함
    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("User 저장 정상")
    fun saveUserTest() {
        // given
        val userCreateRequest = UserCreateRequest("yubora", null)

        // when
        userService.saveUser(userCreateRequest)

        // then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("yubora")
        assertThat(results[0].age).isNull()
    }

    @Test
    @DisplayName("User 조회 정상")
    fun getUsersTest() {
        // given
        userRepository.saveAll(listOf(
            User("yubora", 100),
            User("yugreen", null)
        ))

        // when
        val results = userService.getUsers()

        // then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("yubora", "yugreen")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(100, null)
    }

    @Test
    @DisplayName("User 수정 정상")
    fun updateUserNameTest() {
        // given
        val savedUser = userRepository.save(User("yubora", null))
        val request = UserUpdateRequest(savedUser.id!!, "purple")

        // when
        userService.updateUserName(request)

        // then
        val result = userRepository.findById(savedUser.id!!).get()
        assertThat(result.name).isEqualTo("purple")
    }

    @Test
    @DisplayName("User 삭제 정상")
    fun deleteUserTest() {
        // given
        userRepository.save(User("yubora", 100))

        // when
        userService.deleteUser("yubora")

        // then
        assertThat(userRepository.findAll()).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 없는 User도 응답에 포함")
    fun getUserLoanHistoriesTest1() {
        // given
        userRepository.save(User("yubora", null))

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("yubora")
        assertThat(results[0].books).isEmpty()
    }

    @Test
    @DisplayName("대출 기록이 많은 User의 응답 정상")
    fun getUserLoanHistoriesTest2() {
        // given
        val savedUser = userRepository.save(User("yubora", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser, "book1", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "book2", UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser, "book3", UserLoanStatus.RETURNED),
        ))
//        savedUser.loanBook(Book.fixture("book1"))
//        savedUser.loanBook(Book.fixture("book2"))
//        savedUser.loanBook(Book.fixture("book3"))
//        savedUser.returnBook("book3")
//        userRepository.save(savedUser)

        // when
        val results = userService.getUserLoanHistories()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("yubora")
        assertThat(results[0].books).hasSize(3)
        assertThat(results[0].books).extracting("name").containsExactlyInAnyOrder("book1", "book2", "book3")
        assertThat(results[0].books).extracting("isReturn").containsExactlyInAnyOrder(false, false, true)
//        assertThat(results[0].books[0].name).isEqualTo("book1")
//        assertThat(results[0].books[0].isReturn).isFalse
//        assertThat(results[0].books[1].name).isEqualTo("book2")
//        assertThat(results[0].books[1].isReturn).isFalse
//        assertThat(results[0].books[2].name).isEqualTo("book3")
//        assertThat(results[0].books[2].isReturn).isTrue
    }

    @Test
    @DisplayName("대출 기록 조회 정상")
    fun getUserLoanHistoriesTest() {
        // given
        val user = userRepository.save(User("yubora", null))
        val book = bookRepository.save(Book.fixture("book1"))
        user.loanBook(book)

        // when
        val result = userService.getUserLoanHistories()

        // then
        assertThat(result[0].name).isEqualTo("yubora")
        assertThat(result[0].books).hasSize(1)
        assertThat(result[0].books[0].name).isEqualTo("book1")
        assertThat(result[0].books[0].isReturn).isFalse
    }

}