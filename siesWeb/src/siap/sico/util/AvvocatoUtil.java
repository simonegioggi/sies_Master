package siap.sico.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import f3b.util.F3BException;
import siap.sico.SICOException;
import siap.sico.decodifiche.controller.IComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.siep.avvocato.model.AvvocatoModel;

/**
 * MEV_21_ aggiunta classe di utility per calcolare il luogo di nascita avvocati reginde
 *
 * @author sgioggi
 */
public class AvvocatoUtil {

	public static ComuneModel calcolaComuneNascita(String cf) throws F3BException {

		if (cf.length()<16) {
			throw new SICOException(SICOException.USER_MESSAGE,
					"Codice Fiscale "+cf+" non valido");
		}
		// Codice Comune Catastale
		String ccc = cf.substring(11, 15).toUpperCase();

		// sostituire gli ultimi 3 caratteri del codice catastale con
		// 0 = L | 1 = M | 2 = N | 3 = P | 4 = Q | 5 = R | 6 = S | 7 = T | 8 = U | 9 = V
		// H501 --> H50M; H501 --> H5LM; H501 --> HRLM;
		Pattern p = Pattern.compile("\\d");
		for (int i = 3; i > 0; i--) {
			char ch = ccc.charAt(i);
			if (Character.isDigit(ch))
				break;
			String s = Character.toString(ch);
			Matcher m = p.matcher(s);
			if (!m.matches())
				ccc = ccc.replace(ch, sostituisciCarattere(ch));
		}

		try {
			// ricavo il comune dal COD_CATASTALE_COMUNE
			IComune ic = SICOLookupRemote.getComuneRemote();
			ComuneModel cm = new ComuneModel(ic.ExRicercaComuneByCodCatastale(ccc));
	
			// valore di ritorno
			return cm;
		} catch (Exception ex) {
			throw new F3BException(ex);
		}
			
	}

	private static char sostituisciCarattere(char ch) {

		char ret;

		switch (ch) {
		case 'L':
			ret = '0';
			break;
		case 'M':
			ret = '1';
			break;
		case 'N':
			ret = '2';
			break;
		case 'P':
			ret = '3';
			break;
		case 'Q':
			ret = '4';
			break;
		case 'R':
			ret = '5';
			break;
		case 'S':
			ret = '6';
			break;
		case 'T':
			ret = '7';
			break;
		case 'U':
			ret = '8';
			break;
		default: // 'V'
			ret = '9';
			break;
		}

		// valore di ritorno
		return ret;
	}

	public static void valorizzaDatiReginde(AvvocatoModel am, String pec, String codStatoNascita,
			String descLuogoNascitaReginde, String descrComuneStudio) {

		// per il luogo residenza sarà aggiunta una nuova colonna che conterrà la descrizione del Comune
		// sede dello studio come presente in ReGIndE, abbandonando la valorizzazione della colonna
		// "COD_COMUNE_RESIDENZA", che resterà per i dati pregressi
		am.setCodComuneResidenza(null);
		am.setPec(pec);
		am.setFlagRegInde("SI");
		am.setDescrComuneStudio(descrComuneStudio);
		am.setDescLuogoNascitaReginde(descLuogoNascitaReginde);
		am.setCodStatoNascita(codStatoNascita);
	}

}