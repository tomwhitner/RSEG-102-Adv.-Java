
FTClient/FTServer (c) 2012 Tom Whitner

 FTClient/FTServer provides a pair of application which offers a 
  rudimentary implementation of the File Transfer Protocol (FTP).

 These applications use two ports for communication:
  Command Port (8189) - This port is used to communicate commands 
  Data Port (8190) - This port is used to transfer the files

 These applications support sending and receiving both ascii and binary files.


FTClient

 Commands - The client application supports the following commands:
 
  OPEN hostname - open a connection to the specified host
  CLOSE - closes the connection
  QUIT - quits the client, closing the connection if open
  MODE A|B - sets the transfer mode to Ascii or Binary
  GET filename - retrieves the specified file from the server
  PUT filename - stores the specified file to the server
  KILL - kills the server (admin priveleges required)
  HELP - prints this listing

  Commands are not case-sensitive.
  
 Users - The following users are supported
  
  Anonymous - use any password
  Admin - password is 'admin'
  
  User names are not case-sensitive.  Passwords are case-sensitive.
  The Admin account can be used to terminate the server from the client with the KILL command.


FTServer

 Commands - The server application supports the following commands:
 
 	USER - Transmit user name to server
	PASS - Transmit password to server
	TYPE - Set the file type: ascii or binary
	PASV - Request a separate socket for file transfer
	RETR - Retrieve (get) a file from the server
	STOR - Store (put) a file to the server
	QUIT - Close the connection
	KILL - Kill the server and close the connection, if authorized

 This is just for information.  The client handles sending these commands to the server.


Sample Files
 
 There are four sample files provided to test the service.
  client.jpg - Sample binary file to test PUT
  client.txt - Sample ascii file to test PUT
  server.png - Sample binary file to test GET
  server.txt - Sample ascii file to test GET


Known Limitations

 The user's password is not masked in the FTClient application for the reasons listed here:
    http://java.sun.com/developer/technicalArticles/Security/pwordmask/ 
  
 Admin is the only named account.  It is hard-coded.
 
 There is no support for directories.  Directories cannot be changed or listed.  
   All files are stored to and retrieved from predefined directories.
   
 If the target file exists, it will simply be overwritten without warning.
   
   
Patterns

 The following patterns were used in the implementation of these applications:
  Command - Both the client and server employ the Command Pattern to implement the various commands each supports.
  Strategy - The Strategy Pattern is used to abstract away the differences of the ascii and binary file transfer.
  Singleton - The two stateless file transfer strategies are implemented as Singletons as there is only ever one instance of each needed.
  Template Method - The file transfer commands for both the client and server employ the Template Method pattern as the file transfer algorithm 
     is the similar for both puts and gets.  Only the direction of the transfer and minor message details are different.
  State - A minor variation of the State Strategy is used in the client to manage the behavior of commands that make sense only when the connection 
     is open or closed.  This is accomplished by substituting and InvalidCommand in place of the actual command when the command does not make
     sense for the current connection state.
    
Sample Scenario

	Begin by running the client and server applications.
	Proceed by following the script below.
	User input is left justified; Applcation output is indented.
	
	Welcome to FTClient...
OPEN localhost
	100 FTServer Ready.
	User:
anonymous
	200-User 'anonymous' accepted.
	200 Send email for password.
	Password:
test@gmail.com
	200 User 'anonymous' logged in.
	Connected to localhost
get server.txt
	200-Ready to accept data connection.
	200-Send token to connect.
	200 Token = -3522419962662846009
	100 Begin receiving.
	200 File sent.
	File received.
put client.txt
	200-Ready to accept data connection.
	200-Send token to connect.
	200 Token = -7199798071408646044
	100 Begin sending.
	200 File received.
	File sent.
mode b
	200 Mode set to Binary.
get server.png
	200-Ready to accept data connection.
	200-Send token to connect.
	200 Token = 2390053721138672911
	100 Begin receiving.
	200 File sent.
	File received.
put client.jpg
	200-Ready to accept data connection.
	200-Send token to connect.
	200 Token = 7382588854311984005
	100 Begin sending.
	200 File received.
	File sent.
close
	200 Goodbye!
	Connnection closed.
open localhost
	100 FTServer Ready.
	User:
admin
	200-User 'admin' accepted.
	200 Password required for user 'admin'
	Password:
admin
	200-User 'admin' logged in.
	200 User has administrator privileges.
	Connected to localhost
kill
	200 Terminating Server.  Goodbye!
	Server terminated.
quit
	Quitting.  Goodbye.


References

 FTP info: http://cr.yp.to/ftp.html
