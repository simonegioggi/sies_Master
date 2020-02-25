/**
 * AVVOCATURA
 */
package it.eng.giustizia.avvocatura.util;

import f3b.log.LogF3B;
import it.eng.giustizia.avvocatura.ws.type.ricercaSoggettiConProcedimenti.SOGGETTOTYPE;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.soggetto.model.SoggettoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * @author Gioggi
 */
public class SoggettiConProcedimentiMapper {

	// variabile di classe per il log
	private static Logger avvocaturaLogger = Logger.getLogger(LogF3B.AVVOCATURA_LOG);

	@SuppressWarnings("rawtypes")
	public static List<SOGGETTOTYPE> mapSoggettiConProcedimenti(Vector soggettiConProcedimenti) {

		// info per il log
		avvocaturaLogger.info("Starting Point della classe: SoggettiConProcedimentiMapper, metodo: mapSoggettiConProcedimenti");

		// instanzio un oggetto di tipo "ArrayList"
		List<SOGGETTOTYPE> lst = null;
		if (soggettiConProcedimenti != null) {
			// inizializzo l'oggetto di tipo "ArrayList"
			lst = new ArrayList<SOGGETTOTYPE>(soggettiConProcedimenti.size());
			for (int i = 0; i < soggettiConProcedimenti.size(); i++) {
				// instanzio ed valorizzo un oggetto di tipo "FascicoloGPModel"
				FascicoloGPModel fgpm = (FascicoloGPModel) soggettiConProcedimenti.get(i);
				// instanzio ed valorizzo un oggetto di tipo "SoggettoModel"
				SoggettoModel sm = fgpm.getFascicoloSiusModel().getSoggetto();
				// instanzio ed inizializzo un oggetto di tipo "SOGGETTOTYPE"
				SOGGETTOTYPE st = new SOGGETTOTYPE();
				// imposto i dati
				if (PropertyUtil.isPresent(sm.getCodComuneNascita()) &&
						!"-".equals(sm.getCodComuneNascita()))
					st.setCodComuneNascita(sm.getCodComuneNascita());
				else
					st.setCodComuneNascita(null);
				st.setCodCs(sm.getCodCs());
				if (PropertyUtil.isPresent(sm.getCodProvinciaNascita()) &&
						!"-".equals(sm.getCodProvinciaNascita()))
					st.setCodProvinciaNascita(sm.getCodProvinciaNascita());
				else
					st.setCodProvinciaNascita(null);
				st.setCodStatoNascita(sm.getCodStatoNascita());
				st.setCognome(sm.getCognome());
				if (PropertyUtil.isPresent(sm.getCognomeMadre()))
					st.setCognomeMadre(sm.getCognomeMadre());
				else
					st.setCognomeMadre(null);
				st.setDataNascita(Mapper.creaDataTypeSoggettiConProcedimenti(sm.getDataNascita()));
				String descrComuneNascita = null;
				if (PropertyUtil.isPresent(sm.getDescrComuneNascita()) &&
						!"-".equals(sm.getDescrComuneNascita()))
					descrComuneNascita = sm.getDescrComuneNascita();
				else if (PropertyUtil.isPresent(sm.getDescComuneNascitaEstero()) &&
						!"-".equals(sm.getDescComuneNascitaEstero()))
					descrComuneNascita = sm.getDescComuneNascitaEstero();
				st.setDescrComuneNascita(descrComuneNascita);
				st.setDescrStatoNascita(sm.getDescrStatoNascita());
				if (sm.getIdSoggetto() != null)
					st.setIdSoggetto(sm.getIdSoggetto().toBigInteger());
				else
					st.setIdSoggetto(null);
				st.setNome(sm.getNome());
				if (fgpm.getFascicoloSiusModel().getNumFascicoli() != null)
					st.setNumeroFascicoli(fgpm.getFascicoloSiusModel().getNumFascicoli().toBigInteger());
				else
					st.setNumeroFascicoli(new BigInteger("0"));
				if (PropertyUtil.isPresent(sm.getPaternita()))
					st.setPaternita(sm.getPaternita());
				else
					st.setPaternita(null);
				st.setSesso(sm.getSesso());

				// aggiungo alla lista di ritorno
				lst.add(st);
			}
		}

		// valore di ritorno
		return lst;
	}

	
}