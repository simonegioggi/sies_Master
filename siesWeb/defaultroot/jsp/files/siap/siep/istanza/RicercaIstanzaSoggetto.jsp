<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.istanza.model.IstanzaSoggettoEventoFascicoloSiepModel"%>
<%@ page import="siap.siep.istanza.action.ICostantiIstanza"%>

<jsp:useBean id="istsogevefasc" scope="request" class="java.util.Vector"/>

<%
//==============================================================================
// Jsp per la visualizzazione della ricerca Istanze
// Vengono visualizzati i risultati sia per la Ricerca per Soggetto Presentante
// che per la ricerca per Oggetto Istanza
//==============================================================================
%>

<html>
  <head>
    <title>[S.I.E.S.] - Dettaglio RIcerca Istanza</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript">
    // Richiamo della finestra di pop-up per inserire motivazione Annullamento
    function conferma(a_action, a_entityname1, a_entityvalue1 ,a_entityname2 ,a_entityvalue2)
    {
      if (window.confirm("Confermi l'annullamento?"))
      {
        var  desktop = window.open("/jsp/Main.jsp?Action=" + a_action + "&" + a_entityname1 + "=" +a_entityvalue1 + "&" + a_entityname2 + "=" +a_entityvalue2, "Annulla"," top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
        window.parent.close();
      }
    }

    function chiama(idIstanza)
    {
      window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istanza.action.ActLoadAnnullaIstanza&<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>="+idIstanza,"Annulla_Istanza", "top="+eval(screen.height/2-200/2)+",left="+eval(screen.width/2-450/2)+", toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=200");
    }
</script>
</head>


  <body class="corpo">
    <FORM name="comandi" >
      <table>
        <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
          <td class="LBG">
            <font class="label">Funzione :</font>&nbsp;
            <font class="campo">Elenco Istanze</font>
          </td>
        </tr>
      </table>
<jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>
<br>
    </FORM>
  <div align=center>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Luogo di Nascita</td>
      <td class="int">Data di Nascita</td>
      <td class="int">Oggetto</td>
      <td class="int">Numero SIEP</td>
      <td class="int">Annullato</td>
      <td class="int">Azioni</td>
    </tr>
<%
  Iterator itx = istsogevefasc.iterator();
  while ( itx.hasNext() )
  {
    IstanzaSoggettoEventoFascicoloSiepModel lMod = (IstanzaSoggettoEventoFascicoloSiepModel)itx.next();
%>
    <tr>
      <td class="l"><font class="label"><%=StringUtils.toStringJSP(lMod.getSoggetto().getCognome())%>&nbsp;</font></td>
      <td class="l"><font class="label"><%=StringUtils.toStringJSP(lMod.getSoggetto().getNome())%>&nbsp;</font></td>
      <td class="l">
        <font class="label"><%if ( !(lMod.getSoggetto().getDescrComuneNascita().equals("-")) ) { %>
          <%=StringUtils.toStringJSP(lMod.getSoggetto().getDescrComuneNascita()) %>&nbsp;
      (<%=lMod.getSoggetto().getCodProvinciaNascita()%>)
<%
     }
     else
          {
      if (lMod.getSoggetto().getDescrComuneNascita().compareTo("-")==0){%>
         <font class="label"><%=lMod.getSoggetto().getDescComuneNascitaEstero()%>  (<%=lMod.getSoggetto().getDescrStatoNascita().toUpperCase()%>)&nbsp;</font>
      <% }else {%>
         <font class="label"><%=lMod.getSoggetto().getDescrComuneNascita()%> (<%=lMod.getSoggetto().getCodProvinciaNascita()%>)&nbsp;</font>
      <%}
          }%>
        </font>
      </td>
      <td class="l" nowrap><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lMod.getSoggetto().getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;</td>
      <td class="l"><font class="label"><%=StringUtils.toStringJSP(lMod.getIstanza().getDescrMotivo())%></font>&nbsp;</td>
      <td class="l"><font class="label"><%=StringUtils.toStringJSP(lMod.getFascicoloSiep().getChiaveAnno())%>/<%=StringUtils.toStringJSP(lMod.getFascicoloSiep().getChiaveProgr())%></font>&nbsp;</td>
      <td class="l">
<%
        String lAnnullato = "N";
        if(lMod.getIstanza() != null && lMod.getIstanza().getCodStatoIstanza()!= null && lMod.getIstanza().getCodStatoIstanza().compareTo("C")==0)
        {
        lAnnullato = "S";
%>
             <a class="cliccabile" href="javascript:chiama('<%=lMod.getIstanza().getIdIstanza()%>');" title="ANNULLAMENTO">
             <font class="cRosso">ANNULLATO</font></a>

<%
        }
        else
        {
%>
          &nbsp;
<%
        }
%>
</td>

      <td class="c">
        <jsp:include page="<%=IWebConstants.PG_BUTTONS%>">
          <jsp:param name="CampoIdEntita"  value="<%=ICostantiIstanza.CAMPO_ID_ISTANZA%>"/>
          <jsp:param name="ValoreIdEntita" value="<%=lMod.getIstanza().getIdIstanza()%>"/>
          <jsp:param name="annullato"      value="<%=lAnnullato%>" />
        </jsp:include>
      </td>
    </tr>
<%
  }
%>
  </table>
  </div>
  </body>
</html>