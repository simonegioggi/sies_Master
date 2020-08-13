<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Date" %>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.security.model.FunctionModel" %>

<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sico.security.model.FunzioneModel" %>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.titoloesecutivo.action.ICostantiTitoloEsecutivo" %>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoritaCompetente" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrProvvedimento" scope="request" class="java.lang.String"/>
<jsp:useBean id="AutoritaEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="DescrAutoritaEmittente" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="FascSiepTrovato" scope="request" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="TornaQui" scope="request" class="java.lang.String" />
<%
String aChiaveAnno = "";
String aChiaveProgr = "";
String aChiaveUfficio = "";
String aSedeUfficio = "";
String aDataProvvedimento = "";
String aAnnoSentenza = "";
String aNumeroSentenza = "";
String aDataIrrevocabilità = "";
String aNote = "";
String aCognomeRif = "";
String aNomeRif = "";
String aComuneNasRif = "";
String aDataNasRif = "";

if (FascSiepTrovato.getChiaveAnno() != null) {
	aChiaveAnno = FascSiepTrovato.getChiaveAnno().toString();
	aChiaveProgr = FascSiepTrovato.getChiaveProgr().toString();
	aChiaveUfficio = FascSiepTrovato.getChiaveUfficio();
	aSedeUfficio = FascSiepTrovato.getDescrComuneUfficio();
	aDataProvvedimento = StringUtils.toStringJSP( DateUtils.getDateToString(FascSiepTrovato.getSentenza().getDataProvvedimento(), "dd/MM/yyyy"));
	// Ticket#20200812012 - Anno e numero sentenza non sono obbligatori in caso di Ordinanza Mis. Sic. andava in nullpointer
	aAnnoSentenza   = StringUtils.toStringJSP(FascSiepTrovato.getSentenza().getAnnoSentenza(), " ");
	aNumeroSentenza = StringUtils.toStringJSP(FascSiepTrovato.getSentenza().getNumeroSentenza(), " ");
	// anche la data di nascita può essere null
	aDataNasRif = StringUtils.toStringJSP(FascSiepTrovato.getSoggetto().getDataNascita(), " ");
	// end Ticket#20200812012
	// modifica conseguente alla variazione di SentenzaModel - Romaggioli 29/07/2009
  	// aDataIrrevocabilità = StringUtils.toStringJSP( DateUtils.getDateToString(FascSiepTrovato.getSentenza().getDataIrrevocabilita(), "dd/MM/yyyy"));
  	// aNote = "";
 	aCognomeRif = FascSiepTrovato.getSoggetto().getCognome();
  	aNomeRif = FascSiepTrovato.getSoggetto().getNome();
  	aComuneNasRif = FascSiepTrovato.getSoggetto().getDescrComuneNascita();
}
/*else {
	aDataProvvedimento = "";
	aAnnoSentenza = "";
	aNumeroSentenza = "";
	aDataIrrevocabilità = "";
	aNote = "";
}*/

// Controllo soggetti.
String aCognome = "";
String aNome = "";
String aComuneNas = "";
String aDataNas = "";
if (fascicoloSiusGP.getFascicoloSiusModel().getSoggetto() != null) {
	aCognome = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getCognome();
	aNome = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getNome();
	aComuneNas = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getDescrComuneNascita();
	aDataNas = fascicoloSiusGP.getFascicoloSiusModel().getSoggetto().getDataNascita().toString();
}

