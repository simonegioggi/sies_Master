package siap.siep.motivoevento.action;

import f3b.web.IWebConstants;
/**
* <p>Title: ICostantiMotivoEvento</p>
* <p>Description: Classe di costanti di MotivoEvento</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface ICostantiMotivoEvento
{
		 public static final String CAMPO_ID_MOTIVO_EVENTO = "IdMotivoEvento"; 
		 public static final String CAMPO_COD_MOTIVO_REVOCA = "CodMotivoRevoca"; 
		 public static final String CAMPO_COD_MOTIVO_REVOCA_PM = "CodMotivoRevocaPm"; 
		 public static final String CAMPO_MOTIVAZIONI = "Motivazioni"; 
		 public static final String CAMPO_EVE_ID_EVENTO = "EveIdEvento"; 
		 public static final String PG_LOAD_RICERCAMOTIVOEVENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/motivoevento/LoadRicercaMotivoEvento.jsp";
		 public static final String PG_LOAD_DETTAGLIOMOTIVOEVENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/motivoevento/LoadRicercaMotivoEvento.jsp";
		 public static final String PG_RICERCAMOTIVOEVENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/motivoevento/RicercaMotivoEvento.jsp";
		 public static final String PG_LOAD_INSERISCIMOTIVOEVENTO	= IWebConstants.ROOT_DIR + "files/siap/siep/motivoevento/LoadInserisciMotivoEvento.jsp";
}