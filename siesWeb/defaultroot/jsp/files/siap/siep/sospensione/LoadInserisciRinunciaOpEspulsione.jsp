<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="codiceAutoritaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaC"   scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoAutorita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="filtroMinorenni" scope="request" class="java.lang.String"/>

<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione Espulsione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    var desktop;
    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
    }
  </script>
  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    // Chiamata lista Avvocati.
    function ListaAvvocati(a_formname)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.avvocato.action.ActLoadRicercaAvvocato&formname="+a_formname+"&modalita=BREVE", "Ricerca_Avvocato","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=450,height=500");
    }

  function Verify()
  {
<%
    if((!lPosizione.isLibero()) || (((lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S")) && (penaresidua.getDataFinePresunta()!= null && penaresidua.getDataFine() == null) ) ))
    {
     if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))))
     {
      if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
      {
%>
        if (document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
            document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
        if (document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
            document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

          var data_to_verifica = document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciRinOpEspulsione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

          if (!ControllaData(data_to_verifica) )
          {
            alert('Data fine pena non valida');
            return false;
          }
<%
        }
      }
    }
%>

  if (document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
     document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
  if (document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
     document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

   var data_to_verify_emi = document.LoadInserisciRinOpEspulsione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciRinOpEspulsione.<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
   if (!ControllaData(data_to_verify_emi) )
   {
     alert('Data di Emissione non valida');
     return false;
   }



  if (document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
     document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
  if (document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
     document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

   var data_to_verify_tra = document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciRinOpEspulsione.<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
   if (!ControllaData(data_to_verify_tra) )
   {
     alert('Data di Trasmissione non valida');
     return false;
   }


  if (document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value.length==1)
     document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value='0'+document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value;
  if (document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value.length==1)
     document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value='0'+document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value;

   var data_to_verify_veremi = document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value+'-'+document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value+'-'+document.LoadInserisciRinOpEspulsione.VERBALE_<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE %>.value;
   if (!ControllaData(data_to_verify_veremi) )
   {
     alert('Data di Emissione non valida');
     return false;
   }


  if (document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value.length==1)
     document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value='0'+document.LoadInserisciRinOpEspulsione.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value;
  if (document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value.length==1)
     document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value='0'+document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value;

   var data_to_verify_verpe = document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value+'-'+document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value+'-'+document.LoadInserisciRinOpEspulsione.<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>.value;
   if (!ControllaData(data_to_verify_verpe) )
   {
     alert('Data di Pervenimento non valida');
     return false;
   }


   if(document.LoadInserisciRinOpEspulsione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciRinOpEspulsione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
   {
     alert("Il  Magistrato Firmatario è obbligatorio");
     document.LoadInserisciRinOpEspulsione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
     return false;
   }


  return true;
}

  function ListaCSSA(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

// magistrato competente
  function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
  {
    var desktop;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
  }

  </script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>  

</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <font class="campo">RINUNCIA OPPOSIZIONE ESPULSIONE ART.16 COMMA 5 D.LO 286/1998 E SUCC.MOD.
</font>
</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciRinOpEspulsione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciRinunciaOpEspulsione">
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="">
  <input type="HIDDEN" title="IdPena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">


  <table>
    <tr>
      <td class="l">Posizione Giuridica </td>
      <td class="L" colspan=5>
          <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {%>
              DETENUTO PER ALTRA CAUSA
<%    }
      else
     {%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%   }%>
         </font>
   </td>
</tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>


                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>

<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
          </tr>
<%
          }
        }
%>


   <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
%>
</tr>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }



       if (penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
  if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <td class="l">Data Fine Pena</td>
         <td class="L" colspan=2>
           <input title = "Giorno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>"  <%=IWebConstants.UTIL_DATA%> >
           -
           <input title = "Mese Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" <%=IWebConstants.UTIL_DATA%> >
           -
           <input title = "Anno Data Fine Pena" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
         </td>
<%
          }else if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
            </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
                 <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
                </td>
<%            }
        }
      }
}
%>

       </tr>
   <tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input title = "Giorno Data Emissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input title = "Giorno Data Trasmissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Trasmissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Trasmissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
     </tr>
</table>
<table width='100%'>
   <tr>
      <td class="Titolo" colspan='4'>Dati Comunicazione Rinuncia Opposizione</td>
   </tr>
   <tr>
      <td class="l" width="30%">Protocollo N.</td>
      <td class="l" colspan="3">
        <input Title="Protocollo N." name="<%=ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO%>" type="text" size="50" maxlength="50">
      </td>
   </tr>
   <tr>
      <td class="l">Data Emissione Comunicazione</td>
      <td class="L" >
          <input title = "Giorno Data Emissione Comunicazione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="VERBALE_<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione Comunicazione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="VERBALE_<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione Comunicazione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="VERBALE_<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      </td>
      <td class="l">Data Ricezione Comunicazione</td>
      <td class="L">
          <input title = "Giorno Data Ricezione Comunicazione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Ricezione Comunicazione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Ricezione Comunicazione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      </td>
     </tr>
</table>
<table width='100%'>
      <tr>
        <td class="l" width="30%">Istituto di Detenzione</td>
        <td class="l" colspan='3'>
<%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>
              <input readonly Title="Istituto" name="VERBALE_Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="VERBALE_<%= ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRinOpEspulsione','VERBALE_<%= ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE %>','VERBALE_Comune');">
              <img src="/images/filefolder.gif" border=0></a>
<%}else {%>
              <input readonly Title="Istituto" name="VERBALE_Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="VERBALE_<%= ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRinOpEspulsione','VERBALE_<%= ICostantiVerbale.IST_DET_ID_ISTITUTO_DETENZIONE %>','VERBALE_Comune');">
              <img src="/images/filefolder.gif" border=0></a>
       <%}%>

      </tr>

	<%-- MEV10-s3: modificato layout con aggiunta etichette e combo --%>
	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario</td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
        <td class="l" colspan="3">
			<input title="Sede UEPE Competente" name="VERBALE_Indirizzo" value="" size=35 readonly>
          	<input type="hidden" name="VERBALE_<%= ICostantiVerbale.CSS_ID_CSSA%>" value="" size=35 >
          	<a href="Javascript:ListaCSSAIdMinor('LoadInserisciRinOpEspulsione','VERBALE_<%= ICostantiVerbale.CSS_ID_CSSA %>','VERBALE_Indirizzo', 'comboCSSAMinor');">
            	<img src="/images/filefolder.gif" border=0>
          	</a>
       	</td>
	</tr>

      <tr>
        <td class="l">Autorità che ha inviato la comunicazione</td>
        <td class="l" colspan='3'>
          <select title="Autorità che ha inviato la comunicazione" class="small" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>">
            <%=tipoAutorita%>
          </select>
        </td>
      </tr>
      <tr>
          <td class="l">Luogo Autorità</td>
          <td class="L">
          <input title="Luogo" value="" type="text" name="<%=  ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciRinOpEspulsione','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
          </td>
           <td class="l">Indirizzo</td>
           <td class="L">
           <TEXTAREA title="Note" name="<%= ICostantiVerbale.CAMPO_NOTE %>"  cols=30></textarea>
          </td>
      </tr>
