<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.sige.richiestaatti.action.ICostantiRichiestaAtti" %>
<%@ page import="siap.sico.evento.model.EventoNotificaModel" %>
<%@ page import="f3b.web.IWebConstants" %>
<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>
<%@ page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige" %>
<%@ page import="siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel"%>

<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>
<jsp:useBean id="FascicoloSigeEsteso" 	scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" />
<!--jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/-->

<%
    Vector <ProvvedimentoSigeEventoModel> atti=(Vector <ProvvedimentoSigeEventoModel>)request.getAttribute("atti");
%>

<html>
<head>
  <title>[S.I.E.S.] - Elenco Atti Istruttori Richiesti</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  
  <script language="JavaScript" src=<%=IWebConstants.JS_CONFIRM %>></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>

<%
String lFunAnnullaValidazione = "siap.sius.richiestaatti.action.ActAnnullaValidazioneRichiestaAttoIstruttorio";

boolean archiviato = false;
int i = 0;

//if (modalita.equals("M") && atti.size() > 0 )
// MEV 15 - Revisione SIGE
if (atti.size() > 0 )
{
%>
  <script language="JavaScript">

  var lung;   // numero di campi d'aggiornamento.

  function inizializza()
  {
    if (typeof(document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.length) == "undefined")
      lung = 0;
    else
      lung = document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.length;
   //alert("lung : " +lung);
  return;
  }

  function aggiornaID(i, ind, data)
  {
    if (lung == 0)
    {
       document.ListaAtti.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>.value = ind;
    }
    else
    {
       document.ListaAtti.<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>[i].value = ind;
    }
    data_invio[i] = data;
  }

  function ControlliDate(DataIni,data_to_verify)
  {
    var ritorno = true;

    //alert("data1: " + DataIni);
    //alert("data2: " + data_to_verify);

    if (data_to_verify.length > 2 && ControllaData(DataIni))
    {
      if (!ControllaData(data_to_verify))
      {
        alert('Data scorretta: '+ data_to_verify);
        ritorno = false;
      }
      else if (! CompareDate(DataIni, data_to_verify))
      {
          alert('Data di Restituzione -' +data_to_verify  +'- non può precedere quella di richiesta -' + DataIni + '-.');
          ritorno = false;
      }
    }
    return ritorno;
  }

  function  Verify()
  {
    var ritorno = true;
    var data_ricezione;
    var i;

    if (lung == 0)
    {
      data_ricezione = document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>.value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>.value;
      ritorno = ControlliDate(data_invio[0],data_ricezione);
    }
    else
    {
      for(i = 0; ritorno &&(i < lung); i++)
      {
    	data_ricezione = document.ListaAtti.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>[i].value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>[i].value+'/'+document.ListaAtti.<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>[i].value;
        ritorno = ControlliDate(data_invio[i],data_ricezione);
      }
    }
    return ritorno;
  }
 </script>
<%
  } // endif modalita
%>

</head>
<%
      //if (modalita.equals("M") && atti.size() > 0)
      // MEV 15 - Revisione SIGE
      if (atti.size() > 0)
      {
%>
<body class="corpo" onLoad="inizializza()">
<%
     }else
      {
%>
<body class="corpo">
<%
  } // endif modalita
%>

   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
  if (modalita.equals("M"))
  {
%>
        <font class="label">Stato Atti Richiesti / Solleciti </font>
<%
  } else {
%>
        <font class="label">Ricerca Atti Richiesti</font>
<%
  }
%>
      </td>
  <!-- BOTTONE DI RITORNO -->
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
   </table>
<br>
<%
  if (FascicoloSigeEsteso != null)
  {
%>
   <table>
      <tr>
        <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
      </tr>
   </table>
<%
  } // endif fascicoloSige
