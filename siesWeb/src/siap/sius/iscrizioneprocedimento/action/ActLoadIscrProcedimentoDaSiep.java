package siap.sius.iscrizioneprocedimento.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadIscrProcedimentoDaSiep
 * </p>
 * <p>
 * Description: Classe Action per il caricamento della form di Iscrizione Procedimento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActLoadIscrProcedimentoDaSiep extends ActionSiap implements ICostantiIscrProcedimento {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Se l'Evento è stato individuato, nella request viaggia l'IdEventoInviato.
		BigDecimal lIdEvento;

		if (isSessionAttributeNullObj("IdEventoInviato"))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione Ricevuto Atto incompleto (manca l'EVENTO)!");
		else {
			lIdEvento = (BigDecimal) (getSessionAttribute("IdEventoInviato"));
		}

		// Leggo l'Evento legato all' IdEventoInviato.
		EventoModel lEvento = new EventoModel();
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		lEvento = lCtrlEv.ExRicercaEventoByKey(lIdEvento);

		// Se il Fascicolo SIEP Origine non è stato messo in sessione, non proseguo.
		if (isSessionAttributeNullObj("fascicolo"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento SIEP origine non selezionato!");

		// Dettaglio Fascicolo SIEP.
		FascicoloSiepModel fascicolo = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		setRequestAttribute("fascicolo", getSessionAttribute("fascicolo"));

		// Dall'utente connesso prelevo il codice ufficio.
		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(
				ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		String StrCodiceUfficioUtente = lUtenteConnesso.getUfficioUtente().getCodUfficio();

		// Istanzio il Model del nuovo fascicolo e valorizzo il codice ufficio.
		FascicoloGPModel lFasGP = new FascicoloGPModel();
		lFasGP.getFascicoloSiusModel().setChiaveUfficio(StrCodiceUfficioUtente);

		// Imposta Tipo Ufficio.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSius());
		setRequestAttribute("tipoUfficioSius", "" + lOption);

		// Imposta Tipo Atto.
		if (lEvento != null && lEvento.getCodTipoEvento() != null) {
			String lDescrAtto = DecodificheUtils.getDescbyCode(
					DecodificheManager.getInstance().getTipoEvento(), lEvento.getCodTipoEvento());
			String lCodTipoAtto = DecodificheUtils
					.getCodebyDesc(DecodificheManager.getInstance().getTipoAtto(), lDescrAtto);
			lOption = new Option(DecodificheManager.getInstance().getTipoAtto(), lCodTipoAtto);
		} else
			lOption = new Option(DecodificheManager.getInstance().getTipoAtto());

		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		if (lEvento != null && lEvento.getCodUfficioEmittente() != null) {
			IUfficio lCtrlUf = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUfficioMittente = lCtrlUf.getUfficioByKey(lEvento.getCodUfficioEmittente());
			String lCodMittenteAtto = DecodificheUtils.getCodebyDesc(
					DecodificheManager.getInstance().getMittenteAtto(),
					lUfficioMittente.getDescrTipoUfficio());
			lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), lCodMittenteAtto, 36);
			setRequestAttribute("sedeMittenteAtto", lUfficioMittente.getDescrComune());
		} else {
			lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 36);
			setRequestAttribute("sedeMittenteAtto", "");
		}
		setRequestAttribute("mittenteAtto", "" + lOption);

		if (lEvento != null && lEvento.getDataEmissione() != null)
			setRequestAttribute("dataAtto", lEvento.getDataEmissione());
		else
			setRequestAttribute("dataAtto", DateUtils.getSysDate());

		// Imposta Contenuto.
		lOption = new Option();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		if (strCodTipoUfficio.equals("TDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(), 75);
		else if (strCodTipoUfficio.equals("UDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
		else if (strCodTipoUfficio.equals("TDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), 75);
		else if (strCodTipoUfficio.equals("UDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		// 19/02/2008 Imposta la Collection Contenuto.
		// MEV_66: distinguo per ufficio minorile
		Collection lCol;
		if ("UDSM".equals(strCodTipoUfficio))
			lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDSM();
		else
			lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDS();
		// FINE MEV_66
		setRequestAttribute("collContenuto", lCol);

		// Imposta Oggetto.
		// lOption = new Option( DecodificheManager.getInstance().getMotivoProvvedimento(), 75);
		// setRequestAttribute("oggetto", "" + lOption );

		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();

		DettaglioFascicoloModel lDettaglio = null;

		lDettaglio = lCtrl.ExDettaglioFascicoloSiep(fascicolo.getIdFascicoloSiep());

		if (lDettaglio == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Fascicolo non presente");

		// Valorizzo la data fine pena e la posizione giuridica in 2 variabili poste nella request.
		Date dataFinePena = null;
		String posGiuridica = "-";

		if ((!Utils.isNullObj(lDettaglio.getPenaResidua()))
				&& (!Utils.isNullObj(lDettaglio.getPenaResidua().getDataFine()))
				&& (!Utils.isNullObj(lDettaglio.getPenaResidua().getFlagValidato()))
				&& lDettaglio.getPenaResidua().getFlagValidato().equals("S"))
			dataFinePena = lDettaglio.getPenaResidua().getDataFine();

		setRequestAttribute("dataFinePena", dataFinePena);

		// Imposta la posizione giuridica.
		// la Collection lPosizioneGiuridica viene composta dai 3 gruppi distinti di P.G.
		// 5/7/2006 - Modifica per evitare l'imbroglio delle combo della posizione giuridica
		// Messo new al posto di una eguaglianza tra reference....
		Collection lCollPosGiuIscrizione = DecodificheManager.getInstance().getPosizioneGiuridicaIscrizione();
		Collection lCollPosGiuEsecuzione = DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione();
		Collection lCollPosGiuAltra = DecodificheManager.getInstance().getPosizioneGiuridicaAltraCausa();
		ArrayList lPosizioneGiuridica = new ArrayList(lCollPosGiuIscrizione);
		lPosizioneGiuridica.addAll(lCollPosGiuEsecuzione);
		lPosizioneGiuridica.addAll(lCollPosGiuAltra);

		if ((lDettaglio.getPosizioneGiuridica() != null)) {
			posGiuridica = lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica();
			lOption = new Option(lPosizioneGiuridica,
					lDettaglio.getPosizioneGiuridica().getCodPosizioneGiuridica(), 66);
		} else
			lOption = new Option(lPosizioneGiuridica, 66);

		setRequestAttribute("posGiuridica", posGiuridica);

		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		setRequestAttribute("magistrato", "" + lOption);

		// STUB 09/01/2004 Imposta il luogo detenzione.
		// Attenzione: se il luogo detenzione è per la causa attuale, viene valorizzato idLuogoDetenzione;
		// se il luogo detenzione è per altra causa, viene valorizzato idAltraCausa.
		String luogoDetenzione = "";
		String idLuogoDetenzione = "";
		String idAltraCausa = "";
		if ((lDettaglio.getLuogoDetenzione() != null)
				&& (lDettaglio.getLuogoDetenzione().getIstDetIdIstitutoDetenzione() != null)) {
			if (lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione() != null)
				luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrizione();
			else
				luogoDetenzione = lDettaglio.getLuogoDetenzione().getIstitutoDetenzione()
						.getDescrTipoIstituto() + " - "
						+ lDettaglio.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune();

			idLuogoDetenzione = lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione().toString();
		}

		else if ((lDettaglio.getAltraCausa() != null)
				&& lDettaglio.getAltraCausa().getIstDetIdIstitutoDetenzione() != null) {
			if (lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione() != null)
				luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrizione();
			else
				luogoDetenzione = lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrTipoIstituto()
						+ " - " + lDettaglio.getAltraCausa().getIstitutoDetenzione().getDescrComune();

			idAltraCausa = lDettaglio.getAltraCausa().getIdAltraCausa().toString();
			// STUB 11/10/2004 (commentato il) idLuogoDetenzione =
			// lDettaglio.getLuogoDetenzione().getIdLuogoDetenzione().toString();
		}
		setRequestAttribute("luogoDetenzione", luogoDetenzione);
		setRequestAttribute("idLuogoDetenzione", idLuogoDetenzione);
		setRequestAttribute("idAltraCausa", idAltraCausa);

		// Distinguo l'origine del procedimento (SIEP=E, SIUS=U); quindi IE=Inserimento da Siep.
		setRequestAttribute("modalita", "IE");

		// STUB 01/03/2005 Si seleziona la JSP e la modalita di iscrizione in base al tipo Ufficio (Tribunale
		// SIUS=IT, Ufficio SIUS=IU);
		// MERGE v10: aggiunta casistica
		if ("UDS".equals(strCodTipoUfficio) || "UDSM".equals(strCodTipoUfficio))
			return PG_LOAD_ISCRPROCEDIMENTOUDS;
		else
			return PG_LOAD_ISCRPROCEDIMENTO;
	}

}