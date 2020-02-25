<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.lang.String" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>

<html>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="notifiche"       scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="curatore"  	  scope="request" class="siap.sius.curatore.model.CuratoreSiusModel"/>
<jsp:useBean id="UtenteConnesso"  scope="session" class="siap.sico.utente.model.UtenteModel"/>

<%
  BigDecimal IdEvento = (BigDecimal) request.getAttribute("IdEvento");

  boolean read_only = true;
  int ii=0;         // indice date modificabili
  //Modifica del 17/11/2016 MEV_50
  String tipoUffConnesso = null;
  if(UtenteConnesso != null && UtenteConnesso.getUfficioUtente() != null && 
	 UtenteConnesso.getUfficioUtente().getCodTipoUfficio() != null) {
	  tipoUffConnesso = UtenteConnesso.getUfficioUtente().getCodTipoUfficio();
  }
%>

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>


<head>
<title>[S.I.E.S.] - Registrazione date Notifica</title>

<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
<script language="JavaScript">
  var lung = 0;       // lunghezza array campo id notifica
  var lungDate = 0;   // lunghezza array campo data avvenuta notifica
  var read_only = false;
<%
if(modalita.equals("M"))
{
%>
  var modifica = true;
<%
}
else
{
%>
  var modifica = false;
<%
}
%>

  function inizializza()
  {
    if (typeof(document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>) == "undefined")
    {
      read_only = true;
    }
    else
    {
      if (typeof(document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.length) == "undefined")
        lung = 0;
      else
        lung = document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.length;
    //alert("lung : " +lung);
      if (typeof(document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>.length) == "undefined")
        lungDate = 0;
      else
        lungDate = document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>.length;
    }
    //alert('lungDate: '+lungDate);
    return;
  }

  function aggiornaID(i, ind, data)
  {
    if (lungDate == 0)
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.value = ind;
    }
    else
    {
      document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>[i].value = ind;
    }
     data_invio[i] = data;
  }

  function ControlliDate(data_to_verify , i)
  {
    var ritorno = true;
    var oggi = "<%=DateUtils.getSysDate("dd/MM/yyyy")%>"


    if( (data_to_verify.length > 2) && (ControllaData(data_invio[i])))
    {
      if (!ControllaData(data_to_verify))
      {
        alert('Data scorretta: '+ data_to_verify);
        //alert(data_to_verify);
        ritorno = false;
      }
      else if (! CompareDate(data_invio[i], data_to_verify))
      {
        alert('Data Notifica non può precedere Data Invio ');
        ritorno = false;
      }
      else if ( !CompareDate(data_to_verify,oggi))
      {
        alert('Data Notifica non può essere una data futura ');
        ritorno = false;
      }
    }
    return ritorno;
  }

  function  Verifica()
  {
    var ritorno = false;
    var data_ricezione = "";
    var i;

    if (read_only)
    {
      ritorno = true
    }
    else
    {
      if (lungDate == 0)
      {
        data_ricezione = document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;
        if(data_ricezione.length == 2)
        {
          if(!modifica)
          {
            document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.value = "";
          }
        }
        else
        {
          ritorno = ControlliDate(data_ricezione,0);
        }
      }
      else
      {
        for(i = 0; i < lungDate; i++)
        {
          data_ricezione = document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[i].value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[i].value+'/'+document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[i].value;

          if(data_ricezione.length == 2)
          {
            if(!modifica)
            {
              document.DettaglioNotifica.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>[i].value = "";
            }
          }
          else
          {
            ritorno = ControlliDate(data_ricezione,i);
            if (!ritorno)
              break;
          }
        }
      }
    }
    return ritorno;
  }

</script>
</head>
<body class="corpo" onLoad="inizializza()">
<FORM name="intestazione">
<table>
	<tr>
		<td class="LBG">
			<a href="Javascript:window.print();">
				<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0">
			</a>
		</td>
		<td class="LBG">
			<font class="label">Funzione :</font>&nbsp;
<% if(modalita.equals("M"))
{ %>
        	<font class="campo">Modifica date Notifica</font>
<%
}
else
{
%>
        	<font class="campo">Inserimento date Notifica</font>
<%
}
%>
		</td>
        <td class="LBG">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.provvedimento.action.ActRicercaFSPNotifica&noQuery=K">
            	<img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          	</a>
		</td>
	</tr>
