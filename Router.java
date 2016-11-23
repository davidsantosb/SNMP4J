
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;


import org.apache.commons.net.telnet.TelnetClient;
import java.io.InputStream;
import java.io.PrintStream;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
//import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class Router {
	
	private static final int MaxIf = 20;
	private static final int MaxParam = 8;
	public String  ipAddress;
	public String  port; //161 
	public int    snmpVersion;
	public String  community;	
	private String [] oidValues;
	private String[][] RouterTable;
	public	String ospfIfMetricValue;
	ResponseEvent response;
	public String [] PPSperInf;
	
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
		this.PPSperInf=new String [MaxIf];
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
			                //System.out.println(Value);
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
					//System.out.println("Error: Response PDU is null");
				}
			}
		}
		
		//System.out.println("NULL");
		//System.out.println("DONE");
	}
	
	
	/*
	
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
						int j=0;
						Vector<? extends VariableBinding>  vbs = responsePDU.getVariableBindings();
		                String Value = vbs.toValueString();
			            RouterTable[i][5]=Value;
		                j++;
		                System.out.println(Value);
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
	}*/
	
	
	public void costCalculation() throws IOException{
		
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
		PDU pdu = new PDU();
		pdu.setType(PDU.GETBULK);
		pdu.setMaxRepetitions(10);
		pdu.add(new VariableBinding(new OID(oidValues[4])));
		response = snmp.getBulk(pdu, comtarget);
		
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
		                PPSperInf[j]=Value;
		                j++;
		                System.out.println(Value);
		            }
				}
				else
				{
					//System.out.println("Error: Request Failed");
					//System.out.println("Error Status = " + errorStatus);
					//System.out.println("Error Index = " + errorIndex);
					//System.out.println("Error Status Text = " + errorStatusText);
				}
			}
			else
			{
				//System.out.println("Error: Response PDU is null");
			}
				
		}
		
		//Comparacion
		for (int i=0;i<MaxIf;i++){
			
			
			if(PPSperInf[i]!=null){
				String interfazActual=RouterTable[i][1];
				//int velocidad=Integer.parseInt(RouterTable[i][2]);
				Long velocidad=Long.parseLong(RouterTable[i][2])/10; //REVISAR!!!!!!!
				//System.out.println(interfazActual);
				int PPSantes=Integer.parseInt(RouterTable[i][4]);
				int PPSahora=Integer.parseInt(PPSperInf[i]);;
				int diferencia=PPSahora-PPSantes;
				//System.out.println("Diferencia: "+diferencia);
				
				
				long porcentaje=(diferencia*800)/(10*velocidad);					
				//System.out.println("Porcentaje: "+porcentaje);
				int nuevoCosto=64;
								
				if(porcentaje<=30){
					nuevoCosto=64;				
				}
								
				if(porcentaje>30 && porcentaje <=50){
					nuevoCosto=100;					
				}		

				if(porcentaje>50 && porcentaje <=70){				
					nuevoCosto=250;					
				}
				if(porcentaje>70 && porcentaje <=80){
					nuevoCosto=500;
				}
				if(porcentaje>80 && porcentaje <=90){
					nuevoCosto=900;
				}
				if(porcentaje>90 && porcentaje <=100){
					nuevoCosto=1000;	
				}
				
				System.out.println("En: "+this.ipAddress+ " Interfaz: "+interfazActual+" Porcentaje: "+ porcentaje+" Costo: "+ nuevoCosto);
				AutomatedTelnetClient clienteTelnet= new AutomatedTelnetClient(this.ipAddress,interfazActual,nuevoCosto);	
				System.out.println();
				
				this.RefreshTables();
				
			}
			
			
		
			
		}
			
		System.out.println("VAMOS BIEN");
		
		//Comparar
		
		// Calcular nuevo Costo
		
		// Sacar nombre interfaz
		
		// Enviar comando
		
		
		
		
	}
	
	
	
	
	
}