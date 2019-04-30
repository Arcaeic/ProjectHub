# Threaded Web Servers
Created Tuesday 23 January 2018

### Single-Threaded

Web server with cache and disk
wait for a request
process the request
check cache; if hit, break
otherwise, retrieve from disk (slow)
respond to the request
One request at a time
or create a new process on each request (expensive)


### Multi-threaded
Dispatcher thread
wait for a request
hand off the request
Worker threads
Process the request
disk I/O
respond to the request
"many" requests at a time



