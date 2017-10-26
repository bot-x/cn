/*** testclient.java ***/

import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;

class testclient
{

public static void main(String args[])throws IOException
{
InetAddress addr=InetAddress.getByName("Localhost");
System.out.println(addr);

Socket connection=new Socket(addr,5000);

BufferedInputStream in=new BufferedInputStream(connection.getInputStream());
DataOutputStream out=new DataOutputStream(connection.getOutputStream());
Scanner scr=new Scanner(System.in);// this will be used to accept i/p from console


System.out.println(".......Client........");
System.out.println("Connect");
System.out.println("Enter the number of frames to be requested to the server");
int c=scr.nextInt();

out.write(c);
out.flush();

System.out.println("Enter the type of trans. Error=1 ; No Error=0");
int choice=scr.nextInt();
out.write(choice);

int check=0;
int i=0;
int j=0;

if(choice==0)
{
for(j=0;j<c;++j)
{
i=in.read();
System.out.println("received frame no: "+i);
System.out.println("Sending acknowledgement for frame no: "+i);
out.write(i);
out.flush();
}
out.flush();
}
else
{
for(j=0;j<c;++j)
{
i=in.read();
if(i==check)
{
System.out.println("received frame no: "+i);
System.out.println("Sending acknowledgement for frame no: "+i);
out.write(i);
++check;
}
else
{
--j;
System.out.println("Discarded frame no: "+i);
System.out.println("Sending NEGATIVE ack");
out.write(-1);
}
out.flush();
}
}//end of else for error

in.close();
out.close();
System.out.println("Quiting");

}// end of main method
}// end of main class

/*
Output:-

gescoe@gescoe-OptiPlex-3020:~/Desktop$ javac testclient.java
gescoe@gescoe-OptiPlex-3020:~/Desktop$ java testclient
Localhost/127.0.0.1
.......Client........
Connect
Enter the number of frames to be requested to the server
4
Enter the type of trans. Error=1 ; No Error=0
0
received frame no: 0
Sending acknowledgement for frame no: 0
received frame no: 1
Sending acknowledgement for frame no: 1
received frame no: 2
Sending acknowledgement for frame no: 2
received frame no: 3
Sending acknowledgement for frame no: 3
Quiting

gescoe@gescoe-OptiPlex-3020:~/Desktop$ java testclient
Localhost/127.0.0.1
.......Client........
Connect
Enter the number of frames to be requested to the server
7
Enter the type of trans. Error=1 ; No Error=0
1
received frame no: 0
Sending acknowledgement for frame no: 0
received frame no: 1
Sending acknowledgement for frame no: 1
Discarded frame no: 3
Sending NEGATIVE ack
Discarded frame no: 4
Sending NEGATIVE ack
Discarded frame no: 5
Sending NEGATIVE ack
Discarded frame no: 6
Sending NEGATIVE ack
received frame no: 2
Sending acknowledgement for frame no: 2
received frame no: 3
Sending acknowledgement for frame no: 3
received frame no: 4
Sending acknowledgement for frame no: 4
received frame no: 5
Sending acknowledgement for frame no: 5
received frame no: 6
Sending acknowledgement for frame no: 6
Quiting
gescoe@gescoe-OptiPlex-3020:~/Desktop$ 
*/


/*** testserver.java  ****/

import java.io.*;
import java.net.*;
import java.util.*;
class testserver
{
public static void main(String args[])throws IOException
{
System.out.println("...........Server..........");
System.out.println("Waiting for connection....");
InetAddress addr=InetAddress.getByName("Localhost");
ServerSocket ss=new ServerSocket(5000);

Socket client=new Socket();
client=ss.accept();

BufferedInputStream in=new BufferedInputStream(client.getInputStream());
DataOutputStream out=new DataOutputStream(client.getOutputStream());

System.out.println("Received request for sending frames");
int p=in.read();

boolean f[]=new boolean[p];

int pc=in.read();
System.out.println("Sending....");

if(pc==0)
{
for(int i=0;i<p;++i)
{
System.out.println("sending frame number "+i);
out.write(i);
out.flush();
System.out.println("Waiting for acknowledgement");
try
{
Thread.sleep(7000);
}
catch(Exception e){}

int a=in.read();
System.out.println("received acknowledgement for frame "+i+" as "+a);
}
out.flush();
}
else
{
for(int i=0;i<p;++i)
{
if(i==2)
{
System.out.println("sending frame no "+i);
}
else
{
System.out.println("sending frame no "+i);
out.write(i);
out.flush();
System.out.println("Waiting for acknowledgement ");
try
{
Thread.sleep(7000);
}
catch(Exception e){}

int a=in.read();

if(a!=255)
{
System.out.println("received ack for frame no: "+i+" as "+a);
f[i]=true;
}
}// end of inner else
}// end of for

// check which frames have not been ack

for(int a=0;a<p;++a)
{
if(f[a]==false)
{
System.out.println("Resending frame "+a);
out.write(a);
out.flush();
System.out.println("Waiting for ack ");
try
{
Thread.sleep(5000);
}
catch(Exception e){}

int b=in.read();
System.out.println("received ack for frame no: "+a+" as "+b);
f[a]=true;
}
}
out.flush();
}// end of else which is for error 

in.close();
out.close();
client.close();
ss.close();
System.out.println("Quiting");

}// end main method
}// end main class

/*
Output:-
gescoe@gescoe-OptiPlex-3020:~$ cd Desktop
gescoe@gescoe-OptiPlex-3020:~/Desktop$ javac testserver.java
gescoe@gescoe-OptiPlex-3020:~/Desktop$ java testserver
...........Server..........
Waiting for connection....
Received request for sending frames
Sending....
sending frame number 0
Waiting for acknowledgement
received acknowledgement for frame 0 as 0
sending frame number 1
Waiting for acknowledgement
received acknowledgement for frame 1 as 1
sending frame number 2
Waiting for acknowledgement
received acknowledgement for frame 2 as 2
sending frame number 3
Waiting for acknowledgement
received acknowledgement for frame 3 as 3
Quiting


gescoe@gescoe-OptiPlex-3020:~/Desktop$ java testserver
...........Server..........
Waiting for connection....
Received request for sending frames
Sending....
sending frame no 0
Waiting for acknowledgement 
received ack for frame no: 0 as 0
sending frame no 1
Waiting for acknowledgement 
received ack for frame no: 1 as 1
sending frame no 2
sending frame no 3
Waiting for acknowledgement 
sending frame no 4
Waiting for acknowledgement 
sending frame no 5
Waiting for acknowledgement 
sending frame no 6
Waiting for acknowledgement 
Resending frame 2
Waiting for ack 
received ack for frame no: 2 as 2
Resending frame 3
Waiting for ack 
received ack for frame no: 3 as 3
Resending frame 4
Waiting for ack 
received ack for frame no: 4 as 4
Resending frame 5
Waiting for ack 
received ack for frame no: 5 as 5
Resending frame 6
Waiting for ack 
received ack for frame no: 6 as 6
Quiting
gescoe@gescoe-OptiPlex-3020:~/Desktop$*/




