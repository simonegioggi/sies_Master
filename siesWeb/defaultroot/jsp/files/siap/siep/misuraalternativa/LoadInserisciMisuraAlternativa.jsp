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
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>
<%@ page import="siap.sius.depositoordinanzapc.action.ICostantiDepositoOrdinanzaPc"%>
<%@ page import="siap.sico.cssa.action.ICostantiCSSA"%>

<jsp:useBean id="penaComplessivaSanzioneSostitutiva" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel"/>
<jsp:useBean id="evento"             scope="request" class="siap.sico.evento.model.EventoModel"/>
<jsp:useBean id="modalita"           scope="request" class="java.lang.String"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="sedesorveglianza"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="sedepolizia"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="sedeautcompetente"   scope="request" class="siap.siep.notifica.model.NotificaModel"/>
<jsp:useBean id="magistrato"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="magistratosorveglianza"    scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="tipoIstituto"       scope="request" class="java.lang.String"/>
<jsp:useBean id="StrdataInizioPena"  scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="StrdataFinePenaA"    scope="request" class="java.lang.String"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="tipoUfficio"       scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioMagistrato"       scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaE"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>
<jsp:useBean id="sorveglianza"       scope="request" class="java.lang.String"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="verbale"      scope="request" class="siap.siep.verbale.model.VerbaleModel"/>
<jsp:useBean id="daticssa"      scope="request" class="siap.sico.cssa.model.CSSAModel"/>
<jsp:useBean id="luogodetenzione"      scope="request" class="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"/>

<% // n.b. JSP NON UTILIZZATA (13/03/2009) %>

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
<title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
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
    // Chiamata all'elenco degli UDS
      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }
    function Verify()
	  {
		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

      if (!ControllaData(data_to_verify) )
		  {
       alert('Data di emissione non valida');
			 return false;
		  }

         if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value='0'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>.value;


      var campo = document.LoadInserisciMisuraAlternativa.<%= ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA %>.value;

      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[0].value == '-' &&
           (campo == '10' || campo =='07' || (campo == "16") || ( campo == "17")
           || ( campo == "20") || ( campo == "46") || ( campo == "47") || ( campo == "26") || ( campo == "30")) )
			{
       alert("Il campo Autorità per l'Esecuzione di un Condannato Libero è obbligatorio!");
       document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[0].focus();
			 return false;
		  }

      if (document.LoadInserisciMisuraAlternativa.<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>[1].value == '-' &&
           (campo == '10' || campo =='07' || (campo == "16") || ( campo == "17")
           || ( campo == "20") || ( campo == "46") || ( campo == "47") || ( campo == "26") || ( campo == "30")) )
			{
       alert("Il campo Autorità per la Notifica di un Condannato Libero è obbligatorio!");
       document.LoadInserisciMisuraAlternativa.<%= ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA %>[1].focus();
			 return false;
		  }

      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
      {
        alert("Il Cognome del Magistrato è obbligatorio");
        return false;
      }

      if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
      {
        alert("Il Nome del Magistrato è obbligatorio");
        return false;
      }

    if(document.LoadInserisciMisuraAlternativa.flag.value=="")
    {
       if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS %>.value=="")
        {
          alert("Inserire l'ANNO del Fascicolo SIUS!");
          return false;
         }
       if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS %>.value.length>4 ||document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS %>.value.length<4)
        {
          alert("L'ANNO del Fascicolo SIUS non è corretto!");
          return false;
         }
    }

  if(document.LoadInserisciMisuraAlternativa.flag2.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS %>.value=="")
      {
        alert("Inserire la CHIAVE del Fascicolo SIUS!");
        return false;
      }
  }

  if(document.LoadInserisciMisuraAlternativa.flag3.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>.value=="")
      {
        alert("Inserire l'ANNO dell'ORDINANZA!");
        return false;
      }
     if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>.value.length>4 ||document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>.value.length<4)
        {
          alert("L'ANNO dell'ORDINANZA non è corretto!");
          return false;
         }
  }

 if(document.LoadInserisciMisuraAlternativa.flag4.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>.value=="")
      {
        alert("Inserire il NUMERO dell'ORDINANZA!");
        return false;
      }
  }
