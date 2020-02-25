<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.ICostantiFunzioni" %>
<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codIncarico" scope="request" class="java.lang.String" />
<jsp:useBean id="descrIncarico" scope="request" class="java.lang.String" />
<jsp:useBean id="tipoUfficio" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDal" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAl" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>

  <table>
    <tr>
<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";


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
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&hufUtConnesso=<%=ufUtConnesso%>&hlIncludeArchiviati=<%=lIncludeArchiviati%>&hcodIncarico=<%=codIncarico%>&hdescrIncarico=<%=descrIncarico%>&htipoUfficio=<%=tipoUfficio%>&hdataDal=<%=dataDal%>&hdataAl=<%=dataAl%><%=retParam%>">
                  <img src="/images/dettagli.gif" width="12" height="12" alt="Elenco Procedimenti del Soggetto" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_RICERCA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>&hufUtConnesso=<%=ufUtConnesso%>&hlIncludeArchiviati=<%=lIncludeArchiviati%>&hcodIncarico=<%=codIncarico%>&hdescrIncarico=<%=descrIncarico%>&htipoUfficio=<%=tipoUfficio%>&hdataDal=<%=dataDal%>&hdataAl=<%=dataAl%><%=retParam%>">
                  <img src="/images/new24.gif" width="12" height="12" alt="Iscrizione Procedimento UEPE" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_MODIFICA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
                  <img src="/images/modifica.gif" alt="Modifica" width="12" height="12" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_CANCELLA))
            {
%>
              <td>
                <a href="Javascript:conferma('<%=lFun.getNameAction()%>','<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>','<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%>');">
                  <img src="/images/delete.gif" width="12" height="12" alt="Cancella" border="0">
                </a>
              </td>
<%
            }
            if(lFun.getFunctionType().equals(ICostantiFunzioni.TIPO_STAMPA))
            {
%>
              <td>
                <a href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=<%=lFun.getNameAction()%>&<%=request.getParameter(ICostantiSecurity.CAMPO_ID_ENTITA)%>=<%=request.getParameter(ICostantiSecurity.VALORE_ID_ENTITA)%><%=retParam%>">
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