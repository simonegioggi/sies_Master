package siap.sige.fogliocomplementare.model;

import siap.sige.documentoallegato.model.DocumentoAllegatoModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import f3b.model.GenericModel;

public class FoglioComplementareModel extends GenericModel {
    private static final long serialVersionUID = 7438767933550887303L;
	private DocumentoAllegatoModel fc=null;
	private ProvvedimentoSigeEventoModel provvedimento=null;
	
	public DocumentoAllegatoModel getFc() {
		return fc;
	}
	
	public void setFc(DocumentoAllegatoModel fc) {
		this.fc = fc;
	}
	
	public ProvvedimentoSigeEventoModel getProvvedimento() {
		return provvedimento;
	}
	
	public void setProvvedimento(ProvvedimentoSigeEventoModel provvedimento) {
		this.provvedimento = provvedimento;
	}
}
