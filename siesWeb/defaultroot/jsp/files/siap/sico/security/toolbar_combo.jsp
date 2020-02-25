<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.web.ISIAPCostantiWeb" %>
<%@ page import="siap.sico.utente.model.UtenteModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.siep.sentenza.model.SentenzaModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

<script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONTROL_UPLOAD_NEW%>"></script>

<%
  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

  String lModificabile= "SI";
  boolean lDiProprieta = true; //Booleno che indica se il fasicolo è prorpio o di un altro ufficio


  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
    // Nel caso di "Posizione Materiale Fascicolo SIUS" se il fascicolo
    // non è modificabile bisogna disattivare anche il bottone di Inserimento.
    if (lModificabile.equalsIgnoreCase("NO") && request.getParameter("tipo_posizione_materiale") != null)
    {
       if (request.getParameter("tipo_posizione_materiale").equalsIgnoreCase("SIUS"))
				{
          lDiProprieta = false;
				}
    }
  }
  else
  {
  	UtenteModel lUtenteMod = new UtenteModel((UtenteModel)session.getAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
  	String lUffUtente =  lUtenteMod.getUfficioUtente().getCodUfficio();

  	if (!(	lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UDS")||
    				lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("TDS")||
         		lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UEPE") ||
         		lUtenteMod.getUfficioUtente().getCodTipoUfficio().equals("UEPESS") ))
   {
      FascicoloSiepModel lFas = (FascicoloSiepModel)session.getAttribute("fascicolo");

      if (lFas != null)
      {
        if (!lUffUtente.equals(lFas.getChiaveUfficio()))
        {
          lDiProprieta = false;
        }
      }

    }
  }

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {

    //Visualizzazione della combo
    Iterator lIterCombo = lFunFiglie.iterator();
    if( lFunFiglie.size() != 0 )
    {
       boolean inseritaPrima = false;
       boolean daInserire = false;

%>
      <SCRIPT LANGUAGE="JavaScript">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        function submit( aAction )
        {
          var lCampoIdEntita="<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>";

          document.location.href='<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>='+aAction+'&'+lCampoIdEntita+'=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>'+'&TornaQui=<%=TornaQui%>';
        }
      </SCRIPT>
<%
        while(lIterCombo.hasNext())
        {
          FunctionModel lFun = (FunctionModel)lIterCombo.next();

          if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO))
          {

              if(lModificabile.compareTo("SI")==0 && lDiProprieta)
              {
                    daInserire = true;
              }
              else
                {
                  if( lFun.getFunctionType().compareTo("I")==0
                    ||lFun.getFunctionType().compareTo("M")==0
                    ||lFun.getFunctionType().compareTo("C")==0 )
                    {
                       daInserire = false;
                    }
                  else
                  {
                      daInserire = true;
                  }
                }
                // Inserimento della funzione nella combo
                if (daInserire )
                {
                  if (!inseritaPrima)
                  {
                     inseritaPrima = true;
%>              <select name="vai">  <%
                  }
%>              <option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option><%
                }
             lIterCombo.remove();
          }
        }

       if (inseritaPrima)
       {
%>
        </select>
        <a href="javascript:submit(document.forms[0].vai.value);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>vedi24.gif" alt="Vai" width="24" height="24" border="0">
        </a>
<%
       }
    }
  }

  if(   request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE) != null
     && !request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE).equals(""))
  {
    String lAzioneChiamante = request.getParameter(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE);
%>
    <td class="LBG">
      <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lAzioneChiamante%>">
        <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
      </a>
    </td>
<%
  }
%>