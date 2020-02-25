package siap.sico.magistrato.util;

import java.util.ArrayList;
import java.util.Collection;

import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.util.SICOLookupRemote;
import f3b.util.F3BException;

/**
 * Classe Utilità per la Gestione Magistrati.
 * <p>
 * 
 * @author user
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class MagistratoUtils {

	/**
	 * Metodo che ritorna l'elenco delle sezioni da visualizzare nelle combo.
	 * <p>
	 * 
	 * @return Collection
	 * @throws F3BException
	 */
	public static Collection getElencoMagistratiPerRicercaSige(String aCodUfficio) throws F3BException {

		IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
		ArrayList lColl = new ArrayList(lCtrl.ExElencoCbxMagistratiByCodUfficio(aCodUfficio));
		Collection lCollRet = new ArrayList();
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setCode("Tutti");
		lDecMod.setDescription("Tutti i magistrati");
		lCollRet.add(lDecMod);
		lDecMod = new DecodificheModel();
		lDecMod.setCode("Nessuno");
		lDecMod.setDescription("Magistrato non presente");
		lCollRet.add(lDecMod);
		lCollRet.addAll(lColl);
		return lCollRet;
	}

}