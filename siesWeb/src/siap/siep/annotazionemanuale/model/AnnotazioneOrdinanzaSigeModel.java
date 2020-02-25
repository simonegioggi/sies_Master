package siap.siep.annotazionemanuale.model;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import f3b.log.LogF3B;

public class AnnotazioneOrdinanzaSigeModel extends AnnotazioneOrdinanzaModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4849329461354131690L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected ProvvedimentoSigeModel mProvSige;

	public AnnotazioneOrdinanzaSigeModel() {
		super();
		mProvSige = null;
	}

	public AnnotazioneOrdinanzaSigeModel(AnnotazioneManualeModel aAnnotazioneManuale, EventoModel aEvento,
			ProvvedimentoSigeModel aProvSige) {
		super(aAnnotazioneManuale, aEvento);
		mProvSige = aProvSige;
	}

	//
	// METODI GET()
	//

	public ProvvedimentoSigeModel getProvSige() {
		return mProvSige;
	}

	//
	// METODI SET()
	//

	public void setProvSige(ProvvedimentoSigeModel aValore) {
		mProvSige = aValore;
	}

	/**
	 * Funzione ricopia Anno/Num da Provvedimento ad Annotazione_Manuale
	 *
	 */
	public void setAnnoNumGeAnnotazione() {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("setAnnoNumGeAnnotazione ");

		if (mProvSige != null && mAnnotazioneManuale != null) {
			if (mProvSige.getChiaveAnno() != null)
				mAnnotazioneManuale.setAnnoGe(mProvSige.getChiaveAnno());
			if (mProvSige.getChiaveProgr() != null)
				mAnnotazioneManuale.setNumeroGe(mProvSige.getChiaveProgr().toString());

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("AnnotazioneManuale: " + mAnnotazioneManuale);

		}
	}

}