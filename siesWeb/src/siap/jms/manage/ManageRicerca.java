package siap.jms.manage;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import siap.jms.ICostantiJMS;
import siap.jms.JMSLookupRemote;
import siap.jms.SIAPSender;
import siap.jms.messaggio.controller.IMessaggio;
import siap.jms.messaggio.model.MessaggioModel;
import siap.jms.util.ParserMessage;
import siap.sico.jms.controller.IRicercaSICOJMS;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.jms.controller.IRicercaJMS;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.StringUtils;
import f3b.util.xml.TreeModel;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ManageRicerca implements ICostantiJMS {

	// 28/05/2019 [EC] - Modifico la destinazione su log PER MEV PROBLEMA CODE INTRODOTTO IN SIES 11.3
	private static Logger siesLogger = Logger.getLogger(LogF3B.JMS_LOG);
	
	//[EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: aggiungo una lista di uffici minorili
	protected static Set<String> ufficiMinori = new HashSet<String>();
	static {
		ufficiMinori.add("PMM");
		ufficiMinori.add("DIBM");
		ufficiMinori.add("GIPM");
		ufficiMinori.add("GUPM");
		ufficiMinori.add("CAPSM");
		ufficiMinori.add("TDSM");
		ufficiMinori.add("UDSM");
	}
	
	/**
	 * Metodo che controlla se l'ufficio emittente della ricerca sia un ufficio minorenne o meno
	 * 
	 * [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP
	 * 
	 * @param tipoUfficioMittente
	 * @return
	 * @throws F3BException
	 */
	protected String checkMinori(String tipoUfficioMittente) throws F3BException {
		String ret = "";

		if (!ufficiMinori.contains(tipoUfficioMittente)) {
			ret = tipoUfficioMittente;
		}
		
		if (StringUtils.checkValidValue(ret)) {
			siesLogger.info("[JMS]: l'ufficio mittente " + tipoUfficioMittente + " è un ufficio di tipo maggiorenni");
		}
		else{
			siesLogger.info("[JMS]: l'ufficio mittente " + tipoUfficioMittente + " è un ufficio di tipo minorenne");
		}

		return ret;
	}

//	private boolean parseMino(String tipoUfficio) {
//
//		boolean ret = false;
//		if ("PGCAP".equals(tipoUfficio)) {
//			ret = true;
//		}
//		return ret;
//	}

	public ManageRicerca() {
	}

	/**
	 * Elabora i messaggi di richiesta ricerca. Prepara la risposta e la spedisce sulla coda inPartenza
	 * SIAPSender.send().
	 * 
	 * @param aMessage
	 * @throws F3BException
	 */
	public void elaboraMessaggioRicerca(ObjectMessage aMessage) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio METODO elaboraMessaggioRicerca");

		TreeModel lTree = (TreeModel) aMessage.getObject();
		ParserMessage lParser = new ParserMessage(lTree);

		if (!(aMessage.getStringProperty(TIPO_MESSAGGIO).equals(RICHIESTA_RICERCA))) {
			throw new F3BException(
					"TIPO MESSAGGIO diverso da RICHIESTA_RICERCA! Impossibile elaborare il Messaggio");
		}

		MessaggioModel lMessage = null;

		if (aMessage.getStringProperty(TIPO_OPERAZIONE).equals(RICERCA_FASCICOLO)) {
			// Ricerco Fascicolo in BDI
			// Metodo che ricerca il fascicolo e prepara il Messaggio di risposta
			IRicercaJMS lCtrlMess = SIEPLookupRemote.getRicercaJMS();
			siesLogger.info("[JMS]: invocato il METODO ExRicercaFascicoloSiep dalla classe ManageRicerca");
			//lMessage = lCtrlMess.ExRicercaFascicoloSiep(lParser.getFascicolo());
			// [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: invoco il nuovo metodo passando anche il conrollo su ufficio minorenne o meno
			MessaggioModel lMessModel = new MessaggioModel(aMessage);
			String codUffMitt = lMessModel.getCodUfficioMittente(); // getCodUfficioUtenteConnesso()
			UfficioModel uffMod = UfficioUtils.getUfficioByCodUfficio(codUffMitt);
			siesLogger.info("[JMS]: >>>> codice tipo ufficio per cui sto invocando la ricerca ExRicercaFascicoloSiep >>>" + uffMod.getCodTipoUfficio() );
			lMessage = lCtrlMess.ExRicercaFascicoloSiep(lParser.getFascicolo(), checkMinori(uffMod.getCodTipoUfficio()));
			lMessage.setCodTipoOperazione(ESITO_RICERCA_FASCICOLO);
		} 
		else {
			if (aMessage.getStringProperty(TIPO_OPERAZIONE).equals(RICERCA_FASCICOLO_PER_TRASFERIMENTO)) {
				IRicercaJMS lCtrlMess = SIEPLookupRemote.getRicercaJMS();
				siesLogger.info("[JMS]: invocato il METODO ExRicercaFascicoloSiepPerTrasferimento dalla classe ManageRicerca");
				//lMessage = lCtrlMess.ExRicercaFascicoloSiepPerTrasferimento(lParser.getFascicolo());
				// [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: invoco il nuovo metodo passando anche il conrollo su ufficio minorenne o meno
				MessaggioModel lMessModel = new MessaggioModel(aMessage);
				String codUffMitt = lMessModel.getCodUfficioMittente(); // getCodUfficioUtenteConnesso()
				UfficioModel uffMod = UfficioUtils.getUfficioByCodUfficio(codUffMitt);
				siesLogger.info("[JMS]: >>>> codice tipo ufficio per cui sto invocando la ricerca ExRicercaFascicoloSiepPerTrasferimento >>>" + uffMod.getCodTipoUfficio() );
				// [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: mi costruisco la variabile booleana invocando il nuovo metodo					
				lMessage = lCtrlMess.ExRicercaFascicoloSiepPerTrasferimento(lParser.getFascicolo(), checkMinori(uffMod.getCodTipoUfficio()));
				
				lMessage.setCodTipoOperazione(ESITO_FASCICOLO_PER_TRASFERIMENTO);
			} else {
				if (aMessage.getStringProperty(TIPO_OPERAZIONE).equals(RICERCA_SOGGETTO)) {
					siesLogger.info("[JMS]: invocato il METODO ExRicercaSoggetto dalla classe ManageRicerca");
					IRicercaSICOJMS lCtrlSoggetto = SICOLookupRemote.getRicercaSICOJMSRemote();
					lParser.getSoggetto().setCodUfficioInserimento(null);
					MessaggioModel lMessModel = new MessaggioModel(aMessage);
					String codUffMitt = lMessModel.getCodUfficioMittente(); // getCodUfficioUtenteConnesso()
					UfficioModel uffMod = UfficioUtils.getUfficioByCodUfficio(codUffMitt);
					siesLogger.info("[JMS]: >>>> codice tipo ufficio per cui sto invocando la ricerca del soggetto >>>" + uffMod.getCodTipoUfficio() );
					// [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: mi costruisco la variabile booleana invocando il nuovo metodo
					boolean checkMajor = StringUtils.checkValidValue(checkMinori(uffMod.getCodTipoUfficio()));		
					siesLogger.info("[JMS]: >>>> l'ufficio è un ufficio maggiorenne??? >>>" + checkMajor );
					lMessage = lCtrlSoggetto.ExRicercaSoggetto(lParser.getSoggetto(),
							aMessage.getStringProperty(COD_BDI_MITTENTE), checkMajor ); // [EC] - 16/01/2018: - ANOMALIA VISIBILITA MINORE SIEP: invoco il nuovo metodo
					lMessage.setCodTipoOperazione(ESITO_RICERCA_SOGGETTO);
				} else
					throw new F3BException("TIPO OPERAZIONE di tipo ricerca non implementato!");
			}
		}

		// =======================================================
		// Viene rispedito il messaggio Esito al Mittente
		// =======================================================
		if (lMessage != null) {
			lMessage.setDescrBdiDestinataria(aMessage.getStringProperty(BDI_MITTENTE));
			lMessage.setCodBdiDestinataria(aMessage.getStringProperty(COD_BDI_MITTENTE));
			lMessage.setCodBdiMittente(aMessage.getStringProperty(COD_BDI_DESTINATARIA));
			lMessage.setDescrBdiMittente(aMessage.getStringProperty(BDI_DESTINATARIA));
			// lMessage.setCodTipoOperazione(aMessage.getStringProperty(TIPO_OPERAZIONE));
			lMessage.setCodTipoMessaggio(ESITO_RICERCA);

			if (aMessage.getStringProperty(UFFICIO_MITTENTE) != null)
				lMessage.setCodUfficioDestinatario(aMessage.getStringProperty(UFFICIO_MITTENTE));
			else
				lMessage.setCodUfficioDestinatario("-");

			if (aMessage.getStringProperty(UFFICIO_DESTINATARIO) == null)
				lMessage.setCodUfficioMittente("-");
			else
				lMessage.setCodUfficioMittente(aMessage.getStringProperty(UFFICIO_DESTINATARIO));

			lMessage.setCodiceUtenteMittente("OPENJMS");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("\nCorrelation ID = " + aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO)
					+ "\n");

			// 20101028
			if (aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO) != null) // aggiunta il 20101028 per
																				// prove
				lMessage.setJmsCorrelationIdMessage(aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO));

			lMessage.setDataInvio(DateUtils.getSysDate());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("[JMS]: aMessage : " + aMessage.toString());

			if (aMessage.getStringProperty(ID_MESSAGGIO) != null)
				lMessage.setIdMessaggio(new BigDecimal(aMessage.getStringProperty(ID_MESSAGGIO)));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("SPEDIZIONE MESSAGGIO ESITO RICERCA AL MITTENTE  : " + lMessage.toString() + "....");
		SIAPSender lSender = new SIAPSender();
		lSender.send(lMessage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("...MESSAGGIO ESITO RICERCA RISPEDITO AL MITTENTE");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");
	}

	public boolean esitoRicercaSoggettoNotSend(ObjectMessage aMessage) throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: inizio");

		IMessaggio lCtrMess = JMSLookupRemote.getMessaggioRemote();
		lCtrMess.ExRicercaCancellaMessaggioEsitoByCorrelationId(
				aMessage.getStringProperty(CORRELATION_ID_MESSAGGIO),
				aMessage.getStringProperty(COD_BDI_MITTENTE));

		// ------------------------------

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("[JMS]: fine");

		return false;
	}
}