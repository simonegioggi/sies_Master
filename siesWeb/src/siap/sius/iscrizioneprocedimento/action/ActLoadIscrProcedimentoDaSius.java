package siap.sius.iscrizioneprocedimento.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.Utils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.controller.IDepositoDecreto; // 23/06/2005
import siap.sius.depositodecreto.model.DepositoDecretoModel; // 23/06/2005
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc; // 23/06/2005
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel; // 23/06/2005
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadIscrProcedimentoDaSius
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
public class ActLoadIscrProcedimentoDaSius extends ActionSiap implements ICostantiIscrProcedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		// Se l'Evento è stato individuato, nella request viaggia l'IdEventoInviato.
		BigDecimal lIdEvento;
		if (isSessionAttributeNullObj("IdEventoInviato"))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione Ricevuto Atto incompleto (manca l'EVENTO)!");
		else {
			lIdEvento = (BigDecimal) (getSessionAttribute("IdEventoInviato"));
			// this.removeSessionAttribute("IdEventoInviato");
		}
		// Leggo l'Evento legato all' IdEventoInviato.
		EventoModel lEvento = new EventoModel();
		IEvento lCtrlEv = SICOLookupRemote.getEventoRemote();
		lEvento = lCtrlEv.ExRicercaEventoByKey(lIdEvento);

		// STUB 23/06/2005 Leggo il Decreto o Ordinanza per correggere la presaincarico.
		if (lEvento.getCodTipoProvvedimento().compareTo("02") == 0) {
			DepositoDecretoModel lDecreto = new DepositoDecretoModel();
			IDepositoDecreto lCtrlDec = SIUSLookupRemote.getDepositoDecretoRemote();
			lDecreto = lCtrlDec.ExRicercaDepositoDecretoByIdEvento(lIdEvento);
			setRequestAttribute("tipoProvvedimento", "Decreto");
			setRequestAttribute("annoProvvedimento", lDecreto.getAnnoS72().toString());
			setRequestAttribute("progrProvvedimento", lDecreto.getNumS72().toString());
		}
		if (lEvento.getCodTipoProvvedimento().compareTo("03") == 0) {
			DepositoOrdinanzaPcModel lOrdinanza = new DepositoOrdinanzaPcModel();
			IDepositoOrdinanzaPc lCtrlOrd = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
			lOrdinanza = lCtrlOrd.ExRicercaDepositoOrdinanzaPcByEvento(lIdEvento);
			setRequestAttribute("tipoProvvedimento", "Ordinanza");
			setRequestAttribute("annoProvvedimento", lOrdinanza.getAnnoS3().toString());
			setRequestAttribute("progrProvvedimento", lOrdinanza.getNumS3().toString());
		}

		// Dall'utente connesso prelevo il codice ufficio.
		// UtenteModel lUtenteConnesso = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		// String StrCodiceUfficioUtente = lUtenteConnesso.getUfficioUtente().getCodUfficio();

		// Recupero del fascicolo Sius inviato.
		FascicoloGPModel lFasGPInviato = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// STUB 11/05/2004 si passa l'Id del fascicolo inviato nella request.
		String idFascicoloInviato = lFasGPInviato.getFascicoloSiusModel().getIdFascicoloSius().toString();
		setRequestAttribute("idFascicoloInviato", idFascicoloInviato);

		// Imposta Tipo Atto.
		// Option TipoProvvRicevuto = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		String lDescrProvvedimento = DecodificheUtils.getDescbyCode(
				DecodificheManager.getInstance().getTipoProvvedimenti(), lEvento.getCodTipoProvvedimento());
		String lCodTipoAtto = DecodificheUtils.getCodebyDesc(DecodificheManager.getInstance().getTipoAtto(),
				lDescrProvvedimento);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto(), lCodTipoAtto);
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		IUfficio lCtrlUf = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUfficioMittente = lCtrlUf.getUfficioByKey(lEvento.getCodUfficioEmittente());
		String lCodMittenteAtto = DecodificheUtils.getCodebyDesc(
				DecodificheManager.getInstance().getMittenteAtto(), lUfficioMittente.getDescrTipoUfficio());
		// lOption = new Option( DecodificheManager.getInstance().getMittenteAtto(),
		// lFasGPInviato.getGeneraleProcedimentoModel().getCodTipoMittenteAtto(), 36 );
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), lCodMittenteAtto, 36);
		setRequestAttribute("mittenteAtto", "" + lOption);
		setRequestAttribute("sedeMittenteAtto", lUfficioMittente.getDescrComune());
		setRequestAttribute("dataAtto", lEvento.getDataEmissione());

		// Imposta Contenuto.
		String strCodMotivo = Utils.isNullObj(lEvento.getCodMotivo()) ? "" : lEvento.getCodMotivo();

		if (strCodTipoUfficio.equals("TDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(), strCodMotivo,
					75);
		else if (strCodTipoUfficio.equals("UDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), strCodMotivo,
					75);
		else if (strCodTipoUfficio.equals("TDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(), strCodMotivo,
					75);
		else if (strCodTipoUfficio.equals("UDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), strCodMotivo,
					75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento(), strCodMotivo, 75);

		setRequestAttribute("contenuto", "" + lOption);

		// Imposta la Collection Contenuto.
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

		// Imposta Posizione Giuridica.
		lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridicaEsecuzione());
		setRequestAttribute("posizioneGiuridica", "" + lOption);

		// Imposta l'elenco magistrati.
		String lCodUfficio = getCodUfficioUtenteConnesso();
		IMagistrato lMagCtrl = SICOLookupRemote.getMagistratoRemote();
		lOption = new Option(lMagCtrl.ExElencoCbxMagistratiByCodUfficio(lCodUfficio));
		setRequestAttribute("magistrato", "" + lOption);

		// STUB 25/02/2004 Si seleziona la JSP e la modalita di iscrizione in base al tipo Ufficio (Tribunale
		// SIUS=IT, Tribunale SIUS senza F. SIEP=IS, Ufficio SIUS=IU);
		// MERGE v10: aggiunta casistica
		if ("UDS".equals(strCodTipoUfficio) || "UDSM".equals(strCodTipoUfficio)) {
			setRequestAttribute("modalita", "IU");
			return PG_LOAD_ISCRPROCEDIMENTOUDS;
		} else {
			setRequestAttribute("modalita", "IT");
			return PG_LOAD_ISCRPROCEDIMENTO;
		}
	}

}