<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.Iterator" %>
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.log.LogF3B" %>


<%@ page import="siap.siep.notifica.model.NotificaModel" %>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.sico.evento.model.EventoNotificaModel"%>
<%@ page import="siap.siep.autoritaesterna.model.AutoritaEsternaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>


<%@page import="org.apache.log4j.Logger"%>
<%-- // [FT] - 05/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog --%>
<% final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG); %>
<jsp:useBean id="OrdiniEsecuzione" scope="request" class="java.util.Vector" />
<%
//==============================================================================
// Jsp (popup) che visualizza la lista degli ordini di esecuzione emessi 
// Selezionando un ordine i dati dei destinatari vengono caricati nella 
// finestra chiamante
//==============================================================================
%>
<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Ordini Esecuzione </title>

    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript">

      function controlla()
      {
<%
        boolean esistonoDati = false;
        if( !OrdiniEsecuzione.isEmpty() )
          esistonoDati = true;
%>
        if(<%=!esistonoDati%>)
        {
          alert("Nessun dato presente");

          window.parent.close();
        }
      }

      //========================================================================
      // Carica i dati del destinatario negli opportuni campi della parent
      //========================================================================
      function insertIT ( idAutEst
                         ,codTipoAutorita
                         ,descrSede
                         ,noteAut
                         ,dataEmissione
                         ,oggettoOE
                        )
      {
    	  // MEV_39: gestiti meglio i commenti
        //=
        //if(idAutEst!='null')
<%--         window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.value=descrTipoAutorita; --%>
        //else
<%--         window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.value="-"; --%>
          
        //=
        if(codTipoAutorita!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>.value=codTipoAutorita;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_R%>.value="-";
          
        //=
        if(descrSede!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>.value=descrSede;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_R%>.value="";
      
        //= Indirizzo
        if(noteAut!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R%>.value=noteAut;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_R%>.value="";

        //=
        if(dataEmissione!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_DATA_EMISSIONE%>.value=dataEmissione;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.<%=ICostantiEvento.CAMPO_DATA_EMISSIONE%>.value="";

        //=
        if(oggettoOE!='-')
          window.parent.opener.document.<%=request.getParameter("formname")%>.descrMotivoOE.value=oggettoOE;
        else
          window.parent.opener.document.<%=request.getParameter("formname")%>.descrMotivoOE.value="";

        window.parent.close();
      }
  	</script>
  </head>

  <body class="corpo" onload="controlla();">
  <form method="POST" name="elenco" action="<%=IWebConstants.PG_MAIN%>">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>
        <font class="campo">
          Lista Ordini Esecuzione
        </font>
      </td>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="int">Data Emissione</td>
      <td class="int">Oggetto</td>
      <td class="int">Destinatari per l'esecuzione</td>
      <td class="int" width=5%>Azioni</td>
    </tr>
<%
    if( !OrdiniEsecuzione.isEmpty() )
    {
      Iterator itx = OrdiniEsecuzione.iterator();
      for (int i = 0; itx.hasNext(); i++)
      {
        EventoNotificaModel lEventoNotifica = (EventoNotificaModel)itx.next();
        EventoModel  lEvento = lEventoNotifica.getEvento();
        // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("Notifiche = "+lEventoNotifica.getNotifiche().length);
        
        int count=0;
        AutoritaEsternaModel lModAut = new AutoritaEsternaModel();
        NotificaModel lNotMod = new NotificaModel();
        
        while(count < lEventoNotifica.getNotifiche().length){
          lNotMod = lEventoNotifica.getNotifiche()[count];
          
          // La notifica all'autorità esterna dovrebbe essere unica per l'evento
          if (lNotMod.getAutoritaEsterna() != null && lNotMod.getCodTipoNotifica().equals("E")) {
            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug("notifica = "+lNotMod);
            // [FT] - 05/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug("getAutoritaEsterna() = "+lNotMod.getAutoritaEsterna());
            
            lModAut = lNotMod.getAutoritaEsterna();
          }
          count++;
        }
        
      %>
        <tr>
          <td class="c">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"), "-")%>
          </td>
          <td class="l">
          <% if( lEvento.getCodMotivo() != null ) { %>
          <%=StringUtils.toStringJSP(lEvento.getDescrMotivo(),"-")%>
          <% } %>
          </td>
          <%-- MEV_39: aggiunto "di" tra le due descrizioni --%>
          <td class="l">
            <%=StringUtils.toStringJSP(lModAut.getDescrTipoAutorita(),"-")%> di <%=StringUtils.toStringJSP(lModAut.getDescrSede(),"-")%>
          </td>
          <td class="c">
          <%-- MEV_39: usata il metodo cStrForJS --%>
            <a href="Javascript:insertIT( '<%=StringUtils.toStringJSP(lModAut.getIdAutoritaEsterna(),"-")%>'
                                         ,'<%=StringUtils.toStringJSP(lModAut.getCodTipoAutorita(),"-")%>'
                                         ,'<%=StringUtils.cStrForJS(lModAut.getDescrSede())%>'
                                         ,'<%=StringUtils.cStrForJS(lNotMod.getNote())%>'
                                         ,'<%=StringUtils.toStringJSP(DateUtils.getDateToString(lEvento.getDataEmissione(),"dd-MM-yyyy"), "-")%>'
                                         ,'<%=StringUtils.cStrForJS(lEvento.getDescrMotivo())%>'
                                        );">
              <img align="middle" src="/images/fileselected.gif" border="0" Title="Seleziona Ordine Esecuzione per la Restituzione">
            </a>
          </td>
        </tr>
<%
      }
    }
%>
    </table>
  </form>
</body>
</html>