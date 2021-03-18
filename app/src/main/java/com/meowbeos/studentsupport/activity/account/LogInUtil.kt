package com.meowbeos.studentsupport.activity.account

object LogInUtil {

    private val existingAccount = listOf("nono","yeahyeah")

    /*
    *the input is not valid if...
    * ...the username and password is empty
    * ...the username is not there
    * ...the confirmed password is not the same as the real password
    * ...the password contains less than 6 digits
     */

    fun validateLogInInput(
        username: String,
        password: String,
        confirmationPassword: String
    ):Boolean {
        return true
    }
}