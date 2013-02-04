package com.soulgalore.jdbcmetrics.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

class ResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream byteOutputStream;
	private final ServletOutputStream outputStream;
	private final StringWriter stringWriter;
	private final PrintWriter printWriter;

	private boolean useOutputStream = true;

	ResponseWrapper(HttpServletResponse response) {
		super(response);
		byteOutputStream = new ByteArrayOutputStream();
		outputStream = new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				byteOutputStream.write(b);
			}
		};
		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		useOutputStream = false;
		return printWriter;
	}

	@Override
	public void flushBuffer() throws IOException {
		// don't flush it
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		ServletOutputStream out = new ServletOutputStream() {
			@Override
			public void write(int b) throws IOException {
				outputStream.write(b);
			}
		};
		return out;
	}

	public void write() throws IOException {
		if (!useOutputStream) {
			super.getWriter().print(stringWriter.toString());
		} else {
			super.getOutputStream().write(byteOutputStream.toByteArray());
		}
	}
}
