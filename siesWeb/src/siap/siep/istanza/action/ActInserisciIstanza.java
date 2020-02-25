package siap.siep.istanza.action;

/**
 * <p>Title: ActInserisciIstanza</p>
 * <p>Description: Classe Action per l'inserimento di Istanza</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.action.ICostantiAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istanza.controller.IIstanza;
import siap.siep.istanza.model.IstanzaModel;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciIstanza extends ActionSiap implements ICostantiIstanza {
	/**
	 * Azione di Inserimento del Istanza
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {
		IstanzaModel lIstMod = new IstanzaModel();

		// lIstMod.setIdIstanza( getRequestBigDecimalParameter( CAMPO_ID_ISTANZA) );

		// ------ Gestione Soggetto a cui si riferisce l'Istanza ------------------//
		boolean lPresenzaFascicoloNonArchiviato = false;

		SoggettoModel lSogMod = new SoggettoModel();

		FascicoloSiepModel lFasRetMod = null;

		// Il soggetto viene associato all'istanza:
		// * se presente in BDI(individuato da codice CS o Atto di Nascita) questo viene associato all'istanza
		// * se non trovato attraverso codice CS o atto di nascita viene inserito nella tabella soggetto e
		// associato all'istanza
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		lSogMod.setSesso(getRequestStringParameter(ICostantiSoggetto.CAMPO_SESSO));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
				ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(
				getRequestStringParameter(ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA));
		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());
		lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));
		lSogMod.setNazionalita(getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA));
		lSogMod.setCodCs(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_CS));
		lSogMod.setAttoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_ATTO_NASCITA));

		lSogMod.setFlagPresenzaFascicolo("N");

		lSogMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lSogMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lSogMod.setDataInserimento(DateUtils.getSysDate());

		//
		lIstMod.setCodEsito("31"); // ******
		lIstMod.setCodStatoIstanza("I"); // ISTANZA

		// Sentenza
		lIstMod.setAnnoSentenza(getRequestBigDecimalParameter(CAMPO_ANNO_SENTENZA));
		lIstMod.setNumeroSentenza(getRequestStringParameter(CAMPO_NUMERO_SENTENZA));
		lIstMod.setDataSentenza(getRequestDateParameter(CAMPO_ANNO_DATA_SENTENZA, CAMPO_MESE_DATA_SENTENZA,
				CAMPO_GIORNO_DATA_SENTENZA));
		lIstMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lIstMod.setCodLuogoEmittente(lComMod.getCodComune());
		// Controllo sull'esistenza dell'ufficio per quel comune
		if (!lIstMod.getCodTipoAutoritaEmittente().equals("-"))
			getCodUfficioByCodTipoUfficioDescrComune(lIstMod.getCodTipoAutoritaEmittente(),
					lComMod.getDescrizione());
		lIstMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));

		// * Controllo esistenza fascicolo non archiviato *
		BigDecimal lAnnoFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
		BigDecimal lProgrFascicoloSiep = getRequestBigDecimalParameter(
				ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);

		if (lAnnoFascicoloSiep != null && lProgrFascicoloSiep != null) {
			FascicoloSiepModel lFasMod = new FascicoloSiepModel();
			lFasMod.setChiaveAnno(lAnnoFascicoloSiep);
			lFasMod.setChiaveProgr(lProgrFascicoloSiep);
			lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());

			IFascicoloSiep lFasCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lFasRetMod = lFasCtrl.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasMod);

			// Se il fascicolo esiste non archiviato/definito
			if (lFasRetMod != null && !"01".equals(lFasRetMod.getCodStatoFascicolo())) {
				lPresenzaFascicoloNonArchiviato = true;
				// * Viene associato all'istanza l'id soggetto legato al fascicolo non archiviato,
				// * e non viene preso in considerazione il soggetto indicato nella form
				lIstMod.setSogIdSoggetto(lFasRetMod.getSoggetto().getIdSoggetto());
				// * Vengono associati all'istanza gli estremi della sentenza legati al fascicolo non
				// archiviato,
				// * e non vengono presi in considerazione i dati della sentenza indicati nella form
				SentenzaModel lSentenza = lFasRetMod.getSentenza();
				lIstMod.setAnnoSentenza(lSentenza.getAnnoSentenza());
				lIstMod.setNumeroSentenza(lSentenza.getNumeroSentenza());
				lIstMod.setDataSentenza(lSentenza.getDataProvvedimento()); // Corrisponde a DATA SENTENZA
																			// (perchè?)
				lIstMod.setCodTipoAutoritaEmittente(lSentenza.getCodTipoAutoritaEmittente());
				lIstMod.setCodLuogoEmittente(lSentenza.getCodLuogoEmittente());
				// modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
				// lIstMod.setDataIrrevocabilita( lSentenza.getDataIrrevocabilita() );

				lIstMod.setCodEsito("32"); // ******
				lIstMod.setCodStatoIstanza("R"); // ISTANZA RIFERITA A FASCICOLO
			} else
				throw new F3BException(F3BException.USER_MESSAGE,
						"Il Procedimento non esiste o risulta archiviato");
		}

		// Soggetto presentante
		lIstMod.setCognomeSoggettoPresentante(getRequestStringParameter(CAMPO_COGNOME_SOGGETTO_PRESENTANTE));
		lIstMod.setNomeSoggettoPresentante(getRequestStringParameter(CAMPO_NOME_SOGGETTO_PRESENTANTE));

		// Avvocato
		lIstMod.setCognomeAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_COGNOME));
		lIstMod.setNomeAvvocato(getRequestStringParameter(ICostantiAvvocato.CAMPO_NOME));
		lIstMod.setForoCompetenza(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO));

		// Istanza
		lIstMod.setDataPresentazione(getRequestDateParameter(CAMPO_ANNO_DATA_PRESENTAZIONE,
				CAMPO_MESE_DATA_PRESENTAZIONE, CAMPO_GIORNO_DATA_PRESENTAZIONE));
		lIstMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_MOTIVO));
		lIstMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		lIstMod.setAnnoRegistro(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		// -- lIstMod.setProgrRegistro( getRequestBigDecimalParameter( CAMPO_PROGR_REGISTRO) ); // Gestito in
		// automatico

		/*
		 * lIstMod.setCodTipoUfficioDestinatario( getRequestStringParameter(
		 * CAMPO_COD_TIPO_UFFICIO_DESTINATARIO) ); lComMod = new
		 * ComuneModel(getCodComuneByDescr(getRequestStringParameter( CAMPO_COD_LUOGO_DESTINATARIO )) );
		 * lIstMod.setCodLuogoDestinatario( lComMod.getCodComune() ); //Controllo sull'esistenza dell'ufficio
		 * per quel comune if(!lIstMod.getCodTipoAutoritaEmittente().equals("-"))
		 * lIstMod.setCodUfficioDestinatario(getCodUfficioByCodTipoUfficioDescrComune(
		 * lIstMod.getCodTipoUfficioDestinatario(), lComMod.getDescrizione()) ); else
		 * lIstMod.setCodUfficioDestinatario("-");
		 */

		// --- ? lIstMod.setCodTipoUfficioDestinatario( getRequestStringParameter(
		// CAMPO_COD_TIPO_UFFICIO_DESTINATARIO) );
		lIstMod.setCodTipoUfficioDestinatario("-"); // !!
		// --- ? lIstMod.setCodLuogoDestinatario( getRequestStringParameter( CAMPO_COD_LUOGO_DESTINATARIO) );
		lIstMod.setCodLuogoDestinatario("-"); // !!
		// --- ? lIstMod.setCodUfficioDestinatario( getRequestStringParameter( CAMPO_COD_UFFICIO_DESTINATARIO)
		// );
		lIstMod.setCodUfficioDestinatario("-"); // !!

		lIstMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lIstMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// * Il campo DescrUfficioInserimento viene usato come appoggio al CodComuneUtenteConnesso
		// * per inserirlo eventualmente nella tabella evento come CodLuogoEmittente
		lIstMod.setDescrUfficioInserimento(getCodComuneUtenteConnesso());
		lIstMod.setDataInserimento(DateUtils.getSysDate());

		IIstanza lIstCtrl = SIEPLookupRemote.getIstanzaRemote();
		IstanzaModel lIstRetMod = new IstanzaModel();
		if (lPresenzaFascicoloNonArchiviato) {
			lIstRetMod = lIstCtrl.ExInserisciIstanzaFascicoloSiep(lIstMod, lFasRetMod);
		} else {
			lIstRetMod = lIstCtrl.ExInserisciIstanzaSoggetto(lIstMod, lSogMod);
		}
		// String lPage = "";
		return /* lPage = */IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.istanza.action.ActLoadDettaglioIstanza&" + CAMPO_ID_ISTANZA + "="
				+ lIstRetMod.getIdIstanza().toString();
	}

}