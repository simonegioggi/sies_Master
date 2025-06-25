package siap.sico.webservice.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.codici_sies_nsc.controller.ICodiciSiesNsc;
import siap.sico.codici_sies_nsc.model.CodiciSiesNscModel;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.provvedimentisiesnsc.controller.IProvvSiesNsc;
import siap.sico.provvedimentisiesnsc.model.ProvvSiesNscModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sico.webservice.controller.IWebServices;
import siap.siep.SIEPException;

@SuppressWarnings("rawtypes")
public class ActWsBase extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected CodiciSiesNscModel Decodifica(String aDominio, String aCodCentralizzato) throws Exception {

		ICodiciSiesNsc lCtrlDecodifica = SICOLookupRemote.getCodiciSiesNscRemote();
		CodiciSiesNscModel lCodiciSiesNscModel = new CodiciSiesNscModel();
		try {
			lCodiciSiesNscModel = lCtrlDecodifica.ExRicercaCodiciSiesNscById(aDominio, aCodCentralizzato);

			if (lCodiciSiesNscModel == null || lCodiciSiesNscModel.getCoSies() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ActWsBase - Record con Dominio: " + aDominio + " e Codice Centralizzato: "
						+ aCodCentralizzato + " non trovato in CODICI_SIES_NSC.");
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoSies("-");
				// throw new Exception("ActWsBase - Record con Dominio: " + aDominio + " e Codice
				// Centralizzato: " + aCodCentralizzato + " non trovato in CODICI_SIES_NSC.");
			}
		} catch (SIEPException e) {
			throw e;
		}
		return lCodiciSiesNscModel;
	}

	protected String CercaCodiceUfficio(String aCodTipoUfficio, String aCodComune) throws Exception {

		IWebServices lCtrlUfficio = SICOLookupRemote.getWebServicesRemote();
		UfficioModel lUfficioModel = new UfficioModel();
		String lCodUfficio = "";
		try {
			lUfficioModel = lCtrlUfficio.ExRicercaCodiceUfficio(aCodTipoUfficio, aCodComune);
			if (lUfficioModel.getCodUfficio().equals("")) // Record non trovato
			{
				throw new Exception("ActWsBase - Ufficio con COD_TIPO_UFFICIO: " + aCodTipoUfficio
						+ " e COD_COMUNE: " + aCodComune + " non trovato.");
			}
			lCodUfficio = lUfficioModel.getCodUfficio();
		} catch (SIEPException e) {
			throw e;
		}
		return lCodUfficio;
	}

	protected CodiciSiesNscModel DecodificaCodiceSies(CodiciSiesNscModel aCodiciSiesNscModel)
			throws Exception {

		ICodiciSiesNsc lCtrlDecodifica = SICOLookupRemote.getCodiciSiesNscRemote();
		// String lCodiceCentralizzato = "";
		Vector lListaCodiciSiesNsc;
		CodiciSiesNscModel lCodiciSiesNscModel;
		try {
			lListaCodiciSiesNsc = lCtrlDecodifica.ExRicercaCodiciSiesNsc(aCodiciSiesNscModel);

			if (lListaCodiciSiesNsc.size() == 0) {
				// throw new Exception("ActWsBase - Record con Dominio: " + aCodiciSiesNscModel.getCoDomain()
				// + " e Codice Sies: " + aCodiciSiesNscModel.getCoSies() + " non trovato.");
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ActWsBase - Record con Dominio: " + aCodiciSiesNscModel.getCoDomain()
						+ " e Codice Sies: " + aCodiciSiesNscModel.getCoSies() + " non trovato.");
				// lCodiceCentralizzato="";
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoCodcentr("");
				lCodiciSiesNscModel.setCoVal1("");
				lCodiciSiesNscModel.setCoVal2("");
			} else {
				// Prendo il primo elemento del Vettore
				lCodiciSiesNscModel = (CodiciSiesNscModel) lListaCodiciSiesNsc.elementAt(0);
				// lCodiceCentralizzato = lCodiciSiesNscModel.getCoCodcentr();
			}
		} catch (SIEPException e) {
			throw e;
		}

		return lCodiciSiesNscModel;
	}

	// ##################### PROVVEDIMENTI ESECUZIONE #####################
	protected ProvvSiesNscModel DecodificaCodCentralizzato(String aDominio, String aCodCentralizzato)
			throws Exception {

		IProvvSiesNsc lCtrlDecodificaCodCentr = SICOLookupRemote.getProvvSiesNscRemote();
		ProvvSiesNscModel lProvvSiesNscModel = new ProvvSiesNscModel();
		try {
			lProvvSiesNscModel = lCtrlDecodificaCodCentr.ExRicercaProvvSiesNscById(aDominio,
					aCodCentralizzato);

			if (lProvvSiesNscModel == null || lProvvSiesNscModel.getProvvSiesMotivo() == null) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ActWsBase - Record con Dominio: " + aDominio + " e Codice Centralizzato: "
						+ aCodCentralizzato + " non trovato in PROVV_SIES_NSC.");
				lProvvSiesNscModel = new ProvvSiesNscModel();
				lProvvSiesNscModel.setProvvSiesMotivo("-");
				lProvvSiesNscModel.setProvvSiesEsito("-");
				// throw new Exception("ActWsBase - Record con Dominio: " + aDominio + " e Codice
				// Centralizzato: " + aCodCentralizzato + " non trovato in CODICI_SIES_NSC.");
			}
		} catch (SIEPException e) {
			throw e;
		}
		return lProvvSiesNscModel;
	}

	protected ProvvSiesNscModel DecodificaMotivoEsitoSies(ProvvSiesNscModel aProvvSiesNscModel)
			throws Exception {

		IProvvSiesNsc lCtrlDecodificaCodCentr = SICOLookupRemote.getProvvSiesNscRemote();
		Vector lListaProvvSiesNsc;
		ProvvSiesNscModel lProvvSiesNscModel;
		try {
			lListaProvvSiesNsc = lCtrlDecodificaCodCentr.ExRicercaProvvSiesNsc(aProvvSiesNscModel);

			if (lListaProvvSiesNsc.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ActWsBase - Record con Dominio: " + aProvvSiesNscModel.getProvvDomain()
						+ " - Motivo SIES: " + aProvvSiesNscModel.getProvvSiesMotivo() + " - Esito SIES:"
						+ aProvvSiesNscModel.getProvvSiesEsito() + " non trovato.");
				lProvvSiesNscModel = new ProvvSiesNscModel();
				lProvvSiesNscModel.setProvvCodcentr("-");
			} else {
				// Prendo il primo elemento del Vettore
				lProvvSiesNscModel = (ProvvSiesNscModel) lListaProvvSiesNsc.elementAt(0);
			}
		} catch (SIEPException e) {
			throw e;
		}

		return lProvvSiesNscModel;
	}

	protected EventoModel CercaEvento(BigDecimal lIdEvento) throws Exception {

		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = new EventoModel();
		try {
			lEventoModel = lCtrlEvento.ExRicercaEventoByKey(lIdEvento);
		} catch (SIEPException e) {
			throw new F3BException(
					"ActWsBase - Errore nella Ricerca dell'Evento. Contattare il servizio di Help Desk");
		}
		return lEventoModel;
	}
	
	
	/**
	 * nuovo metodo MEV_21
	 * Metodo di decodifice che restituiesce un Vettore invece che un model solo nel 
	 * caso dei comuni con più occorrenze su SIES per lo stesso codice NSC, va data
	 * la possibilità al chiamate di scegliere il comune in base alla data di nascita
	 * del soggetto
	 * @param aDominio
	 * @param aCodCentralizzato
	 * @return
	 * @throws Exception
	 */
	protected Vector <CodiciSiesNscModel> DecodificaComune (String aDominio, String aCodCentralizzato) throws Exception {
		Vector <CodiciSiesNscModel> listaCodici = null;
		
		ICodiciSiesNsc lCtrlDecodifica = SICOLookupRemote.getCodiciSiesNscRemote();
		
		CodiciSiesNscModel lCodiciSiesNscModel = new CodiciSiesNscModel();
		lCodiciSiesNscModel.setCoDomain(aDominio);
		lCodiciSiesNscModel.setCoCodcentr(aCodCentralizzato);
		
		try {
			listaCodici = lCtrlDecodifica.ExRicercaCodiciSiesNsc(lCodiciSiesNscModel);

			if (listaCodici == null || listaCodici.size() == 0) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("ActWsBase - Record con Dominio: " + aDominio + " e Codice Centralizzato: "
						+ aCodCentralizzato + " non trovato in CODICI_SIES_NSC.");
				lCodiciSiesNscModel = new CodiciSiesNscModel();
				lCodiciSiesNscModel.setCoSies("-");
				listaCodici.add(lCodiciSiesNscModel);
				// throw new Exception("ActWsBase - Record con Dominio: " + aDominio + " e Codice
				// Centralizzato: " + aCodCentralizzato + " non trovato in CODICI_SIES_NSC.");
			}
		} catch (SIEPException e) {
			throw e;
		}
		return listaCodici;
	}

}