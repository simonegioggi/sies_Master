package siap.sico.webservice.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import f3b.log.LogF3B;

public class ActcallWsNsc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String chiamoNSC(String flussoXML) {

		// public String
		// chiamoNSC(it.mig.sies.type.DATICHIAMATATRASFERIMENTODocument.DATICHIAMATATRASFERIMENTO flussoXML )
		// {

		String messaggio = "";
		Object rispostaWS = null;

		try {
			Call call = (Call) new Service().createCall();
			call.setTargetEndpointAddress(new URL(
					"http://10.0.20.74:9000/axis/IscriviProvvedimentoProvvisorio.jws")); // IP Ufficio
			// "http://10.5.206.149:9001/sies/iscriviProvvedimentoProvvisorio")); //IP CASELLARIO
			// "http://10.5.207.164:8001/sies/iscriviProvvedimentoProvvisorio")); //IP CASELLARIO

			// call.setOperationName(new QName("urn:ciao", "sayHello"));
			call.setOperationName(new QName("urn:IscriviProvvedimentoProvvisorio",
					"iscriviProvvedimentoProvvisorio"));
			// call.setOperationName(new QName("urn:IscriviProvvedimentoProvvisorio",
			// "iscriviProvvedimentoProvvisorio"));

			// call.addParameter("in1", XMLType.XSD_STRING, ParameterMode.IN); // Aggiunto x STRING
			// call.addParameter("dATI_CHIAMATA_TRASFERIMENTO", XMLType.XSD_ANYTYPE, ParameterMode.IN); //
			// Aggiunto x dATI_CHIAMATA_TRASFERIMENTO
			rispostaWS = call.invoke(new Object[] { flussoXML });
			// messaggio = "il Web service ha risposto: " + (String) rispostaWS;
		} catch (MalformedURLException ex) {
			messaggio = "errore: l'url non è esatta";
		} catch (ServiceException ex) {
			messaggio = "errore: la creazione della chiamata è fallita";
		} catch (RemoteException ex) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("------------------------------");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Errore WS", ex);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("------------------------------");
			messaggio = "errore: l'invocazione del WS è fallita";
		} finally {
			siesLogger.info(messaggio);
		}
		return (String) rispostaWS;
	}

}