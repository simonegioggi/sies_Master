<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>
<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<%@ page import="siap.sige.aula.action.ICostantiAula" %>
<%@ page import="siap.sige.udienza.action.ICostantiUdienzaSige"%>
<%@ page import="siap.sige.udienzaparti.action.ICostantiPartiUdienza"%>
<%@ page import="siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige"%>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";

  String lModificabile = "";
  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
  }

  // Costruzione del secondo parametro IdEventoUdienza
  String Param2 = "";
  if (request.getParameter("CampoIdEventoUdienza") != null && request.getParameter("CampoIdEventoUdienza").length() > 0 )
  if (request.getParameter("ValoreIdEventoUdienza") != null && request.getParameter("ValoreIdEventoUdienza").length() > 0 )
      Param2 = "&" + ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA + "=" + request.getParameter("ValoreIdEventoUdienza");

  // Costruzione del terzo parametro IdUdienzaSige
  String Param3 = "";
  if (request.getParameter("CampoIdUdienzaSige") != null && request.getParameter("CampoIdUdienzaSige").length() > 0 )
  if (request.getParameter("ValoreIdUdienzaSige") != null && request.getParameter("ValoreIdUdienzaSige").length() > 0 )
      Param3 = "&" + ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE + "=" + request.getParameter("ValoreIdUdienzaSige");

  // Costruzione del quarto parametro IdUdienzaProcedimentoSige
  String Param4 = "";
  if (request.getParameter("CampoIdUdienzaProcedimentoSige") != null && request.getParameter("CampoIdUdienzaProcedimentoSige").length() > 0 )
  if (request.getParameter("ValoreIdUdienzaProcedimentoSige") != null && request.getParameter("ValoreIdUdienzaProcedimentoSige").length() > 0 )
      Param4 = "&" + ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE + "=" + request.getParameter("ValoreIdUdienzaProcedimentoSige");

  // Costruzione del quinto parametro TipoParte
  String Param5 = "";
  if (request.getParameter("CampoCodTipoParte") != null && request.getParameter("CampoCodTipoParte").length() > 0 )
  if (request.getParameter("ValoreCodTipoParte") != null && request.getParameter("ValoreCodTipoParte").length() > 0 )
      Param5 = "&codTipoParte=" + request.getParameter("ValoreCodTipoParte");

      String totParam = Param2+Param3+Param4+Param5;
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
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) )
            {
%>
                <td>
                  <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=totParam%>">
                    <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
                  </a>
                </td>
<%
            }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA) && lModificabile.equals("SI") )
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=totParam%>">
                  <img src="/images/modifica24.gif" alt="Modifica Parte" width="24" height="24" border="0">
                </a>
              </td>
<%
            }

            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA_DIFENSORE) && lModificabile.equals("SI") )
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%><%=totParam%>">
                  <img src="/images/modificaDif24.gif" alt="Modifica Difensore e Convocazione Parte" width="24" height="24" border="0">
                </a>
              </td>
<%
            }
            
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && lModificabile.equals("SI") )
            {
%>
              <td>
                <a href="Javascript:myConfirm('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','<%=totParam%>');">
                  <img src="/images/delete24.gif" width="24" height="24" alt="Cancella" border="0">
                </a>
              </td>
<%
            }
          }
        } // end while(lIterBottoni.hasNext())
        	
      } // end if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
%>            
        
    </tr>
  </table>