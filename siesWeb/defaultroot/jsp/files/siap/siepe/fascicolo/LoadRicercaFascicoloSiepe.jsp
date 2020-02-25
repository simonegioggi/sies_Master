<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.siepe.fascicolo.model.FascicoloSiepeRicercaModel"%>
<%@ page import="siap.siepe.fascicolo.action.ICostantiFascicoloSiepe"%>

<jsp:useBean id="modalita"  scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoUfficioSiepeTrattino" scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoUfficioConnesso" scope="request" class="java.lang.String"/>
<jsp:useBean id="ComuneUfficioConnesso" scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Ricerca Procedimento SIEPE</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">
    var desktop;
    // Lista Uffici per TIPO_UFFICIO ( UEPE / UEPESS )
    function ListaUffici(a_formname, a_fieldname, codTipoUfficio)
    {
      desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function checkTipoUfficio()
    {
      if ("<%=TipoUfficioConnesso%>" == 'UEPE')
      {
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=1;
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
        document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.selectedIndex=1;
        document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value="<%=ComuneUfficioConnesso%>";
      }
      else
      {
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>.selectedIndex=2;
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO%>.value="<%=ComuneUfficioConnesso%>";
        document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.selectedIndex=2;
        document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value="<%=ComuneUfficioConnesso%>";
      }
    }

    function Verify()
    {
      return true;
    }

    function VerifyG()
    {
      return false;
    }
  </script>

  <script language="JavaScript">
    function radioBase()
    {
      var nodeIntervallo;
      var nodeDescIntervallo;
      var nodeData;
      var nodedivDesc;
      var nodePrincipale;

      checkTipoUfficio();

      nodeIntervallo=document.getElementById('intervallo');
      nodedivDesc=document.getElementById('divDesc');
      nodeData=document.getElementById('data');
      nodeDataIntervallo=document.getElementById('dataIntervallo');
      nodePrincipale=document.getElementById('Principale');

      if(document.f.tipoRicerche[0].checked)
      {
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>[0].selected;

        nodeIntervallo.style.visibility='hidden';
        nodedivDesc.style.visibility='hidden';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='hidden';
        nodePrincipale.style.visibility='visible';
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.focus();
      }
      else if (document.f.tipoRicerche[1].checked )
      {
        document.g.tipo[1].checked=true;
        document.g.tipo[0].checked=true;
        radio();
        nodedivDesc.style.visibility='visible';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='hidden';
        nodePrincipale.style.visibility='hidden';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
      }
    }

    function radio()
    {
      var nodeIntervallo;
      var nodeData;
      var nodeDataIntervallo;

      nodeIntervallo=document.getElementById('intervallo');
      nodeData=document.getElementById('Data');
      nodeDataIntervallo=document.getElementById('dataIntervallo');

      if(document.g.tipo[0].checked)
      {
        pulisci();
        nodeIntervallo.style.visibility='visible';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='hidden';
        document.f.valoreRadio.value='0';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.focus();
      }
      else if (document.g.tipo[1].checked )
      {
        pulisci();
        nodeIntervallo.style.visibility='hidden';
        nodeData.style.visibility='visible';
        nodeDataIntervallo.style.visibility='hidden';
        document.f.valoreRadio.value='1';
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>.focus();
      }else{
        pulisci();
        nodeIntervallo.style.visibility='hidden';
        nodeData.style.visibility='hidden';
        nodeDataIntervallo.style.visibility='visible';
        document.f.valoreRadio.value='2';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.focus();
      }
    }
    function pulisci()
    {
      document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.value='';
      document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>.value='';
      document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
      document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
      document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
      document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
      document.b.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>.value='';
      document.b.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>.value='';
      document.b.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>.value='';
      document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
      document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
      document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
      document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
      document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
      document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
    }
    function VerifyD()
    {

      if (document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.value=="" &&
      		document.d.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>.value=="")
      {
        alert("Valorizzare l'Anno ");
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }

      if (document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.value!="" &&
      		document.d.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE%>.value!="")
      {
        alert("Impostare la ricerca per Procedimento SIEPE o per Procedimento UEPE");
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>.focus();
        return false;
      }

      if (document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>.value=="" &&
      		document.d.<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>.value=="")
      {
        alert("Valorizzare il progressivo");
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
      if (document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>.value!="" &&
      		document.d.<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE%>.value!="")
      {
        alert("Impostare la ricerca per Procedimento SIEPE o per Procedimento UEPE");
        document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>.focus();
        return false;
      }
      return true;
    }
  </script>

  <script language="JavaScript">
    function VerifyA()
          {
      // Non è possibile specificare solo il numero o solo l'anno
      if( (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno inizio ricerca");
        return false;
      }
      // Se non valorizzato, si Imposta il Progressivo iniziale.
      if( (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0) )
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='1';

      if( (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value.length == 0) )
      {
        alert("Valorizzare Anno di fine ricerca");
        return false;
      }
      // Se non valorizzato, si Imposta il Progressivo finale.
      if( (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value.length == 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value='999999';

      // Non è possibile cercare per numero/anno fine minore di numero/anno inizio
      if( (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value.length != 0)
           && (document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value.length != 0) )
      {
        if(document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value < document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {
          alert("Anno inizio maggiore Anno fine");
          return false;
        }
        else if(document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value == document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value)
        {

          if(parseInt(document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value) < parseInt(document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value))
          {
            alert("Numero inizio maggiore Numero fine");
            return false;
          }
        }
      }
      return true;
    }

    function VerifyC()
    {
    if (document.c.<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='0'+document.c.<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value;
    if (document.c.<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='0'+document.c.<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value;

    if (document.c.<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value.length==1)
       document.c.<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='0'+document.c.<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value;
    if (document.c.<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value.length==1)
        document.c.<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='0'+document.c.<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value;

    var data_inizio=document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
    var data_fine=document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value+'/'+document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;

      if(!ControllaDataPassaVuota(data_inizio))
      {
        alert('Data di inizio non valida');
        return false;
      }
      if(!ControllaDataPassaVuota(data_fine))
      {
        alert('Data di fine non valida');
        return false;
      }

      if(data_inizio.length==2 || data_fine.length==2)
       return true;

      if(!CompareDate(data_inizio,data_fine))
      {
        alert('La Data di fine non può essere inferiore alla data di inizio');
        return false;
      }
      return true;
    }

    function VerifyChiamate(id)
    {
      if (document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value=="-" )
      {
        alert("L' Ufficio è obbligatorio");
        document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.focus();
        return false;
      }
      if (document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value=="")
      {
        alert('Il campo Sede Ufficio è obbligatorio');
        document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.focus();
        return false;
      }
      if(id==1)
      {
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value;
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value;
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
      }
      if(id==2)
      {
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value;
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value;
        // Controllo della data iscrizione.
        var data_iscrizione=document.b.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>.value+'/'+document.b.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>.value+'/'+document.b.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>.value;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'
        if (! ControllaData(data_iscrizione))
        {
          alert('Data iscrizione non valida');
          return false;
        }
        // Controllo della data iscrizione <= data di sistema
        if (! CompareDate(data_iscrizione, data_sistema))
        {
          alert('Data iscrizione > della data odierna');
          return false;
        }

        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value='';
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value='';
      }
      if(id==3)
      {
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.value;
        document.c.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value = document.g.<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>.value;
        // Controllo dell'intervallo date iscrizione.
        var gg_in = FillDM(document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>.value);
        var mm_in = FillDM(document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>.value);
        var aa_in = document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>.value;
        var dataIni = gg_in + "/" + mm_in + "/" + aa_in;
        var gg_fi = FillDM(document.c.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>.value);
        var mm_fi = FillDM(document.c.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>.value);
        var aa_fi = document.c.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>.value;
        var dataFine = gg_fi + "/" + mm_fi + "/" + aa_fi;
        var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>'

				// STUB Controlli Sostituiti il 14/01/2005 con i seguenti:
        if (dataIni.length != 2 && dataIni.length != 10)
        {
          alert ("Data di inizio periodo errata");
          return false;
        }
        if (dataFine.length != 2 && dataFine.length != 10)
        {
          alert ("Data di fine periodo errata");
          return false;
        }
        else if ( (dataIni.length == 10) && (ControllaData (dataIni) == false) )
        {
          // entrambe le date valorizzate
          alert ("Errore nella data : " + dataIni);
          return false;
        }
        else if ( (dataFine.length == 10) && (ControllaData (dataFine) == false) )
        {
          alert ("Errore nella data : " + dataFine);
          return false;
        }
        else if ((dataFine.length == 10)               &&
                 ( dataIni.length == 10)               &&
                 CompareDate(dataIni,dataFine)== false )
        {
          alert ("Data di Fine minore di Data inizio periodo");
          return false;
        }
        else if ((dataFine.length == 10)                       &&
                  CompareDate(dataFine, data_sistema)== false)
        {
          alert ("Data di Fine maggiore di Data sistema");
          return false;
        }
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>.value='';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>.value='';
        document.a.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>.value='';
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>.value='';
        document.b.<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>.value='';
      }
    }

    function ListaUfficiDistretto(a_formname,a_fieldname,a_fieldname2)
    {
      var TipoUff = document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>.value;
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadRicercaComunePerDistretto&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldname2="+a_fieldname2+"&codTipoUff="+TipoUff, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
  </script>


</head>
  <body class="corpo" onLoad="radioBase();">
  <form name="f">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;<font class="campo">Ricerca Procedimento</font>
<%
        FascicoloSiepeRicercaModel lModel = new FascicoloSiepeRicercaModel();
%>
      </td>
    </tr>
  </table>
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaFascicoloSiepe">
  <input type="HIDDEN" name="valoreRadio" value="">
  <table width=85%>
    <tr>
    <tr><td class="Titolo" >Tipo Ricerca</td></tr>
       <td class="c">Base &nbsp;<input type="radio" name="tipoRicerche" value="base" checked  onClick="radioBase();">
                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Avanzata  &nbsp; <input type="radio" name="tipoRicerche" value="avanzata"  onClick="radioBase();">
       </td>
    </tr>
  </table>
  </form>

  <div id="divDesc" style="visibility:hidden; position:relative; top:-13px; width:100%;">
    <form  name="g">
    <table width=85%>
      <tr><td class="Titolo" colspan ="2" >Ricerca valida per procedimenti di un singolo Ufficio</td>
      <tr>
        <td class="l">Tipo Ufficio <font class=ob>(*)</font></td>
        <td class="L">
          <select title="tipoUfficioSiepeTrattino" class=small name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>" >
            <%= tipoUfficioSiepeTrattino %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede <font class=ob>(*)</font> </td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>" value="<%=lModel.getDescrComuneUfficio()%>" type="text" maxlength="35" size="35">
             <a href="Javascript:ListaUffici('g','<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>',document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>[document.g.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>.options.selectedIndex].value);">
             <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>
      <tr>
        <td class="c"colspan ="2">Intervallo Numero Procedimenti &nbsp;<input type="radio" name="tipo" value="descIntervallo"  onClick="radio();">
          &nbsp;&nbsp;Data Iscrizione &nbsp;<input type="radio" name="tipo" value="descData"  onClick="radio();">
          &nbsp;&nbsp;Intervallo Date di  Iscrizione &nbsp;<input type="radio" name="tipo" value="descIntervalloData"   onClick="radio();"></td>
      </tr>
    </table >
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaFascicoloSiepe">
    </form>
  </div>

  <div id="intervallo" style="visibility:visible; position:relative; top:-30px; width:100%;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="a">
    <table  width=85%>
      <tr><td class="Titolo" colspan=4>Intervallo Procedimenti</td></tr>
      <tr>
        <td class="L" >
          <font class="label">Anno/Numero Iniziale </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Procedimento Iniziale" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero Procedimento Iniziale" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="L">
          <font class="label">Anno/Numero Finale </font>
        </td>
        <td class="l">
          <input type="text" title="Anno Procedimento Finale" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">/
          <input type="text" title="Numero Procedimento Finale" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
        <td class="l" >
          <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(1);">
        </td>
      </tr>
    </table>
    <br>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaFascicoloSiepe">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>" >
  </FORM>
    <br><br>
  </div>

  <div id="data" style="visibility:hidden; position:relative; top:-136px; width:100%;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="b">
    <table width=85%>
      <tr><td class="Titolo" colspan ="2" >Specifica Data di Iscrizione</td>
      </tr>
      <tr>
        <td class="L"  width="18%"> Data <font class=ob>(*)</font></td>
        <td class="L" >
          <input type="text" title="Giorno Iscrizione" name="<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          <input type="text" title="Mese Iscrizione" name="<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          <input type="text" title="Anno Iscrizione" name="<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l" >
          <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(2);">
        </td>
      </tr>
    </table >
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaFascicoloSiepe">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>" >
    </FORM>
  </div>

  <div id="dataIntervallo" style="visibility:hidden; position:relative; top:-204px; width:100%;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="c">
    <table width=85%>
      <tr> <td class="Titolo"  colspan ="4" >Intervallo Date di Iscrizione</td>
      </tr>
      <tr>
        <td class="L" width="20%" >
          <font class="label"> Data Iniziale </font>
        </td>
        <td class="l" >
          <input type="text" title="Giorno Iscrizione Iniziale" name="<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          <input type="text" title="Mese Iscrizione Iniziale" name="<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          <input type="text" title="Anno Iscrizione Iniziale" name="<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="L" >
          <font class="label">Data Finale </font>
        </td>
        <td class="l">
          <input type="text" title="Giorno Iscrizione Finale" name="<%=ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          <input type="text" title="Mese Iscrizione Finale" name="<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">-
          <input type="text" title="Anno Iscrizione Finale" name="<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l" >
          <input class="bottone" type="submit" name="RICERCA" value="Ricerca" onClick="javascript:return VerifyChiamate(3);">
        </td>
      </tr>
    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaFascicoloSiepe">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO2%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO2%>" >
    </form>
  </div>
  <br>

  <div id="Principale" style="visibility:hidden; position:absolute; top:90px; width:100%;">
  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="d">
    <table width=85%>
      <tr><td class="Titolo" colspan=2>Specifico Procedimento</td></tr>
      <tr>
        <td class="l" width=38%>Procedimento SIEPE (Anno/Numero) </td>

        <td class="l">
          <input Title="Anno SIEPE"  type="text" name="<%= ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero SIEPE" type="text" name="<%= ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>

      <tr>
        <td class="l" width=38%>Procedimento UEPE (Anno/Numero/Progr.)</td>

        <td class="l">
          <input Title="Anno UEPE"  type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_ANNO_UEPE %>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Numero UEPE" type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_NUM_UEPE %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
          /<input Title="Progressivo UEPE" type="text" name="<%=ICostantiFascicoloSiepe.CAMPO_PROGR_UEPE %>" maxlength="6" size="6" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)">
        </td>
      </tr>


       <tr>
        <td class="l">Tipo Ufficio </td>
        <td class="L">
          <select title="tipoUfficioSiepeTrattino" class=small name="<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>" >
            <%= tipoUfficioSiepeTrattino %>
          </select>
        </td>
      </tr>

      <tr>
        <td class="l">Sede </td>
        <td class="l">
           <input Title="Sede Ufficio" name="<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO%>" value="<%=lModel.getDescrComuneUfficio()%>" type="text" maxlength="35" size="35">
             <a href="Javascript:ListaUffici('d','<%=ICostantiFascicoloSiepe.CAMPO_DESCR_COMUNE_UFFICIO%>',document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>[document.d.<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_UFFICIO%>.options.selectedIndex].value);">
            <img src="/images/filefolder.gif" border=0> </a>
        </td>
      </tr>

      <tr>
        <td>
          <input onClick="javascript:return VerifyD();" class="bottone" type="submit"  name="RICERCA" value="Ricerca">
        </td>
      </tr>

    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siepe.fascicolo.action.ActRicercaFascicoloSiepe">
  <%--/div--%>
  </form>
  </div>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("d");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>","numeric","Il campo Numero Procedimento può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","numeric","Il campo Anno può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("a");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>","numeric","Il campo Numero Procedimento Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_INIZIALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>","numeric","Il campo Numero Procedimento Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_PROGR_FINALE%>","maxlen=6","La lunghezza massima per il Numero Procedimento è di 6 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>","numeric","Il campo Anno Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_INIZIALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>","numeric","Il campo Anno Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>","maxlen=4","La lunghezza massima per l'Anno Procedimento è di 4 caratteri");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_CHIAVE_ANNO_FINALE%>","minlen=4","La lunghezza minima per l'Anno Procedimento è di 4 caratteri");

    frmvalidator.setAddnlValidationFunction("VerifyA");
  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("b");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>","numeric","Il campo Data di Iscrizione può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE%>","lt=3000");

  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("c");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","maxlen=2","La lunghezza massima per il giorno di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_INIZIALE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","maxlen=2","La lunghezza massima per il giorno di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_GIORNO_ISCRIZIONE_FINALE%>","lt=31");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","maxlen=2","La lunghezza massima per il mese di inizio è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_INIZIALE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>","maxlen=2","La lunghezza massima per il mese di fine è di 2 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>","gt=1");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_MESE_ISCRIZIONE_FINALE%>","lt=12");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","numeric","Il campo Data Iniziale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","maxlen=4","La lunghezza massima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","minlen=4","La lunghezza minima per l'anno di inizio è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_INIZIALE%>","lt=3000");

    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>","numeric","Il campo Data Finale può avere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>","maxlen=4","La lunghezza massima per l'anno di fine è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>","minlen=4","La lunghezza minima per l'anno di fine è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiepe.CAMPO_ANNO_ISCRIZIONE_FINALE%>","lt=3000");

    frmvalidator.setAddnlValidationFunction("VerifyC");
  </script>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator  = new Validator("g");

    frmvalidator.setAddnlValidationFunction("VerifyG");
  </script>

  </body>
</html>