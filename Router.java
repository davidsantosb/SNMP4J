import java.io.IOException;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
//import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Router {
	
	private static final int MaxIf = 20;
	private static final int MaxParam = 6;
	public String  ipAddress;
	public String  port; //161 
	public int    snmpVersion;
	public String  community;	
	private String [] oidValues;
	private String[][] RouterTable;
	public	String ospfIfMetricValue;
	ResponseEvent response;
	
	public Router() {
		this.ipAddress=
		this.port="161";
		this.snmpVersion=SnmpConstants.version2c;;
		this.community="cisco";	
		this.oidValues= new String[MaxParam];
		oidValues[0]="1.3.6.1.2.1.2.2.1.1";//Interface IDX
		oidValues[1]="1.3.6.1.2.1.2.2.1.2";//Interface tables (names)
		oidValues[2]="1.3.6.1.2.1.2.2.1.5";//If Speed (configured)
		oidValues[3]="1.3.6.1.2.1.2.2.1.8";//Interface State
		oidValues[4]="1.3.6.1.2.1.2.2.1.16";//The total number of octets transmitted out of the interface, including framing characters.
		oidValues[5]="1.3.6.1.2.1.4.20.1.1";// Interface IP Address
		oidValues[6]="1.3.6.1.2.1.4.20.1.2";// Interface IP Idx
		oidValues[7]="1.3.6.1.2.1.14.8.1.4";// Ospf Metric Value
		this.RouterTable = new String[MaxIf][MaxParam];
	}
	
	public void RefreshTables() throws IOException
	{		
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
		transport.listen();
		// Create Target Address object
		CommunityTarget comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(this.community));
		comtarget.setVersion(this.snmpVersion);
		comtarget.setAddress(new UdpAddress(this.ipAddress + "/" + this.port));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);
		
		Snmp snmp = new Snmp(transport);
		for(int i=0; i < 5;i++)
		{
			PDU pdu = new PDU();
			pdu.setType(PDU.GETBULK);
			pdu.setMaxRepetitions(10);
			pdu.add(new VariableBinding(new OID(oidValues[i])));
			response = snmp.getBulk(pdu, comtarget);

			// Process Agent Response
			if (response != null)
			{
				PDU responsePDU = response.getResponse();
				if (responsePDU != null)
				{
					int errorStatus = responsePDU.getErrorStatus();
					int errorIndex = responsePDU.getErrorIndex();
					String errorStatusText = responsePDU.getErrorStatusText();
					if (errorStatus == PDU.noError)
					{
						int j=0;
						Vector<? extends VariableBinding>  vbs = responsePDU.getVariableBindings();
						for (VariableBinding vb : vbs) 
						{
			                String Value = vb.toValueString();
				            RouterTable[j][i]=Value;
			                j++;
			                System.out.println(Value);
			            }
					}
					else
					{
						System.out.println("Error: Request Failed");
						System.out.println("Error Status = " + errorStatus);
						System.out.println("Error Index = " + errorIndex);
						System.out.println("Error Status Text = " + errorStatusText);
					}
				}
				else
				{
					System.out.println("Error: Response PDU is null");
				}
			}
		}
		//System.out.println("DONE");
	}
	public void RefreshOthers() throws IOException
	{
		TransportMapping<?> transport = new DefaultUdpTransportMapping();
		transport.listen();
		// Create Target Address object
		CommunityTarget comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(this.community));
		comtarget.setVersion(this.snmpVersion);
		comtarget.setAddress(new UdpAddress(this.ipAddress + "/" + this.port));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);
		
		Snmp snmp = new Snmp(transport);
		for(int i=MaxParam-2; i < MaxParam;i++)
		{
			PDU pdu = new PDU();
			pdu.setType(PDU.GET);
			//String aux = new StringBuilder(ospfIfMetricValue).append(RouterTable[i][4]).append(".0.0").toString();
			//System.out.println(aux);
			pdu.add(new VariableBinding(new OID(oidValues[i])));
			response = snmp.getBulk(pdu, comtarget);

			// Process Agent Response
			if (response != null)
			{
				PDU responsePDU = response.getResponse();
				if (responsePDU != null)
				{
					int errorStatus = responsePDU.getErrorStatus();
					int errorIndex = responsePDU.getErrorIndex();
					String errorStatusText = responsePDU.getErrorStatusText();
					if (errorStatus == PDU.noError)
					{
						int j=0;/*
						Vector<? extends VariableBinding>  vbs = responsePDU.getVariableBindings();
		                String Value = vbs.toValueString();
			            RouterTable[i][5]=Value;
		                j++;
		                System.out.println(Value);*/
					}
					else
					{
						System.out.println("Error: Request Failed");
						System.out.println("Error Status = " + errorStatus);
						System.out.println("Error Index = " + errorIndex);
						System.out.println("Error Status Text = " + errorStatusText);
					}
				}
				else
				{
					System.out.println("Error: Response PDU is null");
				}
			}
		}
	}
}

