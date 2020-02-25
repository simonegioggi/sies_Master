package siap.siep.penaaccessoria.action;

import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.lock.model.LockModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciPenaAccessoria
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di PenaAccessoria
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
public class ActLoadModificaPenaAccessoria extends ActionSiap implements ICostantiPenaAccessoria {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		// Controllo che non si stia lavorando su una entità in modifica ad altri
		LockModel lck = lockIfNotLocked("pena accessoria",
				getRequestStringParameter(CAMPO_ID_PENA_ACCESSORIA), getCodUtenteConnesso());
		if (lck != null) {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"La " + lck.getEntity() + " è in gestione ad un altro utente! <BR>Riprovare più tardi!");
			return IWebConstants.PG_MESSAGE;
		}

		PenaAccessoriaModel lPenAcc = new PenaAccessoriaModel();

		lPenAcc.setIdPenaAccessoria(
				getRequestBigDecimalParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA));

		IPenaAccessoria lPenaCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		lPenAcc = lPenaCtrl.ExRicercaPenaAccessoriaByKey(lPenAcc);

		setRequestAttribute("penaaccessoria", lPenAcc);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoPeneAccessorie(),
				lPenAcc.getCodTipoPenaAccessoria());
		setRequestAttribute("TipoPenaAccessoria", "" + lOption);
		lOption = new Option(
				DecodificheUtils.getDecodesWithoutCode(
						DecodificheManager.getInstance().getTipoPeneAccessorie(), "999"),
				lPenAcc.getCodNuovoTipoPenaAccessoria());
		setRequestAttribute("TipoPenaAccessoriaNoAltre", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getDurataPeneAccessorie(), lPenAcc.getDurata());
		setRequestAttribute("DurataPeneAccessorie", "" + lOption);

		// 21/06/2010 Sostituzione Elenco Autorità Emittenti
		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(),
		// lPenAcc.getCodTipoUfficioSentenzaRevo());
		lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(),
				lPenAcc.getCodTipoUfficioSentenzaRevo());
		setRequestAttribute("autoritaSentenza", "" + lOption);

		// STUB 24/02/2006 Esclusione di "CSS" e "PM" da TipoUfficioS.
		Collection lColl = DecodificheUtils.getDecodesWithoutCodes(
				DecodificheManager.getInstance().getTipoUfficioS(), new String[] { "CSS", "PM" });
		Collection lColl2 = DecodificheUtils.getDecodesWithCodes(
				DecodificheManager.getInstance().getTipoUfficio(), new String[] { "GIPMI", "TMI" });
		/* boolean lBool = ( */lColl.addAll(lColl2)/* ) */;
		lOption = new Option(lColl, lPenAcc.getCodTipoUfficioOrdinanzaGE());
		setRequestAttribute("autoritaOrdinanza", "" + lOption);

		// STUB 23/03/2006 Costruzione opzioni "Tenore Ordinanza"
		Collection lColTenoreOrdinanza = null;
		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TENORE_ORDINANZA_PA");
		lColTenoreOrdinanza = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTenoreOrdinanza = new Option(lColTenoreOrdinanza, lPenAcc.getFlagCondonata(), 50);
		setRequestAttribute("tenoreOrdinanza", "" + lOptionTenoreOrdinanza);

		// Costruzione combo "Fonte Reato" e "Sottonumerazione"
		lOption = new Option(DecodificheManager.getInstance().getTipoFonteReato(), lPenAcc.getCodFonteGE());
		setRequestAttribute("TipiFontiReato", "" + lOption);
		lOption = new Option(DecodificheManager.getInstance().getSottonumerazione(),
				lPenAcc.getCodSottonumerazioneGE());
		setRequestAttribute("TipiSottonumerazione", "" + lOption);

		setRequestAttribute("modalita", "M");

		return PG_LOAD_INSERISCIPENAACCESSORIA; // restituisce la jsp di VIEW
	}
}
