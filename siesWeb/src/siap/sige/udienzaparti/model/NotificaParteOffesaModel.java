package siap.sige.udienzaparti.model;

import f3b.model.GenericModel;

public class NotificaParteOffesaModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6559441923499159591L;
	
	private String nominativo=null;
    private boolean avvocato=false;
    private String indirizzoNotifica=null;
    
	public String getNominativo() {
		return nominativo;
	}

	public void setNominativo(String nominativo) {
		this.nominativo = nominativo;
	}

	public boolean getAvvocato() {
		return avvocato;
	}

	public void setAvvocato(boolean avvocato) {
		this.avvocato = avvocato;
	}

	public String getIndirizzoNotifica() {
		return indirizzoNotifica;
	}

	public void setIndirizzoNotifica(String indirizzoNotifica) {
		this.indirizzoNotifica = indirizzoNotifica;
	}
}
