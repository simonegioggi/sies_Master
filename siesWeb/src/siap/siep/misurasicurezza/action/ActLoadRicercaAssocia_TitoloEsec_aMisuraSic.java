package siap.siep.misurasicurezza.action;

import org.apache.log4j.Logger;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.action.ICostantiUfficio;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.rifasiep.action.ICostantiRifFascicoloSiep;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRicercaAssocia_TitoloEsec_aMisuraSic
 * </p>
 * <p>
 * Description: Classe Action per la load ricerca titoo esecutivo da associare a un M.S.
 * </p>
 *
 * Serve per Associare una Mis Sic di un fascicolo ad un Titolo Esecutivo (nel caso in cui la M.S. sia
 * arrivata nel fascicolo da un CUMULO)
 * 
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: Intersistemi Italia spa
 * </p>
 * 
 * @version 8.2
 */
@SuppressWarnings("rawtypes")
public class ActLoadRicercaAssocia_TitoloEsec_aMisuraSic extends ActionSiap implements
		ICostantiMisuraSicurezza, ICostantiSentenza, ICostantiFascicoloSiep {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	//private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

//		BigDecimal lFascID = null;
		String lStatoFasc = null;
//		String lFlagVal = null;

		BigDecimal lidMis = null;
		lidMis = getRequestBigDecimalParameter(ICostantiMisuraSicurezza.CAMPO_ID_MISURA_SICUREZZA);
		IMisuraSicurezza CtlrMis = SIEPLookupRemote.getMisuraSicurezzaRemote();
		MisuraSicurezzaModel lMisMod = null;
		lMisMod = CtlrMis.ExRicercaMisuraSicurezzaByKey(lidMis);
		setRequestAttribute("MisuraSic", lMisMod);

//		lFascID = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();
		lStatoFasc = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getCodStatoFascicolo();
//		lFlagVal = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getFlagValidato();
		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		// siesLogger.debug("-- ActLoadRicercaAssocia_TitoloEsec -ID_Fasc = "+lFascID+" -lStatoFasc = "+lStatoFasc+" -lFlagVal = "+lFlagVal);

		if (lStatoFasc.equals("01"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Fascicolo Archiviato/Definito. Impossibile effettuare una operazione di cumulo");

		FascicoloSiepModel lFasSiepMod = null;

		if (!isRequestParameterNullObj("ChiaveSiepAnno")) {
			// ============================================================================
			// Tasto Carica - Acquisizione Titolo esecutivo da stesso ufficio o distretto
			// ============================================================================
			BigDecimal lAnnoFas = getRequestBigDecimalParameter("ChiaveSiepAnno");
			BigDecimal lNumFas = getRequestBigDecimalParameter("ChiaveSiepProgressivo");
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			// siesLogger.debug("-- ActLoadRicercaAssocia_TitoloEsec -Seconda Volta dati da RICERCARE-lAnnoFas = "+lAnnoFas+" -lNumFas ="+lNumFas);

			if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)
					&& !"".equals(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO))) {
				BigDecimal lIncrementoAccorpato = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);
				lNumFas = lNumFas.add(lIncrementoAccorpato);
			}

			IUfficio lUff = SICOLookupRemote.getUfficioRemote();
			UfficioModel lUffMod = new UfficioModel();

			lUffMod = lUff.getUfficioByCodTipoUffDescrComune(
					getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP),
					getRequestStringParameter(ICostantiUfficio.CAMPO_SEDE_UFFICIO));

			lFasSiepMod = new FascicoloSiepModel();
			IFascicoloSiep lFasc = SIEPLookupRemote.getFascicoloSiepRemote();

			lFasSiepMod.setChiaveAnno(lAnnoFas);
			lFasSiepMod.setChiaveProgr(lNumFas);
			lFasSiepMod.setChiaveUfficio(lUffMod.getCodUfficio());

			lFasSiepMod = lFasc.ExRicercaFascicoloSiepByProgrAnnoCodUfficio(lFasSiepMod);

			if (lFasSiepMod != null && lFasSiepMod.getIdFascicoloSiep() != null)
				setRequestAttribute("aModelFas", lFasSiepMod);
			else
				throw new SIEPException(SIEPException.USER_MESSAGE, "Fascicolo SIEP " + lAnnoFas + "/"
						+ lNumFas + " Inesistente:<br> Impossibile effettuare l'operazione");
		}

		// Tribunale Iscrizione Fascicolo
		// MERGE v10: cambiata query di riferimento:
		// WINDOWS ordina: 1) Procura della Repubblica 2) Procura Generale
		// UNIX ordina: 1) Procura Generale 2) Procura della Repubblica
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice(), "-");
	    // 27-05-2016 - Riciclo dopo Primo Collaudo per V.10
	    //lOption.setFilter( new String[] {"PM","PMM","PMPT","PGCAP","PGCSS","PGMI","PGMID","PMI" } );
	    lOption.setFilter( new String[] {"PM","PMM","PGCAP"} );
	    // 27-05-2016 - END Riciclo
		// recupero dell'utente dalla sessione.
	    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
	    // seleziono ufficio dell'utente connesso
	    lOption.setSelected(lUtenteMod.getUfficioUtente().getCodTipoUfficio());
	    if (!isRequestParameterNullObj(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP)) {
	    	String codTipoUffFascSiep = getRequestStringParameter(ICostantiRifFascicoloSiep.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP);
		    if (!codTipoUffFascSiep.equals(lUtenteMod.getUfficioUtente().getCodTipoUfficio()))
		    	lOption.setSelected(codTipoUffFascSiep);
	    }
		setRequestAttribute("TipoTrib", "" + lOption);

		// Autorit� Emittente Titolo Esecutivo
		Option lOptionAut = new Option(DecodificheManager.getInstance().getAutoritaEmittente_Sorveglianza(),
				"-");
		if (lFasSiepMod != null && lFasSiepMod.getIdFascicoloSiep() != null
				&& lFasSiepMod.getSentenza() != null && lFasSiepMod.getSentenza().getIdSentenza() != null
				&& lFasSiepMod.getSentenza().getDescrTipoAutoritaEmittente() != null) {
			lOptionAut = new Option(DecodificheManager.getInstance().getAutoritaEmittente_Sorveglianza(),
					lFasSiepMod.getSentenza().getCodTipoAutoritaEmittente());
		}
		setRequestAttribute("autoritaEmi", "" + lOptionAut);

		// Tipo Provvedimento Sorv : Decreto / Ordinanza
		Option lOptionProvv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti(), "-");
		lOptionProvv.setFilter(new String[] { "01", "02", "03", "-" });
		if (lFasSiepMod != null && lFasSiepMod.getIdFascicoloSiep() != null
				&& lFasSiepMod.getSentenza() != null && lFasSiepMod.getSentenza().getIdSentenza() != null
				&& lFasSiepMod.getSentenza().getDescrTipoProvvedimento() != null) {
			lOptionProvv = new Option(DecodificheManager.getInstance().getTipoProvvedimenti(), lFasSiepMod
					.getSentenza().getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoProvvedimenti", "" + lOptionProvv);

		setRequestAttribute("LuogoUtenteConnesso", this.getUfficioUtenteConnesso().getDescrComune());

		// esegue la query per recuperare l'elenco degli uffici accorpati di tipo PM
		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAccTotali = lUACon.ListaUfficiAccorpati(null, null);
		setRequestAttribute("ufficiAccorpati", lUffAccTotali);

		return PG_LOAD_RICERCA_TITOLOESEC_DA_ASSOCIARE_A_MIS_SIC; // restituisce la jsp di VIEW
	}

}
