<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="siap.siep.risultatoricerca.action.ICostantiRisultatoRicerca"%>
<%@ page import="siap.sico.decodifiche.controller.DecodificheManager"%>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>

<jsp:useBean id="posizioneGiuridica" scope="request" class="java.util.ArrayList"/>
<jsp:useBean id="TipiNazione" scope="request" class="java.lang.String"/>

<html>
<head>
  <title> [S.I.E.S.] - Ricerca Procedimenti per applicazioni Benefici - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%> ></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
  function Radio()
  {
    if(!document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_POSIZIONE_GIURIDICA_AGGREGATA%>[0].checked)
     {
       document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_COD_POSIZIONE_GIURIDICA%>.disabled=true;
     }
     else
     {
      document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_COD_POSIZIONE_GIURIDICA%>.disabled=false;
     }
  }

  function verifica()
  {

  	  if ((document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_NUM_ANNI_PENA_RES%>.value != ""
  	      || document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_NUM_MESI_PENA_RES%>.value != ""
  	      || document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_NUM_GIORNI_PENA_RES%>.value != "")
  	      &&
  	      (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_NUM_ANNI_PENA_SEN%>.value != ""
  	      || document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_NUM_MESI_PENA_SEN%>.value != ""
  	      || document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_NUM_GIORNI_PENA_SEN%>.value != ""))
      {
        alert("E' stato immesso sia il quantum di pena residua sia il quantum di pena irrogata in sentenza. Eliminarne uno dei due.");
        return false;
      }



      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
           && (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno iniziale");
        return false;
      }
      if( (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0)
           && (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero iniziale");
        return false;
      }
      if( (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
           && (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno finale");
        return false;
      }
      if( (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0)
           && (document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        alert("Valorizzare Numero finale");
        return false;
      }

     var data_reato=document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_GIORNO_DATA_REATO%>.value+'/'+document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_MESE_DATA_REATO%>.value+'/'+document.LoadRicercaRisultatoRicerca.<%=ICostantiRisultatoRicerca.CAMPO_ANNO_DATA_REATO%>.value;

      if(!ControllaDataPassaVuota(data_reato))
      {
        alert('Data massima di commesso reato non valida');
        return false;
      }

//disabilità il taso ricerca
    document.LoadRicercaRisultatoRicerca.RICERCA.disabled =true;


  }

 </script>
 </head>

<body class="corpo" onLoad="Radio();">
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadRicercaRisultatoRicerca">

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.risultatoricerca.action.ActRicercaRisultatoRicerca">

    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class="LBG"><font class="label">Funzione :</font> <font class="campo">Ricerca Procedimenti per applicazioni Benefici</font></td>
      </tr>
    </table>

    <table width="90%">
    <tr><td>&nbsp;</td></tr>
    <tr><td class="l" colspan="4">Indicare eventuale intervallo procedimenti per restringere ricerca:</td></tr>
     <tr>
      <td class="L">
        <font class="label">
          Anno/Numero Iniziale
        </font>
      </td>
      <td class="l" width="25%">
        <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="13" size="13">
      </td>
      <td class="L" width="25%">
        <font class="label">
          Anno/Numero Finale
        </font>
      </td>
      <td class="l" width="25%">
        <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onkeypress="return TicTabNumField(this,event)">
        /
        <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="13" size="13">
      </td>
     </tr>
    </table>
    <table width="90%">
     <tr><td>&nbsp;</td></tr>
     <tr>
      <td class="L">Indicare data massima di commesso reato :</td>
      <td class="L">
        <input type="text" title="Giorno Reato" name="<%=ICostantiRisultatoRicerca.CAMPO_GIORNO_DATA_REATO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Mese Reato" name="<%=ICostantiRisultatoRicerca.CAMPO_MESE_DATA_REATO%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        -
        <input type="text" title="Anno Reato" name="<%=ICostantiRisultatoRicerca.CAMPO_ANNO_DATA_REATO%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
     </tr>
     <tr><td>&nbsp;</td></tr>
     <tr>
        <td class="l" colspan="8">Indicare il quantum di pena residua da espiare:</td>
     </tr>
     <tr>
        <td class="L" colspan="8">
                  Anni
                  <input title="Anni" size=2 maxlength=2  type="text" name="<%= ICostantiRisultatoRicerca.CAMPO_NUM_ANNI_PENA_RES %>"  >
                  Mesi
                  <input title="Mesi" size=2 maxlength=2  type="text" name="<%= ICostantiRisultatoRicerca.CAMPO_NUM_MESI_PENA_RES%>"  >
                  Giorni
                  <input title="Giorni" size=2 maxlength=2 type="text" name="<%= ICostantiRisultatoRicerca.CAMPO_NUM_GIORNI_PENA_RES %>"  >
        </td>
      </tr>
     </table>
     <table width="90%">
      <tr><td>&nbsp;</td></tr>
      <tr>
         <td class="l">Posizione giuridica aggregata: </td>
         <td class="l">Nessuna</td>
         <td class="l"><input type="radio" name="<%=ICostantiRisultatoRicerca.CAMPO_POSIZIONE_GIURIDICA_AGGREGATA%>" value="0" checked onclick="Radio();"></td>
         <td class="l">In espiazione pena in carcere</td>
         <td class="l"><input type="radio" name="<%=ICostantiRisultatoRicerca.CAMPO_POSIZIONE_GIURIDICA_AGGREGATA%>" value="1" onclick="Radio();"></td>
         <td class="l">In misura tutte</td>
         <td class="l"><input type="radio" name="<%=ICostantiRisultatoRicerca.CAMPO_POSIZIONE_GIURIDICA_AGGREGATA%>" value="2" onclick="Radio();"></td>
         <td class="l">Libero e assimilati</td>
         <td class="l"><input type="radio" name="<%=ICostantiRisultatoRicerca.CAMPO_POSIZIONE_GIURIDICA_AGGREGATA%>" value="3" onclick="Radio();"></td>

     </tr>
     <tr>
        <td class="l">Indicare singola posizione giuridica</td>
        <td class="l" colspan="8">
          <select title="Posizione Giuridica" name="<%= ICostantiRisultatoRicerca.CAMPO_COD_POSIZIONE_GIURIDICA %>">
<%

            Iterator lIter = posizioneGiuridica.iterator();
            while(lIter.hasNext())
            {
              DecodificheModel lDecMod = (DecodificheModel)lIter.next();
              if(lDecMod.getCode().equals("10") || lDecMod.getCode().equals("03"))
               {
%>
                <option style="color:red" value="<%=lDecMod.getCode()%>"/><%=lDecMod.getDescription()%>
<%             }
               else if(lDecMod.getCode().equals("13") || lDecMod.getCode().equals("12")
                       || lDecMod.getCode().equals("14") || lDecMod.getCode().equals("27"))
               {
%>
                <option style="color:orange" value="<%=lDecMod.getCode()%>"/><%=lDecMod.getDescription()%>
<%             }
               else
               {
%>
                <option value="<%=lDecMod.getCode()%>"/><%=lDecMod.getDescription()%>
<%
               }
            }
%>
          </select>
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
      <tr>
        <td class="l" colspan="9">Per ricercare procedimenti privi di ordine di esecuzione, indicare quantum pena irrogata in sentenza:</td>
      </tr>
      <tr>
       <td class="L" colspan="9">
                  Anni
                  <input title="Anni" size=2 maxlength=2  type="text" name="<%= ICostantiRisultatoRicerca.CAMPO_NUM_ANNI_PENA_SEN %>"  >
                  Mesi
                  <input title="Mesi" size=2 maxlength=2  type="text" name="<%= ICostantiRisultatoRicerca.CAMPO_NUM_MESI_PENA_SEN%>"  >
                  Giorni
                  <input title="Giorni" size=2 maxlength=2 type="text" name="<%= ICostantiRisultatoRicerca.CAMPO_NUM_GIORNI_PENA_SEN %>"  >
        </td>
       </tr>
       <tr><td>&nbsp;</td></tr>
       <tr>
          <td class="l">Nazionalità</td>
          <td class="L">
              <select name="<%= ICostantiRisultatoRicerca.CAMPO_COD_NAZIONE%>"><%=TipiNazione%> </select>
          </td>
       </tr>
       <tr><td>&nbsp;</td></tr>
       <tr>
          <td colspan="2">
              <INPUT class="bottone" type="submit"  name="RICERCA" value="Ricerca">
          </td>
      </tr>
    </table>
 </form>
<script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadRicercaRisultatoRicerca");


  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_INIZIALE%>","numeric","Il campo Numero Procedimento Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_INIZIALE%>","maxlen=13","La lunghezza massima per il Numero Procedimento è di 13 caratteri");

  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_FINALE%>","numeric","Il campo Numero Procedimento Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_PROGR_FINALE%>","maxlen=13","La lunghezza massima per il Numero Procedimento è di 13 caratteri");

  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");



  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_NUM_ANNI_PENA_RES%>","numeric");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_NUM_MESI_PENA_RES%>","numeric");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_NUM_GIORNI_PENA_RES%>","numeric");


//PENA IRROGATA IN SENTENZA CONTROLLO FORMALE
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_NUM_ANNI_PENA_SEN%>","numeric");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_NUM_MESI_PENA_SEN%>","numeric");
  frmvalidator.addValidation("<%=ICostantiRisultatoRicerca.CAMPO_NUM_GIORNI_PENA_SEN%>","numeric");

  frmvalidator.setAddnlValidationFunction("verifica");
  </script>

</body>
</html>