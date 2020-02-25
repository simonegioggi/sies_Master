<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
  String lModificabile = "SI";

  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
  }

  String lAnnullato = "N";
  if (request.getParameter("annullato") != null)
  {
    lAnnullato = request.getParameter("annullato");
  }
  
  String elencoDecreti=(request.getParameter ("ElencoDecreti")==null?"false":request.getParameter ("ElencoDecreti"));

  // Costruzione del secondo parametro opzionale Luigi 15-07-2005
  String Param2 = "";
  if (request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) != null && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV).length() > 0 )
  if (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV) != null && request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV).length() > 0 )
      Param2 = "&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV) + "=" + request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA_PROVV);

  // Costruzione del terzo parametro opzionale Ambrosino 22-03-2010
  String Param3 = "";
  if (request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITAPP) != null && request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITAPP).length() > 0 )
  if (request.getParameter(ICostantiSecurity.VALORE_ID_ENTITAPP) != null && request.getParameter(ICostantiSecurity.VALORE_ID_ENTITAPP).length() > 0 )
      Param3 = "&" + request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITAPP) + "=" + request.getParameter(ICostantiSecurity.VALORE_ID_ENTITAPP);

  // Costruzione del quarto parametro opzionale
  // Parametro utilizzato come discriminante nel caso di ufficio PGCAP,
  // per visualizzare i procedimenti di soggetti minorenni (solo nel caso di dettaglio)
  String Param4 = "";
  if (request.getParameter("CampoDet") != null && request.getParameter("CampoDet").length() > 0 )
  if (request.getParameter("ValoreDet") != null && request.getParameter("ValoreDet").length() > 0 )
      Param4 = "&" + request.getParameter("CampoDet") + "=" + request.getParameter("ValoreDet");
  
  String ParamCodMagis = "";
  if (request.getParameter("CampoCodMagis") != null && request.getParameter("CampoCodMagis").length() > 0 )
	  if (request.getParameter("ValoreCodMagis") != null && request.getParameter("ValoreCodMagis").length() > 0 )
		  ParamCodMagis = "&" + request.getParameter("CampoCodMagis") + "=" + request.getParameter("ValoreCodMagis");
  
  
      Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

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
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_DETTAGLIO))
            {
%>
              <td>
            <%if(request.getParameter("ValoreAzioneChiamante")!=null && !request.getParameter("ValoreAzioneChiamante").equals(""))
              {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>&<%=request.getParameter("CampoAzioneChiamante")%>=<%=request.getParameter("ValoreAzioneChiamante")%><%=Param2%><%=Param3%><%=Param4%>&ElencoDecreti=<%=elencoDecreti%>&IdEvento=<%=request.getParameter("idEvento") %>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>
            <%}else
              {%>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=Param2%>&ElencoDecreti=<%=elencoDecreti%>&IdEvento=<%=request.getParameter("idEvento")%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Dettagli" border="0">
                </a>

            <%}%>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && (lModificabile.equals("SI") && lAnnullato.equals("N")))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=Param2%><%=ParamCodMagis%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA)&& (lModificabile.equals("SI") && lAnnullato.equals("N")))
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%><%=Param2 %>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
            {
        	// Nuova Stampa Luigi 26-11-2004
%>
              <td>
                <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>')">
                  <img src="/images/print.gif" alt="Stampa" width="12" height="12" border="0">
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