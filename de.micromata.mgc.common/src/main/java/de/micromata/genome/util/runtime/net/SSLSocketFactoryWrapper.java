//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.genome.util.runtime.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

/**
 * Simple wrapper.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class SSLSocketFactoryWrapper extends SSLSocketFactory
{
  protected SSLSocketFactory target;

  public SSLSocketFactoryWrapper()
  {

  }

  public SSLSocketFactoryWrapper(SSLSocketFactory target)
  {
    this.target = target;
  }

  @Override
  public Socket createSocket() throws IOException
  {
    return target.createSocket();
  }

  @Override
  public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException
  {
    return target.createSocket(arg0, arg1, arg2, arg3);
  }

  @Override
  public Socket createSocket(InetAddress arg0, int arg1) throws IOException
  {
    return target.createSocket(arg0, arg1);
  }

  @Override
  public Socket createSocket(Socket arg0, InputStream arg1, boolean arg2) throws IOException
  {
    return target.createSocket(arg0, arg1, arg2);
  }

  @Override
  public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException
  {
    return target.createSocket(arg0, arg1, arg2, arg3);
  }

  @Override
  public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3) throws IOException, UnknownHostException
  {
    return target.createSocket(arg0, arg1, arg2, arg3);
  }

  @Override
  public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException
  {
    return target.createSocket(arg0, arg1);
  }

  @Override
  public boolean equals(Object obj)
  {
    return target.equals(obj);
  }

  @Override
  public String[] getDefaultCipherSuites()
  {
    return target.getDefaultCipherSuites();
  }

  @Override
  public String[] getSupportedCipherSuites()
  {
    return target.getSupportedCipherSuites();
  }

  @Override
  public int hashCode()
  {
    return target.hashCode();
  }

  @Override
  public String toString()
  {
    return target.toString();
  }

  public SSLSocketFactory getTarget()
  {
    return target;
  }

  public void setTarget(SSLSocketFactory target)
  {
    this.target = target;
  }

}
