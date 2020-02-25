<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>

<%@ page import="siap.jms.messaggio.model.MessaggioModel"%>
<%@ page import="siap.jms.messaggio.action.ICostantiMessaggio"%>
<%@ page import="siap.jms.ICostantiJMS"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>


<jsp:useBean id="IstruttoriaCumulo" scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="Messaggi"          scope="request" class="java.util.Vector"/>

<%
//===================================================================================
// Jsp per la visualizzazione delle richieste atti di trasmissione cmptz per Cumulo
//===================================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Riscontro Trasmissione/Solleciti</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    
    <script language="JavaScript">
    
    function eseguiAzione(aTipoAzione, aIdMes)
    {
        lAzione = "siap.siep.modulocumulo.action.ActDettaglioRichiestaAttiCumulo";
        document.f.<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO %>.value = aIdMes;
        document.f.<%=IWebConstants.ACTION_FIELD%>.value = lAzione;
        document.f.submit();
    }
    
    //==========================================================================
    // Ritorna alla Griglia Della Gestione Cumulo
    //==========================================================================
    function tornaIndietro(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }
    </script>
    
  </head>

 <body class="corpo">
 <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font><font class="campo">Riscontro Richieste/Solleciti Trasmessi</font>&nbsp;&nbsp;
     </td>
      <!-- BOTTONE DI RITORNO -->
      <td class="LBG">
        <a href="javascript:tornaIndietro('siap.siep.istruttoriacumulo.action.ActLoadGrigliaCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>
  <br>
  
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>

  <FORM action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="">
    <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
    <input type="hidden" name="<%=ICostantiMessaggio.CAMPO_ID_MESSAGGIO%>" >
 

