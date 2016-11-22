import java.io.IOException;
/*
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;*/


public class gestor {

	public gestor() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Router r1=new Router();
		r1.ipAddress="192.168.100.1";
		r1.RefreshTables();
		
		Router r2=new Router();
		r2.ipAddress="192.168.102.1";
		r2.RefreshTables();
				
		Router r3=new Router();
		r3.ipAddress="192.168.103.1";
		r3.RefreshTables();
		
		Router r4=new Router();
		r4.ipAddress="192.168.104.1";
		r4.RefreshTables();
		
		Router r5=new Router();
		r5.ipAddress="192.168.105.1";
		r5.RefreshTables();
		
		Router r6=new Router();
		r6.ipAddress="192.168.106.1";
		r6.RefreshTables();
		System.out.println("DONE");
	}
	
	
	

}
