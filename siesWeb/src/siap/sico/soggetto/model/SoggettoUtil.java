package siap.sico.soggetto.model;

import java.util.Date;
import java.util.List;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;

@SuppressWarnings("rawtypes")
public class SoggettoUtil {

	public static void controllaCF(SoggettoModel sm) throws F3BException {

		// alloco una nuova istanza di DomainBusinessDelegate
		CodiceFiscale cf = new CodiceFiscale();
		// popolo l'oggetto cf
		cf.setNome(sm.getNome());
		cf.setCognome(sm.getCognome());
		cf.setSesso(sm.getSesso());
		// String giorno = sm.getGiorno();
		// if (giorno.length() < 2)
		// giorno = "0" + giorno;
		// String mese = sm.getMese();
		// if (mese.length() < 2)
		// mese = "0" + mese;
		// cf.setDataNascita(giorno + "/" + mese + "/" + sm.getAnno());
		Date dn = !Utils.isNullObj(sm.getDataNascita()) ? sm.getDataNascita()
				: sm.getDataNascitaPresuntaCalc();
		cf.setDataNascita(DateUtils.getDateToString(dn, "dd/MM/yyyy"));
		String comune;
		if ("039".equals(sm.getCodStatoNascita())) {
			IComune ic = SICOLookupRemote.getComuneRemote();
			ComuneModel cm = ic.ExRicercaComuneByKey(sm.getCodComuneNascita());
			comune = cm.getCodCatastaleComune();
		} else {
			// Imposto la descrizione della Nazione di Nascita
			DecodificheModel dm = new DecodificheModel();
			dm.setContesto("NAZIONE");
			dm.setCode(sm.getCodStatoNascita());
			List nazioni = (List) DecodificheManager.getInstance().getNazioni();
			int index = nazioni.indexOf(dm);
			String description = ((DecodificheModel) nazioni.get(index)).getCodiceAlt2();
			comune = description;
		}
		cf.setComune(comune);

		// Controllo se il codice fiscale è coerente con i dati anagrafici del soggetto
		// 1) controllo diretto cf del sm = cf generato dalla classe CodiceFiscale
		// 2) controllo negli 8 cf generati dalla classe CodiceFiscale
		// 3) controllo congruità formale dalla classe CodiceFiscaleFormale
		// 4) controllo dei primi 6 caratteri
		String cfStandard = cf.getCodiceFiscale();
		String cfToControl = sm.getCodFiscale();
		if (!Utils.isPresent(cfToControl)) {
			String msg = "Attenzione! Il Codice Fiscale e' Obbligatorio e deve essere conforme alle regole"
					+ " dettate dall'Agenzia delle Entrate";
			throw new SIEPException(SIEPException.USER_MESSAGE, msg);
		}
		boolean isValidCF = cfToControl.equalsIgnoreCase(cfStandard);
		// se è valido passo altrimenti passo allo step successivo
		while (!isValidCF) {
			String[] cfValidi = cf.generaCodFiscValidi();
			// controllo degli 8 cf generati dalla classe
			for (int i = 0; i < cfValidi.length; i++) {
				if (cfToControl.equalsIgnoreCase(cfValidi[i])) {
					isValidCF = true;
					break;
				}
			}
			if (isValidCF)
				break;
			CodiceFiscaleFormale cff = new CodiceFiscaleFormale(cfToControl);
			boolean isCfFormalmenteOK = cff.controllaCorrettezza();
			boolean isToBeForced = false;
			if (isCfFormalmenteOK) {
				boolean testSixChars = cf.isEqualCFFirstSixDigit(cfToControl, cfStandard);
				if (testSixChars) {
					isValidCF = true;
					isToBeForced = true;
				} else
					isCfFormalmenteOK = false;
			}
			if (!isCfFormalmenteOK || (isToBeForced /* && sm.getFlagForzaturaCF().equals("N") */)) {
				String msg = "Attenzione! Il Codice Fiscale indicato non e' conforme alle regole dettate "
						+ "dall'Agenzia delle Entrate";
				throw new SIEPException(SIEPException.USER_MESSAGE, msg);
			}
		}
	}

}