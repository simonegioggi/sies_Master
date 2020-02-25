package siap.sius.produzioneatti.action;

/**
 * <p>Title: ActLoadRichiestaParere</p>
 * <p>Description: Classe Action per la load di RichiestaCarichiPendenti</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.html.Option;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadRichiestaParere extends ActRicercaFSPuntuale implements ICostantiProduzioneAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	protected FascicoloGPModel mFasGPMod = null;

	public String processRequest() throws Exception {
		// Passando il parametro noQuery non effettua nuovamente la ricerca
		// if( isRequestParameterNullObj( "noQuery") )

		super.processRequest();

		UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
				.getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

		// 25/09/2006 Si Imposta l'Autorita Competente.
		// MERGE v10: cambiata query di riferimento:
		// WINDOWS ordina: 1) PM 2) PMM 3) PGCAP
		// UNIX ordina: 1) PGCAP 2) PM 3) PMM
		Collection<?> lCol = (DecodificheManager.getInstance()).getTipoUfficioPerCodice();
		Option lOption = new Option(lCol);
		lOption.setFilter(new String[] { "PM", "PGCAP", "PMM" }); // solo le Autorità competenti.

		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("UDS")) {
			lOption.setSelected("PM");
			setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "PM"));
		}
		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("TDS")) {
			lOption.setSelected("PGCAP");
			setRequestAttribute("descTipoUfficioS", DecodificheUtils.getDescbyCode(lCol, "PGCAP"));
		}

		String filtroMinorenni = super.getFiltroMinorenni();
		setRequestAttribute("filtroMinorenni", filtroMinorenni);
		setRequestAttribute("codTipoUfficioS", "" + lOption);
		/*
		 * // Decodifica PM Collection lCol = (DecodificheManager.getInstance()).getTipoUfficio();
		 * if(getUfficioUtenteConnesso().getCodTipoUfficio().equals("UDS")) {
		 * setRequestAttribute("codTipoUfficioS","PM"); setRequestAttribute("descTipoUfficioS",
		 * DecodificheUtils.getDescbyCode(lCol,"PM")); }
		 * 
		 * if(getUfficioUtenteConnesso().getCodTipoUfficio().equals("TDS")) {
		 * setRequestAttribute("codTipoUfficioS","PGCAP"); setRequestAttribute("descTipoUfficioS",
		 * DecodificheUtils.getDescbyCode(lCol,"PGCAP")); }
		 */

		Collection<?> lColMotivoParere = null;
		DecodificheModel lModel = new DecodificheModel();

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		lModel.setFiltro("PARERE");

		lColMotivoParere = lDecodifiche.ExRicercaDecodifiche(lModel);

		lOption = new Option(lColMotivoParere, 35);

		if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("TDS"))
			lOption = new Option(lColMotivoParere, "0750", 35);

		// Inserimento filtro per Remissione Debito (non ci sono "Osservazioni per Grazia")
		if ((((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getGeneraleProcedimentoModel()
				.getCodOggettoProcedimento().compareTo("U011")) == 0)
			lOption.setFilter(new String[] { "0750", "0751" });

		setRequestAttribute("colMotivoParere", "" + lOption);

		// 28.11.2008 selezione motivi inammissibilità x RV_ABBREVIATION=CPP IN CASO DI PROCEDIMENTO PENE PECUNIARIE
		// Istanzio il Model
		FascicoloGPModel lFasGP = new FascicoloGPModel();

		String StrCodiceUfficioUtente = lUtenteConnesso.getUfficioUtente().getCodUfficio();
		// setRequestAttribute("CodiceUfficioUtente", StrCodiceUfficioUtente);

		// lFasGP.getFascicoloSiusModel().setChiaveUfficio(StrCodiceUfficioUtente);

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO))
			lFasGP.getFascicoloSiusModel().setChiaveAnno(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR))
			lFasGP.getFascicoloSiusModel().setChiaveProgr(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR));

		// FascicoloSiepController lCtrl = new FascicoloSiepController();
		IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

		// STUB 07/06/2004 Sdoppio la chiamata
		if (this.mControl)
			mFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente);
		else
			mFasGPMod = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficioNoControl(
					getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
					getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente);

		if ((mFasGPMod != null && mFasGPMod.getGeneraleProcedimentoModel() != null)
				&& (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento()
						.compareTo(ICostantiDepositoOrdinanzaPc.OGG_CONV_PENE_PECUNIARIE) == 0)) {
			if (getUfficioUtenteConnesso().getCodTipoUfficio().equals("UDS"))
				listaMotiviCPP();
		} else {
			listaMotivi();
		}

		return PG_LOAD_RICHIESTAPARERE; // restituisce la jsp di VIEW
	}

	private void listaMotivi() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: inizio");
		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("tipo ufficio ->" + lTipoUff);

		// Chiamata Controller per la ricerca delle Decodifiche.
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilita(lTipoUff));
		// Se si tratta di un procedimento di Remissione Debito, si aggiungono i
		// nuovi motivi di inammissibilità
		if ((mFasGPMod != null) && (mFasGPMod.getGeneraleProcedimentoModel() != null)
				&& (mFasGPMod.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo("U011") == 0)) {
			Vector lVectRd = new Vector(lDecCtrl.ExListaMotiviInammissibilitaRD(lTipoUff));
			lVect.addAll(lVectRd);
		}
		// Fine aggiunta motivi di inammissibilità per Remissione Debito

		// MEV10-s3: per i minori elimino un elemento dalla lista vettoriale
		if ("TDSM".equals(getUfficioUtenteConnesso().getCodTipoUfficio()) ||
				"UDSM".equals(getUfficioUtenteConnesso().getCodTipoUfficio())) {
			Iterator it = lVect.iterator();
			while (it.hasNext()) {
				DecodificheModel dm = (DecodificheModel) it.next();
				if ("13".equals(dm.getCode()))
					it.remove();
			}
		}

		setRequestAttribute("motivi", lVect);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotivi: fine");
	}

	private void listaMotiviCPP() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotiviCPP: inizio");
		String lTipoUff = getUfficioUtenteConnesso().getCodTipoUfficio().toUpperCase();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("tipo ufficio ->" + lTipoUff);

		// Chiamata Controller per la ricerca delle Decodifiche.
		IDecodifiche lDecCtrl = SICOLookupRemote.getDecodificheRemote();
		Vector lVect = new Vector(lDecCtrl.ExListaMotiviInammissibilitaCPP(lTipoUff));
		setRequestAttribute("motivi", lVect);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".listaMotiviCPP: fine");
	}

}