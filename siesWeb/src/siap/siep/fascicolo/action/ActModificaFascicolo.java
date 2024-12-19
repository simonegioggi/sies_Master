package siap.siep.fascicolo.action;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActModificaFascicolo - Class Action per la Modifica del Fascicolo
 *
 * @version 1.0
 */
public class ActModificaFascicolo extends ActionSiap implements ICostantiFascicoloSiep {

	private UtenteModel lUtenteMod = null;
	private FascicoloSiepModel lFasMod = null;

	public void oscuraProcessRequest() throws Exception {

		lFasMod.setIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP));
		lFasMod.setVisibilitaMinorenne("N");
		lFasMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lFasMod.setDataAggiornamento(DateUtils.getSysDate());
		lFasMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());

		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lCtrl.ExModificaVisibilitaMinoreFascicoloSiep(lFasMod);
	}

	public void defaultProcessRequest() throws Exception {

		lFasMod.setIdFascicoloSiep(getRequestBigDecimalParameter(CAMPO_ID_FASCICOLO_SIEP));
		// lFasMod.setChiaveAnno( getRequestBigDecimalParameter( CAMPO_CHIAVE_ANNO) );
		// lFasMod.setChiaveUfficio();
		// lFasMod.setChiaveProgr( getRequestBigDecimalParameter( CAMPO_CHIAVE_PROGR) );
		// lFasMod.setCodStatoFascicolo( getRequestStringParameter( CAMPO_COD_STATO_FASCICOLO) );
		// lFasMod.setDataArrivoSentenza( getRequestDateParameter(
		// CAMPO_ANNO_ARRIVO_SENTENZA,CAMPO_MESE_ARRIVO_SENTENZA,CAMPO_GIORNO_ARRIVO_SENTENZA) );
		lFasMod.setDataIscrizione(getRequestDateParameter(CAMPO_ANNO_ISCRIZIONE_ATTI,
				CAMPO_MESE_ISCRIZIONE_ATTI, CAMPO_GIORNO_ISCRIZIONE_ATTI));
		// lFasMod.setDataArchiviazione( getRequestDateParameter(
		// CAMPO_ANNO_ARCHIVIAZIONE,CAMPO_MESE_ARCHIVIAZIONE,CAMPO_GIORNO_ARCHIVIAZIONE) );
		// lFasMod.setCodMotivoArchiviazione("-");
		// lFasMod.setLetteraFascicolo( getRequestStringParameter( CAMPO_LETTERA_FASCICOLO) );
		// lFasMod.setAnnoFascicoloUnione( getRequestStringParameter( CAMPO_ANNO_FASCICOLO_UNIONE) );
		// lFasMod.setNumFascicoloUnione( getRequestStringParameter( CAMPO_NUM_FASCICOLO_UNIONE) );
		// lFasMod.setDataUnione( getRequestDateParameter( CAMPO_ANNO_UNIONE, CAMPO_MESE_UNIONE,
		// CAMPO_GIORNO_UNIONE ) );
		lFasMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		// lFasMod.setTestoAggiuntivo( getRequestStringParameter( CAMPO_TESTO_AGGIUNTIVO) );
		// lFasMod.setFlagValidato( getRequestStringParameter( CAMPO_FLAG_VALIDATO) );
		// lFasMod.setCodOperatoreInserimento( getRequestStringParameter( CAMPO_COD_OPERATORE_INSERIMENTO) );
		// lFasMod.setDataInserimento( getRequestDateParameter( CAMPO_DATA_INSERIMENTO) );
		lFasMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lFasMod.setDataAggiornamento(DateUtils.getSysDate());
		lFasMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		// lFasMod.setSogIdSoggetto( getRequestBigDecimalParameter( CAMPO_SOG_ID_SOGGETTO) );
		// lFasMod.setSenIdSentenza( getRequestBigDecimalParameter( CAMPO_SEN_ID_SENTENZA) );
		// lFasMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter( CAMPO_FAS_SIE_ID_FASCICOLO_SIEP)
		// );
		// STUB 15/12/2005.
		lFasMod.setDataIrrevocabilita(getRequestDateParameter(CAMPO_ANNO_DATA_IRREVOCABILITA,
				CAMPO_MESE_DATA_IRREVOCABILITA, CAMPO_GIORNO_DATA_IRREVOCABILITA));

		// inizio modifica marzo 2010
		lFasMod.setDataArrivoAtto(getRequestDateParameter(CAMPO_ANNO_ARRIVO_ATTO, CAMPO_MESE_ARRIVO_ATTO,
				CAMPO_GIORNO_ARRIVO_ATTO));
		// fine modifica marzo 2010
		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		lCtrl.ExModificaFascicoloSiep(lFasMod);
	}

	public String processRequest() throws Exception {

		lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		lFasMod = new FascicoloSiepModel();

		if (!this.isRequestParameterNullObj("oscuraEtichettaMinore")) {
			oscuraProcessRequest();
		} else {
			defaultProcessRequest();
		}

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
				+ lFasMod.getIdFascicoloSiep().toString();
	}

}