</table>
<table width="100%">
   <tr>
     <td class="Titolo" width="100%" colspan=4> Magistrato Firmatario </td>
   </tr>
  <tr>
   <td class="l">Magistrato Firmatario</td>
   <td class="L" colspan="3">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciRinOpEspulsione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
  </tr>
  <tr>
     <td class="Titolo" width="100%" colspan=6> Destinatari</td>
  </tr>
<%
 if(lPosizione.isMisuraAlternativa() || "02".equals(lPosizione.getCodPosizioneGiuridica())
	|| "04".equals(lPosizione.getCodPosizioneGiuridica()))
{
%>
    <tr>
      <td class="l" width='30%'>Autorità delegata all'espulsione</td>
      <td class="L" colspan="3">
         <select  Title="Autorità delegata all'espulsione"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
            <%=codiceAutoritaE%>
         </select>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
      <input title="Sede delegata all'espulsione"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciRinOpEspulsione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols=30 ></textarea>
      </td>
   </tr>
   <tr>
     <td class="l" width='30%'>Autorità di polizia preposta la controllo</td>
     <td class="L" colspan="3">
       <select  Title="Autorita di polizia preposta la controllo"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_C%>">
         <%=codiceAutoritaC%>
       </select>
  </tr>
  <tr>
     <td class="l">Sede</td>
     <td class="L">
          <input title="Sede Autorita di polizia preposta la controllo"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciRinOpEspulsione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_C%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>

           <td class="l">Indirizzo</td>
           <td class="L">
              <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=30 ></textarea>
            </td>
    </tr>

	<tr>
		<td class="Titolo" colspan="4">UEPE/USSM</td>
	</tr>
	<tr>
		<td class="l" width="30%">Destinatario competente al controllo</td>
		<td class="l"><%=MinorMask.comboCSSATrattino(MinorMask.ComboCSSAId)%></td>
	</tr>
	<tr>
		<td class="l">Sede</td>
       	<td class="l" colspan="3">
        	<input title="Sede UEPE Competente" name="Indirizzo" value="" size=60 readonly>
        	<input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
         	<a href="Javascript:ListaCSSAIdMinor('LoadInserisciRinOpEspulsione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo','comboCSSAMinor');">
          		<img src="/images/filefolder.gif" border=0>
         	</a>
      	</td>
	</tr>

<%
} else if("03".equals(lPosizione.getCodPosizioneGiuridica()))
{
%>
 <tr>
     <td class="l" width='30%'>Istituto di Detenzione</td>
  <td class="l" colspan="3">
<%if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRinOpEspulsione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
<%}else {%>
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciRinOpEspulsione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
       <%}%>
