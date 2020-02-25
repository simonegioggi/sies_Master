package siap.sige.udienzaprocedimento.dettaglioruolo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.model.XModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActStampaFlashVerbaleUdienza
 * </p>
 * <p>
 * Description: Classe Azione responsabile della richiesta stampa
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
public class ActStampaFlashVerbaleUdienza extends ActionSiap implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("inizio");

		// Preleva dalla sessione i dati dell'utente connesso.
		// String lCodiceOperatore = getCodUtenteConnesso();
		// String lCodiceUfficio = getCodUfficioUtenteConnesso();
		// String lCodComune = getCodComuneUtenteConnesso();
		String lOrderBy = null;
		BigDecimal lIdEsperto = null;

		String lIdDocumento = ID_TEMPLATE_VERBALE_UDIENZA;

		// Lettura IDUdienza
		BigDecimal lIdUdienza = null;
		if (!isRequestParameterNullObj(CAMPO_UDI_ID_UDIENZA_SIGE))
			lIdUdienza = getRequestBigDecimalParameter(CAMPO_UDI_ID_UDIENZA_SIGE);

		String lCodMagistrato = null;
		if (!isRequestParameterNullObj(ICostantiMagistrato.CAMPO_COD_MAGISTRATO))
			lCodMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		String lStatoProcedimento = null;
		// getRequestStringParameter("lStatoProcedimento") ;

		String tipoProc = "TUTTI";

		// Preleva dal FascicoloGPModel
		// Necessario Poichè lo si preleva anche nel controller di stampa
		BigDecimal lIdFasSige = null;
		// if( !isRequestAttributeNullObj(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE))
		lIdFasSige = getRequestBigDecimalParameter(ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE);

		// IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
		// FascicoloGPModel lFasGPSius = lFasCtrl.ExRicercaFascicoloByKey(lIdFasSius );

		// Preleva la data udienza.
		// Date lDataUdienza = null;
		// if( !isRequestAttributeNullObj("dataUdienza"))
		// lDataUdienza = getRequestDateParameter("dataUdienza","dd-MM-yyyy") ;

		// Prepara il model EventoNotifica.
		// Imposta i dati necessari per la gestione dell'evento.

		/*
		 * EventoModel lEve = new EventoModel(); lEve.setCodTipoEvento("07"); lEve.setDataEmissione(
		 * lDataUdienza ); lEve.setCodMotivo("0700"); lEve.setCodUfficioEmittente(lCodiceUfficio);
		 * lEve.setCodLuogoEmittente(lCodComune); lEve.setCodOperatoreInserimento(lCodiceOperatore);
		 * lEve.setCodUfficioInserimento(lCodiceUfficio); lEve.setDataInserimento(DateUtils.getSysDate());
		 * lEve.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy"))); lEve.setCodEsito("-");
		 * lEve.setCodTipoProvvedimento("-"); lEve.setCodLuogoDestinatario("-");
		 * lEve.setCodTipoUfficioDestinatario("-"); lEve.setFasSiuIdFascicoloSius( lIdFasSige );
		 */
		// lEve.setFasSiuIdFascicoloSius( lFasGPSius.getFascicoloSiusModel().getIdFascicoloSius() );

		/*
		 * String lIdTemplate = new String(); // Individuazione template if
		 * (isRequestParameterNullObj(ICostantiTemplate.CAMPO_ID_TEMPLATE)) lIdTemplate =
		 * ricercaIdTemplate(lEve); else lIdTemplate =
		 * getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);
		 *
		 * lEve.setTemIdTemplate(lIdTemplate);
		 */

		// Chiamata al Controller
		/*
		 * 20080208 Eliminato.
		 *
		 * IEvento lCtrl2 = SICOLookupRemote.getEventoRemote(); lEve = lCtrl2.ExInserisciEvento(lEve );
		 */

		// Preleva dalla sessione i dati dell'utente connesso.
		// UfficioModel lUfficio = getUfficioUtenteConnesso();

		// Imposta il modello di stampa per tipo Ufficio
		/*
		 * if( lUfficio.getCodTipoUfficio().equalsIgnoreCase("TDS")) lEve.setTemIdTemplate(
		 * ID_TEMPLATE_VERBALE_UDIENZA ); else if( lUfficio.getCodTipoUfficio().equalsIgnoreCase("UDS"))
		 * lEve.setTemIdTemplate( ID_TEMPLATE_VERBALE_UDIENZA_UDS ); else throw new
		 * SIGEException(SIGEException.USER_MESSAGE,
		 * "Modello di stampa non disponibile per il tipo ufficio.");
		 */

		ByteArrayOutputStream lReport = null;

		// Generazione documento di stampa

		/*
		 * IStampaSige lCtrlSta = SIGELookupRemote.getStampaRemote();
		 *
		 * lByteArrayOut = lCtrlSta.ExPreStampaVerbaleUdienza(lEve, lUfficio.getCodUfficio(),
		 * super.getUtenteConnesso());
		 */

		/* Informazioni ufficio */
		String lTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lDescTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio().toUpperCase();

		XModel lXModel = new XModel();

		lXModel.setTipoUfficio(lTipoUfficio);
		lXModel.setTipoUfficioT1(lDescTipoUfficio);
		lXModel.setUfficio(getUfficioUtenteConnesso().getDescrComune().toUpperCase());

		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();

		lReport = lCtrlUdPr.ExStampaProcedimentixUdienza(lIdUdienza, lIdFasSige, lCodMagistrato, lIdEsperto,
				lXModel, lIdDocumento, lOrderBy, getUtenteConnesso(), lStatoProcedimento, tipoProc,
				getCodUfficioUtenteConnesso());

		// Prepara la pagina di destinazione
		if (lReport != null)
			setRequestAttribute("report", lReport);
		else
			throw new SIGEException(SIGEException.USER_MESSAGE, "Nessun documento è stato generato!");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("fine");

		return IWebConstants.PG_DOWNLOAD;
	}

	/**
	 * Funzione di ricerca Template in base al Tipo Provvedimento, Tipo Evento, Cod Esito e Flag (U/T). Viene
	 * usata questa funzione quando nella form non esiste l'indicazione del template da usare. La funzione
	 * restituisce il primo dei template se individuati.
	 * <p>
	 *
	 * @param aEvento
	 * @return
	 * @throws Exception
	 */
	/*
	 * private String ricercaIdTemplate(EventoModel aEvento) throws Exception { // [FT] - 03/08/2016 - MAC_LOG
	 * - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	 * siesLogger.debug(this.getClass().getName() + ".ricercaIdTemplate(EventoModel): inizio");
	 *
	 * ITemplate lTempCtrl = SICOLookupRemote.getTemplateRemote();
	 *
	 * // Si recupera info dell'Ufficio Utente connesso serve per ricavare il FLAG TEMPLATE UfficioModel
	 * lUfficio = getUfficioUtenteConnesso();
	 *
	 * // Si valorizza il filtro di ricerca sui Template TemplateModel lTemplate = new TemplateModel();
	 * lTemplate.setCodTipoProvvedimento(aEvento.getCodTipoProvvedimento());
	 * lTemplate.setCodTipoEvento(aEvento.getCodTipoEvento()); lTemplate.setCodEsito(aEvento.getCodEsito());
	 * // Preleva dal codice tipo ufficio la prima lettera, in questo caso può essere // T = TDS o U = UDS.
	 * lTemplate.setFlagTemplate(lUfficio.getCodTipoUfficio().substring(0,1));
	 *
	 * try { // Ricerca dei Template Vector lTemplates = lTempCtrl.ExRicercaTemplate(lTemplate); if
	 * (lTemplates != null && lTemplates.size() > 0) lTemplate = (TemplateModel) lTemplates.get(0); else throw
	 * new F3BException(F3BException.USER_MESSAGE,"Non esiste il template di stampa !"); } catch (Exception e)
	 * { throw new F3BException(F3BException.USER_MESSAGE,"Errore nella ricerca del template : " +
	 * e.toString()); }
	 *
	 * // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
	 * LogF3B.getLogger() siesLogger.debug(this.getClass().getName() +
	 * ".ricercaIdTemplate(EventoModel): fine");
	 *
	 * return lTemplate.getIdTemplate(); }
	 */

}