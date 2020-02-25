<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.siep.statis.action.ICostantiStatis"%>
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sius.statistiche.action.ICostantiStatistiche"%>
<%@ page import="siap.sico.security.action.ICostantiSecurity" %>
<%@ page import="siap.sius.cancelleriaassegnataria.action.ICostantiCancelleriaAssegnataria" %>

<jsp:useBean id="posizioneGiuridica" scope="request" class="java.lang.String"/>
<jsp:useBean id="cancelleriaAssegnataria" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAtto" scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSIUSTrattino" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato" scope="request" class="java.lang.String"/>


<head>
  <title> [S.I.E.S.] - Ricerca Procedimento Per Posizione Giuridica - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    var desktop;
    var flagBloccaUfficio = false;
    
    // Lista Procure per Distretti ( TDS )oppure Lista UDS
    /*
    function ListaTDS_UDS(a_formname,a_fieldname)
    {
      if (flagBloccaUfficio)
        return;
      
      var valore = document.f.<%=ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO%>.value;
      var i = document.f.<%=ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO%>.selectedIndex;
      if ( i == 0 || i == 2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Uffici di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
      else
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    }
    */
    
    function checkTipoUfficio()
    {
      if ("<%=TipoUfficioConnesso%>" == 'UDS')
      {
        document.f.<%=ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=2;
        document.f.<%=ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
      }else{
        document.f.<%=ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=1;
        document.f.<%=ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
      }
    }
    
    function radioStatoProcedimento() {
      var nodeEstremiPendenti;
      var nodeEstremiTutti;
      
      nodeEstremiPendenti=document.getElementById('estremiPendenti');
      nodeEstremiTutti=document.getElementById('estremiTutti');
      
      if (document.f.<%=ICostantiStatistiche.RADIO_STATO_PROCEDIMENTO%>[0].checked) {
        nodeEstremiTutti.style.visibility='visible';
        nodeEstremiPendenti.style.visibility='hidden';
        pulisciCampiStatoProcedimento();
      } else if (document.f.<%=ICostantiStatistiche.RADIO_STATO_PROCEDIMENTO%>[1].checked) {
        nodeEstremiTutti.style.visibility='hidden';
        nodeEstremiPendenti.style.visibility='visible';
    } else {
        nodeEstremiTutti.style.visibility='hidden';
        nodeEstremiPendenti.style.visibility='hidden';
        pulisciCampiStatoProcedimento();
    }
    }

    function pulisciCampiStatoProcedimento()
    {
      document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_FINE_PENDENZA%>.value='';
      document.f.<%=ICostantiStatistiche.CAMPO_MESE_FINE_PENDENZA%>.value='';
      document.f.<%=ICostantiStatistiche.CAMPO_ANNO_FINE_PENDENZA%>.value='';
    }

    function Verify()
    {
        var gg_in = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value);
        var mm_in = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value);
        var aa_in = document.f.<%=ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;

        var gg_fi = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value);
        var mm_fi = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_FINALE%>.value);
        var aa_fi = document.f.<%=ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
      
    var dataIni = gg_in + "/" + mm_in + "/" + aa_in;
    var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;

    var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
