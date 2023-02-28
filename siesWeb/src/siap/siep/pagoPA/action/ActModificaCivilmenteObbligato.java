package siap.siep.pagoPA.action;

import java.math.BigDecimal;
import java.util.List;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.ICivilmenteObbligato;
import siap.siep.pagoPA.model.CivilmenteObbligatoModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-13 Title: ActModificaCivilmenteObbligato Description: Classe Action per la modifica del Civilmente
 * Obbligato
 *
 * @author sgioggi
 * @version 1.0
 */
public class ActModificaCivilmenteObbligato extends ActionSiap implements ICostantiPagoPA {

	public String processRequest() throws Exception {

		// Identificativo della parte
		String idCivilmenteObbligato = getRequestStringParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO);

		CivilmenteObbligatoModel com = riempiDatiCivilmenteObbligato();
		com.setIdCivilmenteObbligato(new BigDecimal(idCivilmenteObbligato));
		com.getResidenza().setIdParteUdienza(new BigDecimal(idCivilmenteObbligato));

		ICivilmenteObbligato ico = SIEPLookupRemote.getCivilmenteObbligatoRemote();
		ico.ExModificaCivilmenteObbligato(com);

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.siep.pagoPA.action.ActDettaglioParteUdienza");
		lRedir.setParameter(CAMPO_ID_CIVILMENTE_OBBLIGATO, idCivilmenteObbligato);

		// Torna alla pagina di dettaglio parti senza aggiungerla allo stack
		lRedir.setParameter(IWebConstants.LINK_RITORNO, "10");

