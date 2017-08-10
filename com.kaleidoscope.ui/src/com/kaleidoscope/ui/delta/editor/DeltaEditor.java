package com.kaleidoscope.ui.delta.editor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.presentation.EcoreEditor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.URIHandlerImpl;
import org.eclipse.ui.IEditorInput;

import com.kaleidoscope.core.delta.javabased.JavaBasedEdge;
import com.kaleidoscope.core.delta.javabased.operational.AddEdgeOp;
import com.kaleidoscope.core.delta.javabased.operational.AttributeChangeOp;
import com.kaleidoscope.core.delta.javabased.operational.DeleteEdgeOp;
import com.kaleidoscope.core.delta.javabased.operational.DeleteNodeOp;
import com.kaleidoscope.core.delta.javabased.operational.OperationalDelta;

import KaleidoscopeDelta.DeleteNodeOP;
import KaleidoscopeDelta.Edge;
import KaleidoscopeDelta.KaleidoscopeDeltaFactory;

public class DeltaEditor extends EcoreEditor {
	private static final Logger logger = Logger.getLogger(DeltaEditor.class);

	private OperationalDelta delta;

	// The user is going to perform changes on this model
	private EObject originalModel;

	// The changes will be mapped to this unchanged model
	private EObject copiedModel;

	// Mapping is: originalModel ---> copiedModel
	private Copier copier;

	private Resource contentResource;

	private Boolean dirty = true;

	@Override
	public void createModel() {
		super.createModel();

		contentResource = editingDomain.getResourceSet().getResources().get(0);
		originalModel = contentResource.getContents().get(0);

		copier = new Copier();
		copiedModel = copier.copy(originalModel);
		copier.copyReferences();

		delta = new OperationalDelta();

		new OnlineChangeDetector(delta, originalModel);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		createNewDeltaResource();
		dirty = false;
	}

	@Override
	protected void doSaveAs(URI uri, IEditorInput editorInput) {
		doSave(new NullProgressMonitor());
	}