</td>
</tr>
    <tr>
      <td class="l" width='30%'>Autorità delegata all'espulsione</td>
      <td class="L" colspan="3">
         <select  Title="Autorità delegata all'espulsione"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
            <%=codiceAutoritaC%>
         </select>
    </tr>
    <tr>
      <td class="l">Sede</td>
      <td class="L">
      <input title="Sede delegata all'espulsione"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciRinOpEspulsione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>"  cols=30 ></textarea>
      </td>
   </tr>
<%
}
%>
    <tr>
      <td class="Titolo" colspan='6'>Destinatario per Notifica</td></tr>
<%
     int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        <table>
          <tr>
            <td class="l" width="100%">Per Avvocato&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getCognome())%>&nbsp;<%=StringUtils.toStringJSP(lAvv.getAvvocato().getNome())%>
              </font>
              &nbsp;Foro di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>
              </font>
              &nbsp;Difensore di&nbsp;
              <font class="campo">
                <%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescrTipo())%>
              </font>
            </td>
            <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>" type="text" name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>"  maxlength="35" size="35">
          </tr>
        </table>
         <table>
          <tr><td class="l" width="35%">Autorità Destinazione </td >
          <td class="L" colspan="3">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>

     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <%-- MEV_21 (avvocati) Sostituzione di getAvvocato().getForo() con getAvvocato().getDescComuneSedeForo() --%>
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getDescComuneSedeForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciRinOpEspulsione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
        <td class="l">Note</td>
       <td  class="L">
          <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>"  cols=30 ></textarea>
       </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
 }
%>
  </tr>
</table>
</table>
<table width="100%">
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciRinOpEspulsione");

<%
if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
  if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
  {
    if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
    {
%>
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2099");

<%
      }
    }
  }
%>

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>