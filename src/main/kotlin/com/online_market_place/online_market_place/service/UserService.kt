//package com.online_market_place.online_market_place.service
//
//import com.online_market_place.online_market_place.dto.UserRequestDTO
//import com.online_market_place.online_market_place.entiy.user.UserEntity
//import com.online_market_place.online_market_place.repository.user.UserRepository
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Service
//
//@Service
//class UserService (@Autowired private val UserRepository: UserRepository) {
//
//    fun createUser(userRequest: UserRequestDTO): String {
//        val user =  UserEntity(
//            firstName = userRequest.firstName,
//            lastName = userRequest.lastName,
//            email = userRequest.email,
//            password = userRequest.password,
//            address = userRequest.address,
//            phoneNumber = userRequest.phoneNumber,
//            emailVerified = false,
//            verificationToken = null,
//            tokenExpiryDate = null,
//
//
//                )
//       UserRepository.save(user)
//        return "User created successfully"
//    }
//
//}