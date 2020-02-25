/**
 * 
 */
package siap.siep.statoesecuzione.controller;

import java.sql.Connection;

import org.apache.log4j.Logger;

import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.statoesecuzione.dao.StatoEsecuzioneSqlDAO;
import siap.siep.statoesecuzione.model.EventoModel;
import f3b.log.LogF3B;

/**
 * Classe che realizza la categoria degli eventi Archiviazione
 * @author Giselda De Vita
 *
 */
public class StatoEsecuzioneArch extends StatoEsecuzioneElement {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * 
	 */
	public StatoEsecuzioneArch() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param aCopy
	 */
	public StatoEsecuzioneArch(StatoEsecuzioneElement aCopy) {
		super(aCopy);
	}

	public void elabora(siap.sico.evento.model.EventoModel aEvento)
	{
		Connection lConn = null;
		StatoEsecuzioneSqlDAO lStatEsec = null;

		try{
			EventoModel lEvento = new EventoModel(aEvento);

			lEvento.setFamiglia("ARCH");
			//lEvento.setDescrMotivo(null);
			lEvento.setDescrLuogoEmittente(null);
			lEvento.setDescrUfficioEmittente(null);
			
			//descrizione_data
			lEvento.setDescrizioneData(mCostanti.getProperty("INTERRUZIONE_IN_DATA"));			

			//data
			lEvento.setData(aEvento.getDataEmissione());

			/*se è 0019 o 0022 allora dobbiamo cercare i dati del cumulo*/

			if(	lEvento.getCodMotivo().equals("0019")
					||lEvento.getCodMotivo().equals("0022")
					||lEvento.getCodMotivo().equals("0356")
					||lEvento.getCodMotivo().equals("0357"))
			{

				lConn =getDBConnection();
				lStatEsec = new StatoEsecuzioneSqlDAO(lConn);
				FascicoloSiepModel lFasc = lStatEsec.ricercaFascicoloCumulo(aEvento.getFasSieIdFascicoloSiep());

				lEvento.setAnnoUnione(lFasc.getAnnoFascicoloUnione());
				lEvento.setNumeroUnione(lFasc.getNumFascicoloUnione());
				lEvento.setDataUnione(lFasc.getDataUnione());

				if(lEvento.getCodMotivo().equals("0022")||lEvento.getCodMotivo().equals("0357"))
					lEvento.setUfficioUnione(lFasc.getDescrTipoUfficioUnione() + " di " + lFasc.getDescrComuneUfficioUnione());

				if(lEvento.getCodMotivo().equals("0019")
						||lEvento.getCodMotivo().equals("0022"))
				{
					lEvento.setDescrTipoProvvedimento("Archiviazione");
					lEvento.setDescrMotivo(lEvento.getDescrMotivo().substring(14));
				}
			}

			this.mEventoStatoEsecuzione = lEvento;

		}
		catch(Exception ex)
		{
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di mLog
			siesLogger.error("Errore in StatoEsecuzioneArch.elabora",ex);
			ex.printStackTrace();
		}
		finally
		{
			try{
				cleanup(lStatEsec);
				cleanup(lConn);
			}catch(Exception eee ){}

		}

	}



}