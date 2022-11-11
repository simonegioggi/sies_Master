package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.util.FascicoloSigeUtils;
import siap.sige.magistratoassegnatario.model.MagistratoAssegnatarioModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciRinvioUdienza
 * </p>
 * <p>
 * Description: Classe Action per la load della form di inserimento Ordinanza Rinvio Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 *
 * @version 1.0
 */
public class ActLoadInserisciRinvioUdienza extends ActRicercaFSigePuntuale
		implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected FascicoloSigeEstesoModel mFasEsteso;
	// protected String mRetPage = PG_LOAD_INSERISCIORDINANZARINVIOUDIENZA;
	protected String mRetPage = "";

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		// Gestione bottone di ritorno.
		super.setLinkRitorno();

		if (isRequestParameterNullObj("ritorno"))
			super.processRequest();

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = getFascicoloSigeEstesoInSessione();
		BigDecimal idFascicoloSige = mFasEsteso.getFascicoloSige().getIdFascicoloSige();

		// Viene effettuato il controllo sulla preesistenza di un Provvedimento definitorio
		// già emesso per il Fascicolo SIGE.
		// Se esiste almeno un provvedimento di questo tipo non può esserne emesso una Ordinanza
		// di Rinvio Udienza.
		if (super.esisteProvvedimentoDefinitorio()) {
			// Ticket#20211129018 - Rinvio udienza Sige: aggiunto metodo
			// si puo' emettere nuovo provvedimento se lo stato del fascicolo è 14,16,20,21
			if (!super.contolloStatoFascicoloConProvvedimentoDefinitorio()) {
				siesLogger.debug(
						"Posso iscrivere il provvedimento poiche' lo stato del fascicolo e' in uno di questi 4:"
								+ " Opposizione - Accoglie (fissa l'udienza) = 14;"
								+ " Ricorso convertito in opposizione = 16;"
								+ " Decreto Fissazione Udienza = 20;"
								+ " Ricorso convertito in opposizione (Fissa Udienza) = 21.");
			} else {
				throw new SIGEException(SIGEException.USER_MESSAGE,
						"Per il procedimento indicato è già stato emesso un Provvedimento di tipo definitorio! "
								+ "Non è consentito emettere un nuovo provvedimento.");
			}
		}

		// Ricerca esistenza di udienze per quel Fascicolo con stato F o S o N
		UdienzaProcedimentoSigeModel lUdiMod = new UdienzaProcedimentoSigeModel();
		IUdienzaProcedimentoSige lUdiProcCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		lUdiMod = lUdiProcCtrl
				.ExRicercaUltimaUdienzaProcedimentoSigeByFascicoloByFlagRinviata(idFascicoloSige, "'F','S'");

		if (lUdiMod == null)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Rinvio Udienza non consentito. "
					+ "Per il procedimento non risulta fissata una precedente data udienza.");

		// Lock per evitare il rinvio ordinanza contemporanea di 2 Udienze per lo stesso fascicolo
		LockModel lck = LockController.lockIfNotLocked(getServletContext(), "ProcedimentoSIGE",
				idFascicoloSige.toString(), getCodUtenteConnesso(), getSession().getId());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Il " + lck.getEntity() + " è in gestione ad un altro utente!<BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		// Solo la prima volta vengono messi i Tenori in sessione
		if (isRequestParameterNullObj(IWebConstants.FLAG_RITORNO) ||
		// 20190517 [SG]: aggiunta or condition
				isSessionAttributeNullObj("tenori")) {
			// Ricerca tenori presenti.
			ITenoreSige lCtrl = SIGELookupRemote.getTenoreSigeRemote();
			TenoreSigeModel aTenore = new TenoreSigeModel();
			aTenore.setFasIdFascicoloSige(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
			Vector lTenoriEstesi = lCtrl.ExRicercaTenoriEstesiAttivi(aTenore);
			// Imposta sessione e Request.
			setSessionAttribute("tenori", lTenoriEstesi);
			setRequestAttribute("tenori", lTenoriEstesi);
		}

		// Avvocati attuali assegnati al fascicolo SIGE.
		FascicoloSigeUtils lFasSigeUtils = new FascicoloSigeUtils();
		Vector lAvvocati = lFasSigeUtils.ricercaAvvocati(mFasEsteso.getFascicoloSige().getIdFascicoloSige());
		if (lAvvocati.size() > 0)
			setRequestAttribute("avvocato", lAvvocati);

		// Magistrato Assegnatario.
		MagistratoAssegnatarioModel lMagAss = mFasEsteso.getMagAssegnatario();
		setRequestAttribute("magistratoassegnatario", lMagAss);

		// Carica Combo x TipoGiudizio.
		String lTipoGiudizio = (mFasEsteso.getFascicoloSige().getCodTipoGiudizio() == null ? "-"
				: mFasEsteso.getFascicoloSige().getCodTipoGiudizio().trim());

		Option lOptionGiudizio = new Option(DecodificheManager.getInstance().getTipoGiudizioSige(),
				lTipoGiudizio, Option.BLANK_ITEM);
		super.setRequestAttribute("giudizio", lTipoGiudizio);

		// Imposta il codice del tipo di giudizio.
		// if (lCodTipoUfficio.compareTo("GIP")==0)
		// lOptionGiudizio.setFilter("M"); // Monocratica.
		// else if ("CASAP_CAS_CAP".indexOf(lCodTipoUfficio)>=0)
		// lOptionGiudizio.setFilter("C"); // Collegiale.

		// Codice Tipo Ufficio dell'utente connesso.
		// String lCodTipoUfficioConnesso = getUfficioUtenteConnesso().getCodTipoUfficio();

		/*
		 * if (filterColl.contains(lCodTipoUfficioConnesso)) { // solo Rito Collegiale lOptionGiudizio = new
		 * Option(DecodificheManager.getInstance().getTipoGiudizioSige(), "C");
		 * lOptionGiudizio.setFilter("C"); //super.setRequestAttribute("giudizio", "C"); } else if
		 * (filterMono.contains(lCodTipoUfficioConnesso)) { // solo Rito Monocratico lOptionGiudizio = new
		 * Option(DecodificheManager.getInstance().getTipoGiudizioSige(), "M");
		 * lOptionGiudizio.setFilter("M"); super.setRequestAttribute("giudizio", "M"); }
		 */

		lOptionGiudizio.setValueBlankItem("-");
		lOptionGiudizio.setSelected(lTipoGiudizio);
		setRequestAttribute("tipoGiudizio", lOptionGiudizio.toString());

		// Evento notifica Model.
		EventoNotificaModel lEve = new EventoNotificaModel();
		setRequestAttribute("evento", lEve);
		setRequestAttribute("modalita", "I");

		// Verifica ed imposta l'id dell'udienza inserita
		// in caso di ritorno da funzione d'inserimento udienza.
		String lIdUdienzaSige = null;
		if (!isRequestParameterNullObj("IdUdienzaSige")) {
			lIdUdienzaSige = getParameter("IdUdienzaSige");
			setRequestAttribute("UdienzaSige", getUdienzaSige(lIdUdienzaSige));
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return mRetPage;
	}

}