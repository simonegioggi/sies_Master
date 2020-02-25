package siap.siep.modulocumulo.model;

import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.model.GenericModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.modulocumulo.action.ICostantiDatiFinaliUlterioriSanzioni;

public class DatiFinaliCumuloAggregatoModel extends GenericModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2561138485418318422L;

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private DatiFinaliCumuloModel mDatiFinaliCumulo;
	private PenaRideterminataCumuloModel mPenaRideterminataCumulo;
	private Vector<DatiFinaliUlterioriSanzioniModel> mListaDatiFinaliUlterioriSanzioni;

	// Altre Sanzioni: Misure Sicurezza Cumulo
	private Vector<MisuraSicurezzaCumuloModel> mListaMisureSicurezza;
	private Vector<PenaAccessoriaCumuloModel> mListaPeneAccessorie;
	// private Vector <SanzioniAmministrativeCumuloModel> mListaSanzioniAmministrative;

	private PosizioneGiuridicaCumuloModel mPosizioneGiuridicaCumulo;
	private PenaRideterminataCumuloModel mPenaResiduaCumulo;

	private EventoNotificaModel mProvvedimentoCumulo;

	// Metodi GET
	public DatiFinaliCumuloModel getDatiFinaliCumulo() {
		return mDatiFinaliCumulo;
	}

	public PenaRideterminataCumuloModel getPenaRideterminataCumulo() {
		return mPenaRideterminataCumulo;
	}

	public Vector<DatiFinaliUlterioriSanzioniModel> getListaDatiFinaliUlterioriSanzioni() {
		return mListaDatiFinaliUlterioriSanzioni;
	}

	public Vector<MisuraSicurezzaCumuloModel> getListaMisureSicurezza() {
		return mListaMisureSicurezza;
	}

	public Vector<PenaAccessoriaCumuloModel> getListaPeneAccessorie() {
		return mListaPeneAccessorie;
	}

	public PosizioneGiuridicaCumuloModel getPosizioneGiuridicaCumulo() {
		return mPosizioneGiuridicaCumulo;
	}

	public PenaRideterminataCumuloModel getPenaResiduaCumulo() {
		return mPenaResiduaCumulo;
	}

	public EventoNotificaModel getProvvedimentoCumulo() {
		return mProvvedimentoCumulo;
	}

	// Metodi SET
	public void setDatiFinaliCumulo(DatiFinaliCumuloModel aDatiFinaliCumulo) {
		this.mDatiFinaliCumulo = aDatiFinaliCumulo;
	}

	public void setPenaRideterminataCumulo(PenaRideterminataCumuloModel aPenaRideterminataCumulo) {
		this.mPenaRideterminataCumulo = aPenaRideterminataCumulo;
	}

	public void setListaDatiFinaliUlterioriSanzioni(
			Vector<DatiFinaliUlterioriSanzioniModel> aListaDatiFinaliUlterioriSanzioni) {
		this.mListaDatiFinaliUlterioriSanzioni = aListaDatiFinaliUlterioriSanzioni;
	}

	public void setListaMisureSicurezza(Vector<MisuraSicurezzaCumuloModel> aListaMisureSicurezza) {
		this.mListaMisureSicurezza = aListaMisureSicurezza;
	}

	public void setListaPeneAccessorie(Vector<PenaAccessoriaCumuloModel> aListaPeneAccessorie) {
		this.mListaPeneAccessorie = aListaPeneAccessorie;
	}

	public void setPosizioneGiuridicaCumulo(PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo) {
		this.mPosizioneGiuridicaCumulo = aPosizioneGiuridicaCumulo;
	}

	public void setPenaResiduaCumulo(PenaRideterminataCumuloModel aPenaResiduaCumulo) {
		this.mPenaResiduaCumulo = aPenaResiduaCumulo;
	}

	public void setProvvedimentoCumulo(EventoNotificaModel aProvvedimentoCumulo) {
		this.mProvvedimentoCumulo = aProvvedimentoCumulo;
	}

	/**
	 * Restituisce la Sanzione Sostitutiva - Semidetenzione se presente
	 * 
	 * @return
	 */
	public DatiFinaliUlterioriSanzioniModel getUltSanSanSosSemidetenzione() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_SEMIDET_SS
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}

		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanSanSosLibertContrl() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LIBCTRL_SS
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanSanSosPPMulta() {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("getUltSanSanSosPPMulta");

		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_MULTA_SS.equalsIgnoreCase(
						lUlterSanzione.getCodTipoUlterioreSanzione()) && lUlterSanzione.getMulta() != null) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanSanSosPPAmmenda() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_AMMENDA_SS
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())
						&& lUlterSanzione.getAmmenda() != null) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanSanSosEspulsione() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_ESPULSIONE_SS
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanSanSosLPU() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_SS
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanConvPPLavSost() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LAVSOST_PP
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanConvPPLibCtrl() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LIBCTRL_PP
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanGiuPacePermDom() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_PERMDOM_GP
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanGiuPaceLavSost() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LAVSOST_GP
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanGiuPaceLPU() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_LPU_GP
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

	public DatiFinaliUlterioriSanzioniModel getUltSanGiuPaceESP() {
		DatiFinaliUlterioriSanzioniModel lUlterioreSanz = null;
		if (mListaDatiFinaliUlterioriSanzioni != null) {
			for (int i = 0; i < mListaDatiFinaliUlterioriSanzioni.size(); i++) {
				DatiFinaliUlterioriSanzioniModel lUlterSanzione = mListaDatiFinaliUlterioriSanzioni
						.elementAt(i);
				if (ICostantiDatiFinaliUlterioriSanzioni.COD_TIPO_SANZIONE_ESP_GP
						.equalsIgnoreCase(lUlterSanzione.getCodTipoUlterioreSanzione())) {
					return lUlterSanzione;
				}
			}
		}
		return lUlterioreSanz;
	}

}