%>
<br>
<%
  if ( atti.size() == 0 )
  {
%>
        <td class="LBG">
          <font class="label"> Non ci sono richieste di atti per il procedimento. </font>
        </td>
<%
  } else
  {
    archiviato = false;
    // MEV 15 - Revisione SIGE
    //if (!modalita.equals("M") || (FascicoloSigeEsteso.getFascicoloSige().getCodStatoFascicolo()).equals("01"))
    //  archiviato = true;
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="100%">
    <tr>
      <td class="int" width="30%">Tipo atto istruttorio richiesto</td>
      <td class="int" width="30%">Destinatario</td>
      <td class="int" width="10%">Data richiesta</td>
      <td class="int" width="23%">Data restituzione</td>
      <td class="int" width="7%">Azioni</td>
    </tr>
<%
    for (ProvvedimentoSigeEventoModel lProvEve : atti) {
        EventoNotificaModel lEveNot = lProvEve.getEventoNotifica();
        NotificaModel[] lNotifiche = lEveNot.getNotifiche();
        
        String tipoAtto=lEveNot.getEvento().getDescrMotivo();
        if (tipoAtto.equalsIgnoreCase("parere"))
        	continue;
		
    // UFFICIO
    for(NotificaModel notifica : lNotifiche)
    {%>
    
    <tr>
    	
    	<td class="l"><%=lEveNot.getEvento().getDescrMotivo() %>
    	
    	<!-- /@emma 13072018 intervento post COLLAUDO 11.2  (stampo l'oggetto della richiesta che corrisponde alla nota posizione 0)     --> 
	    <% if (lEveNot.getCampoNote()!= null && lEveNot.getCampoNote().length > 0 )
	    {
	     int lSizeCampoNote = lEveNot.getCampoNote().length;
	      if (lSizeCampoNote > 0 && "0708".equals(lEveNot.getEvento().getCodMotivo())){      %>
	      
	          &nbsp;<font class="campo"><%=StringUtils.toStringJSP( lEveNot.getCampoNote()[0].getDescr())%></font>
	        
	      <%
	     }
	    }
	    %>
    
        </td>
        <td class="l">
    	
  <%  	
        if(notifica.getUfficio() != null && notifica.getUfficio().getCodUfficio().length() > 1)
        {
%>
      <%=notifica.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=notifica.getUfficio().getDescrComune()%>
<%
    }
    // Autorità Esterna
    else if(notifica.getAutoritaEsterna()!= null)
    {
    	if(notifica.getAutoritaEsterna().getDescrTipoAutorita() != null && !notifica.getAutoritaEsterna().getDescrTipoAutorita().equals("-")){
    %>
    
      		<%=notifica.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;di&nbsp; <%=notifica.getAutoritaEsterna().getDescrSede()%>
<%
    	} else {
%>
			Ufficio Anagrafe&nbsp;di&nbsp; <%=notifica.getAutoritaEsterna().getDescrSede()%>
<%   		
    	}
    }
    // Avvocato SIEP
    else if(notifica.getAvvIdAvvocatoFascicoloSige()!=null)
    {
      if( notifica.getAvvSiep() !=null)
      {
%>
            Avv. &nbsp;<%=StringUtils.toStringJSP(notifica.getAvvSiep().getAvvocato().getCognome() +" "+notifica.getAvvSiep().getAvvocato().getNome())%>&nbsp;
<%
      }
    }
    // Avvocato SIUS
    else if(notifica.getAvvIdAvvocatoFascicoloSige()!=null)
    {
      if(notifica.getAvvSige() !=null)
      {
%>
          Avvocato &nbsp;<%=StringUtils.toStringJSP(notifica.getAvvSige().getAvvocato().getCognome() +" "+notifica.getAvvSige().getAvvocato().getNome())%>&nbsp;
<%
      }
    }
    // UEPE
    else if(notifica.getCssIdCssa()!=null)
    {
      if( notifica.getCSSA() !=null)
      {
%>
				UEPE &nbsp;<%=StringUtils.toStringJSP(notifica.getCSSA().getIndirizzo() +" "+notifica.getCSSA().getComune())%>&nbsp;
<%
      }
    } // Gestione dell' Istituto di Detenzione.
    else if( notifica.getIstDetIdIstitutoDetenzione() != null )
    {
      if( notifica.getIstitutoDetenzione() != null )
      {
%>
        <%=notifica.getIstitutoDetenzione().getDescrTipoIstituto()%>&nbsp;di&nbsp; <%=notifica.getIstitutoDetenzione().getDescrizione()%>
<%
      }
    }
%>
      </td>
      
      <td class="c">
      	<!-- MEV 15 - Revisione SIGE -->
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEveNot.getEvento().getDataEmissione(),"dd-MM-yyyy"),"-") %>
      </td>

      <td class="c">
        	<input onchange="Javascript:aggiornaID('<%=i%>','<%=notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"dd")) %>" type="text" maxlength="2" size="2"
        		name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        	-
        	<input onchange="Javascript:aggiornaID('<%=i%>','<%=notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"MM")) %>" type="text" maxlength="2" size="2"
        		name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"onBlur="javascript:value=FillDM(value)">
        	-
        	<input onchange="Javascript:aggiornaID('<%=i%>','<%=notifica.getIdNotifica()%>','<%=DateUtils.getDateToString(notifica.getDataInvio(),"dd/MM/yyyy")%>');"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(notifica.getDataAvvenutaNotifica(),"yyyy")) %>" type="text" maxlength="4" size="4"
        		name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>"
        		onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
		    <input value="" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" type="HIDDEN">
	  </td>
		
			<% 
				String isBlob = "SI"; 
				if(lProvEve.getEventoNotifica().getEvento().getFlagDocumentoRegistrato()==null)
				{
					isBlob="NO";
				}
      	        String isSolleciti = "SI"; // if(atto.getSollecito() = null){isAllegato="NO";}
			%>
			
      <td class="l">
        
       <jsp:include page="<%=ICostantiRichiestaAtti.PG_BUTTONS %>">
	       <jsp:param name="CampoIdEntita" value="<%=ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE%>" />
	       <jsp:param name="ValoreIdEntita" value="<%=lProvEve.getProvvedimento().getIdProvvedimentoSige()%>" />
	       <jsp:param name="CampoIdEntitaProvv" value="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>"/>
	       <jsp:param name="ValoreIdEntitaProvv" value="<%=lProvEve.getEventoNotifica().getEvento().getCodMotivo()%>" />
	       <jsp:param name="CampoRitorno" value="TornaQui" />
	       <jsp:param name="ValCampoRitorno" value="<%=TornaQui%>"/>
	       <jsp:param name="Solleciti" value="<%=isSolleciti%>" />
	       <jsp:param name="Stampa" value="<%=isBlob%>" />
	       <jsp:param name="idEvento" value="<%=lProvEve.getEventoNotifica().getEvento().getIdEvento()%>" />
       </jsp:include>
        
      </td>
    </tr>
<%
		i++;
    }

  }
%>

</table>

  <% if(!archiviato) {%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sige.richiestaatti.action.ActAggiornaDateAtti" >
          <td>
            <input onclick="Javascript:return Verify();" class="bottone" type="submit" value="Conferma">
          </td>
  <% } %>
    </FORM>
<%
  }  // endif atti.size()
%>
  </body>
<% if(!archiviato) {%>
  <script language="JavaScript">
    var data_invio = new Array(<%=i%>); // array contenente le date di invio necessarie per il controllo
    for (i=0; i < <%=i%>; i++)
      data_invio[i] = "";
  </script>
<% } %>
</html>