package com.tymex.github.data.core

import com.tymex.github.data.core.data.ext.toNetworkResponse
import com.tymex.github.data.core.data.model.NetworkResponse
import junit.framework.TestCase.assertTrue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response

class NetworkResponseTest {

    @Test
    fun `when response is successful and body is present, should return NetworkResponse Success`() {
        val mockResponse = Response.success("Hello World")

        val result = mockResponse.toNetworkResponse()

        assertTrue(result is NetworkResponse.Success)
        assertThat((result as NetworkResponse.Success).data, `is`("Hello World"))
    }

    @Test
    fun `when response is successful but body is null, should return NetworkResponse Error`() {
        val mockResponse: Response<String> = Response.success(null)

        val result = mockResponse.toNetworkResponse()

        assertTrue(result is NetworkResponse.Error)
        assertThat((result as NetworkResponse.Error).message, `is`("Response body is empty"))
    }

    @Test
    fun `when response is unsuccessful with error body, should return NetworkResponse Error`() {
        val errorBody = ResponseBody.create("application/json".toMediaType(), "Not Found")
        val mockResponse: Response<String> = Response.error(404, errorBody)

        val result = mockResponse.toNetworkResponse()

        assertTrue(result is NetworkResponse.Error)
        assertThat((result as NetworkResponse.Error).message, `is`("Not Found"))
    }

    @Test
    fun `when response is unsuccessful with null error body, should return NetworkResponse Error with Unknown`() {
        val mockResponse: Response<String> = Response.error(500, Mockito.mock(ResponseBody::class.java))

        val result = mockResponse.toNetworkResponse()

        assertTrue(result is NetworkResponse.Error)
        assertThat((result as NetworkResponse.Error).message, `is`("Unknown"))
    }
}
