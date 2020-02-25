package siap.sige.sezione.util;

import java.util.ArrayList;
import java.util.Collection;

import siap.sico.decodifiche.model.DecodificheModel;
import siap.sige.sezione.controller.ISezione;
import siap.sige.util.SIGELookupRemote;
import f3b.util.F3BException;

/**
 * Classe Utilità per la Gestione Sezioni.
 * <p>
 * 
 * @author user
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class SezioneUtils {

	/**
	 * Metodo che ritorna l'elenco delle sezioni da visualizzare nelle combo.
	 * <p>
	 * 
	 * @return Collection
	 * @throws F3BException
	 */
	public static Collection getElencoSezioni(String aCodUfficio) throws F3BException {
		ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
		Collection lColl = lCtrl.ExElencoCbxSezioniByCodUfficio(aCodUfficio);
		return lColl;
	}

	public static Collection getElencoCodiciSezioni() {
		Collection lColl = new ArrayList();
		for (int i = 1; i <= 25; i++) {
			DecodificheModel lDecMod = new DecodificheModel();
			lDecMod.setCode("" + i);
			lDecMod.setDescription("" + i);
			lColl.add(lDecMod);
		}
		return lColl;
	}

	public static Collection getElencoSezioniPerRicerca(String aCodUfficio) throws F3BException {
		ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
		ArrayList lColl = new ArrayList(lCtrl.ExElencoCbxSezioniByCodUfficio(aCodUfficio));
		Collection lCollRet = new ArrayList();
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setCode("Tutte");
		lDecMod.setDescription("Tutte le sezioni");
		lCollRet.add(lDecMod);
		lDecMod = new DecodificheModel();
		lDecMod.setCode("Nessuna");
		lDecMod.setDescription("Sezione non presente");
		lCollRet.add(lDecMod);
		lCollRet.addAll(lColl);
		return lCollRet;
	}

	/**
	 * 20171013: [EC] aggiungo metodo per recuperare le sezioni per codice magistrato ed ufficio appartenenza
	 * 
	 * @param codiceMagistrato
	 * @param codUfficioUtenteConnesso
	 * @return
	 * @throws F3BException
	 */
	public static Collection getElencoSezioniModificabiliBycodufficio(String codiceMagistrato,
			String codUfficioUtenteConnesso) throws F3BException {
		ISezione lCtrl = SIGELookupRemote.getSezioneRemote();
		Collection lColl = lCtrl.getElencoSezioniModificabiliByCodUfficio(codiceMagistrato,
				codUfficioUtenteConnesso);
		return lColl;
	}

}