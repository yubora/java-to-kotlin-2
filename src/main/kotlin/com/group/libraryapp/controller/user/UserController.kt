package com.group.libraryapp.controller.user

import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import com.group.libraryapp.dto.user.response.UserResponse
import com.group.libraryapp.service.user.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/user")
    fun saveUser(@RequestBody request: UserCreateRequest) {
        userService.saveUser(request)
    }

    @GetMapping("/user")
    fun gerUsers(): List<UserResponse> {
        return userService.getUsers()
    }

//    @GetMapping("/user")
//    fun gerUsers(): List<UserResponse> = userService.getUsers()

    @PutMapping("/user")
    fun updateUserName(@RequestBody request: UserUpdateRequest) {
        userService.updateUserName(request)
    }

    @DeleteMapping("/user")
    fun deleteUser(@RequestParam name: String) { // @RequestParam을 String?로 받으면 (nullable)
                                                 // Spring이 @RequestParam의 required() 값을 false로 바꾼다.
                                                 // 하지만 deleteUser의 name은 필수로 받아야 하는 값이기 때문에
                                                 // @RequestParam에서 String?으로 받지 않아야 한다.
        userService.deleteUser(name)
    }
}