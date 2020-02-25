<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sico.misuraalternativa.model.MisuraAlternativaModel"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>

<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="sedesorveglianza"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="sedeautcompetente"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="magistratosorveglianza"    scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="autoritaEsternaC"   scope="request" class="siap.siep.autoritaesterna.model.AutoritaEsternaModel"/>
<jsp:useBean id="codiceAutoritaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutoritaC"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaAvv"   scope="request" class="java.lang.String"/>
<jsp:useBean id="sorveglianza"       scope="request" class="java.lang.String"/>
<jsp:useBean id="Cssa"       scope="request" class="java.lang.String"/>
<jsp:useBean id="UffTDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="luogodetenzione"      scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="sedepoliziaNotN"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="sedepoliziaNotC"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="motivoProvv"   scope="request" class="siap.sico.decodifiche.model.DecodificheModel"/>
<jsp:useBean id="tipoUfficio"   scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoIstituto"   scope="request" class="java.lang.String"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="dataeditabile"   scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>



<%
FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();
  String lcodicePosizione = "";
  if (lPosizione != null)
	  lcodicePosizione = lPosizione.getCodPosizioneGiuridica();
  else
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>
<html>
<head>
<title>[S.I.E.S.] - Gestione evento </title>
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


    if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;

     if (!ControllaData(data_to_verify) )
		  {
                     alert('Data di Trasmissione non valida');
			 return false;
		  }



      var campo = document.LoadInserisciMisuraAlternativa.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;




if(document.LoadInserisciMisuraAlternativa.flagmisura.value=="N")
{

    if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.value=="")
      {
        alert("Inserire un UFFICIO di SORVEGLIANZA");
        return false;
      }



   if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'-'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;
       if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione Ordinanza non valida');
			 return false;
		  }



if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO %>.value=="")
      {
        alert("L'anno dell'ordinanza è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO %>.focus();
        return false;
      }

if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO %>.value=="")
      {
        alert("Il numero dell'ordinanza è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO %>.focus();
        return false;
      }

if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.value=="")
      {
        alert("Manca la Sede dell'Ufficio Eittente");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>.focus();
        return false;
      }
	  }

<%if(lcodicePosizione.equals("03") || lcodicePosizione.equals("14"))
{%>

 if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>.value=="")
      {
        alert("Manca la Sede dell' Autorità di destinazione");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>.focus();
        return false;
      }
<%}%>
if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.value=="")
      {
        alert("Manca la sede del Tribunale di Sorveglianza");
        document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>.focus();
        return false;
      }
     if(document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA %>.value=="" || document.LoadInserisciMisuraAlternativa.<%=ICostantiCSSA.CAMPO_ID_CSSA %>.value=="-")
      {
         alert("Inserire l' UEPE Competente");
        return false;

      }


      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il  Magistrato Assegnatario è obbligatorio");
           document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();
        return false;
      }

   if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS %>.value=="")
      {
        alert("L'Ufficio di Sorveglianza è obbligatorio");
        return false;
      }

 if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE %>.value=="")
      {
        alert("Inserire sede Ufficio di Sorveglianza");
        return false;
      }
<%if(lcodicePosizione.equals("03") || lcodicePosizione.equals("14"))
{%>


if(document.LoadInserisciMisuraAlternativa.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value=="")
{
        alert("Il Capo Istituto di Detenzione è obbligatorio");
        return false;
}
<%}%>
<%if(lcodicePosizione.equals("03") || lcodicePosizione.equals("14"))
{%>

if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.selectedIndex].value == '-')

{
        alert("Il Capo Autorità di polizia competente per territorio è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>.focus();

        return false;
}
<%}%>





<%if(avvocati.size() >1)
     {%>
       if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0][document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].selectedIndex].value == '-')
			{
          alert("Il campo Autorità per la Notifica  di un Condannato è obbligatorio!");
          document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			    return false;
		  }
        if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1][document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].selectedIndex].value == '-')
           {

                    alert("Il campo Autorità per la  Notifica di un Condannato  è obbligatorio!");
                    document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[1].focus();
                    return false;
           }
  <%}else{%>
 if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>.selectedIndex].value == '-')
			{
          alert("Il campo Autorità per la Notifica  di un Condannato  è obbligatorio!");
          document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			    return false;
		  }
