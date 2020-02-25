<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.iscrizioneprocedimento.action.ICostantiIscrProcedimento" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="mittenteAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto" scope="request" class="java.lang.String"/>
<jsp:useBean id="oggetto" scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="posGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>
<jsp:useBean id="luogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idLuogoDetenzione" scope="request" class="java.lang.String"/>
<jsp:useBean id="idAltraCausa" scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeMittenteAtto" scope="request" class="java.lang.String"/>

<%
// STUB 12/11/2003 Modifiche per la gestione del dettaglio oggetto (vedi fieldcodesdet).
Date dataFinePena = (Date)request.getAttribute("dataFinePena");
Date dataAtto = (Date)request.getAttribute("dataAtto");

// STUB 11/05/2004 Recupero Id Fascicolo inviato per valorizzare il campo IdFascicoloSiusOrigine di FASCICOLO_SIUS.
// STUB 01/03/2005 Corretta valorizzazione di idFascicoloInviato.
String idFascicoloInviato = "";
if (request.getAttribute("idFascicoloInviato")!=null)
  idFascicoloInviato = (String)request.getAttribute("idFascicoloInviato");
%>
<html>
  <head>
    <title>[S.I.E.S.] - Presa in carico - Iscrizione Procedimento</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var aMagistrato='document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>.[document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>.selectedIndex].value';
      var desktop;
      function checkMagistrato()
      {
        if (typeof (aMagistrato) == 'undefined' || aMagistrato.length < 2 )
        {
          alert('Nessun Magistrato presente in archivio. Impossibile Iscrivere il Procedimento!');
          return false;
        }
        else
	{
          document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>.focus();
          return true;
	}
      }

      function ListaUffici(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      // Chiamata funzione lista Oggetti.
      function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
      {
        // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
        var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
            aLink += "&formname="+a_formname;
            aLink += "&field_contenuto="+a_field_contenuto;
            aLink += "&fieldname="+a_fieldname;
            aLink += "&fieldcodes="+a_fieldcodes;
            aLink += "&fieldcodesdet="+a_fieldcodesdet;
            aLink += "&ifieldcodes="+i_fieldcodes;
            aLink += "&ifieldcodesdet="+i_fieldcodesdet;
        desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
      }

      function Verify()
      {
        if (document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value.length==1)
            document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value='0'+document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value;
        if (document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value.length==1)
            document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value='0'+document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value;

        // Controllo esistenza Magistrati per l'ufficio.
        if (typeof (aMagistrato) == "undefined" || aMagistrato.length < 2 )
        {
          alert("Nessun Magistrato presente in archivio. Impossibile Iscrivere il Procedimento!");
          return false;
        }

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadIscrProcedimento.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>[document.LoadIscrProcedimento.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;

        if(tipoAtto =="-")
        {
          alert("Il Campo Tipo Atto è obbligatorio");
          return false;
        }

        // Controllo della data atto solo se valorizzata.
        var data_atto=document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>.value+'/'+document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>.value+'/'+document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        if ( data_atto!='//')
        {
          if (! ControllaData(data_atto))
          {
            alert('Data atto non valida');
            return false;
          }
          // Controllo della data atto <= data di sistema
          if (! CompareDate(data_atto, data_sistema))
          {
            alert('Data atto > della data odierna');
            return false;
          }
        }

        // Controllo obbligatorietà contenuto.
        var contenuto=document.LoadIscrProcedimento.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadIscrProcedimento.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;

        if(contenuto =="-")
        {
          alert("Il Campo Contenuto è obbligatorio");
          return false;
        }

        // Controllo della data arrivo solo se valorizzata.
        var data_arrivo=document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value+'/'+document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value+'/'+document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value;
        if ( data_arrivo!='//')
        {
          // Controllo di validità della data arrivo.
          if (! ControllaData(data_arrivo))
          {
            alert('Data di arrivo in cancelleria non valida');
            return false;
          }
          // Controllo della data arrivo <= data di sistema
          if (! CompareDate(data_arrivo, data_sistema))
          {
            alert('Data di arrivo > della data odierna');
            return false;
          }
          // Controllo della data atto <= data arrivo in cancelleria
          if (! CompareDate(data_atto, data_arrivo))
          {
            alert('Data atto > data arrivo in cancelleria');
            return false;
          }
        }
        return true;
      }
    </script>
  </head>

  <BODY class="corpo" onload="javascript:checkMagistrato();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        FascicoloGPModel lFascicolo = new FascicoloGPModel();

        String lAction = new String();

        if( modalita.equals("IT") )
        {
          // STUB 20/07/2004 Si distingue il caso dell'assenza del fascicolo SIEP in sessione.
          if (session.getAttribute("fascicolo")==null)
            lAction = "siap.sius.fascicolo.action.ActInsFascicoloDaSoggetto";
          else
            lAction = "siap.sius.fascicolo.action.ActInsFascicoloDaSius";
%>
          <font class="campo">Presa in carico atto pervenuto da SIUS</font>
<%
        }
        else if( modalita.equals("IE") )
        {
          lAction = "siap.sius.fascicolo.action.ActInserisciFascicolo";
%>
          <font class="campo">Presa in carico atto pervenuto da SIEP</font>
<%
        }
%>
        </td>
      </tr>
    </table>
  <br>

  <%-- Visualizzo la sintesi dei dati dell'atto Originario Se si proviene da siep o sius non cambia --%>
  <jsp:include page="<%=ICostantiIscrProcedimento.PG_LOAD_SINTESI_SOGGETTO_SENTENZA%>"/>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='LoadIscrProcedimento'>
  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Fine pena</font>
<%
        // La dataFinePena può essere valorizzata solo in alcuni casi con modalità = "IE"
        if( modalita.equals("IE") && dataFinePena != null )
        {
%>
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly>
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly>
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly>
<%
        }
        else
        {
%>
          <input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" readonly>
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" readonly>
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" readonly>
<%
        }
%>
        <font class="l">&nbsp;&nbsp;Pos. Giuridica </font>
<%
        if( modalita.equals("IE") )
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" DISABLED>
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=posGiuridica%>"  >
<%
        }else{
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  DISABLED>
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="-"  >
<%
        }%>
      </td>
    </tr>

<%
    // STUB 01/01/2004 Gestione del luogo detenzione.
    if(!luogoDetenzione.equals("") )
    {
%>
      <tr>
        <td class="L">
          <font class="label">Detenuto in &nbsp;&nbsp;&nbsp;</font>
          <font class="campo"><%=luogoDetenzione%> &nbsp;&nbsp;&nbsp;&nbsp;</font>
          <input type=checkbox name="<%=ICostantiFascicoloSius.CAMPO_VALIDA_LUOGO_DET%>" value=1 title="Valida il Luogo Detenzione">
        </td>
      </tr>
<%
    }
%>

  </table>

  <br>

  <table cellspacing="2" cellpadding="2">
    <tr>
      <td class="l">Tipo Atto <font class=ob>(*)</font></td>
      <td class="L">
        <select title="tipoAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>">
          <%= tipoAtto %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Data atto</td>
      <td class="L">
<%
      if( dataAtto != null )
      {
%>
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataAtto,"dd")) %>" type="text" name="<%=ICostantiIscrProcedimento.CAMPO_GIORNO_DATA_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataAtto,"MM")) %>" type="text" name="<%=ICostantiIscrProcedimento.CAMPO_MESE_DATA_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataAtto,"yyyy")) %>" type="text" name="<%= ICostantiIscrProcedimento.CAMPO_ANNO_DATA_ATTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
    <%}else{%>
        <input type="text" name="<%=ICostantiIscrProcedimento.CAMPO_GIORNO_DATA_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%= ICostantiIscrProcedimento.CAMPO_MESE_DATA_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" name="<%= ICostantiIscrProcedimento.CAMPO_ANNO_DATA_ATTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      <%}%>
      </td>
    </tr>

    <tr>
      <td class="l">Mittente </td>
      <td class="L">
        <select title="mittenteAtto" class=small name="<%=ICostantiIscrProcedimento.CAMPO_COD_MITTENTE_ATTO%>">
          <%= mittenteAtto %>
        </select>
        &nbsp;&nbsp;
        <input Title="descrMittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_MITTENTE%>" type="text" maxlength="200" size="50">
      </td>
    </tr>

    <tr>
      <td class="l">Sede Mittente </td>
      <td class="l">
        <input Title="Sede Mittente" name="<%=ICostantiIscrProcedimento.CAMPO_DESCR_SEDE_MITTENTE%>"
        value="<%=sedeMittenteAtto%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadIscrProcedimento','<%= ICostantiIscrProcedimento.CAMPO_DESCR_SEDE_MITTENTE %>');">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

    <tr>
      <td class="l">Contenuto <font class=ob>(*)</font></td>
      <td class="L">
        <select title="contenuto" class=small name="<%=ICostantiIscrProcedimento.CAMPO_COD_CONTENUTO%>">
          <%= contenuto %>
        </select>
    </tr>

    <tr>
      <td class="l">Oggetto </td>
      <td class="l">
        <Textarea Title="Oggetto" name="<%= ICostantiIscrProcedimento.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly> <%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()) %> </Textarea>
        <a href="Javascript:ListaOggetti('LoadIscrProcedimento',document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
        <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
        &nbsp;
        <a href="Javascript:ListaOggetti('LoadIscrProcedimento','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadIscrProcedimento.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
        <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Data arrivo in cancelleria <font class=ob>(*)</font></td>
      <td class="L">
        <input  type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input  type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input  type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Magistrato </td>
      <td class="L">
        <select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" >
          <%= magistrato %>
        </select>
    </tr>

    <tr>
      <td class="l">Note</td>
      <td class="l"><Textarea Title="Note" name="<%= ICostantiIscrProcedimento.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getAnnotazione()) %></textarea></td>
    </tr>

  </table>

  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td>
        <input  class="bottone" type="submit" value="Conferma">
      </td>
    </tr>
  </table>

    <%--input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.iscrizioneprocedimento.action.ActIscrizioneProcedimento" --%>
    <%-- Qui il campo ACTION_FIELD va differenziato a seconda se si proviene da siep o da sius --%>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiIscrProcedimento.CAMPO_COD_SEDE_MITTENTE%>">
    <input type="HIDDEN" name="<%=ICostantiIscrProcedimento.CAMPO_COD_OGGETTO%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.LUOGO_DETENZIONE%>" value="<%=luogoDetenzione%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_LUOGO_DETENZIONE%>" value="<%=idLuogoDetenzione%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
    <input type="HIDDEN" name="idFascicoloInviato" value="<%=idFascicoloInviato%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_FASCICOLO_SIUS_ORIGINE%>" value="<%=idFascicoloInviato%>">

  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadIscrProcedimento");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>","minlen=4","La lunghezza del campo Anno Data Atto deve essere di 4 caratteri");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>","req", "Il campo Giorno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>","req", "Il campo Mese Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","req", "Il campo Anno Data Arrivo in cancelleria è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>","minlen=4","La lunghezza del campo Anno Data Arrivo in cancelleria deve essere di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>

  </BODY>
</html>