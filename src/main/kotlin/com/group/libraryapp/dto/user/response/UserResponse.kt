package com.group.libraryapp.dto.user.response

import com.group.libraryapp.domain.user.User

data class UserResponse(
    val id: Long,
    val name: String,
    val age: Int?,
) {

    // 3. 정적 팩토리 메서드
    companion object {
        fun of(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                name = user.name,
                age = user.age
            )
        }
    }

    // 2. 부생성자 보다 정적 팩토리 메서드 방식을 사용하는 것 권장
//    constructor(user: User): this(
//        id = user.id!!,
//        name = user.name,
//        age = user.age
//    )


    // 1. dto에서 init을 사용하는 것 보다 부생성자를 사용하는 것 권장
//    init {
//        id = user.id!!
//        name = user.name
//        age = user.age
//    }
}