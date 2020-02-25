package siap.sius.udienza.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sius.SIUSException;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.magistratorelatore.controller.IMagistratoRelatore;
import siap.sius.magistratorelatore.model.MagistratoRelatoreModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.tenore.model.TenoreModel;
import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;
import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciFissazioneUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Fissazione Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 *
 * @version 1.0
 */
public class ActLoadInserisciOrdinanzaRinvioUdienza extends ActRicercaFSPuntuale implements ICostantiUdienza {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");
		//
		// CODICE RELATIVO AL MAGISTRATO RELATORE ED AGLI AVVOCATI
		//
		FascicoloGPModel lFasGPMod = null;
		MagistratoRelatoreModel lMagRel = null;
		String lFascSospeso = "NO";

		this.setLinkRitorno();
		if (this.isRequestParameterNullObj("ritorno")) {
			// Invoca la process Request della superclasse se provengo dal menu'.
			super.processRequest();
		}

		// Fascicolo Sius
		lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento definitorio
		// già emesso per il Fascicolo SIUS.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso una Ordiannza di Rinvio
		// Udienza.
		RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
		boolean lEsistenzaDoc = lRicerca.verificaEsistenzaProv();
		if (lEsistenzaDoc)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Per il procedimento indicato è già stato emesso un provvedimento. Non è consentito emettere un nuovo provvedimento");

		// Ricerca se esistono udienze per quel GP con stato F o S o N
		UdienzaProcedimentoModel lUdiMod = new UdienzaProcedimentoModel();
		IUdienzaProcedimento lCtrl = SIUSLookupRemote.getUdienzaProcedimentoRemote();
		lUdiMod = lCtrl.ExRicercaUdienzaProcedimentoByGenProFlagRinviata(
				lFasGPMod.getGeneraleProcedimentoModel().getIdGeneraleProcedimento(), "'F','S'");
		if (lUdiMod == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Rinvio Udienza non consentito. Per il procedimento non risulta fissata una precedente data udienza.");

		// Ricerca del Magistrato Relatore
		IMagistratoRelatore lMagCtrl = SIUSLookupRemote.getMagistratoRelatoreRemote();
		lMagRel = lMagCtrl
				.ExRicercaEstesaMagRelByFascicolo(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		setRequestAttribute("magistratorelatore", lMagRel);

		if (lRicerca.verificaEsistenzaSospensione())
			lFascSospeso = "SI";

		// Ricerca avvocati assegnati al fascicolo
		IAvvocato lAvvCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector lAvvocato = lAvvCtrl
				.ExRicercaAvvocatiByFascicoloNoError(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());

		setRequestAttribute("avvocato", lAvvocato);

		String lCodOggetti = new String();
		String lDescOggetti = new String();
		String lCodOggettoProc = new String();
		String lCodDettagli = new String(); // STUB 19/04/2004

		/*
		 * ISSUE MAC : descrizione_intervento Numero MAC : 20191108014 Autore : monica Data : 12/nov/2019
		 * Branch : MAC_20191108014
		 */
		String lAnnotazione = new String();
		// ***** FINE INTERVENTO MAC_20191108014 *****//

		// Preleva il cod Oggetto procedimento per poi passarlo alla Option
		lCodOggettoProc = lFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
		if (lCodOggettoProc == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Oggetto Procedimento assente !");

		setRequestAttribute("codContenuto", lCodOggettoProc);
		// Imposta Contenuto.

		/*
		 * ISSUE MAC : descrizione_intervento Numero MAC : 20191108014 Autore : monica Data : 12/nov/2019
		 * Branch : MAC_20191108014
		 */
		// Preleva le note
		lAnnotazione = lFasGPMod.getGeneraleProcedimentoModel().getAnnotazione();
		// Imposta Contenuto.
		setRequestAttribute("annotazione", lAnnotazione);
		// ***** FINE INTERVENTO MAC_20191108014 *****//

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		Collection lOggetti = null;

		// Dalla lista dei contenuti si elimina U004
		if (strCodTipoUfficio.equals("TDS"))
			lOggetti = DecodificheManager.getInstance().getOggettoProcedimentoTDS();
		else if (strCodTipoUfficio.equals("UDS"))
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U004");
		else if (strCodTipoUfficio.equals("TDSM"))
			lOggetti = DecodificheManager.getInstance().getOggettoProcedimentoTDSM();
		else if (strCodTipoUfficio.equals("UDSM"))
			lOggetti = DecodificheUtils.getDecodesWithoutCode(
					DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), "U004");
		else
			lOggetti = DecodificheUtils
					.getDecodesWithoutCode(DecodificheManager.getInstance().getOggettoProcedimento(), "U004");

		String lDescContenuto = DecodificheUtils.getDescbyCode(lOggetti, lCodOggettoProc);
		setRequestAttribute("contenuto", lDescContenuto);

		// STUB 21/04/2004 Caricamento Array di tenori.
		TenoreModel[] lTenori = lFasGPMod.getTenori();
		for (int i = 0; i < lTenori.length; i++) {
			if (lTenori[i] != null) {
				lCodOggetti += lTenori[i].getCodOggettoTenore() + "|";
				lDescOggetti += lTenori[i].getDescrOggettoTenore() + "\n";
				if (lFasGPMod.getTenori()[i].getCodDettaglioOggetto() != null
						&& lFasGPMod.getTenori()[i].getCodDettaglioOggetto().length() > 1) {
					lCodDettagli += lFasGPMod.getTenori()[i].getCodOggettoTenore()
							+ lFasGPMod.getTenori()[i].getCodDettaglioOggetto() + "|";
				}
			}
		}

		setRequestAttribute("codOggetti", lCodOggetti);
		setRequestAttribute("descOggetti", lDescOggetti);
		setRequestAttribute("codDettagli", lCodDettagli); // STUB 19/04/2004

		// Elenco contenuti.
		Option lOption = new Option();

		if (strCodTipoUfficio.equals("TDS"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDS(),
					lCodOggettoProc, 75);
		else if (strCodTipoUfficio.equals("UDS"))
			lOption = new Option(
					DecodificheUtils.getDecodesWithoutCode(
							DecodificheManager.getInstance().getOggettoProcedimentoUDS(), "U004"),
					lCodOggettoProc, 75);
		else if (strCodTipoUfficio.equals("TDSM"))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoTDSM(),
					lCodOggettoProc, 75);
		else if (strCodTipoUfficio.equals("UDSM"))
			lOption = new Option(
					DecodificheUtils.getDecodesWithoutCode(
							DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), "U004"),
					lCodOggettoProc, 75);
		else
			lOption = new Option(
					DecodificheUtils.getDecodesWithoutCode(
							DecodificheManager.getInstance().getOggettoProcedimento(), "U004"),
					lCodOggettoProc, 75);

		// Preleva elenco degli altri destinatari.
		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), 75);
		setRequestAttribute("tipoAutorita", lOption.toString());

		// Evento notifica Model.
		EventoNotificaModel lEve = new EventoNotificaModel();
		setRequestAttribute("evento", lEve);
		setRequestAttribute("modalita", "I");
		setRequestAttribute("datacameraconsiglio",
				lFasGPMod.getGeneraleProcedimentoModel().getDataCameraConsiglio());
		setRequestAttribute("fascSospeso", lFascSospeso);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return PG_LOAD_INSERISCIORDINANZARINVIOUDIENZA;
	}

}