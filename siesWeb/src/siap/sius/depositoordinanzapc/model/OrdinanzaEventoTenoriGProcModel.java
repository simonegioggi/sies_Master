package siap.sius.depositoordinanzapc.model;

import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;

public class OrdinanzaEventoTenoriGProcModel extends OrdinanzaEventoTenoriModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2023182779786571220L;
	private GeneraleProcedimentoModel mGeneraleProcedimento;

	public OrdinanzaEventoTenoriGProcModel() {
		this.mGeneraleProcedimento = null;
		this.setEvento(null);
		this.setTenori(null);
		this.setOrdinanza(null);
	}

	// Costruttore di Copia.
	public OrdinanzaEventoTenoriGProcModel(OrdinanzaEventoTenoriGProcModel aModel) {
		this.mGeneraleProcedimento = aModel.mGeneraleProcedimento;
		this.setEvento(aModel.getEvento());
		this.setTenori(aModel.getTenori());
		this.setOrdinanza(aModel.getOrdinanza());
	}

	public GeneraleProcedimentoModel getGeneraleProcedimento() {
		return mGeneraleProcedimento;
	}

	public void setGeneraleProcedimento(GeneraleProcedimentoModel aValore) {
		mGeneraleProcedimento = aValore;
	}

}