package siap.siep.avvocato.action;

/**
 * <p>Title: ActModificaDifensore</p>
 * <p>Description: Classe Action per la modifica di Avvocato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActModificaDifensore extends ActionSiap implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Modifica del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("difensore", getRequestStringParameter(CAMPO_ID_AVVOCATO),
				getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector avvocatoPrima = new Vector();
		Vector avvocatoModif = null;

		AvvocatoModel lAvvModPrima = new AvvocatoModel();
		lAvvModPrima.setIdAvvocato(lId);
		avvocatoPrima = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModPrima);
		lAvvModPrima = (AvvocatoModel) avvocatoPrima.get(0);
		// riempie il model
		AvvocatoModel lAvvMod = new AvvocatoModel();
		lAvvMod.setIdAvvocato(lId);
		lAvvMod.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		lAvvMod.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		lAvvMod.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
		lAvvMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
		lAvvMod.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
		lAvvMod.setFax(getRequestStringParameter(CAMPO_FAX));
		lAvvMod.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
		lAvvMod.setCodiceFiscale(getRequestStringParameter(CAMPO_CODICE_FISCALE).toUpperCase());

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_LUOGO_NASCITA)));
		lAvvMod.setCodLuogoNascita(lComMod.getCodComune());
		ComuneModel lComModRes = new ComuneModel(this.getCodComuneByDescr(
				getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
		lAvvMod.setCodComuneResidenza(lComModRes.getCodComune());
		lAvvMod.setDataNascita(getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
				ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA, ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA));
		lAvvMod.setDataSospensione(getRequestDateParameter(CAMPO_ANNO_DATA_SOSPENSIONE,
				CAMPO_MESE_DATA_SOSPENSIONE, CAMPO_GIORNO_DATA_SOSPENSIONE));

		lAvvMod.setDataRadiazione(getRequestDateParameter(CAMPO_ANNO_DATA_RADIAZIONE,
				CAMPO_MESE_DATA_RADIAZIONE, CAMPO_GIORNO_DATA_RADIAZIONE));

		lAvvMod.setCodNonAttivita(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_NON_ATTIVITA));

		lAvvMod.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
		lAvvMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		lAvvMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAvvMod.setDataAggiornamento(DateUtils.getSysDate());
		lAvvMod.setNote(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOTE));
		lAvvMod.setFlagCancellato("N");

		StoricoAvvocatoModel lStoricoModel = new StoricoAvvocatoModel();

		lStoricoModel.setCognome(lAvvModPrima.getCognome());
		lStoricoModel.setNome(lAvvModPrima.getNome());
		lStoricoModel.setAvvIdAvvocato(lAvvModPrima.getIdAvvocato());
		lStoricoModel.setCodLuogoNascita(lAvvModPrima.getCodLuogoNascita());
		lStoricoModel.setDataNascita(lAvvModPrima.getDataNascita());
		lStoricoModel.setForo(lAvvModPrima.getForo());
		lStoricoModel.setIndirizzo(lAvvModPrima.getIndirizzo());
		lStoricoModel.setCodComuneResidenza(lAvvModPrima.getCodComuneResidenza());
		lStoricoModel.setTelefono(lAvvModPrima.getTelefono());
		lStoricoModel.setFax(lAvvModPrima.getFax());
		lStoricoModel.setEMail(lAvvModPrima.getEMail());
		lStoricoModel.setCodiceFiscale(lAvvModPrima.getCodiceFiscale());
		lStoricoModel.setProvincia(lAvvModPrima.getProvincia());
		lStoricoModel.setCap(lAvvModPrima.getCap());
		lStoricoModel.setDataSospesoFinoAl(lAvvModPrima.getDataSospensione());
		lStoricoModel.setDataRadiatoDal(lAvvModPrima.getDataRadiazione());
		lStoricoModel.setCodNonAttivita(lAvvModPrima.getCodNonAttivita());
		lStoricoModel.setCodUfficioAppartenenza(lAvvModPrima.getCodUffAppartenenza());
		lStoricoModel.setAvvIdAvvocato(lAvvModPrima.getIdAvvocato());
		lStoricoModel.setCodUfficioInserimento(lAvvModPrima.getCodUfficioAggiornamento());
		lStoricoModel.setCodOperatoreInserimento(lAvvModPrima.getCodOperatoreAggiornamento());
		lStoricoModel.setDataInserimento(lAvvModPrima.getDataAggiornamento());

		// chiama il controller
		// AvvocatoController lCtrl = new AvvocatoController();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO appar = " + lAvvModPrima.getCodUffAppartenenza());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO ID    = " + lAvvModPrima.getIdAvvocatoStandard());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO PROV  = " + lAvvModPrima.getProvincia());
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" PAOLO UTEN  = " + this.getCodUfficioUtenteConnesso());

		if (!lAvvModPrima.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
			lAvvMod.setIdAvvocatoStandard(lAvvModPrima.getIdAvvocatoStandard());
			lAvvMod.setProvincia(lAvvModPrima.getProvincia());
			lAvvMod.setCap(lAvvModPrima.getCap());
			lAvvMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAvvMod.setDataInserimento(DateUtils.getSysDate());
			lAvvMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAvvMod = lCtrl.ExInserisciAvvocato(lAvvMod);
		} else {
			lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvMod, lStoricoModel);
		}
		AvvocatoModel lAvvModificato = new AvvocatoModel();
		avvocatoModif = new Vector();
		avvocatoModif = lCtrl.ExRicercaAvvocato(lAvvMod);
		lAvvModificato = (AvvocatoModel) avvocatoModif.get(0);

		setRequestAttribute("modalita", "M");
		setRequestAttribute("avvocato", lAvvModificato);

		setRequestAttribute("flagModifica", "S");

		return PG_DETTAGLIO_AVVOCATO;
	}

}