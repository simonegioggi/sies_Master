//  Sono qui contenute le funzioni javascript per richiamare le pop-up
//  utili all'inserimento di uffici destinatari SIUS.

var desktop;
function ListaUSSM (a_formname,a_fieldname) 
{
	  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.cssa.action.ActLoadListaUSSM&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaPMM(a_formname,a_fieldname)
{
	  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaPMM&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali per minorenni:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaTDS(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname+"&NomeLista="+"Lista Tribunali di Sorveglianza:", "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
}
function ListaComuni(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaUffici(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaProcure(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaDistretti&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaUDS(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaCSSA(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.cssa.action.ActLoadListaCSSA&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
function ListaUNEP(a_formname,a_fieldname)
{
  desktop = window.open("/jsp/Main.jsp?Action=siap.sico.ufficio.action.ActLoadRicercaUfficio&formname="+a_formname+"&fieldname="+a_fieldname+"&unep=SI", "Ricerca_Ufficio","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
}
// Funzione di switch tra ListaComuni e Lista Sedi UNEP in base al tipo destinatario
function ListaComuniUNEP(a_formname,a_fieldname, tipo_dest)
{
// alert ("tipo destinatario ->" + tipo_dest);
  if (tipo_dest == "22")
  	ListaUNEP(a_formname,a_fieldname);
  else
  	ListaComuni(a_formname,a_fieldname);
}




