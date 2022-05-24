package siap.sius.avvocato.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.AvvocatoUtil;
import siap.sico.web.ActionSiap;
// MEV_21 mi servono le costanti SIUS 
//import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.sius.avvocato.action.ICostantiAvvocato;
//
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoFascicoloSiusModel;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActInserisciAvvocato
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Avvocato
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciAvvocato extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException 
	{
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();

		//===========================================================
		// Inserisco l'avvocato: 
		// se selezioneto Reginde si va in Insert o Update
		//    - se cambia foro va fatta insert e Update FLAG_REGINDE = NO per il precedente
		// se selezionato SIEP non si fa nulla
		// se iscritto manualmente si va in INSERT (non certificato)
	  //===========================================================
		BigDecimal idAvvocato = this.inserisciAggiornaAvvocato();
		
		// Prima di agganciare l'avvocato al fascicol controlllo che:
		// - non sia già presente sul fascicolo
		// - se è il secondo avvocato deve essere di FIDUCIA
		BigDecimal idFascicoloSius = (((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());		

		siesLogger.debug("Verifico se Avvocato Già Presente sul fascicolo");
		Vector <AvvocatoSiusModel> lVectPrec = null;

		// recupero gli avvocati associati al fascicolo
		AvvocatoFascicoloSiusModel lAvvFascModPrec = new AvvocatoFascicoloSiusModel();
		lAvvFascModPrec.setFasSiuIdFascicoloSius (idFascicoloSius);			
		lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo (null, lAvvFascModPrec);
		
		if (lVectPrec.size() > 0) {
			Iterator iter = lVectPrec.iterator();
			while (iter.hasNext()) {
				AvvocatoModel lAvvModPrec = ((AvvocatoSiusModel) iter.next()).getAvvocato();

				String lDescrTipo = lAvvModPrec.getDescrTipo();
				
				if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
						&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) 
				{
					throw new F3BException(F3BException.USER_MESSAGE,
							"I difensori possono essere due solo se entrambi sono di fiducia!");
				}		
				
			  if (lAvvModPrec.getIdAvvocato().compareTo(idAvvocato) == 0)
					throw new F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta già inserito!");
			}
		}		
			
		
		// Creao il collegamento con il fascicolo SIUS
		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvFascMod.setAvvIdAvvocato (idAvvocato);
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());	
		
		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_NOMINA));
		}
		
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
		}
		
		lAvvFascMod.setDataFineValidita(null);		
		
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod.setCodMotivoDesignazione(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}
		
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod.setCodTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (   !this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA)
				&& (    !this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA) 
						 && !getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA).equals("-")
					 )
				) 
		{
			//@FIXME da vrificare
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {
			lAvvFascMod.setSedeAutorita("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod.setIndirizzoTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod.setCodTipoAutoritaDif(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (    !this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (   !this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF) 
						&& !getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-")
					 )
				) 
		{
		  //@FIXME da vrificare
			String lComneAutoritaDif = getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));
			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}		

		lAvvFascMod.setCodOperatoreInserimento (getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento         (DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento   (this.getCodUfficioUtenteConnesso());		
	
		//=======================
		// Chiamo la funzione di inserimento
	  //=======================
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato (idAvvocato);
		siesLogger.debug("Chiamo la funzione di inserimento AvvocatoFascicoloSius... ");
		lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod);
		
		
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ idAvvocato;
	}
	
	
	
	/**
	 * Vecchio metodo utilizzato prima della MEV_21
	 * @return
	 * @throws F3BException
	 * @deprecated
	 */
	@SuppressWarnings("rawtypes")
	public String processRequestOld() throws F3BException {

		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVectRic = new Vector();
		Vector lVectPrec = null;
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSiusModel lAvvFascModPrec = new AvvocatoFascicoloSiusModel();
		lAvvFascModPrec.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		// AvvocatoController lCtrl = new AvvocatoController();
		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaDifensoreAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = ((AvvocatoSiusModel) lVectPrec.get(0)).getAvvocato();
			String lDescrTipo = lAvvModPrec.getDescrTipo();

			if (lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02")) {

				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");

			}

			if (lAvvModPrec.getIdAvvocato().compareTo(this.getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO)) == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");

		}

		AvvocatoFascicoloSiusModel lAvvFascMod = new AvvocatoFascicoloSiusModel();
		lAvvMod.setIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));

		lVectRic = lCtrl.ExRicercaAvvocato(lAvvMod);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO0 appar = " + lAvvModRic.getCodUffAppartenenza());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO0 appar = " + lAvvModRic.getCognome());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO appar = " + lAvvMod.getIdAvvocato());

		/*
		 * 
		 * if(lAvvModRic.getDataSospensione()!= null && !lAvvModRic.getDataSospensione().equals("-")) throw
		 * new F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta sospeso!");
		 * 
		 * if(lAvvModRic.getDataRadiazione()!= null && !lAvvModRic.getDataRadiazione().equals("-")) throw new
		 * F3BException(F3BException.USER_MESSAGE,"Attenzione: il difensore risulta radiato!");
		 * 
		 * if(lAvvModRic.getCodNonAttivita()!= null && !lAvvModRic.getCodNonAttivita().equals("-")) throw new
		 * F3BException
		 * (F3BException.USER_MESSAGE,"Attenzione: il difensore risulta non in attività per  "+lAvvModRic
		 * .getDescrNonAttivita()+"");
		 */

		// da qui sul secondo model
		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_DESIGNAZIONE,
					ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_DESIGNAZIONE));

		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocatoFascicoloSius.CAMPO_ANNO_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSius.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocatoFascicoloSius.CAMPO_GIORNO_DATA_NOMINA));

		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());

		}
		lAvvFascMod.setDataFineValidita(null);
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod
					.setCodMotivoDesignazione(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod
					.setCodTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA)
				&& (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA).equals("-"))) {
//			String lComuneAutorita = getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA);
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {

			lAvvFascMod.setSedeAutorita("-");

		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod
					.setIndirizzoTipoAutorita(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod
					.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod
					.setCodTipoAutoritaDif(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF)
				&& (!this
						.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF) && !getRequestStringParameter(
						ICostantiAvvocatoFascicoloSius.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {

			lAvvFascMod.setSedeAutoritaDif("-");

		}
		// CAMPO_COD_TIPO_AUTORITA_DIF
		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAvvFascMod.setFasSiuIdFascicoloSius(((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getIdFascicoloSius());
		lAvvFascMod.setAvvIdAvvocato(getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO));
		if (!this.isRequestParameterNullObj(ICostantiAvvocatoFascicoloSius.CAMPO_NOTE)) {

			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocatoFascicoloSius.CAMPO_NOTE));
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("model avvocatoo_sius" + lAvvFascMod);

		// AvvocatoController lCtrl = new AvvocatoController();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO appar = " + lAvvModRic.getCodUffAppartenenza());

		if (!lAvvModRic.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {

			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());

			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());

			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));

			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());

			lAvvMod = lCtrl.ExInserisciAvvocato(lAvvModRic);
		} else {
			StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();

			lStoricoModel.setCognome(lAvvModRic.getCognome());
			lStoricoModel.setNome(lAvvModRic.getNome());
			lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());
			lStoricoModel.setCodLuogoNascita(lAvvModRic.getCodLuogoNascita());
			lStoricoModel.setDataNascita(lAvvModRic.getDataNascita());
			lStoricoModel.setForo(lAvvModRic.getForo());
			lStoricoModel.setIndirizzo(lAvvModRic.getIndirizzo());
			lStoricoModel.setCodComuneResidenza(lAvvModRic.getCodComuneResidenza());
			lStoricoModel.setTelefono(lAvvModRic.getTelefono());
			lStoricoModel.setFax(lAvvModRic.getFax());
			lStoricoModel.setEMail(lAvvModRic.getEMail());
			lStoricoModel.setCodiceFiscale(lAvvModRic.getCodiceFiscale());
			lStoricoModel.setProvincia(lAvvModRic.getProvincia());
			lStoricoModel.setCap(lAvvModRic.getCap());
			lStoricoModel.setDataSospesoFinoAl(lAvvModRic.getDataSospensione());
			lStoricoModel.setDataRadiatoDal(lAvvModRic.getDataRadiazione());
			lStoricoModel.setCodNonAttivita(lAvvModRic.getCodNonAttivita());
			lStoricoModel.setCodUfficioAppartenenza(lAvvModRic.getCodUffAppartenenza());
			lStoricoModel.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());

			lStoricoModel.setCodUfficioInserimento(lAvvModRic.getCodUfficioAggiornamento());
			lStoricoModel.setCodOperatoreInserimento(lAvvModRic.getCodOperatoreAggiornamento());
			lStoricoModel.setDataInserimento(lAvvModRic.getDataAggiornamento());

			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());

			lAvvModRic.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());

			ComuneModel lComModRes = new ComuneModel(
					this.getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

//		AvvocatoSiusModel avvSiusMod = new AvvocatoSiusModel();
		BigDecimal idAvv = null;
		/*avvSiusMod = */lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod); // setta la risposta nella request
																		// setRequestAttribute("avvocato",lVect);
		idAvv = lAvvFascMod.getAvvIdAvvocato();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO CONTR1 = " + lAvvFascMod.getAvvIdAvvocato());

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO idAvv0 = " + idAvv);