	private void createNewDeltaResource() {
		KaleidoscopeDelta.OperationalDelta operationalDelta = createOperationalDeltaFromJavaBasedDelta();
		//operationalDelta.setTargetModel(copiedModel);

		editingDomain.getResourceSet().getResources().remove(contentResource);

		Resource resourceForCopy = editingDomain.getResourceSet().createResource(contentResource.getURI());
		resourceForCopy.getContents().add(copiedModel);

		Resource deltaResource = editingDomain.getResourceSet()
				.createResource(contentResource.getURI().trimFileExtension().appendFileExtension("delta.xmi"));
		deltaResource.getContents().add(operationalDelta);

		try {
			HashMap<String, Object> saveOptions = new HashMap<String, Object>();
			saveOptions.put(XMLResource.OPTION_URI_HANDLER, new AbsolutePluginUriAllElseRelativeHandler());
			deltaResource.save(saveOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private KaleidoscopeDelta.OperationalDelta createOperationalDeltaFromJavaBasedDelta() {
		OnlineChangeDetector.removeDeltaListeners(originalModel);
		KaleidoscopeDelta.OperationalDelta operationalDelta = KaleidoscopeDeltaFactory.eINSTANCE.createOperationalDelta();

		delta.getOperations().stream()
				.map(this::mapEdgeToCopy)
				.map(this::mapDeleteNodeToCopy)
				.map(this::mapAttrDeltaToCopy)
				.filter(o -> !(o instanceof AttributeChangeOp && ((AttributeChangeOp) o).getAffectedNode() == null))
				.forEach(o -> operationalDelta.getOperations().add(o.toOperationalEMF()));

		return operationalDelta;
	}

	private Collection<Edge> removeOpposites(Collection<Edge> allEdges) {
		ArrayList<Edge> edges = new ArrayList<>(allEdges);
		ArrayList<Edge> result = new ArrayList<>();

		for (int i = 0; i < edges.size(); i++) {
			boolean noOpposite = true;
			for (int j = i + 1; j < edges.size(); j++) {
				if (isOppositeOf(edges.get(i), edges.get(j))) {
					noOpposite = false;
					break;
				}
			}

			if (noOpposite)
				result.add(edges.get(i));
		}

		return result;
	}

	private boolean isOppositeOf(Edge edge1, Edge edge2) {
		return edge1.getSrc().equals(edge2.getTrg()) && edge1.getTrg().equals(edge2.getSrc())
				&& toReference(edge2).equals(toReference(edge1).getEOpposite());
	}

	private EReference toReference(Edge edge) {
		return edge.getType();
	}

	private com.kaleidoscope.core.delta.javabased.operational.Operation mapAttrDeltaToCopy(
			com.kaleidoscope.core.delta.javabased.operational.Operation attributeChange) {

		if (attributeChange instanceof AttributeChangeOp) {

			
			AttributeChangeOp attrChange = (AttributeChangeOp) attributeChange;
			EObject copiedNode = copier.get(attrChange.getAffectedNode());
			attrChange.setAffecteNode(copiedNode);
			
			
			return attrChange;
		} else {
			return attributeChange;
		}		
	}

	private com.kaleidoscope.core.delta.javabased.operational.Operation mapDeleteNodeToCopy(
			com.kaleidoscope.core.delta.javabased.operational.Operation delNodeOp) {
		if (delNodeOp instanceof DeleteNodeOp) {

			DeleteNodeOp deleteNodeOp = (DeleteNodeOp) delNodeOp;
			deleteNodeOp.setNode(copier.get(deleteNodeOp.getNode()));
			return deleteNodeOp;
		} else {
			return delNodeOp;
		}

	}

	private List<DeleteNodeOP> mapNodesToCopy(List<DeleteNodeOP> deleteNodesOPs) {
		// deleteNodesOPs.stream().map(o -> copier.get(o.getNode()));
		deleteNodesOPs.stream().forEach(o -> o.setNode(copier.get(o.getNode())));
		return deleteNodesOPs;
		// return
		// deletedNodes.stream().map(copier::get).collect(Collectors.toList());
	}
	private com.kaleidoscope.core.delta.javabased.operational.Operation mapEdgeToCopy(com.kaleidoscope.core.delta.javabased.operational.Operation op) {
		
		if (op instanceof DeleteEdgeOp) {

			DeleteEdgeOp deleteEdgeOp = (DeleteEdgeOp) op;
			JavaBasedEdge edge = deleteEdgeOp.getEdge();
			
			edge.setSrc(copier.containsKey(edge.getSrc()) ? copier.get(edge.getSrc()) : edge.getSrc());
			edge.setTrg(copier.containsKey(edge.getTrg()) ? copier.get(edge.getTrg()) : edge.getTrg());
			edge.setType(copier.containsKey(edge.getType()) ? (EReference) copier.get(edge.getType()) : edge.getType());
			
			return deleteEdgeOp;
		} else if(op instanceof AddEdgeOp){
			AddEdgeOp addEdgeOp = (AddEdgeOp) op;
			JavaBasedEdge edge = addEdgeOp.getEdge();
			
			edge.setSrc(copier.containsKey(edge.getSrc()) ? copier.get(edge.getSrc()) : edge.getSrc());
			edge.setTrg(copier.containsKey(edge.getTrg()) ? copier.get(edge.getTrg()) : edge.getTrg());
			edge.setType(copier.containsKey(edge.getType()) ? (EReference) copier.get(edge.getType()) : edge.getType());
			
			return addEdgeOp;
		}else {
			return op;
		}
	}
	private Collection<Edge> mapEdgesToCopy(Collection<Edge> edges) {
		return edges.stream().map(ae -> {
			Edge e = KaleidoscopeDeltaFactory.eINSTANCE.createEdge();
			e.setType(ae.getType());
			e.setSrc(copier.containsKey(ae.getSrc()) ? copier.get(ae.getSrc()) : ae.getSrc());
			e.setTrg(copier.containsKey(ae.getTrg()) ? copier.get(ae.getTrg()) : ae.getTrg());
			return e;
		}).collect(Collectors.toList());
	}
}

class AbsolutePluginUriAllElseRelativeHandler extends URIHandlerImpl {
	@Override
	public URI deresolve(URI uri) {
		return uri.isPlatformPlugin() ? uri : super.deresolve(uri);
	}
}