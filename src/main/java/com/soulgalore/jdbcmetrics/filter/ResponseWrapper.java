/******************************************************
 * JDBCMetrics
 * 
 * 
 * Copyright (C) 2013 by Magnus Lundberg (http://magnuslundberg.com) & Peter Hedenskog
 * (http://peterhedenskog.com)
 * 
 ****************************************************** 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 ******************************************************* 
 */
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
