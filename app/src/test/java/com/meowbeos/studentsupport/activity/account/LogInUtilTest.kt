package com.meowbeos.studentsupport.activity.account

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LogInUtilTest{

    @Test
    fun `empty username returns false`(){
        val result = LogInUtil.validateLogInInput(
                "",
                "123456",
                "123456"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and password correctly`(){
        val result = LogInUtil.validateLogInInput(
                "non",
                "123456",
                "123456"
        )
        assertThat(result).isTrue()
    }
    @Test
    fun `invalid username and password correctly`(){
        val result = LogInUtil.validateLogInInput(
                "!non",
                "123456",
                "123456"
        )
        assertThat(result).isFalse()
    }


}