<%}%>

<%if(lcodicePosizione.equals("12"))
{%>
if (document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>[document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.selectedIndex].value == '-')

{
        alert("Il Capo Autorità di polizia competente per territorio è obbligatorio");
        document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>.focus();

        return false;
}


<%}%>


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


 function ListaUDS(a_formname,a_fieldname)
        {
          desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
        }
 function ListaComuniTds(formname,fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }


  </script>
</head>
       <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
     <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<% EventoModel lProvvedimento = new EventoModel(evento);

   MisuraAlternativaModel lModel = new MisuraAlternativaModel();
   String lAzione = new String();
   String flagMis = new String();

%>
    <font class="campo">Concessione Liberazione Condizionale</font>


</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMACoLibCond">
  <INPUT type="hidden" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <INPUT type="hidden" name="idPosizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getIdPosizioneGiuridica()%>">



 <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">

<%
if(flagmisura.equals("N"))
{%>
  <input type="HIDDEN" name="flagmisura" value="N">
 <%}else{%>
  <input type="HIDDEN" name="flagmisura" value="S">

<%}%>

<input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">

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

<%
            //   if(lAltraCausa.getDescrLuogoIstituto()!=null)
             //  {
%>
                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>
<%
               //}
%>
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
<%
             // if(lLuogoDetenzione..getDescrLuogo()!=null)
             // {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
<%
              //}
%>
            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
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
              <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
              <%--input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_LUOGO%>  maxlength="6" size="6"--%>
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

       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFinePresunta() != null)
       {
%>
         <td class="l">Data Fine Pena Automatica</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(),"dd-MM-yyyy"))%> &nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
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
%>
      </tr>
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
        <%--input type="HIDDEN" title="Codice Istituto" value="<%=lLuogoDetenzione.getCodTipoIstituto()%>" type="text" name=<%=ICostantiLuogoDetenzione.CAMPO_COD_TIPO_ISTITUTO%>  maxlength="6" size="6"--%>

      <tr>
<%
    if((!lPosizione.isLibero() && !lcodicePosizione.equals("13")) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
        if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
        {
           if ( dataeditabile.equals("S") && penaresidua.getDataFinePresunta() != null)
           {
%>
         <td class="l">Data Fine Pena Manuale</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
           -
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFinePresunta(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
         </td>
<%
          }else if( penaresidua.getDataFine() != null)
          {
%>           <td class="l">Data Fine Pena Manuale</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
            <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>"  name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>"  name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
              <input type="hidden" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>"  name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">

</td>
<%
          }
        }
}
%>


        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L" >
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
</table>
<table   width=90%>
   <tr>
      <td class="Titolo" colspan='8'> Dati Ordinanza Tribunale di Sorveglianza </td>
   </tr>
   <tr>
      <td class="l">Anno /Numero SIUS</td>
  <%if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
     {%>
    <td class="l"><input Title="Anno Fascicolo Sius" value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4"> /

  <%}else
     {%>

    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%> /</font>
    <%}%>
   <%
      if(misuraalternativa== null ||  misuraalternativa.getIdMisuraAlternativa()== null)
     {%>
    <input Title="Numero Sius" value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6">
    </td>
  <%}else
     {%>
     <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font></td>
   <%}%>
  </tr>
<tr>
     <td class="l"> Anno / Numero Ordinanza  <font class=ob>(*)</font></td>
<%
     if( misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
     {%>

        <td class="l"> <input Title="Anno Orinanza" value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4"> /
     <%}
      else
     {%>
             <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%> /</font>

        <%}%>

   <%
     if(misuraalternativa == null ||  misuraalternativa.getIdMisuraAlternativa()== null)
     {%>
             <input Title="Numero Ordinanza" value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6"></td>
   <%}else
     {%>

          <font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font></td>

   <%}%>
  </tr>
 <tr>
      <td class="l">Ufficio Emittente  <font class=ob>(*)</font> </td>
     <%if(misuraalternativa.getChiaveUfficioFascicoloSius()==null || misuraalternativa==null|| misuraalternativa.getIdMisuraAlternativa()==null)
       { %>
      <td class="l">
       <select  Title="Ufficio Emittente"  class="l" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>">
             <%=tipoUfficio%>
       </select></td>
    </tr>
    <tr>
      <td class="l">Sede Ufficio Emittente  <font class=ob>(*)</font></td><td class="l"><font class="campo">
        <input Title="Luogo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT %>" size=35 type="text">
        <a href="Javascript:ListaComuniTds('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>');">
          <img src="/images/filefolder.gif" border=0>
         </a>
      </font>
     </td>
      <%}else
     {%>
    <td class="l"> <font class="campo"> TRIBUNALE DI SORVEGLIANZA DI <%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%></font></td>
    <%}%>
    </tr>
<tr>
      <td class="l">Oggetto Ordinanza </td>
     <%
        if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null )
       {%>

        <td class="L">
            <font class="campo"><%=motivoProvv.getDescription()%></font>
            <input type="hidden" name="<%=ICostantiEvento.CAMPO_COD_MOTIVO%>" value="<%=motivoProvv.getCode()%>">
           </td>

      <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font>&nbsp;</td>
    <% }%>
    </tr>
  <tr>
        <td class="l">Data Emissione Ordinanza </td>
       <%
         if(misuraalternativa == null  || misuraalternativa.getIdMisuraAlternativa() == null)
        {%>
       <td class="l"><font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

    </font></td>
       <%}else{%>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>
          </font>
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataDecisione()))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataDecisione()))%>">
      <INPUT type="hidden" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>" value="<%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataDecisione()))%>">

   </td>
    <%}%>
      </tr>
   <%
       if( misuraalternativa == null  || misuraalternativa.getIdMisuraAlternativa()== null)
       {%>
   <tr>
      <td class="l">Luogo della Prova </td>

      <td class="l"><font class="campo">
        <input Title="Luogo svolgimento della prova" name="<%= ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>"  size=35 type="text">
      </font></td>
</tr>
    <%}else if(misuraalternativa.getDescrLuogoProva()!= null)
     {%>
   <tr>
      <td class="l">Luogo della Prova </td>

    <td class="l"><font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>&nbsp;
         </font>
    </td>
</tr>
    <%}%>
  <tr>