if(document.LoadInserisciMisuraAlternativa.flag5.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>.value=="")
      {
        alert("Inserire un UFFICIO di SORVEGLIANZA");
        return false;
      }
  }
if(document.LoadInserisciMisuraAlternativa.flag6.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA %>.value=="")
      {
        alert("Inserire l'OGGETTO dell'ORDINANZA");
        return false;
      }
  }
if(document.LoadInserisciMisuraAlternativa.flag7.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value=="")
      {
        alert("Inserire il GIORNO dell'EMISSIONE dell'ORDINANZA");
        return false;
      }
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value=="")
      {
        alert("Inserire il MESE dell'EMISSIONE dell'ORDINANZA");
        return false;
      }
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value=="")
      {
        alert("Inserire l'ANNO dell'EMISSIONE dell'ORDINANZA");
        return false;
      }
   if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>.value;

  }
if(document.LoadInserisciMisuraAlternativa.flag8.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>.value=="")
      {
        alert("Inserire il LUOGO della PROVA");
        return false;
      }
  }
if(document.LoadInserisciMisuraAlternativa.flag9.value=="")
  {
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value=="")
      {
        alert("Inserire il GIORNO della Sottoscrizione Verbale Obblighi");
        return false;
      }
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value=="")
      {
        alert("Inserire il MESE della Sottoscrizione Verbale Obblighi");
        return false;
      }
     if(document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>.value=="")
      {
        alert("Inserire l'ANNO della Sottoscrizione Verbale Obblighi");
        return false;
      }
  if (document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
		  if (document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
			  document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value;

		  var data_to_verify = document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.LoadInserisciMisuraAlternativa.<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>.value;

  }
 }
     function ListaCSSA(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

    function ListaMagistrati(a_formname)
    {
      var desktop;
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
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

if(misuraalternativa.getCodTipoMisura().equals("0005") ||misuraalternativa.getCodTipoMisura().equals("0009") ||misuraalternativa.getCodTipoMisura().equals("0010")||misuraalternativa.getCodTipoMisura().equals("0013"))
{
   flagMis = "D";
   lAzione = "siap.siep.misuraalternativa.action.ActInserisciMADetenzioneDomiciliare";

%>
    <font class="campo">Concessione Detenzione Domiciliare</font>

<%
}else if(misuraalternativa.getCodTipoMisura().equals("0001") ||misuraalternativa.getCodTipoMisura().equals("0002") ||misuraalternativa.getCodTipoMisura().equals("0003"))

{
   flagMis = "A";
   lAzione = "siap.siep.misuraalternativa.action.ActInserisciMAAffidamentoInProva";
%>
<font class="campo">Concessione Affidamento in prova</font>
<%
}else
{
   lAzione = "siap.siep.misuraalternativa.action.ActInserisciMisuraAlternativa";

   flagMis = "S";
%>
<font class="campo">Concessione Semilibertà</font>
<%
}
%>
</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciMisuraAlternativa">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.misuraalternativa.action.ActInserisciMisuraAlternativa">
  <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ID_MISURA_ALTERNATIVA%>" value="<%=misuraalternativa.getIdMisuraAlternativa()%>">

  <table cellspacing=4 cellpadding=4>
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
           if(!lAltraCausa.getDescrTipoIstituto().equals("") && lAltraCausa.getDescrTipoIstituto()!= null && !lAltraCausa.getDescrTipoIstituto().equals("-"))
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getDescrTipoIstituto()%></font>

<%
               if(lAltraCausa.getDescrLuogoIstituto()!=null)
               {
%>
                 di<font class="campo"> <%=lAltraCausa.getDescrLuogoIstituto()%></font>
<%
               }
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
        else if(!lLuogoDetenzione.getDescrTipoIstituto().equals("") && lLuogoDetenzione.getDescrTipoIstituto()!= null && !lLuogoDetenzione.getDescrTipoIstituto().equals("-"))
        {
%>
          <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getDescrTipoIstituto()%></font>
<%
              if(lLuogoDetenzione.getDescrLuogo()!=null)
              {
%>
                di<font class="campo"> <%=lLuogoDetenzione.getDescrLuogo()%></font>
<%
              }
%>
            </td>
          </tr>
<%
        }%>
   <input type="HIDDEN" title="Codice Posizione" value="<%=StringUtils.toStringJSP(lPosizione.getCodPosizioneGiuridica())%>" type="text" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>"  maxlength="6" size="6" >
  <% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getIndirizzo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIndirizzo())%></font>&nbsp;
              </td>
              <input type="HIDDEN" title="Codice Posizione" value="<%=lLuogoDetenzione.getDescrLuogo()%>" type="text" name=""  maxlength="6" size="6">
            </tr>
<%
          }
        }