%>
<html>
  <head>
    <title>[S.I.E.S.] - Inserimento Titolo Esecutivo Principale</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
      var desktop;
      function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
      {
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function TrasformaRes(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroRes&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      function TrasformaPret(a_formname,a_fieldname,a_fieldname2)
      {
        desktop = window.open("/jsp/Main.jsp?Action=siap.sico.util.ActCalcolaNumeroPret&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2,"Calcola_Numero_Res","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }
      function VerifyRicerca()
      {
        // Impostazione action di ricerca.
        document.AssegnaTitoloEsecutivo.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.titoloesecutivo.action.ActRicercaTitoloEsecutivo"

        // Controllo Anno/Numero Fascicolo SIEP.
        if ( document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length<4 || document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value<1900 || isNaN(document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value) )
        {
          alert ("Anno Fascicolo SIEP Non Valido");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }
        if ( document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length<=0 || document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>.value<0 || isNaN(document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>.value) )
        {
          alert ("Numero Fascicolo SIEP Non Valido");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }
        // Controllo obbligatorietà autorità Competente ed Emittente.
        if (document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value=="-")
        {
          alert("Il tipo autorità competente è un campo obbligatorio");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }

        if (document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value=="")
        {
          alert("Il luogo per l'autorità competente è un campo obbligatorio");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }

        document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_NUMERO_PROVVEDIMENTO%>.value = "";
        document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_AUTORITA_EMITTENTE%>.value = "-";
        document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_DESCR_LUOGO_EMITTENTE%>.value = "";
        return true;
      }

      function Verify(aModalita)
      {
        // Impostazione action di Inserimento/Modifica.
        if(aModalita=="I")
          document.AssegnaTitoloEsecutivo.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.titoloesecutivo.action.ActInserisciTitoloEsecutivo";

        if(aModalita=="M")
          document.AssegnaTitoloEsecutivo.<%=IWebConstants.ACTION_FIELD%>.value="siap.sius.titoloesecutivo.action.ActModificaTitoloEsecutivo";

        // Controllo Anno/Numero Fascicolo SIEP.
        if ( document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value.length<4 || document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value<1900 || isNaN(document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.value) )
        {
          alert ("Anno Fascicolo SIEP Non Valido");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }
        if ( document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.value.length<=0 || document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>.value<0 || isNaN(document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>.value) )
        {
          alert ("Numero Fascicolo SIEP Non Valido");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP%>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }
        // Controllo obbligatorietà autorità Competente.
        codUfficio = document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.value;
        if (codUfficio=="-")
        {
          alert("Il tipo autorità competente è un campo obbligatorio");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }

        if (document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.value=="")
        {
          alert("Il luogo per l'autorità competente è un campo obbligatorio");
          document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>.focus();
          document.AssegnaTitoloEsecutivo.CONFERMA.disabled=false;
          return false;
        }

        // Controllo soggetti.
	     if (<%=FascSiepTrovato.getIdFascicoloSiep()%> != null  )
	     {
	       var soggettoOri = "<%=aCognome%>"+"<%=aNome%>"+"<%=aComuneNas%>"+"<%=aDataNas%>";
	       var soggettoRif = "<%=aCognomeRif%>"+"<%=aNomeRif%>"+"<%=aComuneNasRif%>"+"<%=aDataNasRif%>";
	       if (soggettoOri != soggettoRif)
	       {
	         if(! confirm("Soggetto del Titolo Esecutivo differente dal soggetto del procedimento SIUS! Si vuole continuare ?" ) )
	         return false;
	       }
	     }
       if(aModalita=="I")
         if(! confirm("Si conferma l'Assegnazione del Titolo Esecutivo?" ) )
           return false;

       if(aModalita=="M")
         if(! confirm("Si conferma la Ridefinizione del Titolo Esecutivo?" ) )
           return false;

       //document.AssegnaTitoloEsecutivo.Ricerca.disabled=true;
       //document.AssegnaTitoloEsecutivo.CONFERMA.disabled=true;
       return true;
     }
     </script>

  </head>

  <body class="corpo">
  <link rel="STYLESHEET" type="text/css" href="/css/style.css">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%
          String lAction = new String();
          if( modalita.equals("I") )
          {
              lAction = "siap.sius.titoloesecutivo.action.ActInserisciRifFascicoloSiep";
%>
            <font class="campo">Assegnazione Titolo Esecutivo Principale</font>
<%
          }
          else if( modalita.equals("M") )
          {
            lAction = "siap.sius.titoloesecutivo.action.ActModificaRifFascicoloSiep";
%>
            <font class="campo">Ridefinizione Titolo Esecutivo Principale</font>
<%
          }
%>
      </td>
    </tr>
  </table>

  <br>

  <table>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table>
  <br>
  <table>
    <tr>
      <td class="lVerdeNB">N.B. Se il Titolo Esecutivo di riferimento è su altra BDI, ricercare prima il procedimento SIEP con apposita funzione </td>
    </tr>
  </table>

  <FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name='AssegnaTitoloEsecutivo'>
    <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="LBG" colspan="2" >
        <font class="label">Estremi del Titolo Esecutivo Principale</font>&nbsp;
      </td>
    </tr>

    <tr>
      <td class="l"width="32%">Anno/Numero SIEP <font class=ob>(*)</font></td>
      <td class="l">
        <input type="text" name="<%=ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>" value ="<%=aChiaveAnno%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        /<input type="text" name="<%=ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>" value ="<%=aChiaveProgr%>" maxlength="14" size="14" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
         &nbsp;&nbsp;<a href="Javascript:TrasformaRes('AssegnaTitoloEsecutivo','<%= ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>','<%= ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>');">
         R.E.S.<img src="/images/filefolder.gif" border=0></a>&nbsp;&nbsp;
         <a href="Javascript:TrasformaPret('AssegnaTitoloEsecutivo','<%= ICostantiTitoloEsecutivo.CAMPO_ANNO_FASCICOLO_SIEP %>','<%= ICostantiTitoloEsecutivo.CAMPO_PROGR_FASCICOLO_SIEP %>');">
         P.T.<img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>

    <tr>
      <td class="l">Autorità <font class=ob>(*)</font></td>
      <td class="L">
        <select class=medium name="<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>" >
          <%= AutoritaCompetente %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Luogo <font class=ob>(*)</font></td>
      <td class="l">
        <input name="<%=ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP%>" value ="<%=aSedeUfficio%>" type="text" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('AssegnaTitoloEsecutivo','<%= ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP %>' , document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>[document.AssegnaTitoloEsecutivo.<%=ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP%>.selectedIndex].value);">
        <img src="/images/filefolder.gif" border=0> </a>
      </td>
    </tr>

    </table>
    <br>

    <table cellspacing=0 cellpadding=0 width="80%">
      <tr>
        <td>
          <input class="bottone" type="submit" name="Ricerca" value="Nuova Ricerca" onClick="javascript:return VerifyRicerca();">
        </td>
      </tr>
    </table>

    <br>
    <br>

    <table cellspacing=0 cellpadding=0 width="80%">
    <tr>
      <td class="l" >
        <font class="label" >Tipo Provvedimento : &nbsp;&nbsp;</font>
        <font class="campo"><%=DescrProvvedimento%>&nbsp;</font>
        <font class="label">del&nbsp;</font>
        <font class="campo"><%=aDataProvvedimento%></font>
      </td>
    </tr>

    <tr>
      <td class="l">
        <font class="label">Anno/Numero Provvedimento : &nbsp;&nbsp;</font>
        <font class="campo"><%=aAnnoSentenza%>/<%=aNumeroSentenza%></font>
        <font class="label">&nbsp;&nbsp;definitivo in Data : &nbsp;</font>
        <font class="campo"><%=aDataIrrevocabilità%></font>
      </td>
    </tr>

    <tr>
      <td class="l">
        <font class="label">Autorità Emittente : </font>
        <font class="campo"><%=DescrAutoritaEmittente%></font>
        <font class="label">&nbsp;di&nbsp;</font>
        <font class="campo"><%=FascSiepTrovato.getSentenza().getDescrLuogoEmittente()%></font>
      </td>
    </tr>
  </table>

  <table cellspacing=0 cellpadding=0 width="80%">
<%
    if (FascSiepTrovato.getSoggetto() != null )
    {%>
      <tr>
        <td class="L" width=100%><font class="label">Soggetto:</font>
        <font class="campo">
          <a class="cliccabile" href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=FascSiepTrovato.getSoggetto().getIdSoggetto()%>&TornaQui=<%=TornaQui%>" title="Soggetto">
          <%=FascSiepTrovato.getSoggetto().getCognome()%>&nbsp;<%=FascSiepTrovato.getSoggetto().getNome()%>
        </a>
      </font>&nbsp;
<%
        if (FascSiepTrovato.getSoggetto().getSesso().compareTo("F")==0)
        {%>
          <font class="label">nata il :</font>&nbsp;
        <%}
         else
        {%>
          <font class="label">nato il :</font>&nbsp;
        <%}
        if(FascSiepTrovato.getSoggetto().getDataNascita() == null)
        {
          if(FascSiepTrovato.getSoggetto().getDataNascitaPresunta().equals("S"))
          {%>
            <font class="campo"><%=StringUtils.toStringJSP(FascSiepTrovato.getSoggetto().getAnnoNascita())%></font>&nbsp;
          <%}else{%>
            <font class="campo">***</font>&nbsp;
        <%}}else{%>
            <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(FascSiepTrovato.getSoggetto().getDataNascita(),"dd-MM-yyyy"))%></font>&nbsp;
       <%}%>
      <font class="label">in : </font>
      <font class="campo">
<%    if (FascSiepTrovato.getSoggetto().getDescrComuneNascita().compareTo("-")==0)
      {%>
        <%=FascSiepTrovato.getSoggetto().getDescrStatoNascita()%>
      <%}
      else
      {%>
        <%=FascSiepTrovato.getSoggetto().getDescrComuneNascita()+ "  ("+FascSiepTrovato.getSoggetto().getCodProvinciaNascita()+")" %>
      <%}
%>
      </font>
     </td>
    </tr>
   <%}%>
    </table>

    <br>
    <table>
      <tr>
        <td class="lVerdeNB">N.B. Confermando l'operazione si associa il procedimento SIUS al Titolo Esecutivo selezionato </td>
      </tr>
    </table>

    <br>
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td>
          <input class="bottone" name="CONFERMA" type="submit" value="Conferma" onClick="javascript:return Verify('<%=modalita%>');">
        </td>
      </tr>
    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>" >
    <input type="HIDDEN" name="<%=ICostantiTitoloEsecutivo.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius()%>">
    <input type="HIDDEN" name="modalita" value="<%=modalita%>" >
<%
    if (FascSiepTrovato.getChiaveAnno() != null )
    {%>
      <input type="HIDDEN" name="<%=ICostantiTitoloEsecutivo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=FascSiepTrovato.getIdFascicoloSiep()%>">
      <input type="HIDDEN" name="<%=ICostantiTitoloEsecutivo.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=FascSiepTrovato.getSentenza().getCodLuogoEmittente()%>">
      <input type="HIDDEN" name="idFascicoloPrincipale" value="<%=FascSiepTrovato.getIdFascicoloSiep()%>">
      <input type="HIDDEN" name="annoFascicoloPrincipale" value="<%=FascSiepTrovato.getChiaveAnno()%>">
      <input type="HIDDEN" name="progrFascicoloPrincipale" value="<%=FascSiepTrovato.getChiaveProgr()%>">
  <%}else{ %>
      <input type="HIDDEN" name="<%=ICostantiTitoloEsecutivo.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" >
      <input type="HIDDEN" name="<%=ICostantiTitoloEsecutivo.CAMPO_COD_LUOGO_EMITTENTE%>" >
  <%}%>

  </FORM>

    <script language="JavaScript" type="text/javascript">

      var frmvalidator  = new Validator("AssegnaTitoloEsecutivo");

      //frmvalidator.setAddnlValidationFunction("Verify");

    </script>

  </body>
</html>