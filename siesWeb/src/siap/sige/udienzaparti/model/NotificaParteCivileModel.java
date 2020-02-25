package siap.sige.udienzaparti.model;

import f3b.model.GenericModel;

public class NotificaParteCivileModel extends GenericModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3183841326679138106L;
    
	
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
