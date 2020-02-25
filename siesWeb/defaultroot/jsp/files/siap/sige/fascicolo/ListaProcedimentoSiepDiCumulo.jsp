<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>
<%@ page language="java" import="f3b.web.IWebConstants" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ListIterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiDecodifiche"%>
<%@ page import="siap.sige.sentenza.model.SentenzaSigeModel"%>
<%@ page import="siap.siep.cumulo.model.*"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.web.RedirectTo"%>
<%@ page import="siap.web.ISIAPCostantiWeb"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.sentenza.model.SentenzaModel"%>


<jsp:useBean id="ListaProcedimentoSiepDiCumulo" scope="request" class="java.util.Vector" />
<jsp:useBean id="SentenzaSiep" scope="request" class="siap.siep.sentenza.model.SentenzaModel" />

<html>
  <head>
    <title>[S.I.E.S.] - Lista Titoli Esecutivi</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
    <script language="JavaScript">
      var strDescr='';
      var strCode='';
      
      //function impostaCumuloSentenza(idSentenzaCumulo)
      //{    
      //	confermaCumuloSentenza('siap.sige.fascicolo.action.ActModificaFascicoloCumuloSentenza','senIdSentenzaCumulo', idSentenzaCumulo) 	
      //	window.opener.refresh();
      //	self.close();
      //}

      function impostaCumulo(idSentenzaCumulo, idEventoProvvCumulo)
      {   
      	confermaCumulo('siap.sige.fascicolo.action.ActModificaFascicoloCumuloSentenza', 'senIdSentenzaCumulo', idSentenzaCumulo, 'idEventoProvvCumulo', idEventoProvvCumulo) 	
      	window.opener.refresh();
      	self.close();
      }
      
      var str;
	  //function confermaCumuloSentenza(a_action, a_entityname, a_entityvalue)
	  //{
	  // 	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue;
	  // 	if (window.confirm('Confermi il Provvedimento Selezionato?')) {
	  //   	window.location.href=str;
	  //  }
	  //}

	  function confermaCumulo(a_action, a_entityname, a_entityvalue, a_entityname1, a_entityvalue1)
	  {
	   	str = "/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname + "=" +a_entityvalue + "&" + a_entityname1 + "=" +a_entityvalue1;
	   	//alert('confermaCumulo');
	   	if (window.confirm('Confermi il Provvedimento Selezionato?')) {
	     	window.location.href=str;
	    }
	  }
	  
    </script>
  </head>

  <body>
  <form name="procedimentiSiepDiCumulo">
    <table>
      <tr>
        <td class=LBG>Selezionare IL Provvedimento</td>
      </tr>
    </table>

    <table width="100%">
<%
	RedirectTo lRedir = new RedirectTo();
	//CumuloModel lCumulo = new CumuloModel();
	EventoModel lEvento = new EventoModel();
	if (ListaProcedimentoSiepDiCumulo == null || ListaProcedimentoSiepDiCumulo.size() < 1) {
%>
		<tr>
			<td>
				<font class="campo">Nessun Cumulo associato al Procedimento</font>
			</td>
		</tr>
<%

	} else {
		Iterator lIterProcedimentoSiepDiCumulo = ListaProcedimentoSiepDiCumulo.iterator();
		
   		while ( lIterProcedimentoSiepDiCumulo.hasNext() ) {
   			//lCumulo = (CumuloModel)lIterProcedimentoSiepDiCumulo.next();
   			lEvento = (EventoModel)lIterProcedimentoSiepDiCumulo.next();
 %>
		<tr>
			<%-- <td class=l>Provvedimento di determinazione pene concorrenti <%=StringUtils.toStringJSP(DateUtils.getDateToString(lCumulo.getSentenza().getDataProvvedimento(),"dd-MM-yyyy"))%></td> --%>			
			<%-- <td class=l>Provvedimento di determinazione pene concorrenti <%=StringUtils.toStringJSP(DateUtils.getDateToString(SentenzaSiep.getDataProvvedimento(),"dd-MM-yyyy"))%></td> --%>
			<% if(lEvento.getDescrTipoProvvedimento() != null && lEvento.getDescrMotivo() != null) { %>
					<td class=l><%=lEvento.getDescrTipoProvvedimento()%>&nbsp;<%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%>&nbsp;<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
			<% } else { %>
					<td class=l>Provvedimento di determinazione pene concorrenti <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"))%></td>
			<% } %>			
			
			<td class=l>
				<%-- <input type="radio" name="provvedimentoCumulo" value="<%=lCumulo.getSenIdSentenza()%>" onclick="Javascript:impostaCumuloSentenza(<%=lCumulo.getSenIdSentenza()%>);" > --%>
				<%-- <input type="radio" name="provvedimentoCumulo" value="<%=lEvento.getIdEvento()%>" onclick="Javascript:impostaCumuloSentenza(<%=SentenzaSiep.getIdSentenza()%>);" > --%>
				<input type="radio" name="provvedimentoCumulo" value="<%=lEvento.getIdEvento()%>" onclick="Javascript:impostaCumulo(<%=SentenzaSiep.getIdSentenza()%>,<%=lEvento.getIdEvento()%>);" >
			</td>
		</tr>
<% 
        }  // end while     
	}  // end else
%>
  </table>
  </form>
</body>
</html>