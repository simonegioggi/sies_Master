<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.Utils"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>

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
<%
Date dataFinePena = (Date)request.getAttribute("dataFinePena");
%>
<html>
  <head>
    <title>[S.I.E.S.] - Gestione Procedimenti SIUS</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    </script>

    <script language="JavaScript">
      var desktop;

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
    </script>

    <script language="JavaScript">
      function Verify()
      {
        if (document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value;
        if (document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value;

        if (document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value;
        if (document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value.length==1)
            document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value='0'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value;

        // Controllo campo chiave anno.
        var anno_sistema='<%=DateUtils.getSysDate("yyyy")%>'
        var chiave_anno=document.LoadInserisciFascicoloManuale.<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.value;

        if(chiave_anno > anno_sistema)
        {
          alert("Il Campo Anno SIUS non può superare l'anno corrente");
          return false;
        }

        // STUB 01/12/2004 Controllo chiave anno minimo. STUB 03/01/2007 Anno minimo cambiato da 1995 a 1990.
        var anno_minimo='1990'
        if(chiave_anno < anno_minimo)
        {
          alert("Il Campo Anno SIUS non è valido");
          return false;
        }

        // Controllo obbligatorietà tipo atto.
        var tipoAtto=document.LoadInserisciFascicoloManuale.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>[document.LoadInserisciFascicoloManuale.<%= ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>.selectedIndex].value;

        if(tipoAtto =="-")
        {
          alert("Il Campo Tipo Atto è obbligatorio");
          return false;
        }

        // Controllo della data atto solo se valorizzata.
        var data_atto=document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO%>.value;
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

        // Controllo della data fine pena solo se valorizzata.
        var data_finepena=document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA%>.value;
        if ( data_finepena!='//')
        {
          if (! ControllaData(data_finepena))
          {
            alert('Data fine pena non valida');
            return false;
          }
          // Controllo della data fine pena => data di sistema.
          // STUB 17/01/2005 aggiunta richiesta di proseguimento.
          if ( !CompareDate( data_sistema, data_finepena ))
          {
            if(! confirm("Data fine pena < Data odierna ! Si vuole continuare ?" ) )
              return false;
          }
        }

        // Controllo obbligatorietà contenuto.
        var contenuto=document.LoadInserisciFascicoloManuale.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFascicoloManuale.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;

        if(contenuto =="-")
        {
          alert("Il Campo Contenuto è obbligatorio");
          return false;
        }

        // Controllo della data arrivo
        var data_arrivo=(document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ARRIVO%>.value)+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_MESE_DATA_ARRIVO%>.value+'/'+document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_ANNO_DATA_ARRIVO%>.value;
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

        // Controllo della data atto <= data arrivo
        if (( data_atto!='//') &&
            (! CompareDate(data_atto, data_arrivo)))
        {
          alert('Data atto > data arrivo in cancelleria');
          return false;
        }
      return true;
      }
    </script>
  </head>

  <body class="corpo" onload="javascript:document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>.focus();" >
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class=LBG><font class="label">Funzione :</font>&nbsp;
<%
        Date lDataIscrizione = new Date();
        String lAction = new String();
        FascicoloGPModel lFascicolo = new FascicoloGPModel();
        if( modalita.equals("IF") )
        {
          lAction = "siap.sius.fascicolo.action.ActInserisciFascicoloManuale";
%>
          <font class="campo">Iscrizione Procedimento Manuale</font>
<%
        }
        else if( modalita.equals("IS") )
        {
          lAction = "siap.sius.fascicolo.action.ActInsFascicoloDaSoggettoManuale";
%>
          <font class="campo">Iscrizione Procedimento manuale da Soggetto</font>
<%
        }
%>
      </td>
    </tr>
  </table>

<%
  if( modalita.equals("IF") )
  {
%>
    <jsp:include page="/jsp/files/siap/sius/fascicolo/DettaglioSoggettoSentenzaSius.jsp"/>
<%
  }
  if( modalita.equals("IS") )
  {
%>
    <jsp:include page="/jsp/files/siap/sico/soggetto/SintesiSoggetto.jsp"/>
<%
  }
%>

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="LoadInserisciFascicoloManuale">

  <table cellspacing=0 cellpadding=0 width=95%>
    <tr>
      <td class="L">
        <font class="label">Fine pena</font>
<%
        // La dataFinePena può essere valorizzata solo in alcuni casi con modalità = "IF"
        if( dataFinePena != null )
        {
%>
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2"readonly>
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2"readonly>
          /
          <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(dataFinePena,"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4"readonly>
<%
        }
        else if( modalita.equals("IS") )
        {
%>
          <input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
<%
        }
        else
        {
%>
          <input type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENA%>" maxlength="2" size="2"readonly>
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_FINE_PENA %>" maxlength="2" size="2"readonly>
          /
          <input type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_FINE_PENA %>" maxlength="4" size="4"readonly>
<%
        }
%>
        <font class="l">&nbsp;&nbsp;Pos. Giuridica </font>
<%
        if( modalita.equals("IS") )
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>">
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=lFascicolo.getGeneraleProcedimentoModel().getCodPosGiuridica()%>" >
<%
        }
        // La Posizione Giuridica è prevalorizzata solo se modalità = "IF"
        else if( ! posGiuridica.equals("") )
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  DISABLED>
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="<%=posGiuridica%>"  >
<%
        }
        else
        {
%>
          <select title="posGiuridica" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>"  DISABLED>
            <%= posizioneGiuridica %>
          </select>
          <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_POS_GIURIDICA%>" value="-">
<%
        }
%>
      </td>
    </tr>

<%
    // Gestione del luogo detenzione.
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
      <td class="lRosso">Anno/Progressivo SIUS <font class=ob>(*)</font></td>

      <td class="l">
        <input Title="Anno SIUS"  type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4">/
        <input Title="Numero SIUS" type="text" name="<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6">
      </td>
    </tr>

    <tr>
      <td class="l">Tipo Atto <font class=ob>(*)</font></td>
      <td class="L">
        <select title="tipoAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_TIPO_ATTO%>">
          <%= tipoAtto %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Data atto </td>
      <td class="L">
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"dd")) %>" type="text" name="<%=ICostantiFascicoloSius.CAMPO_GIORNO_DATA_ATTO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
        /
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"MM")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_MESE_DATA_ATTO %>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
        /
        <input  value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lFascicolo.getGeneraleProcedimentoModel().getDataRichiesta(),"yyyy")) %>" type="text" name="<%= ICostantiFascicoloSius.CAMPO_ANNO_DATA_ATTO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>

    <tr>
      <td class="l">Mittente </td>
      <td class="L">
        <select title="mittenteAtto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MITTENTE_ATTO%>">
          <%= mittenteAtto %>
        </select>
        &nbsp;&nbsp;
        <input Title="descrMittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_MITTENTE%>"type="text" maxlength="200" size="50">
      </td>
    </tr>

    <tr>
      <td class="l">Sede Mittente </td>
      <td class="l">
        <input Title="Sede Mittente" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_SEDE_MITTENTE%>"
        value="<%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrSedeMittente())%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciFascicoloManuale','<%= ICostantiFascicoloSius.CAMPO_DESCR_SEDE_MITTENTE %>');">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

    <tr>
      <td class="l">Contenuto <font class="ob">(*)</font></td>
      <td class="L">
        <select title="contenuto" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>">
          <%= contenuto %>
        </select>
    </tr>

    <tr>
      <td class="l">Oggetto </td>
      <td class="l">
        <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly>
          <%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getDescrOggettoProcedimento()) %>
        </Textarea>
        <a href="Javascript:ListaOggetti('LoadInserisciFascicoloManuale',document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
        <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
        &nbsp;
        <a href="Javascript:ListaOggetti('LoadInserisciFascicoloManuale','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciFascicoloManuale.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
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
      <td class="L" >
        <select title="magistrato" class=small name="<%=ICostantiFascicoloSius.CAMPO_COD_MAGISTRATO%>" >
          <%= magistrato %>
        </select>
    </tr>

    <tr>
      <td class="l">Note</td>
      <td class="l">
        <Textarea Title="Note" name="<%= ICostantiFascicoloSius.CAMPO_NOTE %>" cols=80 rows=5><%=StringUtils.toStringJSP(lFascicolo.getGeneraleProcedimentoModel().getAnnotazione()) %></textarea>
      </td>
    </tr>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma">
      </td>
    </tr>

  </table>

  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_SEDE_MITTENTE%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.LUOGO_DETENZIONE%>" value="<%=luogoDetenzione%>" >
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_LUOGO_DETENZIONE%>" value="<%=idLuogoDetenzione%>">
  <input type="HIDDEN" name="<%=ICostantiFascicoloSius.ID_ALTRA_CAUSA%>" value="<%=idAltraCausa%>">
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("LoadInserisciFascicoloManuale");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","req", "Il campo Anno Fascicolo è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_CHIAVE_PROGR%>","req", "Il campo Progressivo Fascicolo è obbligatorio");

    frmvalidator.addValidation("<%= ICostantiFascicoloSius.CAMPO_CHIAVE_ANNO%>","numeric");
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

  </body>
</html>