//		String lPage = "";
		return /*lPage = */IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.avvocato.action.ActLoadDettaglioAvvocatoFascicolo&" + CAMPO_ID_AVVOCATO + "="
				+ idAvv;
	}
	
	/**
	 * Metodo che effettua inserimento o aggiornamento dell'avvocato e restituisce l'id per 
	 * collegarlo al fascicolo.
	 * Se avvocato selezionato da Reginde il sistema lo ceraca e se trovato lo aggiorna 
	 * altrimenti lo inserisce.
	 * Se avvocato selezionato da SIES (non trovato su reginde) non fa nullea (già presente) e
	 * restituisce solo l'id
	 * Se inserimento manuale lo inserisce non certificato
	 */
	private BigDecimal inserisciAggiornaAvvocato() throws F3BException 
	{		
		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		
		BigDecimal idAvvocato = null;
		
		// Sono possibili 3 casi:
		// 1) Avvocato selezionato da REGINDE
		// 2) Avvocato selezionato da SIES ma certificato REGINDE REGINDE non disponibile)
		// 3) Avvocato inserito manualmente		
		AvvocatoModel amReginde = new AvvocatoModel();
		

		if (   getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA") //Reginde
				|| getRequestStringParameter(CAMPO_ID_AVVOCATO).equals("") //Iscrizione manuale
			 )
		{
      // Si recuperano tutte le informazioni dalla Form solo se avvocato selezionato reginde o inserito 
    	// manualmente. Se selezionao SIEP i dati NON servono
			siesLogger.debug("Iscrizione manuale o Reginde: CAMPO_ID_AVVOCATO = "+getRequestStringParameter(CAMPO_ID_AVVOCATO));
      Date dataNascita = null;
      String codLuogoNascita = "-";
      String codProvincia = "-";
      String codCap = null;
      String codNonAttivita = "-";
      ComuneModel comuneNascita = null;
      String descCodLuogoNascita = "039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA))
          ? getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)
          : getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE);
          
      // Decodifico il luogo di nascita
      siesLogger.debug("Decodifico il luogo di nascita: "+descCodLuogoNascita);
      //**********************************************************************
  		// 20210722 MEV_21 Controllo e valorizzazione comuneNascita.
  		// Se presente, dal codice comune (e dalla descrizione).
  		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
  				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
  			comuneNascita = new ComuneModel(
  					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
  							getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)));
  			codLuogoNascita = comuneNascita.getCodComune();
  			codProvincia = comuneNascita.getCodProvincia();
  			codCap = comuneNascita.getCap();
  			descCodLuogoNascita = comuneNascita.getDescrizione();

  			// altrimenti dalla sola descrizione (rischio omonimi).
  		} else if (getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA).length() > 2) {
  			// new 2022.05.16 se reginde il comune di nascita viene decodificato dal CF
  			if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA")) {
  				String codiFiscAvv = getRequestStringParameter(CAMPO_CODICE_FISCALE);
  				comuneNascita = AvvocatoUtil.calcolaComuneNascita(codiFiscAvv);
  				codLuogoNascita     = comuneNascita.getCodComune();
  				descCodLuogoNascita = comuneNascita.getDescrizione();
  				codCap              = comuneNascita.getCap();
  				codProvincia        = comuneNascita.getCodProvincia();
  			// new 2022.05.16
  			}
  			else {
  			comuneNascita = new ComuneModel(
  					getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)));
  			descCodLuogoNascita = comuneNascita.getDescrizione();
  			codLuogoNascita = comuneNascita.getCodComune();
  			}
  			// altrimenti , in caso di Paese di Nascita Estero, dalla routine che ricava i dati dal C.F.
  		} else if (getRequestStringParameter(CAMPO_COD_STATO_NASCITA).length() == 3
  				&& !("039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA)))
  				&& getRequestStringParameter(CAMPO_CODICE_FISCALE).length() == 16) {
  			comuneNascita = AvvocatoUtil
  					.calcolaComuneNascita(getRequestStringParameter(CAMPO_CODICE_FISCALE));
  			// comuneNascita, in caso di stato estero, conterrà informazioni dello stato.
  		}      
      //**********************************************************************
/*      
      if (Utils.isPresent(descCodLuogoNascita)) 
      {
        try {
          comuneNascita = new ComuneModel(getCodComuneByDescr(descCodLuogoNascita));
        } catch (Exception e) {
          siesLogger.warn("Comune di nascita ["+descCodLuogoNascita+"] non trovato "+e.getMessage());
          
					if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA")) {    
						// Se reginde provo a decodificare dal CF
					          siesLogger.debug("AVVOCATO REGINDE: Provo a decodificare il comune dal Codice Fiscale");
					          // algortimo di omocodia:
					          comuneNascita = AvvocatoUtil
					              .calcolaComuneNascita(getRequestStringParameter(CAMPO_CODICE_FISCALE));
					          siesLogger.debug("comuneNascita da CF = "+comuneNascita);
					} else {
						// inserimento manuale: comune inesistente
						throw e;
					}         
          
        }
        
        if (!Utils.isNullObj(comuneNascita)) {
          if ("039".equals(getRequestStringParameter(CAMPO_COD_STATO_NASCITA)) ) {
            codLuogoNascita = comuneNascita.getCodComune();
            codProvincia = comuneNascita.getCodProvincia();
            codCap = comuneNascita.getCap(); //?????? ce ci fai con il cap del comune di nascita???
            descCodLuogoNascita = comuneNascita.getDescrizione();
          }
        }
      }
*/
      // Recupero Codice e descrizione comune di residenza/studio
      String codLuogoResidenza = "-";
      ComuneModel comuneResidenza = null;
      String descLuogoResidenza = !isRequestParameterNullObj(CAMPO_DESC_COMUNE_STUDIO)
          ? getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO)
          : null;
      if (Utils.isPresent(descLuogoResidenza)) {
        try {
          comuneResidenza = new ComuneModel(getCodComuneByDescr(descLuogoResidenza));
        } catch (Exception e) {
          siesLogger.warn("Comune di residenza ["+descLuogoResidenza+"] non trovato: "+e.getMessage());
        }
        
        if (!Utils.isNullObj(comuneResidenza)) {
          codLuogoResidenza = comuneResidenza.getCodComune();
          descLuogoResidenza = comuneResidenza.getDescrizione();
        }
      }
      
      // Recupero dataNascita, 
      if (    !isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
          && (!isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
          && (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA)))
         )
          dataNascita = getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
                              ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA,
                              ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA);
      
      // Recupero stato Attività Avvocato.
      if (!isRequestParameterNullObj(CAMPO_COD_NON_ATTIVITA) )
          codNonAttivita = getRequestStringParameter(CAMPO_COD_NON_ATTIVITA);
      
      // Imposto i dati dell'avvocato prelevandoli dalla maschera
      amReginde.setIdAvvocato  (null);
      
      
      // Anagrafica
      amReginde.setCognome       (getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
      amReginde.setNome          (getRequestStringParameter(CAMPO_NOME).toUpperCase());
      amReginde.setCodiceFiscale (getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());
      amReginde.setDataNascita   (dataNascita);

      // Nascita
      amReginde.setCodLuogoNascita     (codLuogoNascita);
      amReginde.setDescLuogoNascita    (descCodLuogoNascita);
      amReginde.setProvincia           (codProvincia); // di nascita
      amReginde.setDescLuogoNascitaReginde (descCodLuogoNascita);
      amReginde.setCodStatoNascita (getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
      amReginde.setDescrStatoNascita (null);

      //
      amReginde.setForo               (getRequestStringParameter(CAMPO_FORO).toUpperCase());
      amReginde.setDescComuneSedeForo (null);

      // 
      amReginde.setDescrComuneStudio  (descLuogoResidenza);
      amReginde.setCodComuneResidenza (codLuogoResidenza);        
      amReginde.setIndirizzo (getRequestStringParameter(CAMPO_INDIRIZZO));
      amReginde.setCap (codCap); // de cosa???? Studio
      
      // contatti
      amReginde.setTelefono  (getRequestStringParameter(CAMPO_TELEFONO));
      amReginde.setFax       (getRequestStringParameter(CAMPO_FAX));
      amReginde.setEMail     (getRequestStringParameter(CAMPO_E_MAIL));
      amReginde.setPec       (getRequestStringParameter(CAMPO_PEC));
      
      //
      amReginde.setCodNonAttivita   (codNonAttivita); // a che serve??
      amReginde.setDescrNonAttivita (null);
      
      amReginde.setFlagVisualizza (new BigDecimal(1));        
      amReginde.setIdAvvocatoBonificato (null);        
      amReginde.setDescrTipo (null);        
      amReginde.setDescComuneResidenza (descLuogoResidenza);   
      amReginde.setNote (null);        
      amReginde.setFlagCancellato("N");        
      amReginde.setCodUffAppartenenza ("00000");
      amReginde.setDataSospensione    (null);
      amReginde.setDataRadiazione     (null);
      amReginde.setIdAvvocatoStandard (null);
      
      amReginde.setCodOperatoreInserimento (getCodUtenteConnesso());
      amReginde.setCodUfficioInserimento   (getCodUfficioUtenteConnesso());
      amReginde.setDataInserimento         (DateUtils.getSysDate());
      
      amReginde.setCodOperatoreAggiornamento (null);
      amReginde.setCodUfficioAggiornamento   (null);
      amReginde.setDataAggiornamento         (null);			
		}
		
		
		
    if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA")) {
    	  siesLogger.debug("checkRegingde. Avvocato COA ");
    	  amReginde.setFlagRegInde ("SI");
    	  
        // Provengo da Reginde: quindi si cerca l'avvocato certificato su tabella AVVOCATO;
        // se esiste lo aggiorno, altrimenti inserisco nuovo avvocato da Reginde su SIES!
        
        // Si esegue la ricerca puntuale dell'Avvocato certificato RegInde in SIES. 
        AvvocatoModel lAvvCertRegSies = new AvvocatoModel();
        lAvvCertRegSies.setNome          (getRequestStringParameter(CAMPO_NOME));
        lAvvCertRegSies.setCognome       (getRequestStringParameter(CAMPO_COGNOME));
        lAvvCertRegSies.setCodiceFiscale (getRequestStringParameter(CAMPO_CODICE_FISCALE));
        lAvvCertRegSies.setFlagRegInde   ("SI");
        
        //    
        siesLogger.debug("checkRegingde. Ricerco l'avvocato RegInde su SIES.AVVOCATO");
        lAvvCertRegSies = lCtrl.ExRicercaAvvocatoCertRegInde(lAvvCertRegSies);
        siesLogger.debug("checkRegingde. lAvvCertRegSies trovato = "+lAvvCertRegSies);

        
        /* Se l'avvocato certificato RegInde non è presente in SIES si inserisce 
           Se è già presente in SIES si effettua l'aggiornamento con i dati da Reginde. 
           FORO COMPRESO
        */
        if (lAvvCertRegSies == null) {
        	siesLogger.debug("Avvocato Certificato Reginde assente, lo inserisco a sistema");
          amReginde = lCtrl.ExInserisciAvvocato (amReginde); 
        } else {
        	siesLogger.debug("Avvocato Certificato Reginde già presente in SIES (con id:"+lAvvCertRegSies.getIdAvvocato()+") e certificato sul foro "+lAvvCertRegSies.getForo()+", lo aggiorno");
          
        	// Testo il foro, se non è cambiato vado in update altrimenti
        	if (lAvvCertRegSies.getForo().equals(amReginde.getForo())) {
        		siesLogger.debug("L'avvocato NON ha cambiato foro, aggiorno solo alcuni dati");
          	amReginde.setIdAvvocato                (lAvvCertRegSies.getIdAvvocato());
            
            amReginde.setCodOperatoreAggiornamento (getCodUtenteConnesso());
            amReginde.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
            amReginde.setDataAggiornamento         (DateUtils.getSysDate());
            
            // n.b. aggiorno comunque l'avvocato devo testare comuqne che non sia già legato al fascicolo
            amReginde = lCtrl.ExAggiornaAvvocatoDaReginde(amReginde);        		
        	} else {
        		siesLogger.debug("L'avvocato HA cambiato foro");
        		siesLogger.debug("Invalido il vecchio avvocato: id = "+lAvvCertRegSies.getIdAvvocato());
        		lAvvCertRegSies.setCodOperatoreAggiornamento (getCodUtenteConnesso());
        		lAvvCertRegSies.setCodUfficioAggiornamento   (getCodUfficioUtenteConnesso());
        		lAvvCertRegSies.setDataAggiornamento         (DateUtils.getSysDate());  
        		
        		lCtrl.ExInvalidaAvvocatoReginde (lAvvCertRegSies); 
        		
        		siesLogger.debug("Inserisco il nuovo avvocato");
        		amReginde = lCtrl.ExInserisciAvvocato (amReginde); 
        	}        
        }
        idAvvocato = amReginde.getIdAvvocato();        
      } else  {
        // Avvocato non presente in RegInde, si esegue procedura preesistente
        idAvvocato = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
        
        if (idAvvocato!=null) {
        	siesLogger.debug("Avvocato FORM non selezionato da reginde ma da SIEP con ID "+idAvvocato+", aggancio l'avvocato al fascicolo");
        	// In questo caso non devo inserire nulla ne aggiornare nulla
        }
        else {
        	siesLogger.debug("INSERIMENTO MANUALE");
        	amReginde.setFlagRegInde ("NO");
        	amReginde = lCtrl.ExInserisciAvvocato (amReginde); 
        	idAvvocato = amReginde.getIdAvvocato();
        }
      }		
    
    return idAvvocato;
	}

}