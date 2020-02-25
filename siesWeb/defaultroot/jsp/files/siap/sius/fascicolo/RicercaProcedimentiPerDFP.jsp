<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>

<%@ page import="siap.sius.udienza.action.ICostantiUdienza"%>

<jsp:useBean id="UtenteConnesso" scope="session" class="siap.sico.utente.model.UtenteModel" />

<jsp:useBean id="fascicoli" scope="request" class="java.util.Vector" />

<jsp:useBean id="ufUtConnesso" scope="request" class="java.lang.String" />
<jsp:useBean id="lIncludeArchiviati" scope="request" class="java.lang.String" />
<jsp:useBean id="codContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="descrContenuto" scope="request" class="java.lang.String" />
<jsp:useBean id="codPosGiuridica" scope="request" class="java.lang.String" />
<jsp:useBean id="descrPosGiuridica" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalFinePena" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlFinePena" scope="request" class="java.lang.String" />
<jsp:useBean id="dataDalIscrizione" scope="request" class="java.lang.String" />
<jsp:useBean id="dataAlIscrizione" scope="request" class="java.lang.String" />
<jsp:useBean id="TornaQui"     scope="request" class="java.lang.String"/>


<html>
  <head>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <title>[S.I.E.S.] - Procedimenti per Formazione Ruolo di Udienza</title>
    <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  </head>

 <script language="JavaScript">
    var desktop;

   // Chiamata funzione elenco Udienze.
    function ListaUdienze(rifProc, IdFascicolo)
    {
      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP

       var lLink = "<%=ICostantiUdienza.PG_LOAD_RICERCAUDIENZAXPROCEDIMENTI_DFP%>";
          lLink += "?formname=elencoPerDFP";
          lLink += "&campoGG=<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>";
          lLink += "&campoMM=<%=ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>";
          lLink += "&campoAA=<%=ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA %>";
          lLink += "&campoID=<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>";
          lLink += "&campoColl=Collegio";
          lLink += "&campoLuogo=Luogo";

        // Questo campo viene usato per passare ANNO/PROG del Procedimento
          lLink += "&campo_sub=" + rifProc;
      document.elencoPerDFP.RifProc.value = rifProc;
      document.elencoPerDFP.<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>.value = IdFascicolo;
      desktop = window.open(lLink, "ElencoUdienza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=550" );
    }
  </script>
    <script language="JavaScript">
      function Verify()
      {
          elencoPerDFP.submit();
          alert("A questo punto submit !!!");
      }
  </script>

<%
  // presenza del Link per il bottone di ritorno
  boolean retFlag = false;
  retFlag = ((TornaQui != null) && TornaQui.trim().length() > 1);
  String retParam = retFlag ? ("&TornaQui=" + TornaQui) : "";
%>


  <BODY class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class=label>Funzione :</font>&nbsp;<font class="campo"> Elenco Procedimenti per Formazione Ruolo di Udienza </font></td>
         <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
      <!-- NUOVO BOTTONE PER STAMPA EXCEL --> 
			<td class=l>
				<a class="cliccabile" 
					 href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActRicercaProcedimentiPerDFPExcel">
					<img src="<%=IWebConstants.IMAGES_DIR%>printexcel.gif" alt="Stampa Excel" width="24" height="24" border="0">
				</a>
			</td>
  </table>

  <br>
  <jsp:include page="<%=IWebConstants.PAGINAZIONE_RICERCA%>"></jsp:include>

  <table cellspacing=2 cellpadding=2>
