/*
----------------------------------------------------------------------------------------------------------------
Assignment No:
Title:A Java program to print a DNS (Domain Name System) record for an Internet Address
Code
 
Roll NO:44 
Batch: TEA-3
----------------------------------------------------------------------------------------------------------------
*/
// Print out DNS Record for an Internet Address
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DNSLookup
{
    public static void main(String args[])
    {
        // explain what program does and how to use it 
        if (args.length != 1)
        {
            System.err.println("Print out DNS Record for an Internet Address");
            System.err.println("USAGE: java DNSLookup domainName|domainAddress");
            System.exit(-1);
        }
        try
        {
            InetAddress inetAddress;
            // if first character is a digit then assume is an address
            if (Character.isDigit(args[0].charAt(0)))
            {   // convert address from string representation to byte array
                byte[] b = new byte[4];
                String[] bytes = args[0].split("[.]");
                for (int i = 0; i < bytes.length; i++)
                {
                    b[i] = new Integer(bytes[i]).byteValue();
                }
                // get Internet Address of this host address
                inetAddress = InetAddress.getByAddress(b);
            }
            else
            {   // get Internet Address of this host name
                inetAddress = InetAddress.getByName(args[0]);
            }
            // show the Internet Address as name/address
            System.out.println(inetAddress.getHostName() + "/" + inetAddress.getHostAddress());
            // get the default initial Directory Context
            InitialDirContext iDirC = new InitialDirContext();
            // get the DNS records for inetAddress
            Attributes attributes = iDirC.getAttributes("dns:/" + inetAddress.getHostName());
            // get an enumeration of the attributes and print them out
            NamingEnumeration attributeEnumeration = attributes.getAll();
            System.out.println("-- DNS INFORMATION --");
            while (attributeEnumeration.hasMore())
            {
                System.out.println("" + attributeEnumeration.next());
            }
            attributeEnumeration.close();
        }
        catch (UnknownHostException exception)
        {
            System.err.println("ERROR: No Internet Address for '" + args[0] + "'");
        }
        catch (NamingException exception)
        {
            System.err.println("ERROR: No DNS record for '" + args[0] + "'");
        }
    }
}
/*
OUTPUT:
gescoe@gescoe-OptiPlex-3020:~$ cd Desktop/
gescoe@gescoe-OptiPlex-3020:~/Desktop$ cd gaurav
gescoe@gescoe-OptiPlex-3020:~/Desktop/gaurav/DNS$ javac DNSLookup.java 
gescoe@gescoe-OptiPlex-3020:~/Desktop/gaurav/DNS$ java DNSLookup


::] java DNSLookup diablo.cs.fsu.edu
diablo.cs.fsu.edu/128.186.120.2
-- DNS INFORMATION --
A: 128.186.120.2
MX: 10 mail.cs.fsu.edu.

::] java DNSLookup fsu.edu
fsu.edu/128.186.6.14
-- DNS INFORMATION --
A: 128.186.6.14
NS: trantor.umd.edu., nsx.lbl.gov., dns1.fsu.edu., dns2.fsu.edu., dnsa.fsu.edu.
SOA: dns1.fsu.edu. hostmaster.acns.fsu.edu. 2007020700 3600 1200 604800 86400
MX: 0 ms4.ucs.fsu.edu., 0 ms6.ucs.fsu.edu., 0 ms7.ucs.fsu.edu., 10 ms2.ucs.fsu.edu., 20 ms8.ucs.fsu.edu., 0 ms3.ucs.fsu.edu.

::] java DNSLookup 128.186.6.14
www.fsu.edu/128.186.6.14
-- DNS INFORMATION --
A: 128.186.6.14
MX: 0 ms6.ucs.fsu.edu., 0 ms7.ucs.fsu.edu., 10 ms8.ucs.fsu.edu., 0 ms2.ucs.fsu.edu., 0 ms3.ucs.fsu.edu., 0 ms4.ucs.fsu.edu.

::] java DNSLookup yahoo.com
yahoo.com/216.109.112.135
-- DNS INFORMATION --
A: 66.94.234.13, 216.109.112.135
NS: ns9.yahoo.com., ns1.yahoo.com., ns2.yahoo.com., ns3.yahoo.com., ns4.yahoo.com., ns5.yahoo.com., ns8.yahoo.com.
MX: 1 c.mx.mail.yahoo.com., 1 d.mx.mail.yahoo.com., 1 e.mx.mail.yahoo.com., 1 f.mx.mail.yahoo.com., 1 g.mx.mail.yahoo.com., 1 a.mx.mail.yahoo.com., 1 b.mx.mail.yahoo.com.

::] java DNSLookup 199.232.41.10
www.gnu.org/199.232.41.10
-- DNS INFORMATION --
CNAME: gnu.org.

::] java DNSLookup 199.232.41
199.232.41.0/199.232.41.0
ERROR: No DNS record for '199.232.41'

::] java DNSLookup ghthytf.com
ERROR: No Internet Address for 'ghthytf.com'
*/
