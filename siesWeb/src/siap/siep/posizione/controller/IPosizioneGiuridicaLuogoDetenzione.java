package siap.siep.posizione.controller;

/**
* <p>Title: PosizioneGiuridicaLuogoDetenzioneController</p>
* <p>Description: Classe Controller per PosizioneGiuridica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public interface IPosizioneGiuridicaLuogoDetenzione {

	public Vector ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByIdFascicolo(BigDecimal aIdFascicolo)
			throws F3BException;

	public PosizioneGiuridicaLuogoDetenzioneAltraCausaModel ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaByKey(
			BigDecimal aId) throws F3BException;

	public void ExCancellaPosizioneGiuridicaLuogoDetenzioneAltraCausa(
			PosizioneGiuridicaLuogoDetenzioneAltraCausaModel aPosGiu) throws F3BException;

}