package siap.sius.depositosentenza.model;

import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;

public class SentenzaEventoTenoriGProcModel extends SentenzaEventoTenoriModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3910927234378743721L;

	private GeneraleProcedimentoModel mGeneraleProcedimento;

	public SentenzaEventoTenoriGProcModel() {
		this.mGeneraleProcedimento = null;
		this.setEvento(null);
		this.setTenori(null);
		this.setSentenza(null);
	}

	// Costruttore di Copia.
	public SentenzaEventoTenoriGProcModel(SentenzaEventoTenoriGProcModel aModel) {
		this.mGeneraleProcedimento = aModel.mGeneraleProcedimento;
		this.setEvento(aModel.getEvento());
		this.setTenori(aModel.getTenori());
		this.setSentenza(aModel.getSentenza());
	}

	public GeneraleProcedimentoModel getGeneraleProcedimento() {
		return mGeneraleProcedimento;
	}

	public void setGeneraleProcedimento(GeneraleProcedimentoModel aValore) {
		mGeneraleProcedimento = aValore;
	}

}