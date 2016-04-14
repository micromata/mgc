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

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSL/TLS to create connections with self certificats.
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class EasySSLSocketFactory extends SSLSocketFactoryWrapper
{
  static X509TrustManager untrustManager = new X509TrustManager()
  {

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
      return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType)
    {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType)
    {
    }

  };

  public EasySSLSocketFactory() throws GeneralSecurityException
  {
    this("TLS");
  }

  /**
   * 
   * @param context Must be SSL or TLS
   */
  public EasySSLSocketFactory(String context) throws GeneralSecurityException
  {
    SSLContext ctx = SSLContext.getInstance(context);

    ctx.init(null, new TrustManager[] { untrustManager }, null);
    target = ctx.getSocketFactory();
  }

  public static EasySSLSocketFactory getDefault()
  {
    try {
      return new EasySSLSocketFactory();
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
