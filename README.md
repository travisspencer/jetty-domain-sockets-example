# Jetty Domain Socket Proxy Example

This example shows how to:

1. Create an HTTP service that listens on a UNIX domain socket
2. Proxy HTTP connections to a downstream service that is listening on a UNIX domain socket

Both use Jetty 9.4. 

> *NOTE*: The socket is hard-coded to be `/tmp/test.sock`.

## Clients

When testing just the UNIX socket server, it can be helpful to test without the proxy. This is a little tricky though because most HTTP clients, like Postman, can't connect to a UNIX socket. To test this, the following Python client can be used:

```python
import socket
import sys


sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)

try:
    sock.connect("/tmp/test.sock")
except socket.error, msg:
    print msg
    sys.exit(1)

try:
    sock.sendall("""GET / HTTP/1.1
Host: localhost

""")

    result = sock.recv(10000)

    while len(result) > 0:
        print(result)
        result = sock.recv(10000)

finally:
    sock.close()
``` 

With this or an HTTP client that connects through the proxy, the result will be an HTML page like this:

```html
<html>
    <head>
        <title>Hello World Servlet</title>
    </head>
    <body>Hello World! How are you doing?</body>
</html>
```