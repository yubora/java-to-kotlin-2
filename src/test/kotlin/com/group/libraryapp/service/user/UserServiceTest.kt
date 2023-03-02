package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
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
    private val userService: UserService
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
        val results = userService.users

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
        val savedUser = userRepository.findByName("yubora").get()

        // when
        userService.deleteUser(savedUser.name)

        // then
        val result = userRepository.findByName(savedUser.name)
        assertThat(result).isEmpty
    }

}