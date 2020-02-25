package siap.sige.richiestaatti.controller;

/**
* <p>Title: RichiestaAttiController</p>
* <p>Description: Classe Controller per RichiestaAtti</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.motivazioneprovvedimento.model.MotivazioneProvvedimentoSigeModel;
import siap.sige.provvedimento.model.ProvvedimentoSigeModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import f3b.util.F3BException;

public interface IRichiestaAttiSige
{
public ByteArrayOutputStream ExStampaRichiestaAtti(EventoModel lEvento, BigDecimal aIdFasSige, String lTipoUfficio, UtenteModel aUtenteModel )
						throws F3BException;
public ByteArrayOutputStream ExStampaSollecitiSige(EventoModel lEvento, BigDecimal aIdFasSige, String lTipoUfficio, UtenteModel aUtenteModel )
throws F3BException;
public DocumentoAllegatoModel ExInserisciSollecito ( EventoNotificaModel aEveNot ,DocumentoAllegatoModel aDocAllegato,UfficioModel lUfficio,UtenteModel lUtenteMod,FascicoloSigeEstesoModel lFasEsteso)
throws F3BException;
public ProvvedimentoSigeModel ExInserisciRichiestaParereProv(EventoNotificaModel aEvento, MotivazioneProvvedimentoSigeModel[] aMotivazioniDecreto,ProvvedimentoSigeModel lProvvedimentoSigeModel) 
throws F3BException;
public DocumentoAllegatoModel ExModificaSollecito ( DocumentoAllegatoModel aDocAllegato) throws F3BException;


}
