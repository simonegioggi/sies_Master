package siap.sius.provvedimento.action;

import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActRicercaFSPNotifica
 * </p>
 * <p>
 * Description: Classe Action per la ricerca puntuale del Fascicolo SIUS finalizzata alla notifica
 * provvedimenti
 * </p>
 * Viene effettuata la ricerca del Fascicolo SIUS e dei provvedimenti ad essi collegati.
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaFSPNotifica extends ActRicercaFSPuntuale implements ICostantiFascicoloSius,
		ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		if (isRequestParameterNullObj("noQuery")) {
			// 07/06/2004 Aggiunta valorizzazione mControl = false per evitare i controlli sullo stato
			// procedimento.
			mControl = false;
			super.processRequest();
		}
		// Recupero del FascicoloSiusGP. Se non in sessione solleva un errore di eccezione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		String lmodificabile; // modificabilità delle date di notifica
//		String StrCodiceUfficioUtente = super.getCodUfficioUtenteConnesso();
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// FascicoloGPModel lFasGPMod ; // Fascicolo SIUS

		/*
		 * if (isRequestParameterNullObj("noQuery")) { if( isRequestParameterNullObj( CAMPO_CHIAVE_ANNO ) )
		 * throw new SIUSException (SIUSException.USER_MESSAGE,
		 * "ERRORE : CAMPO_CHIAVE_ANNO  non valorizzato ");
		 * 
		 * if( isRequestParameterNullObj( CAMPO_CHIAVE_PROGR ) ) throw new SIUSException
		 * (SIUSException.USER_MESSAGE, "ERRORE : CAMPO_CHIAVE_PROGR  non valorizzato ");
		 * 
		 * IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote(); lFasGPMod =
		 * lCtrl.ExRicercaFascicoloNoFiltro(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO),
		 * getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), StrCodiceUfficioUtente );
		 * 
		 * //Metto in sessione il fascicolo SIUS per consentire le funzionalità annesse.
		 * //setRequestAttribute("fascicoloSiusGP", lFasGPMod); setSessionAttribute("fascicoloSiusGP",
		 * lFasGPMod); } else lFasGPMod = (FascicoloGPModel ) getSessionAttribute("fascicoloSiusGP");
		 * 
		 * // return PG_DETTAGLIOFASCICOLOSIUS;
		 */
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();

		// Vector lVect =
		// mCtrl.ExRicercaEventoByFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS),COD_EVENTO_PROVVEDIMENTO);
		Vector lVect = mCtrl.ExRicercaEventoByFascicoloSius(lFasGPMod.getFascicoloSiusModel()
				.getIdFascicoloSius(), COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);
		setRequestAttribute("flag_valida", "NO");

		if (lFasGPMod.getFascicoloSiusModel().getCodStatoFascicolo().equals(FASCICOLO_ARCHIVIATO))
			lmodificabile = "NO";
		else {
			// Si controlla la presenza dello scadenzario per definire se modificabili
			IScadenzarioSius lCtrlSc = SIUSLookupRemote.getScadenzarioRemote();
			if (lCtrlSc.ExScadutoScadenzarioSiusByIdFascicoloTipo(lFasGPMod.getFascicoloSiusModel()
					.getIdFascicoloSius(), SCADENZARIO_IRREVOCABILITA))
				lmodificabile = "NO";
			else
				lmodificabile = "SI";
		}
		setRequestAttribute("modificabile", lmodificabile);

		return PG_ELENCOPROVVEDIMENTI;
	}

}