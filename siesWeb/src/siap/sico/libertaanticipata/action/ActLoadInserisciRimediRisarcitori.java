package siap.sico.libertaanticipata.action;

/**
 * <p>Title: ActLoadInserisciRimediRisarcitori</p>
 * <p>Description: Azione Load inserimento/selezione Ordinanza/Decreto di 
 *    concessione Rimedi Risarcitori DL92/2014 </p>
 * @author d.f.
 * @since ott 2014
 */

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

//import java.util.Collection;

public class ActLoadInserisciRimediRisarcitori extends ActionSiap implements ICostantiLibertaAnticipata {
	public String processRequest() throws Exception {
		// =================================================
		// Controllo Presenza del Fascicolo in Sessione
		// =================================================
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// =================================================
		// Controllo Validazione Fascicolo
		// =================================================
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ==============================
		// Controllo Fascicolo definito
		// ==============================
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// =================================================
		// esistenza evento non validato
		// =================================================
		this.isEventoNonValidato();

		// =================================================
		// Posizione Giuridica
		// =================================================
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		setRequestAttribute("posizione", lPosizione);

		// =================================================
		// Pena Complessiva
		// =================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		// =================================================
		// Pena Residua
		// =================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = null;
		if (lPosizione.getCodPosizioneGiuridica() != null
				&& !lPosizione.getCodPosizioneGiuridica().equals("07")) // LIBERO PRIMA (07)
		{
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		} else { // Validata o meno
			lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		}

		if (lPenaResidua == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Reidua non presente. Impossibile eseguire la richiesta.");

		setRequestAttribute("penaresidua", lPenaResidua);

		// ==========================================================================
		// Si cerca la presenza di Ordinanze DL92 non elaborate per presentare
		// un messaggio di avviso all'utente in modo che le selezioni dalla lista
		// ==========================================================================
		// FIXME DL92 controllo presenza Ordinanze non computate. Da implementare
		// ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();
		// LicenzaLibAnticipataModel lModelLib = new LicenzaLibAnticipataModel();
		// List lLicenze =
		// lCtrlLib.ExRicercaLicenzaLibanticipataConcesseDepositateByIdFascicoloSIEP(lIdFascicolo, "NE");
		//
		// if( !lLicenze.isEmpty() )
		// {
		// // IL messaggio di Alert:
		// "Attenzione vi sono ordinanze di Liberazione Anticipata già caricate e non elaborate"
		// // non deve comparire se è stata emessa e validata una comunicazione per quell'ordinanza di LA
		// Iterator iter = lLicenze.iterator();
		// while (iter.hasNext())
		// {
		// LicenzaLibAnticipataModel item = (LicenzaLibAnticipataModel) iter.next();
		// if( !item.isConProvvedimentoValidato() )
		// {
		// setRequestAttribute("FlagLicenzeNonElaborate", "S");
		// }
		// }
		// }

		// ==========================================================================
		// Caricamento delle combo
		// ==========================================================================
		// Oggetti per il caricamento delle combo Tipo Provvedimento
		Option lOptionSorv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza(), "-");
		setRequestAttribute("tipoprovvedimento", "" + lOptionSorv);

		// Oggetti per il caricamento della combo Autorità Emittente
		Option lAutoritaSORV = new Option(DecodificheManager.getInstance().getTipoUfficio(), "-");
		lAutoritaSORV.setFilter(new String[] { "TDS", "UDS", "-" });
		setRequestAttribute("autorita", "" + lAutoritaSORV);

		// // AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		// Option lOption = new Option(DecodificheManager.getInstance().getListaAutoritaSospTDSUDS());
		// setRequestAttribute("autoritaemittente", "" + lOption);

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UfficioModel lUfficioMod = this.getUfficioUtenteConnesso();
		String lCodTipoUfficio = lUfficioMod.getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_RIMEDI_RISARCIORI;
	}

}