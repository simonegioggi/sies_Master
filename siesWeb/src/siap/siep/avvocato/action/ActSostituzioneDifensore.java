package siap.siep.avvocato.action;

/**
 * <p>Title: ActSostituzioneDifensore</p>
 * <p>Description: Classe Action per la sostituzione di un Avvocato su un 
 *    fascicolo SIEP</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoFascicoloSiepModel;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.storicoavvocato.model.StoricoAvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActSostituzioneDifensore extends ActProvvedimentoDifensore implements ICostantiAvvocato {

	/*****************************************************************************
	 * Azione di Sostituzione di un avvocato su un fascicolo SIEP, recupera dalla form i dati del nuovo
	 * avvocato e l'id del vecchio. E quindi aggiorna la data fine validità del vecchio avvocato e inserisce i
	 * dati del nuovo.
	 * 
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		BigDecimal id_fasc_siep = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();
		BigDecimal id_avv_new = getRequestBigDecimalParameter(CAMPO_ID_AVVOCATO);

		// ==========================================================================
		// Recupero i dati del nuovo avvocato selezionato per poter verificare che
		// non sia: sospeso, radiato, o non in attività per qualche motivo.
		// n.b. la finestra di selezione degli avvocati non inserisce questo filtro
		// ==========================================================================
		AvvocatoModel lAvvModRic = new AvvocatoModel();
		lAvvModRic.setIdAvvocato(id_avv_new);

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = lCtrl.ExRicercaAvvocatoPerInserimento(lAvvModRic);
		lAvvModRic = (AvvocatoModel) lVect.get(0);

		if (lAvvModRic.getDataSospensione() != null
				&& !"-".equals(lAvvModRic.getDataSospensione().toString()))
			throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: il difensore risulta sospeso!");

		if (lAvvModRic.getDataRadiazione() != null && !"-".equals(lAvvModRic.getDataRadiazione().toString()))
			throw new F3BException(F3BException.USER_MESSAGE, "Attenzione: il difensore risulta radiato!");

		if (lAvvModRic.getCodNonAttivita() != null && !lAvvModRic.getCodNonAttivita().equals("-")
												   && !lAvvModRic.getCodNonAttivita().equals("A"))	// 20210627	MEV_21
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione: il difensore risulta non in attività per   "
							+ lAvvModRic.getDescrNonAttivita() + "");

		BigDecimal id_avv_old = getRequestBigDecimalParameter("idAvvVecchio");

		// ==========================================================================
		// Recupero l'elenco degli avvocati attualmente associati al fascicolo
		// per controllare che nella sostituzione non vengano inseriti più avvocati
		// se non di fiducia
		// ==========================================================================
		AvvocatoModel lAvvModPrec = new AvvocatoModel();
		AvvocatoFascicoloSiepModel lAvvFascModPrec = new AvvocatoFascicoloSiepModel();
		lAvvFascModPrec.setFasSieIdFascicoloSiep(id_fasc_siep);

		Vector lVectPrec = null;
		try {
			lVectPrec = new Vector();
			// recupera tutti gli avvocati attualmente associati al fascicolo
			// n.b. recupera solo i dati della tabella AVVOCATO
			lVectPrec = lCtrl.ExRicercaAvvocatiAttualiFascicolo(lAvvModPrec, lAvvFascModPrec);
		} catch (Exception e) {
			throw new F3BException(F3BException.USER_MESSAGE,
					"Errore nella verifica degli avvocati già a sistema: " + e.getMessage());
		}

		// ==========================================================================
		// Prima di sostituire l'avvocato vecchio con il nuovo devo verificare
		// che non vengano inseriti più avvocati se non di fiducia
		// ==========================================================================
		if (lVectPrec.size() > 1) {

			lAvvModPrec = (AvvocatoModel) lVectPrec.get(0);
			String lDescrTipo = lAvvModPrec.getDescrTipo();

			if ((lDescrTipo.equalsIgnoreCase("DI FIDUCIA")
					&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("02"))
					|| (lDescrTipo.equalsIgnoreCase("Della Fase di Giudizio")
							&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO).equals("03"))) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi di Fiducia o entrambi della Fase di Giudizio!");
			}
		}

		// ==========================================================================
		// Carico il model di Inserimento con i dati recuperati dalla Form
		// ==========================================================================
		AvvocatoFascicoloSiepModel lAvvFascMod = new AvvocatoFascicoloSiepModel();

		lAvvFascMod.setAvvIdAvvocato(id_avv_new);
		lAvvFascMod.setFasSieIdFascicoloSiep(id_fasc_siep);

		lAvvFascMod.setCodTipoAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO));
		if (lAvvFascMod.getCodTipoAvvocato().equals("01")) { // d'ufficio
			lAvvFascMod.setDataInizioValidita(
					getRequestDateParameter(ICostantiAvvocato.CAMPO_ANNO_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_MESE_DATA_DESIGNAZIONE,
							ICostantiAvvocato.CAMPO_GIORNO_DATA_DESIGNAZIONE));
		} else if (lAvvFascMod.getCodTipoAvvocato().equals("02")) { // di fiducia
			lAvvFascMod.setDataInizioValidita(getRequestDateParameter(
					ICostantiAvvocato.CAMPO_ANNO_DATA_NOMINA, ICostantiAvvocato.CAMPO_MESE_DATA_NOMINA,
					ICostantiAvvocato.CAMPO_GIORNO_DATA_NOMINA));
		} else if (lAvvFascMod.getCodTipoAvvocato().equals("03")) { // Della Fase di Giudizio
			lAvvFascMod.setDataInizioValidita(DateUtils.getSysDate()); // ?????????????
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE)) {
			lAvvFascMod.setCodMotivoDesignazione(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_MOTIVO_DESIGNAZIONE));
		} else {
			lAvvFascMod.setCodMotivoDesignazione("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA))
			lAvvFascMod.setCodTipoAutorita(
					getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA));
		else
			lAvvFascMod.setCodTipoAutorita("-");

		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_SEDE)
				&& (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						&& !getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
								.equals("-"))) {
			String lComneAutorita = getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lComneAutorita));
			lAvvFascMod.setSedeAutorita(lComMod.getCodComune());
		} else {
			lAvvFascMod.setSedeAutorita("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_INDIRIZZO_TIPO_AUTORITA))
			lAvvFascMod.setIndirizzoTipoAutorita(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_INDIRIZZO_TIPO_AUTORITA));
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))
			lAvvFascMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));

		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF))
			lAvvFascMod.setCodTipoAutoritaDif(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF));
		else
			lAvvFascMod.setCodTipoAutoritaDif("-");

		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF) && (!this
				.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF)
				&& !getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_TIPO_AUTORITA_DIF).equals("-"))) {
			String lComneAutoritaDif = getRequestStringParameter(
					ICostantiAvvocato.CAMPO_COD_SEDE_AUTORITA_DIF);
			ComuneModel lComModDif = new ComuneModel(getCodComuneByDescr(lComneAutoritaDif));
			lAvvFascMod.setSedeAutoritaDif(lComModDif.getCodComune());
		} else {
			lAvvFascMod.setSedeAutoritaDif("-");
		}

		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_NOTE)) {
			lAvvFascMod.setNote(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOTE));
		}

		lAvvFascMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAvvFascMod.setDataInserimento(DateUtils.getSysDate());
		lAvvFascMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// ==========================================================================
		// Effettuo la sostituzione che consiste nel valorizzare la data fine
		// validità del vecchio avvocato e nell'inserimento dei dati del nuovo avvocato
		// Queste operazioni vengono effettuate contestualmente per motivi di
		// transazione
		// ==========================================================================

		AvvocatoSiepModel avv_siep_old = lCtrl
				.ExRicercaAvvocatoFascicoloSiepByIdAvvocatoIdFascicolo(id_avv_old, id_fasc_siep);
		AvvocatoFascicoloSiepModel lAvvFascUp = avv_siep_old.getAvvocatoFascicoloSiepModel();
		lAvvFascUp.setDataFineValidita(DateUtils.getSysDate());

		lAvvFascUp.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAvvFascUp.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lAvvFascUp.setDataAggiornamento(DateUtils.getSysDate());

		lAvvFascUp.setMotivo("Sostituzione");

		if (!lAvvModRic.getCodUffAppartenenza().equals(this.getCodUfficioUtenteConnesso())) {
			// Avvocato selezionato di altro uffico (sicuramente avvocato standard 00000).
			// Vado in inserimento in copia
			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());
			lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());
			ComuneModel lComModRes = new ComuneModel(this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAvvModRic.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAvvModRic.setDataInserimento(DateUtils.getSysDate());

			lAvvModRic = lCtrl.ExInserisciAvvocato(lAvvModRic);
		} else {
			// L'Avvocato selezionato è dell'uffico. Storicizzo e vado in Update.
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

			lAvvModRic.setForo(getRequestStringParameter(CAMPO_FORO).toUpperCase());
			lAvvModRic.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase());
			lAvvModRic.setTelefono(getRequestStringParameter(CAMPO_TELEFONO));
			lAvvModRic.setFax(getRequestStringParameter(CAMPO_FAX));
			lAvvModRic.setEMail(getRequestStringParameter(CAMPO_E_MAIL).toUpperCase());

			lAvvModRic.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lAvvModRic.setDataAggiornamento(DateUtils.getSysDate());
			lAvvModRic.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

			ComuneModel lComModRes = new ComuneModel(this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COD_COMUNE_RESIDENZA)));
			lAvvModRic.setCodComuneResidenza(lComModRes.getCodComune());

			lAvvModRic = lCtrl.ExModificaStoricizzaAvvocato(lAvvModRic, lStoricoModel);
		}

		lAvvFascMod.setAvvIdAvvocato(lAvvModRic.getIdAvvocato());
		AvvocatoFascicoloSiepModel lAvvMod = new AvvocatoFascicoloSiepModel();
		lAvvFascMod.setAvvIdAvvocatoFascicoloSost(lAvvFascUp.getIdAvvocatoFascicoloSiep());

		String lFlagValidato = "";
		if (((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato() != null) {
			lFlagValidato = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();
		}

		// se avvocato d'ufficio
		if ("01".equals(lAvvFascMod.getCodTipoAvvocato()) && (lFlagValidato.equals("S"))) {
			EventoNotificaModel lEve = CreoProvvedimento(lAvvFascMod);
			lAvvMod = lCtrl.ExSostituzioneAvvocato(lEve, lAvvFascUp, lAvvFascMod);
			setRequestAttribute("lEve", lEve);
		} else {
			lAvvMod = lCtrl.ExSostituzioneAvvocato(lAvvFascUp, lAvvFascMod);
		}

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.avvocato.action.ActLoadDettaglioAvvocatoAvvocatoFascicoloSiep&idAvvFascicoloSiep="
				+ lAvvMod.getIdAvvocatoFascicoloSiep();
	}

}