<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.ordineesecuzione.action.ICostantiOrdineEsecuzione"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />
<jsp:useBean id="eventi" scope="request" class="java.util.Vector"/>
<jsp:useBean id="ordinamento" scope="request" class="java.lang.String"/>


<%
// Preparazione del bottone di riordino date
  String lRet = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.evento.action.ActRicercaProvvedimentiNonValidati";
%>

<html>
  <head>
    <title>[S.I.E.S.] - Elenco Provvedimenti </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
		<script language="JavaScript">
		function conferma(a_action, a_parameter, a_entityname ,a_parameter2 ,a_entityname2,valoreordinamento)
		{
		  var documentoRegistrato = a_entityname2;
		  if (window.confirm('Confermi la cancellazione ?'))
		  {
		    if (documentoRegistrato=="S")
		    {
		       var  desktop = window.open("<%= IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=" + a_action + "&" + a_parameter + "=" +a_entityname +"&lOrdinamento="+valoreordinamento, "Cancella_provvedimento","  top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
		         window.parent.close();
		    }
		    else
		    {
		      str = "/jsp/Main.jsp?Action=siap.siep.ordineesecuzione.action.ActCancellaProvvedimento&" +a_parameter +"=" + a_entityname+"&lOrdinamento=<%=ordinamento%>" + "&nextAction=siap.sico.evento.action.ActRicercaProvvedimentiNonValidati";
		      window.location.href=str;
		    }
		  }
		}
		
		function chiama(idEvento)
		{
			window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.ordineesecuzione.action.ActLoadCancellaProvvedimento&IdEvento="+idEvento,"Cancella_provvedimento", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
		}
		
		function pr()
		{
			sign = prompt("Confermi la cancellazione?");
		}
		
		</script>

  </head>

  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione : </font>
      <font class="campo">Elenco Provvedimenti PM Non validati</font>&nbsp;&nbsp;
    <input type="HIDDEN" name="validato" value="">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="">
     </td>
    </tr>
  </table>
	<br>	
	<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
	<br>
  <div align="center">
  <table cellspacing=2 cellpadding=2>
    <tr>
			<td class="int">Numero SIEP</td>
<%
     if("EM".equals(ordinamento))
     {
%>
      <td class="int"><font class="cRosso">Data Emissione</font>
<%
     }
     else
     {
%>
       <td class="int">Data Emissione
<%   
     }
%>
        <a href="<%=lRet%>&lOrdinamento=EM">
         <img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="ordina per" border="0">
        </a>&nbsp;&nbsp;
      </td>
<%
     if("IN".equals(ordinamento))
     {
%>
      <td class="int"><font class="cRosso">Data Inserimento</font>
<%
     }
     else
     {
%>
      <td class="int">Data Inserimento
<%
     }
%>
        <a href="<%=lRet%>&lOrdinamento=IN">
         <img align="bottom" src="<%=IWebConstants.IMAGES_DIR%>/bottoni/modifica.gif" alt="ordina per" border="0">
        </a>&nbsp;&nbsp;
      </td>
      <td class="int">Provvedimento</td>
      <td class="int">Autorità</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = eventi.iterator();
  while ( itx.hasNext())
  {
    EventoModel lEvento = (EventoModel)itx.next();

    // Se l'utente è un utente SIUS e il Provvedimento non è validato il Provvedimento non viene visualizzato
    if( ((UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("TDS") || UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("UDS")) && (lEvento.getFlagDocumentoRegistrato()!= null && !lEvento.getFlagDocumentoRegistrato().equals("N")))
        || (!UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("TDS") && !UtenteConnesso.getUfficioUtente().getCodTipoUfficio().equals("UDS"))
      )
    {
%>
    <tr>
      <td class="c"><%=StringUtils.toStringJSP(lEvento.getChiaveAnno())%>/<%=StringUtils.toStringJSP(lEvento.getChiaveProgr())%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
      <td class="l"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataInserimento(),"dd-MM-yyyy"))%></td>
      <td class="l"><%=StringUtils.toStringJSP(lEvento.getDescrTipoProvvedimento())%>
           &nbsp;<%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%>
      </td>
      <td class="l"><%= lEvento.getDescrUfficioEmittente()+ " " + lEvento.getDescrLuogoEmittente()%></td>
      <td class="c">
<%
				String isBlob="SI";
				if(lEvento.getFlagDocumentoRegistrato() == null)
        {
          isBlob="NO";
        }
				
        if(lEvento.getDocBlobOut()==null || lEvento.getDocBlobOut().size()==0)
        {
           isBlob = "NO";
        }
        
        //Flag Documento Registrato
        String lDocReg = "N";
        if(  lEvento.getFlagDocumentoRegistrato() != null
          && lEvento.getFlagDocumentoRegistrato().equals("S"))
        {
          lDocReg = "S";
        }
      
        if(  lEvento.getFlagDocumentoRegistrato() != null
          && lEvento.getFlagDocumentoRegistrato().equals("A"))
        {
          lDocReg = "A";
        }
      	
        if(  lEvento.getCodOperatoreInserimento() != null
          && lEvento.getCodOperatoreInserimento().startsWith("res-"))
        {
          lDocReg = "M";
        }

 		 	 String modificabile = "";

 		 //if (UtenteConnesso.getUfficioUtente().getCodUfficio().equals(lEvento.getCodUfficioInserimento()))
		 if (UtenteConnesso.getUfficioUtente().isUfficioDiCompetenza(lEvento.getCodUfficioInserimento()))
		   {
		     modificabile = "SI";
		   }
		   else
		   {
		     modificabile = "NO";
		   }
%>
        <jsp:include page="<%=ICostantiOrdineEsecuzione.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" />
          <jsp:param name="ValoreIdEntita" value="<%=lEvento.getIdEvento()%>" />
          <jsp:param name="CampoIdEntitaProvv" value="campo" />
          <jsp:param name="ValoreIdEntitaProvv" value="<%=lEvento.getFlagDocumentoRegistrato()%>" />
          <jsp:param name="TipoProvvedimento" value="<%=lEvento.getCodTipoProvvedimento()%>" />
          <jsp:param name="MotivoEvento" value="<%=lEvento.getCodMotivo()%>" />
          <jsp:param name="TipoEvento" value="<%=lEvento.getCodTipoEvento()%>" />
          <jsp:param name="TemIdTemplate" value="<%=lEvento.getTemIdTemplate()%>" />
          <jsp:param name="modalita" value="NV" />
          <jsp:param name="docRegistrato" value="<%=lDocReg%>" />
          <jsp:param name="Evento" value="<%=isBlob%>" />
          <jsp:param name="Modificabile" value="<%=modificabile%>" />
          <jsp:param name="EventoCancellareAnnullare" value="<%=lEvento.getIdEvento()%>" />
          <jsp:param name="lOrdinamento" value="<%=ordinamento%>" />
        </jsp:include>
      </td>
    </tr>
<%
    }
   }
%>
  </table>
  </div>
</body>
</html>