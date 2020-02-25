package siap.sius.produzioneatti.model;

/**
 * <p>Title: </p>
 * <p>Description: Model contenente la descrizione dei parametri di </p>
 * ricerca dell'Elenco Richieste Parere. </p>
 * Da utilizzare come intestazione della stampa relativa.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.Date;

import siap.sico.evento.model.XModel;

public class FiltroPareriModel extends XModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5211569837315659557L;

	private Date mDataEmissioneIniziale = null;
	private Date mDataEmissioneFinale = null;
	private String mDescrOggettoProcedimento = "";
	private String mDescrMotivo = "";

	public FiltroPareriModel() {
		super();
	}

	public FiltroPareriModel(XModel aModel) {
		super(aModel);
	}

	public FiltroPareriModel(XModel aModel, Date aData1, Date aData2, String aOggProc, String aMotivo) {
		super(aModel);
		mDataEmissioneIniziale = aData1;
		mDataEmissioneFinale = aData2;
		mDescrOggettoProcedimento = aOggProc;
		mDescrMotivo = aMotivo;

	}

	public String getDescrOggettoProcedimento() {
		return mDescrOggettoProcedimento;
	}

	public String getDescrMotivo() {
		return mDescrMotivo;
	}

	public Date getDataEmissioneIniziale() {
		return mDataEmissioneIniziale;
	}

	public Date getDataEmissioneFinale() {
		return mDataEmissioneFinale;
	}

	public void setDescrOggettoProcedimento(String aValore) {
		mDescrOggettoProcedimento = aValore;
	}

	public void setDescrMotivo(String aValore) {
		mDescrMotivo = aValore;
	}

	public void setDataEmissioneIniziale(Date aValore) {
		mDataEmissioneIniziale = aValore;
	}

	public void setDataEmissioneFinale(Date aValore) {
		mDataEmissioneFinale = aValore;
	}

}