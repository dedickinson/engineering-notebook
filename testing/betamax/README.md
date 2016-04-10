# Betamax explorations

[Betamax website](http://betamax.software/)

## NOTE
Betamax will create a file named "littleproxy_cert" that must be imported into the JRE's certs:

keytool -import -file littleproxy_cert -alias littleproxy -keystore $JAVA_HOME/jre/lib/security/cacerts

I found I had to run this as sudo on OS X with Oracle Java
