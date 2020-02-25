package siap.siep.annotazioneesitotrasmissione.controller;


/**
* <p>Title: AnnotazioneEsitoTrasmissioneController</p>
* <p>Description: Classe Controller per AnnotazioneEsitoTrasmissione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import f3b.util.F3BException;


public interface IAnnotazioneEsitoTrasmissione{

  public AnnotazioneEsitoTrasmissioneModel ExInserisciAnnotazioneEsitoTrasmissione (AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione )  throws F3BException ;

  public Vector <AnnotazioneEsitoTrasmissioneModel> ExRicercaAnnotazioneEsitoTrasmissione (AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione ) throws F3BException; 

  public void  ExModificaAnnotazioneEsitoTrasmissione (AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione ) throws F3BException;

  public void ExCancellaAnnotazioneEsitoTrasmissione (AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione ) throws F3BException ;

  public BigDecimal ExGetCountAnnotazioneEsitoTrasmissione (AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione ) throws F3BException ;

  public AnnotazioneEsitoTrasmissioneModel ExRicercaAnnotazioneEsitoTrasmissioneById ( BigDecimal aIdEsitoTrasmissione ) throws F3BException ;

  public Vector <AnnotazioneEsitoTrasmissioneModel> ExRicercaAnnotazioneEsitoTrasmissionePaged (AnnotazioneEsitoTrasmissioneModel aAnnotazioneEsitoTrasmissione,int aPage ) throws F3BException ; 

  public AnnotazioneEsitoTrasmissioneModel ExRicercaAnnotazioneEsitoTrasmissioneByIdEvento ( BigDecimal aIdEvento ) throws F3BException ;
}