<%
    if(!(codPosGiuridica.equals("-") && codContenuto.equals("-") &&
       lIncludeArchiviati.equals("0") &&
       dataDalFinePena.equals("") && dataAlFinePena.equals("") &&
       (dataDalIscrizione.equals("")) && (dataAlIscrizione.equals("")) ))
    {
%>
      <tr>
        <td class="Cliccabile">Criteri di Ricerca selezionati:</td>
      </tr>
<%
      if(!(dataDalIscrizione.equals(""))||!(dataAlIscrizione.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Iscrizione :&nbsp;&nbsp;
<%
          if(!(dataDalIscrizione.equals("")))
          {
%>
            Dal <%=dataDalIscrizione%>&nbsp;&nbsp;
<%        }
          if(!(dataAlIscrizione.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataAlIscrizione%>
            </td>
<%        }
       %></tr><%
      }
      if(!(dataDalFinePena.equals(""))||!(dataAlFinePena.equals("")))
      {
%>
        <tr>
          <td class="lVerdeNB">Procedimenti con Data Fine Pena :&nbsp;&nbsp;
<%
          if(!(dataDalFinePena.equals("")))
          {
%>
            Dal <%=dataDalFinePena%>&nbsp;&nbsp;
<%        }
          if(!(dataAlFinePena.equals("")))
          {
%>
            &nbsp;Al&nbsp;&nbsp;<%=dataAlFinePena%>
            </td>
<%        }
       %></tr><%
      }
      if(!lIncludeArchiviati.equals("0"))
      {
%>
        <tr>
          <td class="lVerdeNB">Anche i procedimenti definiti</td>
        </tr>
<%    }
      if(!codContenuto.equals("-"))
      {
%>
        <tr>
          <td class="lVerdeNB">Solo i procedimenti di : <%=descrContenuto%></td>
        </tr>
<%    }
      if(!codPosGiuridica.equals("-"))
      {
%>
        <tr>
          <td class="lVerdeNB">Solo i procedimenti con posizione giuridica : <%=descrPosGiuridica%></td>
        </tr>
<%    }
    }
%>
  </table>
  <br>
<%
  Iterator itx = fascicoli.iterator();
%>
  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="int">Numero SIUS</td>
      <td class="int">Data Iscrizione</td>
      <td class="int">Data Fine Pena</td>
      <td class="int">Contenuto</td>
      <td class="int">Cognome</td>
      <td class="int">Nome</td>
      <td class="int">Pos. Giuridica</td>
      <td class="int">Azioni</td>
    </tr>
<%

   while ( itx.hasNext())
    {
      FascicoloGPModel fascicolo = (FascicoloGPModel)itx.next();
%>
      <tr>
        <td class="c"><font class="label">
          <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>=<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%><%=retParam%>" Title="<%=fascicolo.getFascicoloSiusModel().getDescrTipoUfficio()%>&nbsp;<%=fascicolo.getFascicoloSiusModel().getDescrComuneUfficio()%> - Dettaglio Procedimento" >
            <%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>
          </a>
        </font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getFascicoloSiusModel().getDataIscrizione(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=StringUtils.toStringJSP(DateUtils.getDateToString(fascicolo.getGeneraleProcedimentoModel().getDataFinePena(),"dd-MM-yyyy"),"-")%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getCognome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getFascicoloSiusModel().getSoggetto().getNome()%></font></td>
        <td class="c"><font class="label"><%=fascicolo.getGeneraleProcedimentoModel().getDescrPosGiuridica()%></font></td>
        <td class="c">
        <a  href="Javascript:ListaUdienze('<%=fascicolo.getFascicoloSiusModel().getChiaveAnno()%>/<%=fascicolo.getFascicoloSiusModel().getChiaveProgr()%>', '<%=fascicolo.getFascicoloSiusModel().getIdFascicoloSius()%>');">
        <img src="/images/filefolder.gif"  alt="PreFissazione Udienza"  border=0>
        </a>
        </td>
      </tr>
<%
  }
%>
    </table>
<%
    String lAzione = new String();
    lAzione = "siap.sius.udienzaprocedimento.action.ActInserisciPreFissazioneUdienza";
%>

  <FORM method="POST" name="elencoPerDFP" action="<%=ICostantiUdienza.PG_CONFERMA_PREFISSAZIONE%>">
         <input type="HIDDEN" value="" name="<%=ICostantiUdienza.CAMPO_GIORNO_DATA_UDIENZA %>" maxlength="2" size="2"  >
         <input type="HIDDEN" value="" name="<%= ICostantiUdienza.CAMPO_MESE_DATA_UDIENZA %>" maxlength="2" size="2"  >
         <input type="HIDDEN" value="" name="<%= ICostantiUdienza.CAMPO_ANNO_DATA_UDIENZA%>" maxlength="4" size="4"  >
         <input type="HIDDEN" value="" name="Collegio" maxlength="2" size="2"  >
         <input type="HIDDEN" value="" name="Luogo">
         <input type="HIDDEN" value="<%=lAzione%>" name="<%=IWebConstants.ACTION_FIELD%>"  >
         <input  type="HIDDEN" value="" name="<%=ICostantiUdienza.CAMPO_ID_UDIENZA%>" >
         <input type="HIDDEN" value="" name="RifProc" >
         <input type="HIDDEN" value="" name="<%=ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS%>">
  </FORM>
  <br>

  </body>
</html>