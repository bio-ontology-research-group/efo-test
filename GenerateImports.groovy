import java.util.logging.Logger
import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.*
import org.semanticweb.owlapi.reasoner.*
import org.semanticweb.owlapi.profiles.*
import org.semanticweb.owlapi.util.*
import org.semanticweb.owlapi.io.*
import org.semanticweb.elk.owlapi.*
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary

List imports = ["http://purl.obolibrary.org/obo/BTO", "http://purl.obolibrary.org/obo/CHEBI", "http://purl.obolibrary.org/obo/CL", "http://purl.obolibrary.org/obo/CLO", "http://purl.obolibrary.org/obo/DOID", "http://purl.obolibrary.org/obo/EO", "http://purl.obolibrary.org/obo/FBbt", "http://purl.obolibrary.org/obo/GO", "http://purl.obolibrary.org/obo/HP", "http://purl.obolibrary.org/obo/IAO", "http://purl.obolibrary.org/obo/IDO", "http://purl.obolibrary.org/obo/MP", "http://purl.obolibrary.org/obo/NCBITaxon", "http://purl.obolibrary.org/obo/OBI", "http://purl.obolibrary.org/obo/PATO", "http://purl.obolibrary.org/obo/PO", "http://purl.obolibrary.org/obo/SO", "http://purl.obolibrary.org/obo/UBERON", "http://purl.obolibrary.org/obo/UO", "http://purl.obolibrary.org/obo/ZEA"]

OWLOntologyManager manager = OWLManager.createOWLOntologyManager()
OWLDataFactory fac = manager.getOWLDataFactory()
OWLDataFactory factory = fac
manager.setSilentMissingImportsHandling(true)

def ontset = new TreeSet()
OWLOntology ont = manager.loadOntologyFromOntologyDocument(new File("efo.owl"))
ontset.add(ont)

new File("ontologies").eachFile { f ->
  try {
    ont = manager.loadOntologyFromOntologyDocument(f)
    ontset.add(ont)
  } catch (Exception E) {
    println "Exception: "+E.getMessage()
  }
}

ont = manager.createOntology(IRI.create("http://phenomebrowser.net/efo-merged.owl"), ontset)

ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor()
OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor)
OWLReasonerFactory f1 = new ElkReasonerFactory()
OWLReasoner reasoner = f1.createReasoner(ont,config)

def counter = 0

reasoner.getEquivalentClasses(fac.getOWLNothing()).each {
  println ""+ it.toString() + " is unsatisfiable"
  counter += 1
}
println "Unsatisfiable classes: $counter"
counter = 0

ont.getClassesInSignature(true).each { counter +=1 }
println "Classes: $counter"
counter = 0
ont.getObjectPropertiesInSignature(true).each { counter +=1 }
println "Properties: $counter"
counter = 0
AxiomType.TBoxAxiomTypes.each { ont.getAxioms(it, true).each { counter +=1 } }
println "Logical (TBox) axioms: $counter"
counter = 0
AxiomType.RBoxAxiomTypes.each { ont.getAxioms(it, true).each { counter +=1 } }
println "Logical (RBox) axioms: $counter"

