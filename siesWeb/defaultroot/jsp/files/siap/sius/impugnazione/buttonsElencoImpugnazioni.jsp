<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@page import="siap.siep.provvedimentopm.action.ICostantiProvvedimento"%>
<%@page import="siap.sius.impugnazione.action.ICostantiImpugnazione"%>
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficio"  scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

		String strTitolo="";
        if (tipoUfficio.compareTo("TDS")==0 )
          strTitolo = " Ricorso";
        else
          strTitolo = " Impugnazione";

      //Visualizzazione dei bottoni
      if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
      {
        Iterator lIterBottoni = lFunFiglie.iterator();
        FunctionModel lFun = null;
        while(lIterBottoni.hasNext())
        {
          lFun = (FunctionModel)lIterBottoni.next();

          if(lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
          {
            if((lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA))
              && (request.getParameter("FlagPiuMeno").compareTo("R")==0 ))
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 )  )
                 && ( request.getParameter("FlagAnnullato").compareTo("S")!=0 ) )
              {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&tipoOper=A&TornaQui=<%=TornaQui%>">
                  <img src="/images/aggiorna24.gif" width="12" height="12" alt="Aggiorna<%=strTitolo%>" border="0">
                </a>
              </td>
<%            }
            }
            // La modifica è operativa solo se esiste il ricorso associato al provvedimento.
            if((lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
              && (request.getParameter("FlagPiuMeno").compareTo("R")==0 ))
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 ) )
              	 && ( request.getParameter("FlagAnnullato").compareTo("S")!=0 ) )
              {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/modifica.gif" width="12" height="12" alt="Modifica <%=strTitolo%>" border="0">
                </a>
              </td>
<%            }
            }
            // Il dettaglio del ricorso è operativo per ogni ricorso associato al provvedimento.
            if((lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
              && (request.getParameter("FlagPiuMeno").compareTo("R")==0 )
       		  && ( lFun.getNameAction().compareTo("siap.sius.impugnazione.action.ActLoadDettaglioImpugnazione")==00 ) )
            {
              // Non è possibile trattare ricorsi per decreti di unificazione/Fissazione Udienza.
              if (! ( request.getParameter("CodTipoProvvedimento").compareTo("02")==0 && (request.getParameter("CodMotivo").compareTo("0600")==0  || request.getParameter("CodMotivo").compareTo("0601")==0 ) ))
              {
%>
	              <td>
       		        <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&<%=ICostantiImpugnazione.CAMPO_NUMERO_IMPUGNAZIONI%>=<%=request.getParameter("numeroImpugnazioni")%>&<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>=<%=request.getParameter("CampoIdImpugnazione")%>&TornaQui=<%=TornaQui%>">
               		  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettaglio <%=strTitolo%>" border="0">
               		</a>
           		 </td>
<%         	  }
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA) && (request.getParameter("Stampa").compareTo("SI")==0))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/print.gif" alt="Visualizza Stampa" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_TRASFERIMENTO))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>=<%=request.getParameter("CodTipoProvvedimento")%>&TornaQui=<%=TornaQui%>">
                  <img src="/images/transfer.gif" alt="Trasferisci" width="12" height="12" border="0">
                </a>
              </td>
<%
            }

			//   ANNULLAMENTO
            if( (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)) && (request.getParameter("FlagPiuMeno").compareTo("R")==0 )
            	&& (request.getParameter("FlagAnnullato").compareTo("S")!=0 ) )

            {
%>
	    	  <td>
	      		<a href="Javascript:confermaAnnullamento3Param('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO%>','<%=request.getParameter("CodTipoProvvedimento")%>','<%=ICostantiImpugnazione.CAMPO_ID_IMPUGNAZIONE%>','<%=request.getParameter("CampoIdImpugnazione")%>' );">
				  <img src="/images/delete.gif" width="12" height="12" alt="Annulla <%=strTitolo%>" border="0">
	      		</a>
	    	  </td>
<%
            }
          }
        }
      }
%>
    </tr>
  </table>