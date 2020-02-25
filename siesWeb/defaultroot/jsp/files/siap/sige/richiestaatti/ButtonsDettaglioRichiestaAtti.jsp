<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.template.action.ICostantiTemplate" %>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<jsp:useBean id="Stampabile"            	scope="request" class="java.lang.String"/>
<jsp:useBean id="UploadNO"              	scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     						scope="request" class="java.lang.String"/>

  <script language="JavaScript">

   function stampaSius(lAzione)
   {
       var template = "&<%=ICostantiTemplate.CAMPO_ID_TEMPLATE%>=";
       var  hrefStampa = "<%=IWebConstants.ACTION_FIELD%>="+lAzione+"&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>";
       // Se nel dettaglio esiste la Lista di Template si legge il valore
       if (document.dettaglio != undefined && document.dettaglio.ListaTemplate != undefined)
       {
          template =  template + document.dettaglio.ListaTemplate.value;
          hrefStampa = hrefStampa + template;
          //alert("template ->" + template);
       }

     stampa2("<%=ISIAPCostantiWeb.PG_STAMPA%>",  hrefStampa);
   }
 </script>

<%
  if (Stampabile == null || Stampabile.trim().length() < 1)
    Stampabile = "SI";
// Flag per attivare la funzione di Upload. Può essere disattivata
// passando UploadNO nella request
boolean Upload = true;
     if (UploadNO != null && UploadNO.trim().length() > 0)
         Upload = false;

     Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

    //Visualizzazione dei bottoni di Stampa
    if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
    {
      Iterator lIterBottoni = lFunFiglie.iterator();
      String flagDocReg = StringUtils.toStringJSP(request.getParameter("FlagDocumentoRegistrato"));
      String codMotivo = StringUtils.toStringJSP(request.getParameter("CodMotivo"));
      FunctionModel lFun = null;
      while(lIterBottoni.hasNext())
      {
        lFun = (FunctionModel)lIterBottoni.next();
        if((lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)  && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && Stampabile.compareTo("SI") == 0)
			     && (( flagDocReg != null) || flagDocReg.compareTo("N")!=0 ))

        {
%>
          <!-- BOTTONE DI STAMPA -->
          <td class="LBG">
            <a href="Javascript:stampaSius('<%=lFun.getNameAction()%>')" <% if (Upload) { %> onclick="javascript:lookUpload();" <% }%>>
              <img  align="middle" src="/images/print24.gif" alt="Generazione Stampa" width="24" height="24" border="0">
            </a>
          </td>
<%
        }
        // BOTTONE DI TRASMISSIONE
        if(lFun.getVisualizzazionType() != null && lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO)
          && (codMotivo.compareTo("0538") == 0 || codMotivo.compareTo("0539") == 0) )
        {
          if (flagDocReg.compareTo("S") == 0) {
%>
     			<td class="LBG">
            <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>" >
              <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>net24.gif" alt="Trasmissione richiesta atto" width="24" height="24" border="0">
            </a>
     			</td>
<%				}
        }
      } // endwhile
     // Flag passato nella request per eliminare la funzione di Upload
     if ((Upload) && Stampabile.compareTo("SI") == 0 
		     && (( flagDocReg.compareTo("null")==0) || (flagDocReg=="N") ) )
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
 %>