</table>
</FORM>
<br>
<%
if (fascicoloSiusGP != null) {
%>
<table>
	<tr>
		<jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
   	</tr>
</table>
<%
} // endif fascicoloSiusGP
%>
<br>
<form name="DettaglioNotifica" method="POST" action="<%=IWebConstants.PG_MAIN%>">
<table cellspacing="2" cellpadding="2" width="95%">
<%
if (notifiche.size() == 0) {
%>
	<tr>
	  	<td class="LBG">
	    	<font class="label">Non ci sono notifiche per il provvedimento selezionato.</font>
	  	</td>
	</tr>
<%
} else {
	int i = 0;
  	Iterator itx = notifiche.iterator();
  	while (itx.hasNext()) {
    	NotificaModel notifica = (NotificaModel)itx.next();
	    // Modifica del 17/11/2016 MEV_50
	    // L'uffico TDS non deve vedere le notifiche destinate all'ufficio PM,
	    // l'uffico UDS non deve vedere le notifiche destinate all'ufficio PGCAP
		/* if( notifica.getUfficio() == null || (notifica.getUfficio() != null && !notifica.getUfficio().getCodTipoUfficio().equals("-") && tipoUffConnesso != null && 
	        ((tipoUffConnesso.equals("TDS") && !notifica.getUfficio().getCodTipoUfficio().equals("PM")) ||
	  	    (tipoUffConnesso.equals("UDS") && !notifica.getUfficio().getCodTipoUfficio().equals("PGCAP"))))) */
		// 16022018 [EC] modifico quanto commentato sopra per anomalia segnalata di GASBARRI in fase di precollaudo
		// 20190917 [SG]: SPOSTATO CONTROLLO LATO JAVA
// 		if (notifica.getUfficio() == null
// 				|| (notifica.getUfficio() != null && !notifica.getUfficio().getCodTipoUfficio().equals("-")
// 				&& (notifica.getCodTipoNotifica().equals("C") && (notifica.getUfficio().getCodTipoUfficio().equals("PM")
// 						|| notifica.getUfficio().getCodTipoUfficio().equals("PGCAP"))))) {
%>
	<tr>
		<td class="l"width=9%>Data Invio</td>
		<td class="l"width=35%><font class="campo"><%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%></font></td>
		<td class="l"width=13%>Data Notifica</td>
<%
			if (notifica.getDataAvvenutaNotifica() != null && !(modalita.equals("M")) || notifica.getDataAvvenutaNotifica() == null && modalita.equals("M")) {
%>
		<td class="l"width=33%><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"dd/MM/yyyy")," - - -")%></font>
<%
			} else {
        		read_only = false;
%>
		<td class="l"width=33%>
          	<font class="campo">
              <input type="text" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"dd"),"")%>"
              	onchange="Javascript:aggiornaID('<%=ii%>','<%="" + notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
              	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
              -
              <input type="text" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA %>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"MM"), "")%>"
              	onchange="Javascript:aggiornaID('<%=ii%>','<%="" + notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
              	maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
              -
              <input type="text" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"yyyy"), "")%>"
              	onchange="Javascript:aggiornaID('<%=ii%>','<%="" + notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
              	maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
          </font>
          <input type="hidden" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" value="">
		</td>
<%
      		ii++;
      	}
%>
	</tr>
	<tr>
		<td class="l">Destinatario</td>
<%
		// SOGETTO
    	if (notifica.getSogIdSoggetto() != null) {
        	if (fascicoloSiusGP.getFascicoloSiusModel().getSoggetto() != null) {
%>
            <td class="l" colspan="3">Soggetto &nbsp; <font class="campo"><%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getCognome()%></font>&nbsp;<font class="campo"><%=fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
<%      	}
    	}
    	// UFFICIO
    	if (notifica.getUfficio() != null) {
%>      
		<td class="l" colspan="3"><font class="campo"><%=notifica.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=notifica.getUfficio().getDescrComune()%></font></td>          
	</tr>
	<tr>
		<td>&nbsp;</td>
<%
    	}
    	// Autorità Esterna
    	if (notifica.getAutoritaEsterna() != null) {
%>
		<td class="l" colspan="3"><font class="campo"><%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%></font>&nbsp; di &nbsp; <font class="campo"><%=notifica.getAutoritaEsterna().getDescrSede()%></font></td>
	</tr>
	<tr>
	  	<td>&nbsp;</td>
<%
    	}
    	// Avvocato SIEP
    	if (notifica.getAvvIdAvvocatoFascicoloSiep() != null) {
      		if (notifica.getAvvSiep() != null) {
%>
		<td class="l" colspan="3">Avv. &nbsp;<font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSiep().getAvvocato().getCognome() +" "+notifica.getAvvSiep().getAvvocato().getNome())%></font></td>
	</tr>
 	<tr>
   		<td>&nbsp;</td>
<%
      		}
    	}
    	// Avvocato SIUS
    	if (notifica.getAvvIdAvvocatoFascicoloSius() != null) {
      		if (notifica.getAvvSius() != null) {
%>
		<td class="l" colspan="3">Avvocato &nbsp;<font class="campo"><%=StringUtils.toStringJSP(notifica.getAvvSius().getAvvocato().getCognome() +" "+notifica.getAvvSius().getAvvocato().getNome())%></font></td>
	</tr>
 	<tr>
   		<td>&nbsp;</td>
<%
      		}
    	}
    	// 23/05/2011 Curatore SIUS
    	if (notifica.getCurIdCuratore() != null) {
      		if (curatore.getCuratore() != null && curatore.getCuratore().getCognome() != null) {
%>
		<td class="l" colspan="3"><%=curatore.getDescrTipo().toLowerCase()%>&nbsp; <font class="campo"><%=StringUtils.toStringJSP(curatore.getCuratore().getCognome() +" "+curatore.getCuratore().getNome())%></font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
<%
      		}
    	}
    	// UEPE
    	if (notifica.getCssIdCssa() != null) {
      		if (notifica.getCSSA() != null) {
%>
		<td class="l" colspan="3">UEPE &nbsp;<font class="campo"><%=StringUtils.toStringJSP(notifica.getCSSA().getIndirizzo() +" "+notifica.getCSSA().getComune())%></font></td>
	</tr>
 	<tr>
   		<td>&nbsp;</td>
<%
      		}
    	}
%>
		<td colspan="3">&nbsp;</td>
	</tr>
<%
  		i++;
// 	} // END if modifica commento MEV_50
} // END while
%>
</table>
<table>
	<tr>
<%
	if (!read_only) {
%>
		<td>
		  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.provvedimento.action.ActAggiornaDateNotifiche">
			<input type="hidden" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IdEvento%>">
			<input class="bottone" type="submit" name="INSERISCI" value="Conferma" onclick="Javascript:return Verifica();">
		</td>
<%
	}
%>
	</tr>
</table>
<%
}
%>
</form>

</body>
<%
if (!read_only) {
%>
<script language="JavaScript">
var data_invio = new Array(<%=ii%>); // array contenente le date di invio notifica
for (i = 0; i < <%=ii%>; i++)
	data_invio[i] = "";
</script>
<%
}
%>
</html>