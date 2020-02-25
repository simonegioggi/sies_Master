package siap.sico.evento.model;

import java.util.Vector;

import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;

/**
 * Model Aggregato per contenere l'evento di concessione, le licenze, i  periodo 
 * e l0evento di esecuzione .
 * 
 * 
 * 
 * @author d.fiorletta
 * @since 10/2014 DL92
 */
public class EventoLicenzePeriodiModel {
  /**
   * mEvento Evento a cui sono collegate le licenza. Di solito decreto/ordinanza
   */
  private EventoModel mEvento;
  /**
   * Eventuale evento che punta mEvento (eve_id_evento). E' l'evento SIEP di 
   * esecuzione
   */
  private EventoModel mEventoCollegato;  
  /**
   * mListaLicenze Licenze collegate (eve_id_evento) a mEvento
   */
  private Vector <LicenzaLibAnticipataModel> mListaLicenze;  
  /**
   * Licenze collegate (eve_id_evento) a mEvento con anche i periodi
   */
  private Vector <LicenzaPeriodiLibAnticipataModel> mListaLicenzePeriodi;
  

  // Metodi GETTER
  public EventoModel getEvento() {
    return mEvento;
  }
  public Vector<LicenzaLibAnticipataModel> getListaLicenze() {
    return mListaLicenze;
  }
  public Vector<LicenzaPeriodiLibAnticipataModel> getListaLicenzePeriodi() {
    return mListaLicenzePeriodi;
  }
  
  public EventoModel getEventoCollegato() {
    return mEventoCollegato;
  }
  
  // METODI SETTER
  public void setEvento(EventoModel mEvento) {
    this.mEvento = mEvento;
  }
  public void setListaLicenze(Vector<LicenzaLibAnticipataModel> mListaLicenze) {
    this.mListaLicenze = mListaLicenze;
  }
  public void setListaLicenzePeriodi(Vector<LicenzaPeriodiLibAnticipataModel> mListaLicenzePeriodi) {
    this.mListaLicenzePeriodi = mListaLicenzePeriodi;
  }
  
  public void setEventoCollegato(EventoModel mEvento) {
    this.mEventoCollegato = mEvento;
  }
}
