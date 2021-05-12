package siap.siep.avvocato.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.AvvocatoUtil;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.siep.util.SIEPLookupRemote;

@SuppressWarnings("rawtypes")
public class ActInserisciAvvocato extends ActProvvedimentoDifensore implements ICostantiAvvocato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// paramentro passato solo nel caso di iscrizione guidata
		if (!isRequestParameterNullObj("lTipoFunzione"))
			setRequestAttribute("lTipoFunzione", getRequestStringParameter("lTipoFunzione"));

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName());
		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();

		// MEV_21: controllo se la ricerca proviene da reginde o da sies
		BigDecimal idAvvocato = null;
		boolean flagReginde = false;
		if (getRequestStringParameter(CAMPO_ID_AVVOCATO).contains("COA")) {
			flagReginde = true;
			// se provengo da reginde allora ricerco l'avvocato su tabella AVVOCATO
			// se esiste lo prelevo, altrimenti inserisco nuovo avvocato da reginde su sies!
			Date dataNascita = null;
			String codLuogoNascita = null;
			ComuneModel comuneNascita = null;
			String descCodLuogoNascita = !isRequestParameterNullObj(CAMPO_COD_LUOGO_NASCITA)
					? getRequestStringParameter(CAMPO_COD_LUOGO_NASCITA)
					: null;
			if (Utils.isPresent(descCodLuogoNascita)) {
				try {
					comuneNascita = new ComuneModel(getCodComuneByDescr(descCodLuogoNascita));
				} catch (Exception e) {
					siesLogger.info(e.getMessage());
					// algortimo di omocodia
					comuneNascita = AvvocatoUtil
							.calcolaComuneNascita(getRequestStringParameter(CAMPO_CODICE_FISCALE));
				}
				if (!Utils.isNullObj(comuneNascita)) {
					codLuogoNascita = comuneNascita.getCodComune();
					descCodLuogoNascita = comuneNascita.getDescrizione();
				}
			}
			if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
					&& (!isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
							&& (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))))
				dataNascita = getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_MESE_DATA_NASCITA,
						ICostantiAvvocato.CAMPO_GIORNO_DATA_NASCITA);
			AvvocatoModel am = new AvvocatoModel(null, getRequestStringParameter(CAMPO_COGNOME),
					getRequestStringParameter(CAMPO_NOME),
					getRequestStringParameter(CAMPO_FORO).toUpperCase(), null, null, null, null,
					getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE),
					getRequestStringParameter(CAMPO_COD_STATO_NASCITA), null, null, null, null, null,
					getRequestStringParameter(CAMPO_CODICE_FISCALE), null, null, null, null, null, null, null,
					null, null, null, descCodLuogoNascita, null, null, codLuogoNascita, null, dataNascita,
					null, null, null, getCodUfficioUtenteConnesso(), null, null, null);
			Vector avvocato = lCtrl.ExRicercaAvvocato(am);
			if (!avvocato.isEmpty())
				idAvvocato = ((AvvocatoModel) avvocato.get(0)).getIdAvvocato();
			else {
				// se non lo trovo lo inserisco ex novo
				AvvocatoModel amIns = lCtrl.ExInserisciAvvocato(am);
				idAvvocato = amIns.getIdAvvocato();
			}
		} else
			idAvvocato = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);

		AvvocatoModel lAvvMod = new AvvocatoModel();
		Vector lVectRic = new Vector();
		Vector lVectPrec = null;
		AvvocatoModel lAvvModRic = new AvvocatoModel();

		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascModPrec = new AvvocatoFascicoloSiepModel();
		lAvvFascModPrec.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) (getSessionAttribute("fascicolo"))).getIdFascicoloSiep());

		try {
			lVectPrec = new Vector();
			lVectPrec = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" nessun elemento trovato");
		}

		if (lVectPrec.size() > 0) {
			lAvvModPrec = (AvvocatoModel) lVectPrec.get(0);
			String lDescrTipo = lAvvModPrec.getDescrTipo();

			if ((lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02"))
					|| (lDescrTipo.equalsIgnoreCase("Della Fase di Giudizio")
							&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("03"))) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi di Fiducia o entrambi della Fase di Giudizio!");
			}

			if (lAvvModPrec.getIdAvvocato()
					.compareTo(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato) == 0)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Attenzione: il difensore risulta già inserito!");
		}

		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();
		lAvvMod.setIdAvvocato(idAvvocato);

		lVectRic = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvMod);
		lAvvModRic = (AvvocatoModel) lVectRic.get(0);

		if (lAvvModRic.getDataSospensione() != null
				&& !"-".equals(lAvvModRic.getDataSospensione().toString()))
			throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: il difensore risulta sospeso!");

		if (lAvvModRic.getDataRadiazione() != null && !"-".equals(lAvvModRic.getDataRadiazione().toString()))
			throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: il difensore risulta radiato!");

		if (lAvvModRic.getCodNonAttivita() != null && !lAvvModRic.getCodNonAttivita().equals("-"))
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione: il difensore risulta non in attività per  "
							+ lAvvModRic.getDescrNonAttivita() + "");

		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) {
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("02")) {
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA, ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA));
		}
		if (lAvvFascMod.getCodTipoAvvocato().equals("03")) {
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate());
		}

		lAvvFascMod.setDataFineValidita(null);
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)
				&& (!isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else
			lAvvFascMod.setSedeAutorita("-");

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF) && (!this
				.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)
				&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));

			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAvvFascMod.setFasSieIdFascicoloSiep(
				((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep());
		lAvvFascMod.setAvvIdAvvocato(/* getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO) */idAvvocato);
		if (!isRequestParameterNullObj(ICostantiAvvocato.CAMPO_NOTE))
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOTE));

		if (!lAvvModRic.getCodUffAppartenenza().equals(getCodUfficioUtenteConnesso())) {
			// Selezionato Avvocato Standard: inserisco un nuovo Avvocato in copia
			// associandolo all'uffcio
			// MEV29 07/2015: Aggiunto aggiornamento foro
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			lAvvModRic.setCodUffAppartenenza(getCodUfficioUtenteConnesso());
			lAvvModRic.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());
			// MEV_21: valorizzo nuovi campi da REGINDE ed annullo "setCodComuneResidenza"
			// per il luogo residenza sarà aggiunta una nuova colonna che conterrà la descrizione del Comune
			// sede dello studio come presente in ReGIndE, abbandonando la valorizzazione della colonna
			// "COD_COMUNE_RESIDENZA", che resterà per i dati pregressi
			// ComuneModel lComModRes = new ComuneModel(getCodComuneByDescr(
			// getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			// lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
			if (flagReginde)
				AvvocatoUtil.valorizzaDatiReginde(lAvvModRic, getRequestStringParameter(CAMPO_PEC),
						getRequestStringParameter(CAMPO_COD_STATO_NASCITA),
						getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE),
						getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO));

			lAvvMod = lCtrl.ExInserisciAvvocato(lAvvModRic);
		} else {
			// L'avvocato selezionato è quello dell'ufficio: storicizzo e aggiorno
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
			// MEV 29 - 07/2015 - FIX non aggiornava il foro
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			// MEV_21: valorizzo nuovi campi da REGINDE ed annullo "setCodComuneResidenza"
			// per il luogo residenza sarà aggiunta una nuova colonna che conterrà la descrizione del Comune
			// sede dello studio come presente in ReGIndE, abbandonando la valorizzazione della colonna
			// "COD_COMUNE_RESIDENZA", che resterà per i dati pregressi
			// ComuneModel lComModRes = new ComuneModel(getCodComuneByDescr(
			// getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			// lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());
			if (flagReginde)
				AvvocatoUtil.valorizzaDatiReginde(lAvvModRic, getRequestStringParameter(CAMPO_PEC),
						getRequestStringParameter(CAMPO_COD_STATO_NASCITA),
						getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_REGINDE),
						getRequestStringParameter(CAMPO_DESC_COMUNE_STUDIO));

			lAvvMod = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

		BigDecimal idAvvFascicoloSiep = null;
		String lFlagValidato = "";
		if (((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato() != null)
			lFlagValidato = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();

		if ("01".equals(lAvvFascMod.getCodTipoAvvocato()) && (lFlagValidato.equals("S"))) {
			EventoNotificaModel lEve = CreoProvvedimento(lAvvFascMod);
			idAvvFascicoloSiep = lCtrl.ExInserisciAvvocato(lEve, lAvvMod, lAvvFascMod);
			setRequestAttribute("lEve", lEve);
		} else
			idAvvFascicoloSiep = lCtrl.ExInserisciAvvocato(lAvvMod, lAvvFascMod);

		// valore di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep&idAvvFascicoloSiep="
				+ idAvvFascicoloSiep;
	}

}