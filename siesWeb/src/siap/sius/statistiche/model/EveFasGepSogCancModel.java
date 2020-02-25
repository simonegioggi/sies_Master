package siap.sius.statistiche.model;

/**
* <p>Title: FascicoloSoggAttModel</p>
* <p>Description: Questo Classe Model raggruppa in un unico aggregato tutti i dati collegati ad un Fascicolo, cioè:</p>
 * Fascicolo Siepe, Soggetto, Elenco Attività, Fascicolo SIUS, Fascicolo SIEP, Evento.
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sius.cancelleriaassegnataria.model.CancelleriaAssegnatariaModel;

/**
 * Model rappresentativo dell'aggregato di vari model: Evento, Fascicolo SIUS, Generale Procedimento,
 * Soggetto, Documento_Allegato, CancelleriaAssegnataria Utilizzato per contenere i risultati di ricerche su
 * dati strutturati SIUS.
 * 
 * @author Lesposito
 *
 */
public class EveFasGepSogCancModel extends EveFasGepSogModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5471007997658623716L;

	private CancelleriaAssegnatariaModel mCancelleriaAssegnataria = null;

	// COSTRUTTORE DI DEFAULT
	public EveFasGepSogCancModel() {
		mCancelleriaAssegnataria = null;
	}

	public EveFasGepSogCancModel(EveFasGepSogModel aAncestor) {
		super(aAncestor);
		mCancelleriaAssegnataria = null;
	}

	public EveFasGepSogCancModel(EveFasGepSogModel aAncestor,
			CancelleriaAssegnatariaModel aCancelleriaAssegnataria) {
		this(aAncestor);
		if (aCancelleriaAssegnataria != null)
			mCancelleriaAssegnataria = new CancelleriaAssegnatariaModel(aCancelleriaAssegnataria);
	}

	// COSTRUTTORE DI COPIA
	public EveFasGepSogCancModel(EveFasGepSogCancModel aModel) {
		super(aModel.getEvento(), aModel.getFascicoloSius(), aModel.getGeneraleProcedimento(),
				aModel.getSoggetto(), aModel.getDocumentoAllegato());
		mCancelleriaAssegnataria = aModel.getCancelleriaAssegnataria();
	}

	// METODI GET()
	public CancelleriaAssegnatariaModel getCancelleriaAssegnataria() {
		return mCancelleriaAssegnataria;
	}

	// METODI SET()
	public void setCancelleriaAssegnataria(CancelleriaAssegnatariaModel aModel) {
		this.mCancelleriaAssegnataria = aModel;
	}

}