<% if (Messaggi.size() == 0)  { %>
  <br>
  <table cellspacing=2 cellpadding=2 width="99%">
    <tr>
      <td class="int">Anno/Numero <br>SIEP</td>
      <td class="int">Ufficio Destinatario</td>
      <td class="int">Data Invio</td>
      <td class="int">Soggetto</td>
      <td class="int">Esito</td>
      <td class="int">Data Ultimo <br> Sollecito</td>
      <td class="int">Azioni</td>
    </tr>
    <tr>
      <td class="c" colspan="7">  <br>Non sono attualmente presenti Richieste o Solleciti <br> </td>
    </tr>
  </table>
<% } else { %>

  <div align="center">
  <table cellspacing=2 cellpadding=2 width="99%">
    <tr>
      <td class="int">Anno/Numero <br>SIEP</td>
      <td class="int">Ufficio Destinatario</td>
      <td class="int">Data Invio</td>
      <td class="int">Soggetto</td>
      <td class="int">Esito</td>
      <td class="int">Data Ultimo <br> Sollecito</td>
      <td class="int">Azioni</td>
    </tr>
    
<%
  String coloreLinea = "c"; 
  Iterator itx = Messaggi.iterator();
  while ( itx.hasNext())
  {
    MessaggioModel lMess = (MessaggioModel)itx.next();
    coloreLinea = "c";
    if ( lMess.getFlagVisto().compareTo("S")==0 && 
   		(lMess.getCodEsito().compareTo("01006")==0 || lMess.getCodEsito().compareTo("01001")==0 ) )
      coloreLinea = "cVerde";
    
	String DataUltSoll="";
		
%>
  <tr>
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getChiaveAnnoSiep())%>/<%=StringUtils.toStringJSP(lMess.getChiaveProgrSiep())%></td>     
    <td class="<%=coloreLinea%>">
      <%= lMess.getDescrUfficioDestinatario() +" "+ lMess.getDescrSedeUfficioDestinatario()%>
      <% 
      if (lMess.getMessaggiSollecito()!=null && lMess.getMessaggiSollecito().size()>0) 
	  {
          Vector lMessaggiSollecito = lMess.getMessaggiSollecito();
          Iterator itxSoll = lMessaggiSollecito.iterator();
          while ( itxSoll.hasNext())
          {
          	  MessaggioModel lMessSoll = (MessaggioModel) itxSoll.next();
          	  if(lMessSoll.getDataInvio()!=null )
          	  {	  
          	  //DataUltSoll = DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm");
              %>
              <br>
                <font color="red">Inviato Sollecito il <%= StringUtils.toStringJSP(DateUtils.getDateToString(lMessSoll.getDataInvio(),"dd-MM-yyyy HH:mm"),"") %></font>
<%	  		  } 
       	  }
          
          MessaggioModel UltimoSoll = (MessaggioModel)lMessaggiSollecito.get(0);
          DataUltSoll = DateUtils.getDateToString(UltimoSoll.getDataInvio(),"dd-MM-yyyy HH:mm");
	 } 	%>
   </td>    
    
    <td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
    <td class="<%=coloreLinea%>"><%= lMess.getNomeSoggetto()%>&nbsp;<%= lMess.getCognomeSoggetto()%></td>

<%// Esito %>
<%	if(lMess.getCodEsito()!=null && !lMess.getCodEsito().equals("-") )
	{
		if(lMess.getCodEsito().compareTo("01007")==0 || lMess.getCodEsito().compareTo("01003")==0 )
		{ %>
			<td class="cRosso" nowrap><%=lMess.getDescrEsito()%><br><%=lMess.getRapportoEsito().substring(16)%></td>
<%		}
		else if(lMess.getCodEsito().compareTo("01006")==0 || lMess.getCodEsito().compareTo("01001")==0 ) 
		{ %>
			<td class="<%=coloreLinea%>" nowrap><%=lMess.getDescrEsito()%><br><%=lMess.getRapportoEsito().substring(16)%></td>
<%		}
		else if(lMess.getCodEsito().compareTo("01009")==0 )
		{	%>
			<td class="cverde" nowrap><%=lMess.getDescrEsito()%> e comunicazione trasmessa<br><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMess.getDataInvio(),"dd-MM-yyyy HH:mm"))%></td>
<% 		}			
	}							
	else 
	{ 
		if(lMess.getRapportoEsito().substring(0, 9).compareTo("In Attesa")==0 )
		{	%>    
    		<td class="cRosso"><%= lMess.getRapportoEsito()%></td>
<%		}
		else if(lMess.getRapportoEsito().substring(0, 7).compareTo("Inviato")==0)
		{ %>
			<td class="<%=coloreLinea%>"><%= lMess.getRapportoEsito()%></td>
<%		} 
		else
		{	%>
			<td class="<%=coloreLinea%>"><%= StringUtils.toStringJSP(lMess.getDescrEsito())%></td>
<%		} 	
	}		%>
<%// Data Ultimo Sollecito %>    
<%	if(DataUltSoll.compareTo("")!=0)
  	{%>
    	<td class="<%=coloreLinea%>"><%=DataUltSoll%></td>
<%	}
  	else
  	{ %>    
 		<td class="c"> - </td>
<%	} %>

<%// Azioni %>      
    <td class="<%=coloreLinea%>">
    <table>
      <tr>
        <td>
      		<a href="javascript:eseguiAzione('Dettaglio',<%=lMess.getIdMessaggio()%> )">
          	<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Richiesta" border="0"></a>
        </td>
        <%
        
        //======================================================================
        // TEST se il parser non può leggere il messaggio si segnala come 
        // non elaborabile
        //======================================================================
        %>
<%--         <% if (lMess.getIsErroreParser() && 1==2) { %>         --%>
<!--         <td> -->
<!--           <font color="red"><b><img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione. Messaggio non elaborabile in quanto inviato con una versione antecedente di SIEP" border="0"></b></font> -->
<!--         </td> -->
<%--         <% } %> --%>
      </tr>
    </table>

      
    </td>
  
    <% if (lMess.getIsErroreParser()) { %>
      <td>
        <font color="red"><b><img src="/images/attenzione.jpg" width="12" height="12" alt="Attenzione. Messaggio non elaborabile in quanto inviato con una versione SIEP differente da quella attualmente in uso da questo Ufficio" border="0"></b></font>
      </td>
    <% } %>
  </tr>
<%
  }
%>
    </table>
  </div>
<% } %>

  </FORM>
  </body>
</html>