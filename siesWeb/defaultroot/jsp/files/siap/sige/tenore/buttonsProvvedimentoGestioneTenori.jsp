<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sige.tenore.action.ICostantiTenoreSige"%>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@page import="siap.sige.provvedimento.action.ICostantiProvvedimentoSige"%>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="Modificabile"     scope="request" class="java.lang.String"/>
<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<table>
    <tr>

<%
  // Flag di abilitazione del bottone di modifica
  boolean abilitaModifica = true;

  String modifica = request.getParameter("Modifica");
  if( !"SI".equalsIgnoreCase(Modificabile))
  	abilitaModifica = false;
    	
  // presenza del Link per il bottone di ritorno
  boolean retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  String flagTitoliEsecutivi=request.getParameter("flagTitoliEsecutivi");
  boolean isTitoliEsecutivi =(flagTitoliEsecutivi==null ? false : flagTitoliEsecutivi.equalsIgnoreCase("true"));
  String codOggettoSige=request.getParameter("codOggettoSige");
  String idProvvedimento=request.getParameter("idProvvedimento");
  String idFascicoloSiep=request.getParameter(ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA);
  String idSentenza=request.getParameter("idSenSentenza");
  String esitoTenore=request.getParameter ("esitoTenore");
  String statoProvvValidato=request.getParameter ("statoProvvValidato");
 
  Collection <FunctionModel>lFunFiglie = (Collection <FunctionModel>)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);
  //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) ) {
        for (FunctionModel lFun : lFunFiglie) {
          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO) && !esitoTenore.equals("null") )
            {
              /* Dettaglio Oggetto */
              String toolTip="Dettaglio Oggetto";
              if (!abilitaModifica)
            	  toolTip="Dettaglio Esito Titolo Esecutivo";
%>
              <td width="33%">
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%><%=retParam%>&codOggettoSige=<%=codOggettoSige%>&<%=ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA%>=<%=idFascicoloSiep%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=false&idSenSentenza=<%=idSentenza%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="<%=toolTip %>" border="0">
                </a>
              </td>
          <%}
            	// VERSIONE 11: aggiunte le condizioni di controllo su "statoProvvValidato" (x2)
              if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO) && !esitoTenore.equals("null") && statoProvvValidato != null && statoProvvValidato.equals("N"))
              {
                /* Dettaglio Oggetto */
                String toolTip="Imposta Esito Titolo Esecutivo";
  %>
                <td width="33%">
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.tenore.action.ActLoadModificaEsiti&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%><%=retParam%>&codOggettoSige=<%=codOggettoSige%>&<%=ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA%>=<%=idFascicoloSiep%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=false">
                    <img src="/images/modifica24.gif" width="12" height="12" alt="<%=toolTip %>" border="0">
                  </a>
                </td>
            <%
            }
              if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO) && esitoTenore.equals("null") && statoProvvValidato != null && "N".equals(statoProvvValidato))
              {
                /* Dettaglio Oggetto */
                String toolTip="Imposta Esito Titolo Esecutivo";
  %>
                <td width="33%">
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sige.tenore.action.ActLoadModificaEsiti&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter("ValoreIdEntita")%><%=retParam%>&codOggettoSige=<%=codOggettoSige%>&<%=ICostantiTenoreSige.CAMPO_ID_FASCICOLO_SIEP_SENTENZA%>=<%=idFascicoloSiep%>&idProvvedimento=<%=idProvvedimento%>&isTitoliEsecutivi=false">
                    <img src="/images/icona-un-quarto.png" width="12" height="12" alt="<%=toolTip %>" border="0">
                  </a>
                </td>
            <%
            }   

          // ANNULLAMENTO
	  			if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && (Modificabile.equals("SI")) && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null &&  request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null  )
	  			{
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