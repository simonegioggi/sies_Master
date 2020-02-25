package siap.sige.udienzaparti.action;

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
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActModificaParteUdienza
 * </p>
 * <p>
 * Description: Classe Azione di modifica della Parte Udienza (Offesa/Civile - Fisica/Giuridica)
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 */
public class ActModificaParteUdienza extends ActionSiap implements ICostantiPartiUdienza {

	public String processRequest() throws Exception {

		// Codice del tipo parte da inserire (O=Offesa, C=Civile)
		String codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);

		// Identificativo della parte
		String lIdSoggetto = getRequestStringParameter(CAMPO_ID_SOGGETTO);

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);

		AnagraficaPartiUdienzaModel lAnagraficaParteMod = letturaAnagraficaParte();
		lAnagraficaParteMod.setIdSoggetto(new BigDecimal(lIdSoggetto));
		lAnagraficaParteMod.getResidenza().setIdParteUdienza(new BigDecimal(lIdSoggetto));

		IPartiUdienza lCtrl = SIGELookupRemote.getPartiUdienzaRemote();

		lCtrl.ExModificaParteUdienza(lAnagraficaParteMod, null, new BigDecimal(lIdEventoUdienza));

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzaparti.action.ActDettaglioParteUdienza");
		lRedir.setParameter(CAMPO_ID_SOGGETTO, lIdSoggetto);
		lRedir.setParameter(CAMPO_COD_TIPO_PART, codTipoParte);

		// Torna alla pagina di dettaglio parti senza aggiungerla allo stack
		lRedir.setParameter(IWebConstants.LINK_RITORNO, "10");
		// lRedir.setParameter(IWebConstants.FLAG_RITORNO, "1");

		return lRedir.toString();

	}

	@SuppressWarnings("rawtypes")
	protected AnagraficaPartiUdienzaModel letturaAnagraficaParte() throws Exception {
		AnagraficaPartiUdienzaModel lAnagraficaParteModel = new AnagraficaPartiUdienzaModel();
		ResidenzaModel residenzaMod = new ResidenzaModel();

		// Codice del tipo parte da inserire (O=Offesa, C=Civile)
		String codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);
		lAnagraficaParteModel.setCodTipoPart(codTipoParte);

		// Parte interessata all'udienza (F=Fisica, G=Giuridica)
		String codParte = getRequestStringParameter(RADIO_COD_PARTE);
		lAnagraficaParteModel.setCodParte(codParte);

		// Persona Giuridica
		if (codParte != null && codParte.equals("G")) {

			// Società
			lAnagraficaParteModel
					.setDenominazione(getRequestStringParameter(CAMPO_DENOMINAZIONE).toUpperCase().trim());
			// Ragione Sociale
			if (!isRequestParameterNullObj(CAMPO_RAG_SOCIALE)) {
				lAnagraficaParteModel.setRagSociale(getRequestStringParameter(CAMPO_RAG_SOCIALE));
			}
			// Provincia
			if (!isRequestParameterNullObj(CAMPO_COD_PROVINCIA)) {
				lAnagraficaParteModel.setCodProvincia(getRequestStringParameter(CAMPO_COD_PROVINCIA));
			}
			// Partita IVA/Codice Fiscale
			if (!isRequestParameterNullObj(CAMPO_COD_FISCALE_RAP)) {
				lAnagraficaParteModel.setCodFiscaleRap(getRequestStringParameter(CAMPO_COD_FISCALE_RAP));
			}
			// Sede Legale
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_LEGALE)) {
				lAnagraficaParteModel.setIndSedeLegale(
						getRequestStringParameter(CAMPO_IND_SEDE_LEGALE).toUpperCase().trim());
			}
			// Sede Operativa
			if (!isRequestParameterNullObj(CAMPO_IND_SEDE_OPERATIVA)) {
				lAnagraficaParteModel.setIndSedeOperativa(
						getRequestStringParameter(CAMPO_IND_SEDE_OPERATIVA).toUpperCase().trim());
			}
		}

		// Cognome
		lAnagraficaParteModel.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase().trim());
		// Nome
		lAnagraficaParteModel.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase().trim());
		// Sesso
		lAnagraficaParteModel.setSesso(getRequestStringParameter(CAMPO_SESSO));
		// Data Nascita
		lAnagraficaParteModel.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA,
				CAMPO_MESE_DATA_NASCITA, CAMPO_GIORNO_DATA_NASCITA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod = null;
		if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_NASCITA)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA));
			// Comune Nascita
			lAnagraficaParteModel.setCodComuneNascita(lComMod.getCodComune());
			lAnagraficaParteModel.setCodProvinciaNascita(lComMod.getCodProvincia());
		} else {
			lAnagraficaParteModel.setCodComuneNascita("-");
			lAnagraficaParteModel.setCodProvinciaNascita("-");
		}

		// Stato Nascita
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA)) {
			lAnagraficaParteModel.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		}
		// Comune Nascita Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_NASCITA_ESTERO)) {
			lAnagraficaParteModel.setDescComuneNascitaEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO).toUpperCase().trim());
		}

		// Codice Fiscale/Partita IVA
		if (!isRequestParameterNullObj(CAMPO_COD_FISCALE)) {
			lAnagraficaParteModel
					.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase().trim());
		}

		// Imposto la descrizione della nazione
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lAnagraficaParteModel.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		lAnagraficaParteModel.setDescrStatoNascita(lDescri);

		if (lComMod != null) {
			lAnagraficaParteModel.setDescComuneNascita(lComMod.getDescrizione());
		} else {
			lAnagraficaParteModel.setDescComuneNascita("-");
		}

		// Imposto la descrizione del comune
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(lAnagraficaParteModel.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		if (lIndModel != -1) {
			lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
			lAnagraficaParteModel.setDescrProvinciaNascita(lDescri);
		} else {
			lAnagraficaParteModel.setDescrProvinciaNascita("-");
		}

		// Flag Convocazione Udienza
		lAnagraficaParteModel.setFlagConvUdienza(getRequestStringParameter(CAMPO_CONVOCAZIONE_UDIENZA));

		// Residenza/Domicilio

		// Tipo Residenza
		residenzaMod.setCodTipoResidenza("R");

		// Id Residenza
		if (!isRequestParameterNullObj(CAMPO_ID_RESIDENZA)) {
			residenzaMod.setIdResidenza(getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA));
		}

		// Indirizzo
		if (!isRequestParameterNullObj(CAMPO_INDIRIZZO)) {
			residenzaMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO).toUpperCase().trim());
		}

		// Luogo
		ComuneModel lComuneMod = null;
		if (!isRequestParameterNullObj(CAMPO_COD_COMUNE_RESIDENZA)
				&& getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA).length() > 0) {
			lComuneMod = getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_COMUNE_RESIDENZA));
			residenzaMod.setCodComune(lComuneMod.getCodComune());
			residenzaMod.setCodProvincia(lComuneMod.getCodProvincia());
		} else {
			residenzaMod.setCodComune("-");
			residenzaMod.setCodProvincia("-");
		}

		// CAP
		if (!isRequestParameterNullObj(CAMPO_CAP_RESIDENZA)) {
			residenzaMod.setCap(getRequestStringParameter(CAMPO_CAP_RESIDENZA));
		}

		// Comune Estero
		if (!isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA)) {
			residenzaMod.setDescComuneEstero(
					getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO_RESIDENZA).toUpperCase().trim());
		}

		// Stato
		if (!isRequestParameterNullObj(CAMPO_COD_STATO_RESIDENZA)) {
			residenzaMod.setCodStato(getRequestStringParameter(CAMPO_COD_STATO_RESIDENZA));
		}

		// Imposto la descrizione della Nazione di Residenza
		DecodificheModel lDecModRes = new DecodificheModel();
		lDecModRes.setContesto("NAZIONE");
		lDecModRes.setCode(residenzaMod.getCodStato());
		// List lNazioniRes = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModelRes = lNazioni.indexOf(lDecModRes);
		String lDescriRes = ((DecodificheModel) lNazioni.get(lIndModelRes)).getDescription();
		residenzaMod.setDescrStato(lDescriRes);

		if (lComuneMod != null) {
			residenzaMod.setDescrComune(lComuneMod.getDescrizione());
		} else {
			residenzaMod.setDescrComune(null);
		}

		// Imposto la descrizione della Provincia di Residenza
		lDecModRes = new DecodificheModel();
		lDecModRes.setContesto("PROVINCIA");
		lDecModRes.setCode(residenzaMod.getCodProvincia());
		List lProvincieRes = (List) DecodificheManager.getInstance().getProvincie();
		lIndModelRes = lProvincieRes.indexOf(lDecMod);
		if (lIndModelRes != -1) {
			lDescriRes = ((DecodificheModel) lProvincieRes.get(lIndModelRes)).getDescription();
			residenzaMod.setDescrProvincia(lDescriRes);
		} else {
			residenzaMod.setDescrProvincia("-");
		}

		if (residenzaMod.getIdResidenza() != null) {
			residenzaMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			residenzaMod.setDataAggiornamento(DateUtils.getSysDate());
			residenzaMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		} else {
			residenzaMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			residenzaMod.setDataInserimento(DateUtils.getSysDate());
			residenzaMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		}

		// Domicilio Presso Difensore
		if (!isRequestParameterNullObj(CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE)) {
			residenzaMod.setFlgDomicilioDifensore(
					getRequestStringParameter(CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE));
		}

		lAnagraficaParteModel.setResidenza(residenzaMod);

		lAnagraficaParteModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lAnagraficaParteModel.setDataAggiornamento(DateUtils.getSysDate());
		lAnagraficaParteModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		return lAnagraficaParteModel;
	}

}