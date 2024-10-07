package com.min429.tourism_platform_server.common.exception.handler;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GlobalExceptionController implements ErrorController {

	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest request) {
		Object status = request.getAttribute("javax.servlet.error.status_code");

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());

			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return new ModelAndView("error/error-404");
			}
			else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return new ModelAndView("error/error-500");
			}
		}

		return new ModelAndView("error/error-500");
	}
}
