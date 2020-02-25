<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>

<%
  // Flag che indica se il reato è quello principale o una circostanza
  String lStringFlagReato = request.getParameter("FlagReato");

  boolean lFlagReato = false;
  if(lStringFlagReato.equals("true"))
    lFlagReato = true;

  // Flag che indica se il fascicolo è validato
  String lStringFlagValidato = request.getParameter("FlagValidato");

  boolean lFlagValidato = false;
  if(lStringFlagValidato.equals("S"))
    lFlagValidato = true;

  Collection lFunFiglie = (Collection)request.getAttribute(ICostantiSecurity.FUN_FIGLIE);

  //Visualizzazione dei bottoni
  if( (lFunFiglie != null) && (lFunFiglie.size() != 0) )
  {
    Iterator lIterBottoni = lFunFiglie.iterator();
    FunctionModel lFun = null;
    //Rimuovo i figli di tipo pulsante
     int HowManyButtons=0;
     while(lIterBottoni.hasNext())
     {
       lFun = (FunctionModel)lIterBottoni.next();
       if(lFun.getVisualizzazionType() != null && lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_BOTTONE))
      HowManyButtons++;

     }
    //Visualizzazione della combo
    Iterator lIterCombo = lFunFiglie.iterator();

    while(lIterCombo.hasNext())
    {
      lFun = (FunctionModel)lIterCombo.next();
      if( (lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_INSERIMENTO) && lFlagValidato)
          || !lFlagReato )
      {
        lIterCombo.remove();
      }
    }

    if( lFunFiglie.size()- HowManyButtons!= 0 )
    {
      lIterCombo = lFunFiglie.iterator();
%>
      <SCRIPT LANGUAGE="JavaScript">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        function submit( aAction )
        {
          var lCampoIdEntita="<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>";

          document.location.href='<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>='+aAction+'&'+lCampoIdEntita+'=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>';
        }
      </SCRIPT>

      <select name="vai">
<%
        while(lIterCombo.hasNext())
        {
          lFun = (FunctionModel)lIterCombo.next();
          //Ad un fascicolo Siep non è possibile aggiungere entità correlate a meno della Posizione Giuridica
          if( (lFun.getVisualizzazionType().equals(ICostantiFunzioni.FUNZIONE_COMBO)) ) //Funzione di tipo COMBO

          {
%>
            <option value="<%=lFun.getNameAction()%>"><%=lFun.getLabelFunction()%></option>
<%
            lIterCombo.remove();
          }
        }
%>
        </select>
        <a href="javascript:submit(document.forms[0].vai.value);">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>vedi24.gif" alt="Vai" width="24" height="24" border="0">
        </a>
<%
    }
  }
%>