%>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StrdataInizioPena%>&nbsp;</font></td>
<%
       }

       if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFinePresunta() != null)
       {
%>
         <td class="l">Data Fine Pena Automatica</td>
         <td class="L"><font class="campo"><%=StrdataFinePenaA%> &nbsp;</font></td>
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
%>
      </tr>
        <input type="HIDDEN" title="Codice Istituto" value="" type="text" name=""  maxlength="6" size="6" >
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
      <tr>
<%
        if ( ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D"))) && penaresidua.getDataFine()!=null)
        {
%>
         <td class="l">Data Fine Pena Manuale</td>
         <td class="L" colspan=2>
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE %>">
           /
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiPenaResidua.CAMPO_MESE_DATA_FINE %>">
           /
           <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%>" type="text" size="4" maxlength="4" name="<%= ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE %>">
         </td>
<%
        }
%>
        <input type="HIDDEN" title="Id Pena Residua" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>" type="text" name="<%= ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA %>">
       </tr>
</table>
<table>
   <tr>
      <td class="Titolo" colspan=6> Dati Ordinanza </td>
   </tr>
   <tr>
      <td class="l">N.Sius</td>
  <%if(misuraalternativa.getChiaveAnnoFascicoloSius()==null)
     {%>
    <td class="l"><font class="campo"> <input value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>" type="text" size="4" maxlength="4">
    <input type="hidden" name="flag" value="">
  <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>
    <input type="hidden" name="flag" value="1">
    <%}%>
   /<%if(misuraalternativa.getChiaveProgrFascicoloSius()==null)
     {%>
    <input value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>" type="text" size="6" maxlength="6"></font></td>
    <input type="hidden" name="flag2" value="">
  <%}else
     {%>
     <%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%></font></td>
      <input type="hidden" name="flag2" value="1">
   <%}%>
  </tr>
<tr>
     <td class="l"> Anno / Numero Ordinanza <font class="ob">(*)</font></td>
  <%if(misuraalternativa.getAnnoRegistro()==null)
     {%>
    <td class="l"><font class="campo"> <input value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>" type="text" size="4" maxlength="4">
    <input type="hidden" name="flag3" value="">
   <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>
   <input type="hidden" name="flag3" value="1">
   <%}%>
   /<%if(misuraalternativa.getNumeroRegistro()==null)
     {%>
    <input value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>" type="text" size="6" maxlength="6"></font></td>
   <input type="hidden" name="flag4" value="">
   <%}else
     {%>
    <%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%></font></td>
   <input type="hidden" name="flag4" value="1">
   <%}%>
  </tr>
<tr>
      <td class="l">Tipo Ufficio Sorveglianza <font class="ob">(*)</font></td>
     <%if(misuraalternativa.getCodUfficioSorveglianza()==null)
       {%>
      <td class="l"><font class="campo">
        <input Title="Tipo Ufficio Sorveglianza" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA %>" value="<%=StringUtils.toStringJSP(misuraalternativa.getDescrUfficioSorveglianza())%>" type="text" size=35 ></font>
        <input type="hidden" name="flag5" value="">
     </td>
      <%}else
     {%>
    <td class="l"> <font class="campo"><%=StringUtils.toStringJSP(misuraalternativa.getDescrUfficioSorveglianza())%></font></td>
    <input type="hidden" name="flag5" value="1">
    <%}%>
    </tr>
<tr>
      <td class="l">Oggetto Ordinanza <font class="ob">(*)</font></td>
     <%if(misuraalternativa.getCodTipoMisura()==null)
       {%>
      <td class="l"><font class="campo">
        <input Title="Oggetto Ordinanza" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA %>" value="<%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%>" type="text" size=35 >
     <input type="hidden" name="flag6" value="">
     </font></td>
      <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrTipoMisura())%></font></td>
    <input type="hidden" name="flag6" value="1">
    <% }%>
    </tr>
  <tr>
        <td class="l">Data Emissione Ordinanza <font class="ob">(*)</font></td>
       <%if(misuraalternativa.getDataDecisione()==null)
       {%>
       <td class="l"><font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>"  >
        </font></td>
        <input type="hidden" name="flag7" value="">
       <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(misuraalternativa.getDataDecisione(),"dd-MM-yyyy"))%></font></td>
    <input type="hidden" name="flag7" value="1">
    <%}%>
      </tr>
  <tr>
      <td class="l">Luogo della Prova <font class="ob">(*)</font></td>
     <%if(misuraalternativa.getDescrLuogoProva()==null)
       {%>
      <td class="l"><font class="campo">
        <input Title="Luogo svolgimento della prova" name="<%= ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA %>" value="<%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>" size=35 type="text">
      </font></td>
   <input type="hidden" name="flag8" value="">
    <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%></font></td>
    <%}%>
   <input type="hidden" name="flag8" value="1">
   </tr>
