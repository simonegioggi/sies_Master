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
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>
<%@ page import="siap.siep.util.MinorMask"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="codiceAutoritaC"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="motivoProvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="dataeditabile"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="idmisuraalternativa"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="autoritaEsternaAvv"  scope="request" class="java.lang.String"/>
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
    function ListaDocumentiSius(a_formname)
    {
      var tipoMA ='<%=ICostantiMisuraAlternativa.ESPULSIONE%>';
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.misuraalternativa.action.ActListaDocumentiSius&formname="+a_formname+"&<%=ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP%>=<%=lFascicoloAssociato.getIdFascicoloSiep()%>&<%=ICostantiMisuraAlternativa.CAMPO_NATURA_MA%>=<%=ICostantiMisuraAlternativa.ACCOGLIMENTO%>&<%=ICostantiMisuraAlternativa.CAMPO_TIPO_MA%>="+tipoMA, "Lista_Provvedimenti_Sius", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=800, height=500");
    }

    function pulisciId()
    {
      document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>.value="";
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
        if (document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value.length==1)
            document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value='0'+document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value;
        if (document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value.length==1)
            document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value='0'+document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value;

          var data_to_verifica = document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>.value+'/'+document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>.value+'/'+document.LoadInserisciEspulsione.<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>.value;

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
if(!document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled)
 {
  if (document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
     document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
  if (document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
     document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

   var data_to_verify_emi = document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.LoadInserisciEspulsione.<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;
   if (!ControllaData(data_to_verify_emi) )
   {
     alert('Data di Emissione non valida');
     return false;
   }
}

if(!document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.disabled)
 {
  if (document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
     document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
  if (document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
     document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

   var data_to_verify_tra = document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciEspulsione.<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;
   if (!ControllaData(data_to_verify_tra) )
   {
     alert('Data di Trasmissione non valida');
     return false;
   }
 }


      if (document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
        document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
      if (document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
        document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

      var data_to_verify = document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'-'+document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'-'+document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
      if (!ControllaData(data_to_verify) )
      {
        alert('Data di emissione Ordinanza non valida');
        return false;
      }


      if(document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("La Sede dell'Ufficio Emittente è obbligatoria");
        document.LoadInserisciEspulsione.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }

if(!document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.disabled)
  {
     if(document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
    {
     alert("Il  Magistrato Firmatario è obbligatorio");
     document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
     return false;
    }
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

  function ListaComuniTds(formname,fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }



  function radio()
  {

       var nodedest =document.getElementById('divdest');
       var nodebottone = document.getElementById('divbottone');
       var nodedate = document.getElementById('divdate');

       if(document.LoadInserisciEspulsione.comunica[0].checked)
       {
         nodedest.style.display='block';
         nodebottone.style.display='block';
         nodedate.style.display='block';


         document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.disabled=false;
         document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COGNOME%>.disabled=false;
         document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_NOME%>.disabled=false;


         document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled=false;
         document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.disabled=false;
         document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.disabled=false;

         document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.disabled=false;
         document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.disabled=false;
         document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.disabled=false;

<%
 if(lPosizione.isMisuraAlternativa()|| "02".equals(lPosizione.getCodPosizioneGiuridica())
	|| "04".equals(lPosizione.getCodPosizioneGiuridica()))
{
%>
        document.LoadInserisciEspulsione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=false;

        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=false;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=false;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=false;

<%
}else if("03".equals(lPosizione.getCodPosizioneGiuridica()))
{
%>
        document.LoadInserisciEspulsione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=false;

<%
}
%>
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=false;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=false;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=false;

      }
      else if(document.LoadInserisciEspulsione.comunica[1].checked)
      {
        nodedest.style.display='none';
        nodebottone.style.display='block';
        nodedate.style.display='none';


        document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_COGNOME%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiMagistrato.CAMPO_NOME%>.disabled=true;

        document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.disabled=true;

        document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.disabled=true;
<%
 if(lPosizione.isMisuraAlternativa()|| "02".equals(lPosizione.getCodPosizioneGiuridica())
	|| "04".equals(lPosizione.getCodPosizioneGiuridica()))
{
%>
        document.LoadInserisciEspulsione.<%=ICostantiCSSA.CAMPO_ID_CSSA%>.disabled=true;

        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>.disabled=true;
<%

}else if("03".equals(lPosizione.getCodPosizioneGiuridica()))
{
%>
        document.LoadInserisciEspulsione.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.disabled=true;

<%
}
%>
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.disabled=true;
        document.LoadInserisciEspulsione.<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>.disabled=true;

      }
 }

  </script>

<jsp:include page="/jsp/files/siap/siep/misuraalternativa/MinorScript.jsp"/>

</head>

<body class="corpo" onload="radio();">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <font class="campo">ACCOGLIMENTO OPPOSIZIONE ESPULSIONE ART.16 COMMA 5 D.LO 286/1998 E SUCC.MOD.
</font>
</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciEspulsione">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActInserisciAccoglimentoOpEspulsione">
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <INPUT type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS%>" value="">
  <input type="HIDDEN" title="IdPena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">


<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=idmisuraalternativa%>">
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
</table>
<div id="divdate" style="display:block; position:relative; width:100%;" >
<table width='100%'>
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
</div>
<table width='90%'>
   <tr>
      <td class="Titolo" colspan='8'> Dati Ordinanza Tribunale di Sorveglianza </td>
   </tr>
   <tr>
      <td class="l" nowrap>
        <a href="Javascript:ListaDocumentiSius('LoadInserisciEspulsione');">
          Seleziona dalla lista <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td class="l" colspan="3">
      &nbsp;
      </td>
    </tr>
    <tr>
      <td class="l">Anno /Numero SIUS</td>
      <td class="l">
        <input Title="Anno Fascicolo Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Sius" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
      <td class="l"> Anno / Numero Ordinanza</td>
      <td class="l">
        <input Title="Anno Decreto" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4" onChange="pulisciId();">
        /
        <input Title="Numero Decreto" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6" onChange="pulisciId();">
      </td>
    </tr>
    <tr>
      <td class="l">Ufficio Emittente</td>
      <td class="l" colspan="3">
        <%=MinorMask.comboEmittente(filtroMinorenni, MinorMask.EmittenteTribunale)%>
      </td>
    </tr>
    <tr>
      <td class="l" nowrap>Sede Ufficio Emittente <font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <font class="campo">
          <input Title="Luogo Tribunale Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text" onChange="pulisciId();">
          <a href="Javascript:ListaComuniEmitTdsMinor('LoadInserisciEspulsione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
        </font>
      </td>
    </tr>
    <tr>
      <td class="l" nowrap>Oggetto Ordinanza</td>
      <td class="L" colspan="3">
         <select  Title="Oggetto Ordinanza" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" onChange="pulisciId();">
          <%=motivoProvv%>
        </select>
   </td>
    </tr>
    <tr>
      <td class="l">Data Emissione Ordinanza</td>
      <td class="l" colspan="3">
        <font class="campo">
          <input title = "Giorno Data Emissione Ordinanza" value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
          <input title = "Mese Data Emissione Ordinanza" value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA%> onChange="pulisciId();"> -
          <input title = "Anno Data Emissione Ordinanza" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> onChange="pulisciId();">
        </font>
      </td>
    </tr>
    <tr>
      <td  class="l">Note</td>
      <td  class="L" colspan="3">
        <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=80 rows=2></textarea>
      </td>
    </tr>

<tr>
<td class="l">Inivio Comunicazioni</td>
<td class="l" colspan='3'>
SI &nbsp;<input type="radio" name="comunica" value="S"  checked onclick="radio();">
&nbsp; NO  &nbsp; <input type="radio" name="comunica" value="N"   onclick="radio();">
</td>
</tr>
</table>


<div id="divdest" style="display:block; position:relative; width:100%;">
<table width="100%">
   <tr>
     <td class="Titolo" width="100%" colspan=6> Magistrato Firmatario </td>
   </tr>
  <tr>
   <td class="l">Magistrato Firmatario
   <td class="L" colspan="3">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciEspulsione','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
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
 if(lPosizione.isMisuraAlternativa()|| "02".equals(lPosizione.getCodPosizioneGiuridica())
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
         <a href="Javascript:ListaComuni('LoadInserisciEspulsione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>

      <td class="l">Indirizzo</td>
      <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E%>"  cols=30 ></textarea>
      </td>
   </tr>
   <tr>
      <td class="l" width='30%'>Autorità di polizia preposta al controllo</td>
      <td class="L" colspan="3">
         <select  Title="Autorità delegata all'espulsione"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
            <%=codiceAutoritaC%>
         </select>
   </tr>
   <tr>
      <td class="l">Sede</td>
     <td class="L">
      <input title="Sede delegata all'espulsione"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciEspulsione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C%>"  cols=30 ></textarea>
      </td>
   </tr>
    <tr>
     <td class="l" width='30%'><%=MinorMask.comboCSSA(filtroMinorenni)%> competente al controllo</td>
       <td class="l" colspan="3">
        <input title="UEPE Competente" name="Indirizzo" value="" size=60 readonly>
        <input type="hidden" name="<%=ICostantiCSSA.CAMPO_ID_CSSA%>" value="" size=35 >
         <a href="Javascript:ListaCSSAMinor('LoadInserisciEspulsione','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
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
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciEspulsione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
<%}else {%>
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciEspulsione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
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
         <a href="Javascript:ListaComuni('LoadInserisciEspulsione','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
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
      <td class="Titolo" colspan='6'>Destinatario per Notifica </td></tr>
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
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciEspulsione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
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
</div>
<div id="divbottone" style="display:block; position:relative; width:100%;" >
  <table width="100%">
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
</table>
</div>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciEspulsione");


  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2050");
  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");



//Controlli Data Emissione Nota
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","lt=31");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","gt=1");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","lt=12");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2050");


<%


  if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
  {
    if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
    {
      if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
      {
%>

//Controlli Data Fine Pena
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
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
      }
    }
  }
%>

frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>