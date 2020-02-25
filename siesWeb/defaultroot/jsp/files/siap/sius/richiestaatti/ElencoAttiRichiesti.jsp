<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.richiestaatti.action.ICostantiRichiestaAtti" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.model.NotificaModel"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>

<jsp:useBean id="fascicoloSiusGP" scope="request" class="siap.sius.fascicolo.model.FascicoloGPModel"/>
<jsp:useBean id="atti"            scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita"        scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>

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

if (modalita.equals("M") && atti.size() > 0 )
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
      if (modalita.equals("M") && atti.size() > 0)
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
  if (fascicoloSiusGP != null)
  {
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
    if (!modalita.equals("M") || (fascicoloSiusGP.getFascicoloSiusModel().getCodStatoFascicolo()).equals("01"))
      archiviato = true;
%>
  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="ListaAtti">
  <table width="100%">
    <tr>
      <td class="int" width="30%">Tipo atto istruttorio richiesto</td>
      <td class="int" width="30%">Destinatario</td>
      <td class="int" width="10%">Data richiesta</td>
      <td class="int" width="23%">Data restituzione</td>
      <td class="int" width="23%">Stato validazione</td>
      <td class="int" width="7%">Azioni</td>
    </tr>
<%
    Iterator itx = atti.iterator();
   // int i=0;

    while ( itx.hasNext())
    {
  //    EventoModel atto = (EventoModel)itx.next();
        NotificaModel atto = (NotificaModel)itx.next();
%>
    <tr>
      <td class="l"><%=atto.getDescrizione()%></td>
      <td class="l">
<%
    // UFFICIO
    if(atto.getUfficio()!= null)
    {
%>
      <%=atto.getUfficio().getDescrTipoUfficio()%>&nbsp;di&nbsp; <%=atto.getUfficio().getDescrComune()%>
<%
    }
    // Autorità Esterna
    else if(atto.getAutoritaEsterna()!= null)
    {
%>
      <%=atto.getAutoritaEsterna().getDescrTipoAutorita()%>&nbsp;di&nbsp; <%=atto.getAutoritaEsterna().getDescrSede()%>
<%
    }
    // Avvocato SIEP
    else if(atto.getAvvIdAvvocatoFascicoloSiep()!=null)
    {
      if( atto.getAvvSiep() !=null)
      {
%>
            Avv. &nbsp;<%=StringUtils.toStringJSP(atto.getAvvSiep().getAvvocato().getCognome() +" "+atto.getAvvSiep().getAvvocato().getNome())%>&nbsp;
<%
      }
    }
    // Avvocato SIUS
    else if(atto.getAvvIdAvvocatoFascicoloSius()!=null)
    {
      if( atto.getAvvSius() !=null)
      {
%>
          Avvocato &nbsp;<%=StringUtils.toStringJSP(atto.getAvvSius().getAvvocato().getCognome() +" "+atto.getAvvSius().getAvvocato().getNome())%>&nbsp;
<%
      }
    }
    // UEPE
    else if(atto.getCssIdCssa()!=null)
    {
      if( atto.getCSSA() !=null)
      {
%>
				UEPE &nbsp;<%=StringUtils.toStringJSP(atto.getCSSA().getIndirizzo() +" "+atto.getCSSA().getComune())%>&nbsp;
<%
      }
    } // Gestione dell' Istituto di Detenzione.
    else if( atto.getIstDetIdIstitutoDetenzione() != null )
    {
      if( atto.getIstitutoDetenzione() != null )
      {
%>
        <%=atto.getIstitutoDetenzione().getDescrTipoIstituto()%>&nbsp;di&nbsp; <%=atto.getIstitutoDetenzione().getDescrizione()%>
<%
      }
    }
%>
      </td>
      
      <td class="c">
      	<%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataInvio(),"dd-MM-yyyy"),"-") %>
      </td>

      <td class="c">
<%
      	if (modalita.equals("M")&& !archiviato)
      	{
%>
        	<input onchange="Javascript:aggiornaID('<%=i%>','<%=atto.getIdNotifica()%>','<%=DateUtils.getDateToString(atto.getDataInvio(),"dd/MM/yyyy")%>');"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(atto.getDataAvvenutaNotifica(),"dd")) %>" type="text" maxlength="2" size="2"
        		name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA%>" onBlur="javascript:value=FillDM(value)">
        	-
        	<input onchange="Javascript:aggiornaID('<%=i%>','<%=atto.getIdNotifica()%>','<%=DateUtils.getDateToString(atto.getDataInvio(),"dd/MM/yyyy")%>');"
        		value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(atto.getDataAvvenutaNotifica(),"MM")) %>" type="text" maxlength="2" size="2"
        		name="<%=ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA%>" onBlur="javascript:value=FillDM(value)">
        	-
        	<input onchange="Javascript:aggiornaID('<%=i%>','<%=atto.getIdNotifica()%>','<%=DateUtils.getDateToString(atto.getDataInvio(),"dd/MM/yyyy")%>');"
        		value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataAvvenutaNotifica(),"yyyy"))%>" type="text" maxlength="4" size="4"
        		name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA%>">
<%
      	}
      	else
      	{
%>
      		<%=StringUtils.toStringJSP(DateUtils.getDateToString(atto.getDataAvvenutaNotifica(),"dd-MM-yyyy"),"-")%>
<%
      	} // endif modalita     	
%>
			</td>
			
			<td class="c">
			<%
				if ( (atto.getFlagDocRegistrato()!=null) && (atto.getFlagDocRegistrato().compareTo("S")==0 ) )
        {
			%>
					<a href="Javascript:annulla('Vuoi annullare la validazione della richiesta atto istruttorio ?','<%=lFunAnnullaValidazione%>','<%=ICostantiEvento.CAMPO_ID_EVENTO%>','<%=atto.getEveIdEvento()%>');">
						<img src="/images/TickRed.gif" alt="Annulla validazione richiesta atto istruttorio"  border="0">
					</a>
			<%
				}
      	else
      	{
      %>
          -
			<%
        }
			%>
        <input  value="" name="<%=ICostantiNotifica.CAMPO_ID_NOTIFICA%>" type = "HIDDEN">
      </td>

			<% 
				String isBlob = "SI"; if(atto.getFlagDocRegistrato() == null){isBlob="NO";}
      	String isSolleciti = "SI"; // if(atto.getSollecito() = null){isAllegato="NO";}
			%>
			
      <td class="l">
        <jsp:include page="<%=ICostantiRichiestaAtti.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita" value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=atto.getEveIdEvento()%>" />
          <jsp:param name="CampoRitorno" value="TornaQui" />
          <jsp:param name="ValCampoRitorno" value="<%=TornaQui%>"/>
          <jsp:param name="Solleciti" value="<%=isSolleciti%>" />
          <jsp:param name="Stampa" value="<%=isBlob%>" />
        </jsp:include>
      </td>
    </tr>
<%
    i++;
   }
%>

</table>
  <% if(!archiviato) {%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.richiestaatti.action.ActAggiornaDateAtti" >
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