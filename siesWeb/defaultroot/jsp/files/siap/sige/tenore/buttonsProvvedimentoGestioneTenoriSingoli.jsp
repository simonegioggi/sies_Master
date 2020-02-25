<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="isModificabile"     scope="request" class="java.lang.String"/>
<%-- 20190515 [SG]: aggiunto useBean --%>
<jsp:useBean id="modificaOggettoAtto"	scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<table>
    <tr>

<%
  // Flag di abilitazione del bottone di modifica
  boolean abilitaModifica = true;

  String modifica = request.getParameter("Modifica");
  if( modifica!= null && modifica.compareTo("NO")==0 )
  	abilitaModifica = false;
// 20190515 [SG]: aggiunto controllo
else if (modificaOggettoAtto != null && modificaOggettoAtto.compareTo("NO") == 0)
	abilitaModifica = false;
    	
  // presenza del Link per il bottone di ritorno
  boolean retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  String flagTitoliEsecutivi=request.getParameter("flagTitoliEsecutivi");
  boolean isTitoliEsecutivi =(flagTitoliEsecutivi==null ? false : flagTitoliEsecutivi.equalsIgnoreCase("true"));
  String codOggettoSige=request.getParameter("codOggettoSige");
  String idProvvedimento=request.getParameter("idProvvedimento");
String CodContenuto = request.getParameter("CodContenuto");
    
Collection <FunctionModel>lFunFiglie = (Collection <FunctionModel>)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
// Visualizzazione dei bottoni
if ((lFunFiglie != null) && (lFunFiglie.size() != 0)) {
	for (FunctionModel lFun : lFunFiglie) {
		if (lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE)) {
            if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO)) {
				/* Dettaglio Oggetto */
%>
		<td width="33%">
			<a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%><%=retParam%>">
				<img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio Oggetto" border="0">
			</a>
		</td>
<%
			}
			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && abilitaModifica) {
				String href = IWebConstants.PG_MAIN + "?"+IWebConstants.ACTION_FIELD+"=" +lFun.getNameAction()+"&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)+"=" +request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)+retParam+"&codOggettoSige="+codOggettoSige;
				if (CodContenuto != null)
					href = href + "&CodContenuto=" + CodContenuto;
%>  		

              <td width="33%">
		  	<a href="<%=href%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
          // ANNULLAMENTO
			if (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)
					&& (isModificabile.equals("SI"))
					&& request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null
					&& request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null) {
%>
	    			<td width="33%">
	      			<a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV)%>');">
								<img src="/images/delete.gif" width="12" height="12" alt="Annulla" border="0">
	      			</a>
	    			</td>
<%
				}  // if Annullamento
          } // IF FUNZIONE_BOTTONE
      }  // end WHILE
      
      
    }
%>
    </tr>
  </table>