/*
    if (document.f.<%=ICostantiStatistiche.CAMPO_CHIAVE_UFFICIO%>.selectedIndex==0)
    {
      alert ("Selezionare il Tipo Ufficio.");
      return false;
    }
*/
/*
    var DescrUfficio = document.f.<%=ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO%>.value;
    if ( DescrUfficio.length==0 )
    {
      alert ("Selezionare la Sede Ufficio.");
      return false;
    }
*/
      if (dataIni.length != 2 && dataIni.length != 10)
      {
        alert ("Data Iniziale Iscrizione errata.");
        return false;
      }
      if (dataFine.length != 2 && dataFine.length != 10)
      {
        alert ("Data Finale Iscrizione errata.");
        return false;
      }
      else if ( (dataIni.length == 10) && (ControllaData (dataIni) == false) )
      {
        // entrambe le date valorizzate
        alert ("Errore nella data Iniziale Iscrizione.");
        return false;
      }
      else if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) )
      {
        alert ("Errore nella data Finale Iscrizione.");
        return false;
      }
      else if ((dataFine.length == 10)               &&
               ( dataIni.length == 10)               &&
               CompareDate(dataIni,dataFine)== false )
      {
        alert ("Data di Fine Iscrizione minore di Data Iniziale Iscrizione.");
        return false;
      }
      else if ((dataFine.length == 10)                       &&
                CompareDate(dataFine, data_sistema)== false)
      {
        alert ("Data Finale Iscrizione maggiore della Data di Sistema.");
        return false;
      }

      if(document.f.statoProcedimento[1].checked)
      {
        // Controllo della data fine pendenza.
        var gg_fp = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_GIORNO_FINE_PENDENZA%>.value);
        var mm_fp = FillDM(document.f.<%=ICostantiStatistiche.CAMPO_MESE_FINE_PENDENZA%>.value);
        var aa_fp = document.f.<%=ICostantiStatistiche.CAMPO_ANNO_FINE_PENDENZA%>.value;
        var dataFinePendenza = gg_fp + "/" + mm_fp + "/" + aa_fp;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';

        if ( dataFinePendenza.length < 10 )
        {
          alert ("Valorizzare la Data fine Pendenza.");
      document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>.focus();
          return false;
        }
        if ( (dataFinePendenza.length == 10) && (ControllaData (dataFinePendenza) == false) )
        {
          alert ("Errore nella Data di Fine Pendenza.");
          document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>.focus();
          return false;
        }
        if ((dataFinePendenza.length == 10)  &&   CompareDate(dataFinePendenza, data_sistema)== false)
        {
          alert ("Data di Fine Pendenza maggiore della Data di Sistema");
          document.f.<%=ICostantiFascicoloSius.CAMPO_GIORNO_FINE_PENDENZA%>.focus();
          return false;
        }
        
      }
      
      // Si riabilitano i campi di input per l'ufficio
      //sbloccaUfficio();
     }

    function ListaUfficiDistretto(a_formname,a_fieldname,a_fieldname2)
    {
      var TipoUff = document.f.<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>.value;
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaComunePerDistretto&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&codTipoUff="+TipoUff, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
      
  </script>
</head>

<body class="corpo" onLoad="radioStatoProcedimento();"  >

  <FORM method="POST" name="f" action="<%= IWebConstants.PG_MAIN%>">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.sius.statistiche.action.ActRicercaProcPosizioneGiuridica">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento Per Posizione Giuridica</font>
      </td>
    </tr>
  </table>

  <br>

  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="label">Indicare i criteri di ricerca :</td>
    </tr>
    <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
    <%--
    <tr>
      <td class="l">Tipo Ufficio <font class=ob>(*)</font></td>
      <td class="L">
        <select title="tipoUfficioSIUSTrattino" class=small name="<%=ICostantiFascicoloSius.CAMPO_CHIAVE_UFFICIO%>" >
          <%=tipoUfficioSIUSTrattino%>
        </select>
      </td>
    </tr>
    <tr>
      <td class="l">Sede  <font class=ob>(*)</font></td>
      <td class="l">
         <input Title="Sede Procura" name="<%=ICostantiFascicoloSius.CAMPO_DESCR_COMUNE_UFFICIO%>" type="text" maxlength="35" size="35">
          <a  href="Javascript:ListaTDS_UDS('f','<%= ICostantiStatistiche.CAMPO_DESCR_COMUNE_UFFICIO%>');" >
          	<img src="/images/filefolder.gif" border=0>
          </a>
      </td>
    </tr>
  	--%>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L">
        <select title="posizioneGiuridica" class=small name="<%=ICostantiStatistiche.CAMPO_COD_POSIZIONE_GIURIDICA%>" >
          <%= posizioneGiuridica %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Tipo Atto </td>
      <td class="L">
        <select title="tipoAtto" class=small name="<%=ICostantiStatistiche.CAMPO_COD_OGGETTO%>" >
          <%= tipoAtto %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Magistrato </td>
      <td class="L" >
        <select title="magistrato" class=small name="<%=ICostantiStatistiche.CAMPO_COD_MAGISTRATO%>" >
          <%= magistrato %>
        </select>
      </td>
    </tr>

    <tr>
      <td class="l">Cancelleria Assegnataria </td>
      <td class="L">
        <select title="cancelleriaAssegnataria" class=small name="<%=ICostantiStatistiche.CAMPO_COD_CANCELLERIA%>" >
          <%= cancelleriaAssegnataria %>
        </select>
      </td>
    </tr>

    </table>
    
  <table width="90%">
    <tr>
      <td>&nbsp;</td>
      </tr>
        <tr>
          <td class="Titolo" >Stato Procedimento</td></tr>
        <tr>
      <td class="c">
        Tutti &nbsp; <input type="radio" name="<%=ICostantiStatistiche.RADIO_STATO_PROCEDIMENTO%>" 
                            value="tutti" onClick="radioStatoProcedimento();" checked>
        &nbsp;&nbsp;&nbsp;&nbsp;
        Solo Pendenti &nbsp; 
                     <input type="radio" name="<%=ICostantiStatistiche.RADIO_STATO_PROCEDIMENTO%>" 
                            value="pendenti" onClick="radioStatoProcedimento();" >
      </td>
    </tr>
    <tr>
      <td>
        <div id="estremiTutti" style="visibility:hidden; " >  
            <table  width="100%">
              <tr><td>&nbsp;</td></tr>
            </table>
          </div>
        <div id="estremiPendenti" style="visibility:hidden; " >  
          <table  width="100%">
            <tr>
              <td colspan='2' class="Titolo">Indicare la data di fine Pendenza </td>
            </tr>
            <tr>
                <td class="l">
                  <font class="label">Data Fine Pendenza <font class=ob>(*)</font></font>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
                  <input type="text" title="Giorno Fine Pendenza" name="<%=ICostantiStatistiche.CAMPO_GIORNO_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
                <input type="text" title="Mese Fine Pendenza" name="<%=ICostantiStatistiche.CAMPO_MESE_FINE_PENDENZA%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> -
                <input type="text" title="Anno Fine Pendenza" name="<%=ICostantiStatistiche.CAMPO_ANNO_FINE_PENDENZA%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
                </td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
  </table>

      <table width=90%>
        <tr> <td class="Titolo"  colspan ="4" >Intervallo Date di Iscrizione</td>
        </tr>
        <tr>
          <td class="L" width="20%" >
            <font class="label"> Data Iniziale </font>
          </td>
          <td class="l" >
            <input type="text" title="Giorno Iniziale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Iniziale" name="<%=ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Iniziale" name="<%=ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>"  maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
          </td>
          <td class="L" >
            <font class="label">Data Finale </font>
          </td>
          <td class="l">
            <input type="text" title="Giorno Finale" name="<%=ICostantiStatistiche.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Mese Finale" name="<%=ICostantiStatistiche.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
            <input type="text" title="Anno Finale" name="<%=ICostantiStatistiche.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
          </td>
        </tr>
      </table>
  <p/>
  <table width=90% style="visibility:visible;">
  <tr><td class="L">
      &nbsp;&nbsp;<input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return Verify();">
  </td></tr>
  </table>

  <input type="HIDDEN" name="valoreRadio" value="">
  <input type="HIDDEN" name="valoreStatoProcedimento" value="">

</form>
<script language="JavaScript" type="text/javascript">

 var frmvalidator  = new Validator("f");

  </script>
</body>

</html>