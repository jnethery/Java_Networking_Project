Server Instructions:

When installing a new server, you must first follow these steps:

1.) CreateDatabase
2.) CreateMessageLog

Then, simple run "Server" and clients will be able to connect.
Server will intercept all connections to the predefined socket for the server
SimpleConverseServer will handle all client requests. A SimpleConverseServer thread will run
from the moment a client connects to the server until that client disconnects. 