		return lRedir.toString();
	}

	@SuppressWarnings("rawtypes")
	protected CivilmenteObbligatoModel riempiDatiCivilmenteObbligato() throws Exception {

		CivilmenteObbligatoModel com = new CivilmenteObbligatoModel();
		ResidenzaModel rm = new ResidenzaModel();

		// Parte interessata all'udienza (F=Fisica, G=Giuridica)
		String codPersona = getRequestStringParameter(RADIO_COD_PERSONA);
		com.setCodParte(codPersona);

		// Persona Giuridica
		if (codPersona != null && codPersona.equals("G")) {
			// Società
			com.setDenominazione(getRequestStringParameter(CAMPO_DENOMINAZIONE).toUpperCase().trim());
			// Ragione Sociale
			if (!isRequestParameterNullObj(CAMPO_RAG_SOCIALE)) {
				com.setRagSociale(getRequestStringParameter(CAMPO_RAG_SOCIALE));
			}
			// Provincia
			if (!isRequestParameterNullObj(CAMPO_COD_PROVINCIA)) {
				com.setCodProvincia(getRequestStringParameter(CAMPO_COD_PROVINCIA));
			}
			// Partita IVA/Codice Fiscale
			if (!isRequestParameterNullObj(CAMPO_COD_FISCALE_RAP)) {
				com.setCodFiscaleRap(getRequestStringParameter(CAMPO_COD_FISCALE_RAP));
			}
			// Sede Legale
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_LEGALE)) {
				com.setIndSedeLegale(getRequestStringParameter(CAMPO_IND_SEDE_LEGALE).toUpperCase().trim());
			}
			// Sede Operativa
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_OPERATIVA)) {
				com.setIndSedeOperativa(
						getRequestStringParameter(CAMPO_IND_SEDE_OPERATIVA).toUpperCase().trim());
			}
		}

		// Cognome
		com.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase().trim());
		// Nome
		com.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase().trim());
		// Sesso
		com.setSesso(getRequestStringParameter(CAMPO_SESSO));
		// Data Nascita
		com.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod = null;
		if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_NASCITA)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA));
			// Comune Nascita
			com.setCodComuneNascita(lComMod.getCodComune());
			com.setCodProvinciaNascita(lComMod.getCodProvincia());
		} else {
			com.setCodComuneNascita("-");
			com.setCodProvinciaNascita("-");
		}

		// Stato Nascita
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA)) {
			com.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		}
		// Comune Nascita Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_NASCITA_ESTERO)) {
			com.setDescComuneNascitaEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO).toUpperCase().trim());
		}

		// Codice Fiscale/Partita IVA
		if (!isRequestParameterNullObj(CAMPO_COD_FISCALE)) {
			com.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase().trim());
		}

		// Imposto la descrizione della nazione
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(com.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		com.setDescrStatoNascita(lDescri);

		if (lComMod != null) {
			com.setDescComuneNascita(lComMod.getDescrizione());
		} else {
			com.setDescComuneNascita("-");
		}

		// Imposto la descrizione del comune
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(com.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		if (lIndModel != -1) {
			lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
			com.setDescrProvinciaNascita(lDescri);
		} else {
			com.setDescrProvinciaNascita("-");
		}

		// Residenza/Domicilio
		// Tipo Residenza
		rm.setCodTipoResidenza("R");

		// Id Residenza
		if (!isRequestParameterNullObj(CAMPO_ID_RESIDENZA)) {
			rm.setIdResidenza(getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA));
		}

		// Indirizzo
		if (!isRequestParameterNullObj(CAMPO_INDIRIZZO)) {
			rm.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase().trim());
		}

		// Luogo
		ComuneModel lComuneMod = null;
		if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA).length() > 0) {
			lComuneMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA));
			rm.setCodComune(lComuneMod.getCodComune());
			rm.setCodProvincia(lComuneMod.getCodProvincia());
		} else {
			rm.setCodComune("-");
			rm.setCodProvincia("-");
		}

		// CAP
		if (!isRequestParameterNullObj(CAMPO_CAP_RESIDENZA)) {
			rm.setCap(getRequestStringParameter(CAMPO_CAP_RESIDENZA));
		}

		// Comune Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA)) {
			rm.setDescComuneEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA).toUpperCase().trim());
		}

		// Stato
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_RESIDENZA)) {
			rm.setCodStato(getRequestStringParameter(CAMPO_COD_STATO_RESIDENZA));
		}

		// Imposto la descrizione della Nazione di Residenza
		DecodificheModel lDecModRes = new DecodificheModel();
		lDecModRes.setContesto("NAZIONE");
		lDecModRes.setCode(rm.getCodStato());
		// List lNazioniRes = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModelRes = lNazioni.indexOf(lDecModRes);
		String lDescriRes = ((DecodificheModel) lNazioni.get(lIndModelRes)).getDescription();
		rm.setDescrStato(lDescriRes);

		if (lComuneMod != null) {
			rm.setDescrComune(lComuneMod.getDescrizione());
		} else {
			rm.setDescrComune(null);
		}

		// Imposto la descrizione della Provincia di Residenza
		lDecModRes = new DecodificheModel();
		lDecModRes.setContesto("PROVINCIA");
		lDecModRes.setCode(rm.getCodProvincia());
		List lProvincieRes = (List) DecodificheManager.getInstance().getProvincie();
		lIndModelRes = lProvincieRes.indexOf(lDecMod);
		if (lIndModelRes != -1) {
			lDescriRes = ((DecodificheModel) lProvincieRes.get(lIndModelRes)).getDescription();
			rm.setDescrProvincia(lDescriRes);
		} else {
			rm.setDescrProvincia("-");
		}

		if (rm.getIdResidenza() != null) {
			rm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			rm.setDataAggiornamento(DateUtils.getSysDate());
			rm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		} else {
			rm.setCodOperatoreInserimento(getCodUtenteConnesso());
			rm.setDataInserimento(DateUtils.getSysDate());
			rm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		}

		// Domicilio Presso Difensore
		if (!isRequestParameterNullObj(CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE)) {
			rm.setFlgDomicilioDifensore(getRequestStringParameter(CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE));
		}

		com.setResidenza(rm);

		com.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		com.setDataAggiornamento(DateUtils.getSysDate());
		com.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		return com;
	}

}