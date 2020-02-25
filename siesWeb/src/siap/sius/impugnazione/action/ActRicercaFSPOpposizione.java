package siap.sius.impugnazione.action;

/**
* <p>Title: ActRicercaFSPOpposizione</p>
* <p>Description: Classe Action per la ricerca dei provvedimenti del fascicolo SIUS
*                 Su cui è possibile emettere opposizioni. Per tali provvedimenti
*                 recupera le OPPOSIZIONI se presenti</p>
* <p>Copyright: Copyright (c) 2003</p>

* @since 06/2014
*/

import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.impugnazione.model.ImpugnazioneModel;
import siap.sius.util.SIUSLookupRemote;

public class ActRicercaFSPOpposizione extends ActRicercaFSPuntuale implements ICostantiImpugnazione {

	@SuppressWarnings("unchecked")
	public String processRequest() throws Exception {

		super.mControl = false;
		super.processRequest();

		setLinkRitorno();

		// Recupero del FascicoloSiusGP. Se non in sessione solleva un errore di eccezione.
		if (isSessionAttributeNullObj("fascicoloSiusGP"))
			throw new SIUSException(SIUSException.USER_MESSAGE, "Procedimento non selezionato");

		// FascicoloGPModel fascicoloSiusGP = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

		// ==========================================================================
		// Recupera le Impugnazioni dell'ultimo evento (validato o meno) che presenta
		// un ricorso (flag_piu_meno = 'R')
		// n.b. solo dell'evento più recente che presenta un ricorso
		// ==========================================================================

		// Recupero dei provvedimenti di tipo_evento 01 (provvedimento) per il fascicolo.
		// La query restituisce tutto, anche gli eventi annullati
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		Vector<EventoModel> lVectEventi = mCtrl.ExRicercaEventoByFascicoloSius(
				super.mFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), COD_EVENTO_PROVVEDIMENTO);

		// ==========================================================================
		// Per ogni evento verifico se presenti OPPOSIZIONI
		// ==========================================================================
		Hashtable<BigDecimal, Vector<ImpugnazioneModel>> lOpposizioniPerEvento = new Hashtable<>();
		for (int i = 0; i < lVectEventi.size(); i++) {
			EventoModel lEvento = lVectEventi.elementAt(i);
			IImpugnazione lCtrlImp = SIUSLookupRemote.getImpugnazioneRemote();
			Vector<ImpugnazioneModel> lImpugnazioni = lCtrlImp
					.ExRicercaImpugnazioniByIdEventoTipoProvvTipoImpFlagAnn(lEvento.getIdEvento(),
							lEvento.getCodTipoProvvedimento(), new String[] { "04" }, null);
			lOpposizioniPerEvento.put(lEvento.getIdEvento(), lImpugnazioni);
			// lImpugnazioni.size().

			// new Integer(lImpugnazioni.size()).t
		}

		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		setRequestAttribute("provvedimenti", lVectEventi);
		setRequestAttribute("opposizioni", lOpposizioniPerEvento);

		return PG_ELENCO_PROVVEDIMENTI_OPP;
	}

}