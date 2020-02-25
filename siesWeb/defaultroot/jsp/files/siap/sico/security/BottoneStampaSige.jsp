<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="java.util.Collection" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>


<jsp:useBean id="Stampabile"     scope="request" class="java.lang.String"/>
<jsp:useBean id="Upload"         scope="request" class="java.lang.String"/>
<%
String idEvento=request.getParameter( ICostantiEvento.CAMPO_ID_EVENTO );
if (idEvento==null)
	idEvento="";
%>

  <script language="JavaScript">

   function stampaSige(lAzione)
   {
       var template = "&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=";
       var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>="+lAzione+"&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_ID_EVENTO%>=<%=idEvento%>";
       // Se nel dettaglio esiste la Lista di Template si legge il valore
       if (document.dettaglio != undefined && document.dettaglio.ListaTemplate != undefined)
       {
          template =  template + document.dettaglio.ListaTemplate.value;
          hrefStampa = hrefStampa + template;
          //alert("template ->" + template);
          //alert("hrefStampa ->" + hrefStampa);
       }

     stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
   }
 </script>



<%
  if (Stampabile == null || Stampabile.trim().length() < 1)
    Stampabile = "SI";
  // Flag per attivare la funzione di Upload. Può essere disattivata
  // passando UploadNO nella request
  boolean lUpload = false;

  if (Upload == null || Upload.trim().length() < 1  ||  Upload.compareTo("SI") == 0)
  	lUpload = true;

	/*
	if (Upload != null && UploadNO.trim().length() > 0)
  	lUpload = false;
	*/

  if (Stampabile.compareTo("SI") == 0) {
    Collection <FunctionModel>lFunFiglie = (Collection<FunctionModel>)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

    //Visualizzazione dei bottoni di Stampa
    if( lFunFiglie != null  ) {
      for (FunctionModel lFun : lFunFiglie) {   
        if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) ) {
%>
     <!-- BOTTONE DI STAMPA -->
     <td class="LBG">
      <a href="Javascript:stampaSige('<%=lFun.getNameAction()%>')" <% if (lUpload) { %> onclick="javascript:lookUpload();" <% }%>>
        <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
      </a>
     </td>

<%
        }
      } // endwhile
     // Flag passato nella request per eliminare la funzione di Upload
     if (lUpload)
     {
%>
     <!-- BOTTONE DI UPLOAD -->
     <td class="LBG">
      <a  href="#1" onclick="javascript:lookUpload();">
        <img  align="middle" src="/images/upload24.gif" alt="Upload Stampa" width="24" height="24" border="0">
      </a>
     </td>
<%
     }
    }
  } // endif Stampabile
%>