<%
  if(misuraalternativa != null && misuraalternativa.getDataInizioMisura()!= null){
if(misuraalternativa.getDataInizioMisura() != null ){
%>
  <td class="l">Data Inizio Misura</td>

       <td class="l"><font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataInizioMisura()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataInizioMisura()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataInizioMisura()))%></font></td>
<%}}%>
</tr>
<tr>

<%
if((misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null) ){
if(penaresidua.getDataFine()!= null){
%>
  <td class="l">Data Fine Misura</td>

         <td class="l"><font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDayToString(penaresidua.getDataFine()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getMonthToString(penaresidua.getDataFine()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getYearToString(penaresidua.getDataFine()))%></font></td>
<%}else if(penaresidua.getDataFinePresunta() != null){%>
  <td class="l">Data Fine Misura</td>

 <td class="l"><font class="campo">
          <%=StringUtils.toStringJSP(DateUtils.getDayToString(penaresidua.getDataFinePresunta()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getMonthToString(penaresidua.getDataFinePresunta()))%> -
          <%=StringUtils.toStringJSP(DateUtils.getYearToString(penaresidua.getDataFinePresunta()))%></font></td>
<%}}else if( (misuraalternativa != null && misuraalternativa.getIdMisuraAlternativa()!= null)&& misuraalternativa.getDataFineMisura()!= null)
{%>
  <td class="l">Data Fine Misura</td>

 <td class="l"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDayToString(misuraalternativa.getDataFineMisura()))%> -
    <%=StringUtils.toStringJSP(DateUtils.getMonthToString(misuraalternativa.getDataFineMisura()))%> -
    <%=StringUtils.toStringJSP(DateUtils.getYearToString(misuraalternativa.getDataFineMisura()))%></font></td>
<%}%>

</tr>
<tr>
 <td  class="l">Note</td>
           <td  class="L">
             <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE %>" cols=40 rows=2 ><%= StringUtils.toStringJSP(misuraalternativa.getNote())%></textarea>
            </td>

</tr>
</table>
<table>
   <tr>
     <td class="Titolo" colspan=6> Magistrato Assegnatario </td>
   </tr>
  <tr>
   <td class="l">Magistrato Assegnatario
   <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
       <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input readonly title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      <td>
      </td>
  </tr>
<tr><td>&nbsp;</td></tr>

<%if(lcodicePosizione.equals("03") || lcodicePosizione.equals("14"))
{%>
<tr> <td class="Titolo" colspan="8" width="90%">Destinatario per l'esecuzione</td></tr>
<tr>
     <td class="l">Istituto di Detenzione</td>
    <td class="l">
<%if(posizioneluogoaltra != null && posizioneluogoaltra.getLuogoDetenzione()!= null && posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione() != null)
 {%>

              <input type="hidden" name="istituto" value="S">
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

<%}else {%>


            <input type="hidden" name="istituto" value="S">
              <input readonly Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciMisuraAlternativa','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>

       <%}%>
</td>
        <td rowspan=2 class="l">Note</td>
           <td rowspan=2 class="L">
              <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_IST%>"  cols=20 rows=5 ></textarea>
            </td>


</tr>
<%}%>
<tr><td>&nbsp;</td></tr>
<%if(lcodicePosizione.equals("12"))
{%>

<tr>
      <td class="Titolo" colspan=8 >Destinatario per l'esecuzione</td>
   </tr>

      <tr>
<!--autorità di polizia-->
          <td class="l">Autorità Destinazione </td>
          <td class="L">
            <input type="hidden" name="autoritaE" value="S">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_E%>">
             <%=codiceAutoritaE%>

             </select>
             <input type="hidden" name="notificaPolizia" value="E">
           </td>
           <td rowspan=2 class="l">Note</td>
           <td rowspan=2 class="L">
              <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_POL_E %>"  cols=20 rows=5 ></textarea>
            </td>
           </tr>
    <tr>
      <td class="l">Sede <font class=ob>(*)</font> </td>
     <td class="L">

        <input title="Sede Autorita Esterna"  type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

<%}%>
</table>
<table>



   <tr>
      <td class="Titolo" colspan=8> Notifica UEPE </td>
   </tr>
<!--UEPE competente-->

  <tr>
      <td class="l">UEPE Competente</td>
         <td class="l">
         <input type="hidden" name="cssa" value="S">

         <input type="hidden" name="notifica" value="C">

        <input readonly Title="UEPE Competente" name="Indirizzo" value="<%=StringUtils.toStringJSP(daticssa.getComune())%>-<%=StringUtils.toStringJSP(daticssa.getIndirizzo())%>" size=60 >
        <input type="hidden" Title="UEPE Competente" name="<%=ICostantiCSSA.CAMPO_ID_CSSA %>" value="<%=StringUtils.toStringJSP(daticssa.getIdCSSA())%>" size=35 >
       <a href="Javascript:ListaCSSA('LoadInserisciMisuraAlternativa','<%=ICostantiCSSA.CAMPO_ID_CSSA%>','Indirizzo');">
        <img src="/images/filefolder.gif" border=0>
     </a>

      </td>

         <td rowspan=2 class="l">Note</td>
         <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_CSSA %>"  cols=20 rows=5 ></textarea>
         </td>
    </tr>

<tr><td>&nbsp;<td></tr>
   <tr>
      <td class="Titolo" colspan=8> Ufficio di Sorveglianza Preposto al Controllo</td></tr>
   <tr>
      <td class="l">Ufficio di Sorveglianza</td>
      <td class="L">
      <input type="hidden" name="magSorv" value="S">
      <%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
      <%--input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistratosorveglianza.getCodMagistrato() )%>" type="text" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>"  maxlength="35" size="35" --%>
      <input type="hidden" name="notificaMagistrato" value="C">
       <input title="ufficio" value="<%=UffUDS%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UDS %>" maxlength="35" size="25">
       <a href="Javascript:ListaUDS('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>');">
              <img src="/images/filefolder.gif" border=0> </a>


      </td>
    <td rowspan=2 class="l">Note</td>
         <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_UDS%>"  cols=20 rows=5 ></textarea>
         </td>
    </tr>