<%if(flagMis.equals("A")){%>
  <tr>
        <td class="l">Data Sottoscrizione Verbale Obblighi</td>
       <%if(verbale.getDataEmissione()==null)
       {%>
       <td class="l"><font class="campo">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>"  >
        </font></td>
       <input type="hidden" name="flag9" value="">
       <%}else
     {%>
    <td class="l"><font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(verbale.getDataEmissione(),"dd-MM-yyyy"))%></font></td>
    <input type="hidden" name="flag9" value="1">
    <%}%>
      </tr>
<%}%>
<%if(flagMis.equals("D")){%>
  <tr>
<%if(misuraalternativa.getDataInizioMisura()!= null){%>
        <td class="l">Data Inizio Misura</td>
         <td class="l"><font class="campo">
          <input value="<%=DateUtils.getDayToString(misuraalternativa.getDataInizioMisura())%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INIZIO_MISURA  %>"  > /
          <input value="<%=DateUtils.getMonthToString(misuraalternativa.getDataInizioMisura())%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INIZIO_MISURA  %>"  > /
          <input value="<%=DateUtils.getYearToString(misuraalternativa.getDataInizioMisura())%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INIZIO_MISURA  %>"  >
</font>
        </td>
<%}
if(misuraalternativa.getDataFineMisura()!= null){%>
        <td class="l">Data Fine Misura</td>
         <td class="l"><font class="campo">
          <input value="<%=DateUtils.getDayToString(misuraalternativa.getDataFineMisura())%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_FINE_MISURA  %>"  > /
          <input value="<%=DateUtils.getMonthToString(misuraalternativa.getDataFineMisura())%>" type="text" size="2" maxlength="2" name="<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_FINE_MISURA  %>"  > /
          <input value="<%=DateUtils.getYearToString(misuraalternativa.getDataFineMisura())%>" type="text" size="4" maxlength="4" name="<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_FINE_MISURA  %>"  >
             </font>
        </td>
 <%}%>
</tr>
<%}%>

   <tr><td>&nbsp;</td></tr>
</table>
<table>
   <tr>
     <td class="Titolo" colspan=6> Notifica al Magistrato di Sorveglianza</td>
   </tr>
  <tr>
   <td class="l">Magistrato
   <td class="L">
       <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistrato.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
  </tr>
<tr><td>&nbsp;</td></tr>
<tr>
      <td class="Titolo" colspan=6>Destinatario per Notifica </td></tr>
<%
      int lIdxAvv = 1;
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

    <tr><td>&nbsp;</td>
<%
    lIdxAvv++;
  }
%>
</table>
<table>
<tr>
        <td class="l">Data Emissione</td>
        <td class="L" colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>"  >
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L"colspan=2>
          <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO %>"  > /
          <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiNotifica.CAMPO_MESE_DATA_INVIO %>"  > /
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiNotifica.CAMPO_ANNO_DATA_INVIO %>"  >
        </td>
      </tr>
</table>

