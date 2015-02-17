import java.util.logging.Logger
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.*
import org.semanticweb.owlapi.profiles.*
import org.semanticweb.owlapi.util.*
import org.semanticweb.owlapi.io.*
import org.semanticweb.elk.owlapi.*
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary

OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()
OWLDataFactory factory = fac

OWLOntology ont = manager.loadOntologyFromOntologyDocument(new File("efo.owl"))

def prefixset = new TreeSet()
ont.getClassesInSignature(true).each { cl ->
  def clstring = cl.toString()
  if (clstring.indexOf("_")>-1) {
    prefixset.add(clstring.substring(0,clstring.lastIndexOf("_")).replaceAll("<",""))
  }
}

prefixset.each { print "\""+it+"\", " }