<tr><td>&nbsp;<td></tr>

   <tr>
      <td class="Titolo" colspan=8>Tribunale di Sorveglianza che ha emesso l'Ordinanza</td>
   </tr>

   <tr>
         <td class="l">Destinatario</td >
          <td class="L">TRIBUNALE DI SORVEGLIANZA
         </td>
         <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
            <input type="hidden" name="tds" value="S">

         <td rowspan=2 class="l">Note</td>
         <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_TDS %>"  cols=20 rows=5 ></textarea>
          <input type="hidden" name="notificaTribunale" value="C">

         </td>
   </tr>
<tr>
         <td class="l">Sede <font class=ob>(*)</font></td><td class="L">

         <input title="Sede Tribunale Sorveglianza" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">

         <a href="Javascript:ListaComuniTds('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
         <img src="/images/filefolder.gif" border=0></a></td>

     </tr>

<%if(lcodicePosizione.equals("03") || lcodicePosizione.equals("14"))
{%>
   <tr>
      <td class="Titolo" colspan=8>Autorità di polizia competente per territorio</td>
   </tr>

 <tr>
          <td class="l">Autorità Destinazione </td>
          <td class="L">
          <input type="hidden" name="autoritaC" value="S">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_POLIZIA_C%>">
             <%=codiceAutoritaC%>

             </select>
           </td>
           <td rowspan=2 class="l">Note</td>
           <td rowspan=2 class="L">
              <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_POL_C %>"  cols=20 rows=5 ></textarea>
             <input type="hidden" name="notificaPolizia" value="C">

            </td>
           </tr>
    <tr>
      <td class="l">Sede<font class=ob>(*)</font> </td>
     <td class="L">
<%if(autoritaEsternaC.getDescrSede()!= null){%>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(autoritaEsternaC.getDescrSede()) %>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>"  maxlength="35" size="35">
<%}else{%>
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>"  maxlength="35" size="35">

<%}%>
    <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

<%}%>

    <tr>
      <td class="Titolo" colspan=8>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 0;
      Iterator lItxAvv = avvocati.iterator();
      while(lItxAvv.hasNext())
      {
        AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
%>
        </table>
        <table>
          <tr>
            <td class="l">Per Avvocato&nbsp;
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
          <tr><td class="l">Autorità Destinazione </td >
          <td class="L">
             <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>" >
               <%=autoritaEsternaAvv%>
             </select>
         </td>
        <td rowspan=2 class="l">Note</td>
       <td rowspan=2 class="L">
          <textarea title="Note" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_AVVOCATI%>"  cols=20 rows=5 ></textarea>
       </td>
     </tr>
     <tr>
      <td class="l">Sede </td><td class="L">
        <input title="Sede Foro Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocato().getForo())%>" type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>" maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciOrdineEsecuzione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[<%=lIdxAvv%>]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
  </tr>
  <tr>
<td class="lNoBord" colspan="2">
      <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
    </td>
</tr>
</table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciMisuraAlternativa");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");


  <%if(misuraalternativa == null || misuraalternativa.getIdMisuraAlternativa()== null)
     {%>
   frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>","numeric");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","req","Il Campo Anno Ordinanza è obbligatorio");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>","lt=2050");


  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","numeric");
  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>","req","Il Campo Numero Ordinanza è obbligatorio");


  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

  frmvalidator.addValidation("<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Data Emissione Ordinanza è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","numeric");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>","lt=2050");



<%}%>
<%
if(!lPosizione.isLibero() && !lcodicePosizione.equals("13"))
{
 if ( dataeditabile.equals("S") )
             {%>
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","req","Il campo Giorno Data Fine Pena Manuale è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","req","Il campo Mese Data Fine Pena Manuale è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","req","Il campo Anno Data Fine Pena Manuale è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>","lt=2050");
<%
}
}%>
<%if(lcodicePosizione.equals("03") || lcodicePosizione.equals("14"))
{%>

 frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>","req","Luogo Autorità Destinazione obbligatoria");
 frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_C %>","alphabetic");
<%}%>

<%if(lcodicePosizione.equals("12"))
{%>

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E %>","req","Luogo Autorità Destinazione obbligatoria");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_POL_E%>","alphabetic");
<%}%>

 frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS%>","req","Luogo Tribunale di Sorveglianza obbligatoria");
 frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>","alphabetic");

</script>
</body>
</html>