package siap.sius.produzioneatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.motivazionedecreto.controller.IMotivazioneDecreto;
import siap.sius.motivazionedecreto.model.MotivazioneDecretoModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActLoadDettaglioRichiestaParere
 * </p>
 * <p>
 * Description: Classe Responsabile della visualizzazione dati di dettaglio di un evento generato dalla
 * funzionalità della richiesta parere.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadDettaglioRichiestaParere extends ActionSius implements ICostantiProduzioneAtti {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: inizio ");

		// Se viene passati ID Fascicolo si risale al Fascicolo e lo si mette in sessione
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPMod = lCtrl.ExRicercaFascicoloByKey(
					this.getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ID Fascicolo SIUS : " + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		}

		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Chiama il controller.
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNot = lCtrl.ExRicercaEventoNotificaByKeyForRichiestaAtti(lIdEvento);
		if (lEveNot == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Dato Assente !");

		setLinkRitorno();
		// Chiama il controller delle Motivazioni Decreto
		IMotivazioneDecreto lMotivCtrl = SIUSLookupRemote.getMotivazioneDecretoRemote();
		Vector lVectMotiv = lMotivCtrl.ExRicercaMotivazioniDecretoInammissibilitaByEve(lIdEvento);
		Vector lVectMotivConv = new Vector();

		// Gestione del carattere € da passare alla jsp (al momento solo per Remissione Debito)
		if ((lVectMotiv != null) && (lVectMotiv.size() > 0)) {
			Iterator itx = lVectMotiv.iterator();
			while (itx.hasNext()) {
				MotivazioneDecretoModel mDecMod = (MotivazioneDecretoModel) itx.next();
				mDecMod.setDescrMotivazione(mDecMod.getDescrMotivazione().replace("€", "&#8364;"));
				lVectMotivConv.add(mDecMod);
			}
		}

		// setRequestAttribute( "motivazioniDecreto", lVectMotiv );
		setRequestAttribute("motivazioniDecreto", lVectMotivConv);
		setRequestAttribute("eventoNotifica", lEveNot);

		Collection lColMot = (DecodificheManager.getInstance()).getMotivoProvvedimento();
		setRequestAttribute("codMotivo",
				DecodificheUtils.getDescbyCode(lColMot, lEveNot.getEvento().getCodMotivo()));

		// Modificabilità
		String lModificabile = "NO";
		// String lStampabile = "NO";
		if (IsFascicoloSiusModificabile()) {
			// Stampabilità
			if (lEveNot.getEvento().getFlagDocumentoRegistrato() == null
					|| lEveNot.getEvento().getFlagDocumentoRegistrato().compareTo("N") == 0) {
				// Modificabilità e Stampabilità coincidono
				// lStampabile = "SI";
				lModificabile = "SI";
			}
		}
		setRequestAttribute("Modificabile", lModificabile);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("" + getClass().getName() + " .processRequest: fine ");

		return PG_DETTAGLIO_RICHIESTAPARERE;
	}

}