<table>
    <tr>
          <td class="Titolo" colspan=6> Notifica Esecuzione Esterna</td></tr>
   <tr>
      <td class="l">UEPE Competente </td>
      <td class="l">
        <input Title="UEPE Competente" name="<%= ICostantiCSSA.CAMPO_COD_COMUNE%>" value="<%=StringUtils.toStringJSP(daticssa.getComune())%>" size=35 >
        <a href="Javascript:ListaCSSA('LoadInserisciMisuraAlternativa','<%= ICostantiCSSA.CAMPO_COD_COMUNE %>');">
        <img src="/images/filefolder.gif" border=0></a>
      </td>
      <input type="hidden" value="CSSA" name="<%=ICostantiCSSA.CAMPO_COD_COMUNE%>">
        <td rowspan=2 class="l">Note</td>
         <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE %>"  cols=20 rows=5 ></textarea>
         </td>
    </tr>
<tr><td>&nbsp;</td></tr>
<%if(lPosizione.getCodPosizioneGiuridica().equals("03")||lPosizione.getCodPosizioneGiuridica().equals("14"))
{%>
<tr> <td class="Titolo" colspan=6>Istituto</td></tr>
<tr>
     <td class="l">Destinatario </td>
      <td class="l">ISTITUTO DI DETENZIONE </td></tr>
      <tr><td class="l">Sede</td>
      <td class="l">
        <input title="Sede Istituto Detenzione" value="<%=StringUtils.toStringJSP(luogodetenzione.getDescrLuogo()) %>" type="text" name=""  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<%}%>
    <tr>
      <td class="Titolo" colspan=6> Magistrato di Sorveglianza</td></tr>
   <tr>
      <td class="l">Magistrato </td>
      <td class="L">
       <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratosorveglianza.getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
       <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratosorveglianza.getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"        maxlength="35" size="25">
        <a href="Javascript:ListaMagistrati('LoadInserisciMisuraAlternativa');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
 <tr><td class="Titolo" colspan=6> Notifica Ente di Sorveglianza</td></tr>
   <tr>
         <td class="l">Destinatario</td >
          <td class="L">TRIBUNALE DI SORVEGLIANZA
         </td>
         <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>">
         <td rowspan=2 class="l">Note</td>
         <td rowspan=2 class="L">
          <TEXTAREA title="Note" name="<%= ICostantiMisuraAlternativa.CAMPO_NOTE_TDS %>"  cols=20 rows=5 ></textarea>
         </td>
   </tr>
<tr>
         <td class="l">Sede <font class=ob>(*)</font></td><td class="L">
         <input title="Sede Tribunale Sorveglianza" value="<%=StringUtils.toStringJSP(sedesorveglianza.getDescProvincia())%>" type="text" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>');">
         <img src="/images/filefolder.gif" border=0></a></td>
     </tr>
  <tr><td class="Titolo" colspan=6>Notifica al Condannato </td></tr>
      <tr>
          <td class="l">Autorità Destinazione </td>
          <td class="L">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
             <%=autoritaEsternaE%>
             </select>
           </td>
           <td rowspan=2 class="l">Note</td>
           <td rowspan=2 class="L">
              <TEXTAREA title="Note" name="<%= ICostantiNotifica.CAMPO_NOTE %>"  cols=20 rows=5 ></textarea>
            </td>
           </tr>
    <tr>
      <td class="l">Sede </td>
      <td>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(sedepolizia.getAutoritaEsterna().getDescrSede()) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[0]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
<tr><td class="Titolo" colspan=6>Notifica al Condannato </td></tr>
      <tr>
          <td class="l">Autorità Destinazione </td>
          <td class="L">
            <select  Title="Autorita Esterna"  class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
             <%=autoritaEsternaN%>
             </select>
           </td>
           <td rowspan=2 class="l">Note</td>
           <td rowspan=2 class="L">
              <TEXTAREA title="Note" name="<%= ICostantiNotifica.CAMPO_NOTE %>"  cols=20 rows=5 ></textarea>
            </td>
           </tr>
    <tr>
      <td class="l">Sede </td>
      <td>
        <input title="Sede Autorita Esterna" value="<%=StringUtils.toStringJSP(sedeautcompetente.getAutoritaEsterna().getDescrSede()) %>" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciMisuraAlternativa','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE %>[0]');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
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
  <%if(misuraalternativa.getDataDecisione()==null)
   {%>
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>","lt=2099");
  <%}
  if(verbale.getDataEmissione()==null)
  {%>
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=  ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Invio dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2099");
  <%}%>
  frmvalidator.addValidation("<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>","alphabetic");

</script>
</body>
</html>