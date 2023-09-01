package com.leemccormick.posdemo.aspect;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/* *** NOTE : About STATUS CODE ***
The choice of HTTP status code to use in your response when handling exceptions depends on the nature of the exception and the specific use case. Here are some common HTTP status codes and scenarios in which they are typically used when handling exceptions:

1. **200 OK**: This status code indicates a successful request. You can use it when the operation requested by the client is successfully completed without any errors.

2. **201 Created**: Use this status code when a new resource is successfully created as a result of the request. For example, after successfully creating a new resource, you can respond with a `201 Created` status along with a link to the newly created resource.

3. **204 No Content**: Use this status code when the request was successful, but there is no additional data to return in the response body. This is commonly used for successful DELETE requests.

4. **400 Bad Request**: This status code indicates that the request from the client is malformed or invalid. You can use it when the client sends invalid input data or parameters.

5. **401 Unauthorized**: Use this status code when the client's request lacks proper authentication credentials or the provided credentials are invalid.

6. **403 Forbidden**: This status code is used when the client's request is understood, but it is not allowed due to insufficient permissions. It indicates that the client does not have access to the requested resource.

7. **404 Not Found**: Use this status code when the requested resource does not exist. It indicates that the server could not find the resource at the specified URL.

8. **422 Unprocessable Entity**: This status code is used to indicate that the server understands the request but cannot process it due to semantic errors, such as validation failures. It's often used for input validation errors.

9. **500 Internal Server Error**: This status code indicates a generic server error that was not anticipated. It's commonly used for unexpected exceptions and errors on the server-side. Avoid exposing sensitive details in the response.

10. **503 Service Unavailable**: Use this status code when the server is temporarily unable to handle the request due to maintenance or high load. It informs the client that the server cannot currently process the request but may be able to do so later.

It's important to choose the most appropriate HTTP status code that accurately reflects the nature of the exception. Additionally, include meaningful error messages or details in the response body to assist the client in understanding and potentially resolving the issue.
*/

@Getter
public class ApiErrorException extends RuntimeException {
    private HttpStatus httpStatus;

    public ApiErrorException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public ApiErrorException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        return "ApiErrorException{" +
                "httpStatus=" + httpStatus +
                '}';
    }
}