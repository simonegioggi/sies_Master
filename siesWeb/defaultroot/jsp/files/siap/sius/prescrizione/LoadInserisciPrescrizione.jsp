<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sius.prescrizione.model.PrescrizioneModel"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sius.prescrizione.action.ICostantiPrescrizione"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="prescrizione" scope="request" class="siap.sius.prescrizione.model.PrescrizioneModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nextaction" scope="request" class="java.lang.String"/>

<jsp:useBean id="UffMagComp" scope="request" class="java.lang.String"/>
<jsp:useBean id="LuogoProva" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneCSSA" scope="request" class="java.lang.String"/>
<jsp:useBean id="IDEvento" scope="request" class="java.lang.String"/>



<html>
<head>
  <title>[S.I.E.S.] - Gestione Prescrizione </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript">
      var desktop;
      // Chiamata all'elenco dei Comuni.
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      // Chiamata all'elenco dei CSSA.
      function ListaCSSA(a_formname,a_fieldname, a_fieldcode)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldcode="+a_fieldcode, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>
  <script language="JavaScript">
    function Verify()
    {
       return true;
    }
  </script>


    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  </head>
  <body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <%
        PrescrizioneModel lPrescrizione = new PrescrizioneModel();
        String lAzione = new String();
        if( modalita.equals("I") )
        {
//          lPrescrizione.setDescrLuogoAffidamento( eventoordinanzapc.getOrdinanza().getLuogoSvolgimentoProva() );
//          lPrescrizione.setDescrUffMagistratoCompetente(eventoordinanzapc.getOrdinanza().getDescrUfficioMagistratoComp());
//          lPrescrizione.setDescrComuneCssaCompetente(eventoordinanzapc.getOrdinanza().getDescrComuneCssaComp());

          lPrescrizione.setDescrLuogoAffidamento( LuogoProva );
          lPrescrizione.setDescrUffMagistratoCompetente(UffMagComp);
          lPrescrizione.setDescrComuneCssaCompetente(ComuneCSSA);
          lAzione = "siap.sius.prescrizione.action.ActInserisciPrescrizione";
      %>
        <font class="campo">Inserimento Prescrizioni</font>
      <%
        }
        else if( modalita.equals("M") )
        {
          lAzione = "siap.sius.prescrizione.action.ActModificaPrescrizione";
          lPrescrizione = prescrizione;
      %>
        <font class="campo">Modifica delle Prescrizioni</font>
      <%
        }
      %>
      </td>
    </tr>
  </table>

  <tr>
    <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  </tr>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPrescrizione">
    <table cellspacing=4 cellpadding=4>
      <tr>
				<td class="l" colspan=2>Specificare Prescrizioni:</td>
      </tr>

      <tr>
        <td class="l"><input value="01" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_01%>"></td>
				<td class="l">Non allontanarsi dal Comune di
				<input value="<%=lPrescrizione.getDescrLuogoAffidamento() %>" size=35 type="text" name="<%= ICostantiPrescrizione.CAMPO_COD_LUOGO_AFFIDAMENTO%>"  >
        <a href="Javascript:ListaComuni('LoadInserisciPrescrizione','<%= ICostantiPrescrizione.CAMPO_COD_LUOGO_AFFIDAMENTO%>[0]');">
        <img src="/images/filefolder.gif" border=0></a>
        senza autorizzazione Mag. Sorveglianza <input value="<%=lPrescrizione.getDescrUffMagistratoCompetente() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_COD_UFF_MAGISTRATO_COMPETENTE %>"  >
       </td>
      </tr>

      <tr>
        <td class="l"><input value="02" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_02%>"></td>
        <td class="l">Autorizzazione ad allontanarsi dal Comune di residenza dalle 8 alle 14  </td>
      </tr>

      <tr>
        <td class="l"><input value="03" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_03%>"></td>
        <td class="l">Autorizzazione a recarsi nel Comune di <input value="<%=lPrescrizione.getCodLuogoAutorizzato() %>" type="text" size=35 name="<%= ICostantiPrescrizione.CAMPO_COD_LUOGO_AUTORIZZATO %>"  >
        <a href="Javascript:ListaComuni('LoadInserisciPrescrizione','<%= ICostantiPrescrizione.CAMPO_COD_LUOGO_AUTORIZZATO %>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </tr>

      <tr>
        <td class="l">
          <input value="04" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_04%>">
        </td>
        <td class="l">TOX - Non allontanarsi dal Comune di
          <input value="<%=lPrescrizione.getDescrLuogoAffidamento() %>" size=35 type="text" name="<%= ICostantiPrescrizione.CAMPO_COD_LUOGO_AFFIDAMENTO%>"  >
          <a href="Javascript:ListaComuni('LoadInserisciPrescrizione','<%= ICostantiPrescrizione.CAMPO_COD_LUOGO_AFFIDAMENTO%>[1]');">
          <img src="/images/filefolder.gif" border=0></a>
          senza autorizzazione Mag. Sorveglianza <input value="<%=lPrescrizione.getDescrUffMagistratoCompetente() %>" type="text" name="<%=ICostantiPrescrizione.CAMPO_COD_UFF_MAGISTRATO_COMPETENTE%>"  >
          &nbsp;Non allontanarsi dalla Comunità Terapeutica <input value="<%=lPrescrizione.getDescrComunitaTerapeutica()%>" type="text" name="<%=ICostantiPrescrizione.CAMPO_DESCR_COMUNITA_TERAPEUTICA %>"  >
        </td>
      </tr>

      <tr>
        <td class="l"><input value="05" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_05%>"></td>

        <td class="l">Mantenere costanti rapporti con l' UEPE di   <input value="<%=lPrescrizione.getDescrComuneCssaCompetente() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_ID_CSSA_COMPETENTE%>"  size=35>
        <a href="Javascript:ListaCSSA('LoadInserisciPrescrizione','<%= ICostantiPrescrizione.CAMPO_ID_CSSA_COMPETENTE%>[0]','<%=ICostantiPrescrizione.CAMPO_COMUNE_CSSA_COMP%>[0]');">

        <img src="/images/filefolder.gif" border=0></a></td>
      </tr>

      <tr>
        <td class="l"><input value="06" type="checkbox" name="<%=ICostantiPrescrizione.CAMPO_CK_06%>"></td>
        <td class="l">TOX - Mantenere costanti rapporti con l' UEPE di  <input value="<%=lPrescrizione.getDescrComuneCssaCompetente() %>" type="text" name="<%=ICostantiPrescrizione.CAMPO_ID_CSSA_COMPETENTE%>"  size=35>
        <a href="Javascript:ListaCSSA('LoadInserisciPrescrizione','<%=ICostantiPrescrizione.CAMPO_ID_CSSA_COMPETENTE%>[1]' ,'<%= ICostantiPrescrizione.CAMPO_COMUNE_CSSA_COMP%>[1]');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </tr>

      <tr>
        <td class="l"><input value="07" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_07%>"></td>
        <td class="l">Lavoro con mansioni di  <input value="<%=lPrescrizione.getDescrMansioneLavorativa() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_DESCR_MANSIONE_LAVORATIVA %>"  >
          presso <input value="<%=lPrescrizione.getDescrLuogoLavoro() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_DESCR_LUOGO_LAVORO %>"  >
        </td>
      </tr>

      <tr>
        <td class="l"><input value="08" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_08%>"></td>
        <td class="l">Autorizzazione a spostarsi nell' ambito della Provincia di <input value="<%=lPrescrizione.getCodProvinciaAutorizzata() %>" type="text" size=35 name="<%= ICostantiPrescrizione.CAMPO_COD_PROVINCIA_AUTORIZZATA %>"  >
        <a href="Javascript:ListaComuni('LoadInserisciPrescrizione','<%= ICostantiPrescrizione.CAMPO_COD_PROVINCIA_AUTORIZZATA %>');">
        <img src="/images/filefolder.gif" border=0></a></td>
      </tr>

      <tr>
        <td class="l"><input value="09" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_09%>"></td>
        <td class="l">TOX - Dedicarsi stabilmente allo svolgimento del programma di recupero presso la Comunità Terapeutica / S.E.R.T. di  presso <input value="<%=lPrescrizione.getDescrComunitaTerapeutica()%>" type="text" name="<%= ICostantiPrescrizione.CAMPO_DESCR_COMUNITA_TERAPEUTICA%>" >
        </td>
      </tr>

      <tr>
        <td class="l"><input value="10" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_10%>"></td>
        <td class="l">Non uscire dall'abitazione prima delle ore  <input value="<%=lPrescrizione.getOraUscitaAbitazione() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_ORA_USCITA_ABITAZIONE %>" size=5  >
              e farvi rientro entro le ore <input value="<%=lPrescrizione.getOraRientroAbitazione() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_ORA_RIENTRO_ABITAZIONE %>" size=5  >
        </td>
      </tr>

      <tr>
        <td class="l"><input value="11" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_11%>"></td>
        <td class="l">Non frequentare persone pregiudicate</td>
      </tr>

      <tr>
        <td class="l"><input value="12" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_12%>"></td>
        <td class="l">Presentarsi all'autorità di p.s. per i controlli
        <input value="<%=StringUtils.toStringJSP(lPrescrizione.getNumVolteControllo()) %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_NUM_VOLTE_CONTROLLO %>"  size=3>
        volte alla settimana</td>
      </tr>

      <tr>
        <td class="l"><input value="13" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_13%>"></td>
        <td class="l">Adempiere puntualmente agli obblighi di assistenza familiare verso i congiunti</td>
      </tr>

      <tr>
        <td class="l"><input value="14" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_14%>"></td>
        <td class="l">Comunicare al Magistrato di Sorveglianza, alla P.S. e all' UEPE ogni variazione relativa la luogo di lavoro o di abitazione.</td>
      </tr>

      <tr>
        <td class="l"><input value="15" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_15%>"></td>
        <td class="l">Sottoporsi ogni 15 gg ad esami tossicologici presso il S.E.R.T. / Comunità...</td>
      </tr>

      <tr>
        <td class="l"><input value="16" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_16%>"></td>
        <td class="l">Fa divieto al condannato di ricevere presso il proprio domicilio persone diverse da familiari e conviventi.</td>
      </tr>

      <tr>
        <td class="l"><input value="90" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_90%>"></td>
        <td class="l"><input value="<%=lPrescrizione.getDescrAltraPrescrizione() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_DESCR_ALTRA_PRESCRIZIONE%>"  size=65>
      </tr>

      <tr>
        <td class="l"><input value="91" type="checkbox" name="<%= ICostantiPrescrizione.CAMPO_CK_91%>"></td>
        <td class="l"><input value="<%=lPrescrizione.getDescrAltraPrescrizione() %>" type="text" name="<%= ICostantiPrescrizione.CAMPO_DESCR_ALTRA_PRESCRIZIONE%>" size=65 ></td>
      </tr>

      <tr>
        <td colspan=2>
          <input class="bottone" type="submit" value="Conferma">
          <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
          <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=IDEvento%>" >
          <input type="HIDDEN" name="<%=ICostantiPrescrizione.CAMPO_COMUNE_CSSA_COMP%>" >
          <input type="HIDDEN" name="nextaction" value="<%=nextaction%>" >
          <input type="HIDDEN" name="<%=ICostantiPrescrizione.CAMPO_ID_CSSA_COMPETENTE%>" value="<%=prescrizione.getIdCssaCompetente()%>">
        </td>
      </tr>

  </table>
  </form>
    <script language="JavaScript" type="text/javascript">
      var frmvalidator  = new Validator("LoadInserisciPrescrizione");
      frmvalidator.setAddnlValidationFunction("Verify");
    </script>

  </body>
</html>