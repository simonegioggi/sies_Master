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

<%
	String lCodUffAppartenenza  = request.getParameter("valoreufficio");
  
  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

  String lModificabile= "SI";//Parametro che vale per la cancellazione e la modifica
  String lCancellabile= "SI";//Parametro che vale solo per la cancellazione e non per la modifica
  boolean lAbilitaModifica = true; //Booleno che indica se funzione di modifica è abilitata
  boolean lAbilitaCancella = true; //Booleno che indica se funzione di modifica è abilitata

  // Abilitazione funzioni di tipo Modifica
  if (request.getParameter("Modificabile") != null)
  {
    lModificabile = request.getParameter("Modificabile");
    if (lModificabile.equalsIgnoreCase("NO"))
    {
    	lAbilitaModifica = false;
    }
  }
  
  // Abilitazione funzioni di tipo Cancella
  if (request.getParameter("Cancellabile") != null)
  {
	lCancellabile = request.getParameter("Cancellabile");
    if (lCancellabile.equalsIgnoreCase("NO"))
    {
    	lAbilitaCancella = false;
    }
  }


  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    while(lIterBottoni.hasNext())
    {
      lFun = (FunctionModel)lIterBottoni.next();

      if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      {
        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && lAbilitaModifica)
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>new24.gif" alt="Inserisci" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA)  && lAbilitaModifica)
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>modifica24.gif" alt="Modifica" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA) && lAbilitaCancella
        		&& !("00000").equals(lCodUffAppartenenza) )
        {
%>
           <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>','TornaQui','<%=TornaQui%>');">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>delete24.gif" alt="Cancella" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
        {
        // Nuova Stampa Luigi 26-11-2004
%>
          <a href="Javascript:stampa2( '<%=ISIAPCostantiWeb.PG_STAMPA%>', '<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>')">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>print24.gif" alt="Stampa" width="24" height="24" border="0">
          </a>
<%
        }

        if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO_COPIA))
        {
%>
          <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&TornaQui=<%=TornaQui%>">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrow24.gif" alt="Inserimento con Copia" width="24" height="24" border="0">
          </a>
<%
        }

       // lIterBottoni.remove();
      }
    }

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
          lFun = (FunctionModel)lIterCombo.next();

          if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO))
          {
              if(lAbilitaModifica)
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
%>              <select name="vai" id="funzioni">  <%
                  }
%>              <option id="<%=lFun.getNameAction()%>" value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option><%
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