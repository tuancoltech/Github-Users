package com.tymex.github.data.core

import com.tymex.github.data.core.data.ext.isLastPage
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class LinkHeaderTest {

    @Test
    fun `when link header contains rel next, should return false`() {
        val linkHeader = "<https://api.github.com/users?per_page=20&since=100>; rel=\"next\""
        assertThat(linkHeader.isLastPage(), `is`(false))
    }

    @Test
    fun `when link header does not contain rel next, should return true`() {
        val linkHeader = "<https://api.github.com/users?per_page=20&since=100>; rel=\"prev\""
        assertThat(linkHeader.isLastPage(), `is`(true))
    }

    @Test
    fun `when link header is empty, should return true`() {
        val linkHeader = ""
        assertThat(linkHeader.isLastPage(), `is`(true))
    }

    @Test
    fun `when link header has unrelated text, should return true`() {
        val linkHeader = "Some random header text"
        assertThat(linkHeader.isLastPage(), `is`(true))
    }

    @Test
    fun `when link header contains both rel next and rel prev, should return false`() {
        val linkHeader = "<https://api.github.com/users?per_page=20&since=100>; rel=\"next\", <https://api.github.com/users?per_page=20&since=50>; rel=\"prev\""
        assertThat(linkHeader.isLastPage(), `is`(false))
    }
}
