package siap.siep.istruttoriacumulo.model;

import java.util.List;

import f3b.model.GenericModel;

/**
 * <p>
 * Title: DatiCumuloPerTrasferimentoModel
 * </p>
 * <p>
 * Description: Dati relativi All'IstruttoriaCumulo e tutte le tabelle Collegate
 * </p>
 */
public class DatiCumuloPerTrasferimentoModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2689634213356433752L;

	private List<IstruttoriaCumuloModel> mListIstruttoriaCumulo;

	// COSTRUTTORE DI DEFAULT
	public DatiCumuloPerTrasferimentoModel() {
		mListIstruttoriaCumulo = null;
	}

	// COSTRUTTORE DI COPIA
	public DatiCumuloPerTrasferimentoModel(List<IstruttoriaCumuloModel> aListIstruttoriaCumulo) {
		mListIstruttoriaCumulo = aListIstruttoriaCumulo;
	}

	//
	// METODI GET()
	//
	public List<IstruttoriaCumuloModel> getListIstruttoriaCumulo() {
		return mListIstruttoriaCumulo;
	}

	//
	// METODI SET()
	//
	public void setListIstruttoriaCumulo(List<IstruttoriaCumuloModel> aValore) {
		mListIstruttoriaCumulo = aValore;
	}

}