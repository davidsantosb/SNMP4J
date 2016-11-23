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

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
		Router r1=new Router();
		r1.ipAddress="192.168.100.1";
		Router r2=new Router();
		r2.ipAddress="192.168.102.1";
		Router r3=new Router();
		r3.ipAddress="192.168.103.1";
		Router r4=new Router();
		r4.ipAddress="192.168.104.1";
		Router r5=new Router();
		r5.ipAddress="192.168.105.1";
		Router r6=new Router();
		r6.ipAddress="192.168.106.1";
		
		System.out.println("Instancias de Router Creadas");
		boolean infinito=true;
		
		r1.RefreshTables();
		r2.RefreshTables();
		r3.RefreshTables();
		r4.RefreshTables();
		r5.RefreshTables();
		r6.RefreshTables();
		
		while(infinito){
			
			Thread.sleep(10000);
			r1.costCalculation();
			
		}
		
			
	}
		
	

}